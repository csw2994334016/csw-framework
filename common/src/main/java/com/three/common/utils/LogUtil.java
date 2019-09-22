package com.three.common.utils;

import com.three.common.log.Log;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by csw on 2019/07/20.
 * Description:
 */
public class LogUtil {

    public static Log setLogRequestInfo(Log log) {
        // 获取request
        HttpServletRequest request = HttpServletUtil.getRequest();
        // 获取IP地址
        UserAgentGetter userAgentGetter = new UserAgentGetter(request);
        log.setIpAddress(userAgentGetter.getIpAddr());
        log.setOsName(userAgentGetter.getOS());
        log.setDevice(userAgentGetter.getDevice());
        log.setBrowserType(userAgentGetter.getBrowser());
        return log;
    }
}
