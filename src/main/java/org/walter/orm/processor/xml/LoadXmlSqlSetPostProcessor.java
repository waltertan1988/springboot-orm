package org.walter.orm.processor.xml;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.walter.orm.handler.loading.LoadingSqlSetHandler;
import org.walter.orm.sqlset.SqlSetHolder;
import org.walter.orm.throwable.SqlSetException;

import java.io.IOException;

@Slf4j
@Component
public class LoadXmlSqlSetPostProcessor implements BeanPostProcessor, SupportChecker {
    @Autowired
    private LoadingSqlSetHandler loadingSqlSetHandler;
    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    private final String SQLSET_XML_PATTERN = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
            Constants.OrmPropertiesKey.ORM_SQLSET_XML_LOCATION;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(!support(null)){
            return bean;
        }

        try{
            for (Resource res : configurableApplicationContext.getResources(SQLSET_XML_PATTERN)) {
                Element sqlsetElement = new SAXReader().read(res.getFile()).getRootElement();
                String dataSourceRefStr = sqlsetElement.attributeValue(Constants.SqlSet.DATA_SOURCE_REF);
                final String DEFAULT_DATASOURCE_REF = StringUtils.isBlank(dataSourceRefStr) ? StringUtils.EMPTY : dataSourceRefStr;
                sqlsetElement.elements().forEach(sqlElement -> loadingSqlSetHandler.handle(sqlElement, DEFAULT_DATASOURCE_REF));
            }
        }catch (IOException | DocumentException e){
            throw new SqlSetException(e);
        }finally {
            return bean;
        }
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return SqlSetHolder.isEmpty(AbstractSqlSet.ConfigType.XML);
    }
}
