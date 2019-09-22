package com.three.resource_security.myextends;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by csw on 2018/11/18.
 * Description: 主要责任就是当访问一个url时返回这个url所需要的访问权限
 */
@Service
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static final String[] ENDPOINTS = {"/v2/api-docs", "/swagger-ui.html", "/swagger-resources", "/webjars"};

    /**
     * 返回本次访问需要的权限，可以有多个权限
     * 如果没有匹配的url直接返回null，也就是没有配置权限的url默认都为白名单，想要换成默认是黑名单只要修改这里即可。
     * 意思就是数据库中配置了的权限，那么就要验证访问用户是否具备该权限
     *
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // object 中包含用户请求的request 信息
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        String uri = request.getRequestURI();
        String reUrl = request.getMethod().toLowerCase() + ":" + uri;

        if (uri.startsWith("/internal/") || permitAllUrl(uri)) { // 服务之间内部访问
            return null;
        }

        // 说明该url需要验证用户是否具有该权限
        List<ConfigAttribute> configAttributeList = new ArrayList<>();
        ConfigAttribute configAttribute = new SecurityConfig(reUrl);
        configAttributeList.add(configAttribute);
        return configAttributeList;
    }

    private boolean permitAllUrl(String uri) {
        for (String permitUrl : ENDPOINTS) {
            if (uri.startsWith(permitUrl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法如果返回了所有定义的权限资源，Spring Security会在启动时校验每个ConfigAttribute是否配置正确，不需要校验直接返回null
     *
     * @return
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * 方法返回类对象是否支持校验
     *
     * @param clazz
     * @return
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
