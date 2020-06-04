package org.walter.orm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.walter.orm.configure.EnableSqlSet;

@SpringBootApplication
@EnableConfigurationProperties
@EnableSqlSet(scanPackages = {"org.walter.orm.processor.db.repository",
		"org.walter.orm.repository2",
		"org.walter.orm.repository"})
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
