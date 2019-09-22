package com.three.commonjpa;

import com.three.commonjpa.config.ComponentScanConfig;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.lang.annotation.*;

/**
 * Created by csw on 2019/07/13.
 * Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ServletComponentScan
@EnableJpaAuditing
@Import({ComponentScanConfig.class})
public @interface EnableCommonJpa {
}
