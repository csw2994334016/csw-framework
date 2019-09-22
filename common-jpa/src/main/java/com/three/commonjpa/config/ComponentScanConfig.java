package com.three.commonjpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by csw on 2019/07/16.
 * Description:
 */
@Configuration
@ComponentScan("com.three.commonjpa")
@EnableJpaRepositories("com.three.commonjpa.script")
@EntityScan("com.three.commonjpa.script")
public class ComponentScanConfig {
}
