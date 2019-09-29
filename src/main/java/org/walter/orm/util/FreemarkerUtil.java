package org.walter.orm.util;

import org.walter.orm.throwable.SqlSetException;
import freemarker.cache.StringTemplateLoader;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class FreemarkerUtil {

    private static Configuration cfg;

    private FreemarkerUtil(){}

    static {
        cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setDefaultEncoding("UTF-8");
    }

    public static String parse(String sqlTemplate, Object dataModel) {
        final String templateKey = FreemarkerUtil.class.getSimpleName();
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate(templateKey, sqlTemplate);
        cfg.setTemplateLoader(templateLoader);
        try(Writer out = new StringWriter()) {
            Template template = cfg.getTemplate(templateKey);
            if(dataModel instanceof Map){
                template.process(dataModel, out);
            }else{
                template.process(dataModel, out, new BeansWrapperBuilder(cfg.getVersion()).build());
            }

            return out.toString();
        } catch (IOException | TemplateException e) {
            throw new SqlSetException(e);
        }
    }
}
