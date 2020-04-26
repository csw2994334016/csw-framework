package com.three.commonclient.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * Created by csw on 2018-02-22 上午 11:29.
 */
@ControllerAdvice
public class MyExceptionHandler {

    private Logger logger = LoggerFactory.getLogger("MyExceptionHandler");

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Map<String, Object> errorHandler(Exception ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 500);
        // 根据不同错误获取错误信息
        if (ex instanceof IException) { // 自定义异常
            map.put("msg", ex.getMessage());
        } else if (ex instanceof NullPointerException) {
            map.put("msg", "空指针异常："+ ex.getMessage());
            logger.error(ex.getMessage(), ex);
        } else {
            map.put("msg", ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        return map;
    }

}
