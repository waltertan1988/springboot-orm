package org.walter.orm.parser.xml;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.walter.orm.core.constant.Constants;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.SelectSqlSet;
import org.walter.orm.throwable.SqlSetException;

import java.util.HashMap;

@Lazy
@Slf4j
@Component
public class XmlSelectSqlSetParser extends AbstractXmlSqlSetParser {
    @Override
    public AbstractSqlSet parse(Object... extras) {
        SelectSqlSet sqlSet = new SelectSqlSet();

        try{
            Element select = (Element) extras[0];

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
