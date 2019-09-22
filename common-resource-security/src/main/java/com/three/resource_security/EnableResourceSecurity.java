package com.three.resource_security;

import com.three.resource_security.config.ComponentScanConfig;
import org.springframework.boot.web.servlet.ServletComponentScan;
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
@Import({ComponentScanConfig.class})
public @interface EnableResourceSecurity {
}
