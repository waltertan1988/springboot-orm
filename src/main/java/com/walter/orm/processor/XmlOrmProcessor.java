package com.walter.orm.processor;

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
public class XmlOrmProcessor extends AbstractOrmProcessor {

    private final String SQL_SET_XML_PATTERN = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/**/*-SqlSet.xml";

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Override
    protected Collection<SqlSet> parse() {

        Set<SqlSet> resultSqlSets = new HashSet<>();

        try {
            Resource[] resources = configurableApplicationContext.getResources(SQL_SET_XML_PATTERN);
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
        final String DEFAULT_DATA_SOURCE_REF = ormElement.attributeValue(XmlConstants.ATTR_DATA_SOURCE_REF);
        List<Element> sqlElementList = ormElement.elements();
        for (Element sqlElement : sqlElementList) {
            String id = sqlElement.attributeValue(XmlConstants.ATTR_ID);
            String datasourceRef = DEFAULT_DATA_SOURCE_REF;
            String sqlElementDatasourceRef = sqlElement.attributeValue(XmlConstants.ATTR_DATA_SOURCE_REF);
            if(StringUtils.isNotBlank(sqlElementDatasourceRef)){
                datasourceRef = sqlElementDatasourceRef;
            }
            String parameterType = sqlElement.attributeValue(XmlConstants.ATTR_PARAMETER_TYPE);
            if(StringUtils.isBlank(parameterType)){
                parameterType = HashMap.class.getName();
            }
            String resultType = sqlElement.attributeValue(XmlConstants.ATTR_RESULT_TYPE);
            if(StringUtils.isBlank(resultType)){
                resultType = HashMap.class.getName();
            }
            String sql = sqlElement.getText().replaceAll("<!--.*-->", " ");
            SqlSet sqlSet = new SqlSet(id, SqlSet.Type.XML, datasourceRef, parameterType, resultType, sql);
            result.add(sqlSet);
            log.info("SqlSet: {}", sqlSet.toString());
        }

        return result;
    }

    @Override
    protected SqlSet.Type supportSqlSetType() {
        return SqlSet.Type.XML;
    }

    class XmlConstants {
        private XmlConstants(){}

        public static final String ATTR_DATA_SOURCE_REF = "dataSourceRef";
        public static final String ATTR_ID = "id";
        public static final String ATTR_PARAMETER_TYPE = "parameterType";
        public static final String ATTR_RESULT_TYPE = "resultType";
    }
}
