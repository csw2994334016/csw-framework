package com.three.develop;

import com.three.commonclient.EnableCommonClient;
import com.three.commonjpa.EnableCommonJpa;
import com.three.resource_security.EnableResourceSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCommonClient
@EnableCommonJpa
@EnableResourceSecurity
@EnableAsync
@EnableSwagger2
@EnableJpaRepositories("com.three.develop")
@EntityScan("com.three.develop")
public class DevelopServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevelopServerApplication.class, args);
    }

}
