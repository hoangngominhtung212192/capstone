package com.tks.gwa;

import com.tks.gwa.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.tks.gwa")
@EnableJpaRepositories(basePackages = "com.tks.gwa.repository")
@EntityScan(basePackages = "com.tks.gwa.entity")
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class GwaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GwaApplication.class, args);
    }
}
