package org.walter.orm.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath*:sqlset-config.xml")
public class SqlSetConfiguration {
}
