package com.three.zuulserver.config;

import com.three.common.utils.ThrowableUtil;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    /**
     * 调用异常，将服务的异常解析
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({Exception.class})
    public Map<String, Object> errorHandler(Exception exception) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 500);
        if (exception instanceof FeignException) {
            log.error("feignClient调用异常", exception);
            map.put("msg", "feignClient调用异常：" + exception.getMessage());
            map.put("details", ThrowableUtil.getStackTrace(exception));
        } else if (exception instanceof IllegalArgumentException) {
            log.error("错误请求异常", exception);
            map.put("msg", "错误请求异常：" + exception.getMessage());
            map.put("details", ThrowableUtil.getStackTrace(exception));
        } else if (exception != null) {
            log.error("未知异常", exception);
            map.put("msg", "未知异常：" + exception);
            map.put("details", ThrowableUtil.getStackTrace(exception));
        }
        return map;
    }

}
