package com.three.develop;

import com.three.resource_jpa.EnableCommonResourceJpa;
import com.three.commonclient.EnableCommonClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCommonClient
@EnableCommonResourceJpa
@EnableAsync
@EnableSwagger2
@EnableJpaRepositories("com.three.develop")
@EntityScan("com.three.develop.entity")
public class DevelopServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevelopServerApplication.class, args);
    }

}
