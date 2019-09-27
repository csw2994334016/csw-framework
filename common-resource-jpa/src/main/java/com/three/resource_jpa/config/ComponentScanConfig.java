package com.three.resource_jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by csw on 2019/07/16.
 * Description:
 */
@Configuration
@ComponentScan("com.three.resource_jpa")
@EnableJpaRepositories("com.three.resource_jpa.jpa.script")
@EntityScan("com.three.resource_jpa.jpa.script")
public class ComponentScanConfig {
}
