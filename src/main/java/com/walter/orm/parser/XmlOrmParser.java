package com.walter.orm.parser;

import com.walter.orm.constant.Constants;
import com.walter.orm.common.SqlSet;
import com.walter.orm.throwable.SqlSetException;
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

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class XmlOrmParser extends AbstractOrmParser {

    private final String SQLSET_XML_PATTERN = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/**/*-SqlSet.xml";
    private final String SQLSET_COMMENT_PATTERN = "<!--.*-->";

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Override
    protected Collection<SqlSet> parse() {

        Set<SqlSet> resultSqlSets = new HashSet<>();

        try {
            Resource[] resources = configurableApplicationContext.getResources(SQLSET_XML_PATTERN);
            for (Resource res : resources) {
                resultSqlSets.addAll(createSqlSet(res.getFile()));
                log.info("Created SqlSet from {}...", res.getFilename());
            }
        } catch (IOException | DocumentException e) {
            throw new SqlSetException(e);
        }

        return resultSqlSets;
    }

    private Collection<SqlSet> createSqlSet(File xml) throws DocumentException {
        Set<SqlSet> result = new HashSet<>();
        SAXReader reader = new SAXReader();
        Document document = reader.read(xml);

        Element ormElement = document.getRootElement();
        final String DEFAULT_DATA_SOURCE_REF = ormElement.attributeValue(Constants.SqlSetAttributeConstants.ATTR_DATA_SOURCE_REF);
        List<Element> sqlElementList = ormElement.elements();
        for (Element sqlElement : sqlElementList) {
            String id = sqlElement.attributeValue(Constants.SqlSetAttributeConstants.ATTR_ID);
            String datasourceRef = DEFAULT_DATA_SOURCE_REF;
            String sqlElementDatasourceRef = sqlElement.attributeValue(Constants.SqlSetAttributeConstants.ATTR_DATA_SOURCE_REF);
            if(StringUtils.isNotBlank(sqlElementDatasourceRef)){
                datasourceRef = sqlElementDatasourceRef;
            }
            String parameterType = sqlElement.attributeValue(Constants.SqlSetAttributeConstants.ATTR_PARAMETER_TYPE);
            if(StringUtils.isBlank(parameterType)){
                parameterType = HashMap.class.getName();
            }
            String resultType = sqlElement.attributeValue(Constants.SqlSetAttributeConstants.ATTR_RESULT_TYPE);
            if(StringUtils.isBlank(resultType)){
                resultType = HashMap.class.getName();
            }
            String statement = sqlElement.getText().replaceAll(SQLSET_COMMENT_PATTERN, "");
            SqlSet sqlSet = new SqlSet(id, SqlSet.Type.XML, datasourceRef, parameterType, resultType, statement);
            result.add(sqlSet);
            log.info("SqlSet: {}", sqlSet.toString());
        }

        return result;
    }

    @Override
    protected SqlSet.Type supportSqlSetType() {
        return SqlSet.Type.XML;
    }
}
