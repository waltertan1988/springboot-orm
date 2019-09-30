package org.walter.orm.processor.xml;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.walter.orm.parser.xml.AbstractXmlSqlSetParser;
import org.walter.orm.processor.AbstractLoadSqlSetPostProcessor;
import org.walter.orm.sqlset.SqlSetHolder;
import org.walter.orm.throwable.SqlSetException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class LoadXmlSqlSetProcessor extends AbstractLoadSqlSetPostProcessor {

    private final String SQLSET_XML_PATTERN = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + Constants.OrmPropertiesKey.ORM_SQLSET_XML_LOCATION;
    private final String SQLSET_COMMENT_PATTERN = "<!--.*-->";

    @Autowired
    private List<AbstractXmlSqlSetParser> xmlSqlSetParsers;
    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Override
    protected Set<AbstractSqlSet> getSqlSets() {
        Set<AbstractSqlSet> resultSqlSets = Sets.newHashSet();
        try {
            Resource[] resources = configurableApplicationContext.getResources(SQLSET_XML_PATTERN);
            for (Resource res : resources) {
                createSqlSet(res.getFile(), resultSqlSets);
                log.info("Created SqlSet from {}...", res.getFilename());
            }
        } catch (IOException | DocumentException e) {
            throw new SqlSetException(e);
        }

        return resultSqlSets;
    }

    private void createSqlSet(File xml, final Set<AbstractSqlSet> resultSqlSets) throws DocumentException {
        Element sqlsetElement = new SAXReader().read(xml).getRootElement();
        final String DEFAULT_DATA_SOURCE_REF = sqlsetElement.attributeValue(Constants.SqlSet.DATA_SOURCE_REF);
        List<Element> sqlElementList = sqlsetElement.elements();
        for (Element sqlElement : sqlElementList) {
            String id = sqlElement.attributeValue(Constants.SqlSet.ID);
            String statement = sqlElement.getText().replaceAll(SQLSET_COMMENT_PATTERN, "").trim();

            String datasource = DEFAULT_DATA_SOURCE_REF;
            String _sqlElementDatasourceRef = sqlElement.attributeValue(Constants.SqlSet.DATA_SOURCE_REF);
            if(StringUtils.isNotBlank(_sqlElementDatasourceRef)){
                datasource = _sqlElementDatasourceRef;
            }

            AbstractSqlSet sqlSet = xmlSqlSetParsers.stream().filter(p -> p.support(AbstractXmlSqlSetParser.class, sqlElement))
                    .findFirst().get().parse(sqlElement);
            sqlSet.setId(id);
            sqlSet.setConfigType(AbstractSqlSet.ConfigType.XML);
            sqlSet.setStatement(statement);
            sqlSet.setDataSource(datasource);
            if(resultSqlSets.contains(sqlSet)){
                throw new SqlSetException("Duplicated SqlSet: {}", sqlSet);
            }
            resultSqlSets.add(sqlSet);
        }
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return SqlSetHolder.isEmpty(AbstractSqlSet.ConfigType.XML);
    }
}
