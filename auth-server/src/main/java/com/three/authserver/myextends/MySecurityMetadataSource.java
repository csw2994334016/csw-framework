package com.three.authserver.myextends;

import com.three.authserver.service.SysUserService;
import com.three.common.auth.SysAuthority;
import com.three.common.enums.AuthorityEnum;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SysUserService sysUserService;

    private Map<String, Collection<ConfigAttribute>> map = null;

    /**
     * 加载权限表中所有权限
     */
    private void loadResourceDefine() {
        map = new HashMap<>();
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        List<SysAuthority> sysAuthorityList = sysUserService.findAllSysAuthorities();
        for (SysAuthority sysAuthority : sysAuthorityList) {
            if (AuthorityEnum.BUTTON.getCode() == sysAuthority.getAuthorityType()) {
                array = new ArrayList<>();
                cfg = new SecurityConfig(sysAuthority.getAuthorityUrl());
                // 此处只添加了权限url，其实还可以添加更多权限的信息，比如树形的权限，map根据url作为key值，可以对应权限的集合
                array.add(cfg);
                // 用权限的getUrl() 作为map的key，用ConfigAttribute的集合作为 value，
                map.put(sysAuthority.getAuthorityUrl(), array);
            }
        }
    }

    /**
     * 返回本次访问需要的权限，可以有多个权限
     * 如果没有匹配的url直接返回null，也就是没有配置权限的url默认都为白名单，想要换成默认是黑名单只要修改这里即可。
     * 意思就是数据库中配置了的权限，那么就要验证访问用户是否具备该权限
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (map == null) {
            loadResourceDefine();
        }
        // object 中包含用户请求的request 信息
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        String re = request.getMethod().toLowerCase() + ":" + request.getRequestURI();
        for (String resUrl : map.keySet()) {
            if (resUrl.matches(re)) {
                return map.get(resUrl);
            }
        }
//        // 说明数据库权限表中没有该权限信息
//        List<ConfigAttribute> configAttributeList = new ArrayList<>();
//        ConfigAttribute configAttribute = new SecurityConfig(request.getMethod().toLowerCase() + ":" + request.getRequestURI());
//        configAttributeList.add(configAttribute);
//        return configAttributeList;
        return null;
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
