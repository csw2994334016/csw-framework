package com.three.zuulserver.controller;

import com.three.common.log.Log;
import com.three.common.utils.LogUtil;
import com.three.common.vo.JsonResult;
import com.three.zuulserver.feign.LogClient;
import com.three.zuulserver.feign.Oauth2Client;
import com.three.zuulserver.param.LoginParam;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 登陆、刷新token、退出
 */
@Slf4j
@RestController
public class SysTokenController {

    public static final String GRANT_TYPE = "grant_type";
    public static final String CLIENT_ID = "client_id";
    public static final String SCOPE = "scope";
    public static String BEARER_TYPE = "Bearer";

    @Autowired
    private Oauth2Client oauth2Client;

    /**
     * 系统登陆<br>
     * 根据用户名登录<br>
     * 采用oauth2密码模式获取access_token和refresh_token
     */
    @PostMapping("/sys/login")
    public Map<String, Object> login(@RequestBody LoginParam loginParam) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(GRANT_TYPE, "password");
        parameters.put(CLIENT_ID, loginParam.getClient_id());
        parameters.put("client_secret", loginParam.getClient_secret());
        parameters.put(SCOPE, loginParam.getScope());
        parameters.put("username", loginParam.getUsername());
//        // 为了支持多类型登录，这里在username后拼装上登录类型
//        parameters.put("username", username + "|" + CredentialType.USERNAME.name());
        parameters.put("password", loginParam.getPassword());

        Map<String, Object> tokenInfo = oauth2Client.postAccessToken(parameters);
        saveLoginLog(loginParam.getUsername(), "用户名密码登陆");

        return tokenInfo;
    }

//    /**
//     * 短信登录
//     *
//     * @param phone
//     * @param key
//     * @param code
//     * @return
//     */
//    @PostMapping("/sys/login-sms")
//    public Map<String, Object> smsLogin(String phone, String key, String code) {
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put(OAuth2Utils.GRANT_TYPE, "password");
//        parameters.put(OAuth2Utils.CLIENT_ID, SystemClientConstant.CLIENT_ID);
//        parameters.put("client_secret", SystemClientConstant.CLIENT_SECRET);
//        parameters.put(OAuth2Utils.SCOPE, SystemClientConstant.CLIENT_SCOPE);
//        // 为了支持多类型登录，这里在username后拼装上登录类型，同时为了校验短信验证码，我们也拼上code等
//        parameters.put("username", phone + "|" + CredentialType.PHONE.name() + "|" + key + "|" + code + "|"
//                + DigestUtils.md5Hex(key + code));
//        // 短信登录无需密码，但security底层有密码校验，我们这里将手机号作为密码，认证中心采用同样规则即可
//        parameters.put("password", phone);
//
//        Map<String, Object> tokenInfo = oauth2Client.postAccessToken(parameters);
////        saveLoginLog(phone, "手机号短信登陆");
//
//        return tokenInfo;
//    }
//
//    /**
//     * 微信登录
//     *
//     * @return
//     */
//    @PostMapping("/sys/login-wechat")
//    public Map<String, Object> smsLogin(String openid, String tempCode) {
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put(OAuth2Utils.GRANT_TYPE, "password");
//        parameters.put(OAuth2Utils.CLIENT_ID, SystemClientConstant.CLIENT_ID);
//        parameters.put("client_secret", SystemClientConstant.CLIENT_SECRET);
//        parameters.put(OAuth2Utils.SCOPE, SystemClientConstant.CLIENT_SCOPE);
//        // 为了支持多类型登录，这里在username后拼装上登录类型，同时为了服务端校验，我们也拼上tempCode
//        parameters.put("username", openid + "|" + CredentialType.WECHAT_OPENID.name() + "|" + tempCode);
//        // 微信登录无需密码，但security底层有密码校验，我们这里将手机号作为密码，认证中心采用同样规则即可
//        parameters.put("password", tempCode);
//
//        Map<String, Object> tokenInfo = oauth2Client.postAccessToken(parameters);
////        saveLoginLog(openid, "微信登陆");
//
//        return tokenInfo;
//    }

    @Autowired
    private LogClient logClient;

    /**
     * 登陆日志
     *
     * @param username
     */
    private void saveLoginLog(String username, String message) {
        log.info("{}登录" + message, username);
        Log log = Log.builder().username(username).module("登录").message(message).flag(Boolean.TRUE).time(0L).build();
        LogUtil.setLogRequestInfo(log);
        // 异步
        CompletableFuture.runAsync(() -> {
            try {
                logClient.save(log);
            } catch (Exception e) {
                // do nothing

            }
        });
    }

    /**
     * 系统刷新refresh_token
     *
     * @return
     */
    @PostMapping("/sys/refresh_token")
    public Map<String, Object> refresh_token(@RequestBody LoginParam loginParam) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(GRANT_TYPE, "refresh_token");
        parameters.put(CLIENT_ID, loginParam.getClient_id());
        parameters.put("client_secret", loginParam.getClient_secret());
        parameters.put(SCOPE, loginParam.getScope());
        parameters.put("refresh_token", loginParam.getRefresh_token());

        Map<String, Object> refreshTokenMap = oauth2Client.postAccessToken(parameters);

//        if (refreshTokenMap != null) {
//            oauth2Client.removeToken(access_token);
//        }
        return refreshTokenMap;
    }

    /**
     * 退出
     *
     * @param access_token
     */
    @GetMapping("/sys/logout")
    public JsonResult logout(String access_token, @RequestHeader(required = false, value = "Authorization") String token) {
        if (StringUtils.isBlank(access_token)) {
            if (StringUtils.isNoneBlank(token)) {
                access_token = token.substring(BEARER_TYPE.length() + 1);
            }
        }
        oauth2Client.removeToken(access_token);
        return JsonResult.ok("成功退出登录");
    }
}
