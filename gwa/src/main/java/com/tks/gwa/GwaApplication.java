package com.tks.gwa;

import com.tks.gwa.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.tks.gwa")
@EnableJpaRepositories(basePackages = "com.tks.gwa.repository")
@EntityScan(basePackages = "com.tks.gwa.entity")
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class GwaApplication extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GwaApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(GwaApplication.class, args);
    }
}
