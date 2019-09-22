package com.three.authserver.myextends;

import com.three.common.enums.AdminEnum;
import com.three.authserver.service.SysUser;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by csw on 2018/11/18.
 * Description: 权限决策
 */
@Service
public class MyAccessDecisionManager implements AccessDecisionManager {

    // decide 方法是判定是否拥有权限的决策方法，
    // authentication 包含了当前的用户信息，包括拥有的权限。这里的权限来源就是前面登录时UserDetailsService中设置的authorities。
    // object 包含客户端发起的请求的request信息，可转换为 HttpServletRequest request = ((FilterInvocation) object).getHttpRequest()，可以得到request等web资源。
    // configAttributes 为MySecurityMetadataSource的getAttributes(Object object)这个方法返回的结果，是本次访问需要的权限。
    // 此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (null == configAttributes || configAttributes.size() <= 0) {
            return;
        }
        SysUser user = (SysUser) authentication.getPrincipal();
        if (AdminEnum.YES.getCode() == user.getIsAdmin()) { // 用户是超级管理员
            return;
        }
        for (ConfigAttribute configAttribute : configAttributes) { // 本次访问需要的权限，进行逐个验证，是否在认证用户的权限列表里
            String accessUrl = configAttribute.getAttribute();
            for (GrantedAuthority ga : authentication.getAuthorities()) { // 用户对象拥有哪些权限
                if (accessUrl.trim().equals(ga.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("对不起！您没有访问权限！");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
