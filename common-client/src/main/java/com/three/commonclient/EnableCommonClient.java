package com.three.commonclient;

import com.three.commonclient.config.ComponentScanConfig;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by csw on 2019/07/13.
 * Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ServletComponentScan
@EnableEurekaClient
@EnableFeignClients
@Import({ComponentScanConfig.class})
public @interface EnableCommonClient {
}
