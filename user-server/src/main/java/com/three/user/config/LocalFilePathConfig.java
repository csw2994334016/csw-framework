package com.three.user.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 使系统加载jar包外的文件
 *
 */
@Configuration
public class LocalFilePathConfig {

	/**
	 * 上传文件存储在本地的根路径
	 */
	@Value("${file.prefix}")
	private String filePrefix;

	/**
	 * url前缀
	 */
	@Value("${file.urlPrefix}")
	public String urlPrefix;

	@Bean
	public WebMvcConfigurer webMvcConfigurerAdapter() {
		return new WebMvcConfigurer() {

			/**
			 * 外部文件访问<br>
			 */
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler(urlPrefix + "/**")
						.addResourceLocations(ResourceUtils.FILE_URL_PREFIX + filePrefix + File.separator);
			}

		};
	}
}
