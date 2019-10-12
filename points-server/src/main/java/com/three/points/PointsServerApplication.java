package com.three.points;

import com.three.commonclient.EnableCommonClient;
import com.three.resource_jpa.EnableCommonResourceJpa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCommonClient
@EnableCommonResourceJpa
@EnableSwagger2
@EnableJpaRepositories("com.three.points")
@EntityScan("com.three.points")
public class PointsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PointsServerApplication.class, args);
    }

}
