package org.walter.orm.processor.xml;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.walter.orm.core.common.SupportChecker;
import org.walter.orm.core.constant.Constants;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.executor.loading.LoadingSqlSetExecutor;
import org.walter.orm.handler.loading.LoadingSqlSetHandler;
import org.walter.orm.parser.xml.loading.AbstractLoadingXmlSqlSetParser;
import org.walter.orm.sqlset.SqlSetHolder;
import org.walter.orm.throwable.SqlSetException;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class LoadXmlSqlSetPostProcessor implements BeanPostProcessor, SupportChecker {
    @Autowired
    private List<AbstractLoadingXmlSqlSetParser> parserList;
    @Autowired
    private LoadingSqlSetExecutor loadingSqlSetExecutor;
    @Autowired
    private LoadingSqlSetHandler handler;
    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    private final String SQLSET_XML_PATTERN = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + Constants.OrmPropertiesKey.ORM_SQLSET_XML_LOCATION;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(!support(null)){
            return bean;
        }

        try{
            for (Resource res : configurableApplicationContext.getResources(SQLSET_XML_PATTERN)) {
                Element sqlsetElement = new SAXReader().read(res.getFile()).getRootElement();
                final String DEFAULT_DATA_SOURCE_REF = sqlsetElement.attributeValue(Constants.SqlSet.DATA_SOURCE_REF);
                List<Element> sqlElementList = sqlsetElement.elements();
                for (Element sqlElement : sqlElementList) {
                    AbstractLoadingXmlSqlSetParser parser = parserList.stream().filter(p -> p.support(AbstractLoadingXmlSqlSetParser.class, sqlElement))
                            .findFirst().get();
                    handler.handle(parser, loadingSqlSetExecutor, null, sqlElement, DEFAULT_DATA_SOURCE_REF);
                }
            }
        }catch (IOException | DocumentException e){
            throw new SqlSetException(e);
        }

        return bean;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return SqlSetHolder.isEmpty(AbstractSqlSet.ConfigType.XML);
    }
}
