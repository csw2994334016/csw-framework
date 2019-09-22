package com.three.log;

import com.three.commonclient.EnableCommonClient;
import com.three.commonjpa.EnableCommonJpa;
import com.three.resource_security.EnableResourceSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCommonClient
@EnableCommonJpa
@EnableResourceSecurity
@EnableSwagger2
@EnableJpaRepositories("com.three.log")
@EntityScan("com.three.log")
public class LogServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogServerApplication.class, args);
    }

}
