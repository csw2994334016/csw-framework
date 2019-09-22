package com.three.user;

import com.three.commonjpa.EnableCommonJpa;
import com.three.commonclient.EnableCommonClient;
import com.three.commonjpa.config.ComponentScanConfig;
import com.three.resource_security.EnableResourceSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCommonClient
@EnableCommonJpa
@EnableResourceSecurity
@EnableSwagger2
@Import({ComponentScanConfig.class})
public class UserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServerApplication.class, args);
    }

}
