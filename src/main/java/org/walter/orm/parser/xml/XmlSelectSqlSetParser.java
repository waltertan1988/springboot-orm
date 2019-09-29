package org.walter.orm.parser.xml;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.walter.orm.core.constant.Constants;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.SelectSqlSet;
import org.walter.orm.throwable.SqlSetException;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class XmlSelectSqlSetParser extends AbstractXmlSqlSetParser {

    private final String SQLSET_XML_PATTERN = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + Constants.OrmPropertiesKey.ORM_SQLSET_XML_LOCATION;
    private final String SQLSET_COMMENT_PATTERN = "<!--.*-->";

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    public Collection<AbstractSqlSet> parse() {

        Set<AbstractSqlSet> resultSqlSets = new HashSet<>();

        try {
            Resource[] resources = configurableApplicationContext.getResources(SQLSET_XML_PATTERN);
            for (Resource res : resources) {
                resultSqlSets.addAll(createSqlSet(res.getFile()));
                log.info("Created SqlSet from {}...", res.getFilename());
            }
        } catch (IOException | DocumentException | ClassNotFoundException e) {
            throw new SqlSetException(e);
        }

        return resultSqlSets;
    }

    private Collection<AbstractSqlSet> createSqlSet(File xml) throws DocumentException, ClassNotFoundException {
        Set<AbstractSqlSet> result = new HashSet<>();
        SAXReader reader = new SAXReader();
        Document document = reader.read(xml);

        Element ormElement = document.getRootElement();
        final String DEFAULT_DATA_SOURCE_REF = ormElement.attributeValue(Constants.SqlSet.DATA_SOURCE_REF);
        List<Element> sqlElementList = ormElement.elements();
        for (Element sqlElement : sqlElementList) {
            String id = sqlElement.attributeValue(Constants.SqlSet.ID);
            String statement = sqlElement.getText().replaceAll(SQLSET_COMMENT_PATTERN, "").trim();

            String _datasourceRef = DEFAULT_DATA_SOURCE_REF;
            String _sqlElementDatasourceRef = sqlElement.attributeValue(Constants.SqlSet.DATA_SOURCE_REF);
            if(StringUtils.isNotBlank(_sqlElementDatasourceRef)){
                _datasourceRef = _sqlElementDatasourceRef;
            }
            DataSource dataSource = configurableApplicationContext.getBean(_datasourceRef, DataSource.class);
        }

        return result;
    }

    @Override
    public AbstractSqlSet parse(Object... extras) {
        SelectSqlSet sqlSet = (SelectSqlSet) extras[0];

        try{
            Element select = (Element) extras[1];

            String _resultType = select.attributeValue(Constants.SqlSet.Select.RESULT_TYPE);
            if(StringUtils.isBlank(_resultType)){
                _resultType = HashMap.class.getName();
            }
            Class<?> resultType = Class.forName(_resultType);

            Class<?> multiReturnElementType = null;
            String _multiReturnElementType = select.attributeValue(Constants.SqlSet.Select.MULTI_RETURN_ELEMENT_TYPE);
            if(StringUtils.isNotBlank(_multiReturnElementType)){
                multiReturnElementType = Class.forName(_multiReturnElementType);
            }

            sqlSet.setResultType(resultType);
            sqlSet.setMultiReturnElementType(multiReturnElementType);
        }catch (ClassNotFoundException e){
            throw new SqlSetException(e);
        }

        return sqlSet;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Element select = (Element) args[0];
        return super.support(clz, args) && Constants.SqlSet.Select.class.getSimpleName().toLowerCase().equals(select.getName());
    }
}
