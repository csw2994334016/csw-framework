package com.three.resource_jpa.resource.utils;

import com.alibaba.fastjson.JSONObject;
import com.three.common.auth.LoginUser;
import com.three.common.enums.AdminEnum;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Map;

/**
 * Created by csw on 2019/07/17.
 * Description:
 */
public class LoginUserUtil {

    public static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Auth = (OAuth2Authentication) authentication;
            authentication = oAuth2Auth.getUserAuthentication();
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
                Object principal = authentication.getPrincipal();
                if (principal instanceof LoginUser) {
                    return (LoginUser) principal;
                }
                Map map = (Map) authenticationToken.getDetails();
                map = (Map) map.get("principal");
                return JSONObject.parseObject(JSONObject.toJSONString(map), LoginUser.class);
            }
        }
        return null;
    }

    public static String getLoginUserEmpId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null) {
            if (loginUser.getSysEmployee() != null) {
                return loginUser.getSysEmployee().getId();
            }
        }
        return null;
    }

    public static String getLoginUserEmpFullName() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null) {
            if (loginUser.getSysEmployee() != null) {
                return loginUser.getSysEmployee().getFullName();
            }
        }
        return null;
    }

    public static String getLoginUsername() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser.getUsername();
        }
        return null;
    }

    public static String getLoginUserFirstOrganizationId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null && loginUser.getSysOrganization() != null) {
            return loginUser.getSysOrganization().getOrganizationId();
        }
        return null;
    }

    public static boolean isAdmin() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null && AdminEnum.YES.getCode() == loginUser.getIsAdmin();
    }
}
