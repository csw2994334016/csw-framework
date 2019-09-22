package com.three.authserver;

import com.three.commonclient.EnableCommonClient;
import com.three.commonjpa.EnableCommonJpa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCommonClient
@EnableCommonJpa
@EnableSwagger2
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
