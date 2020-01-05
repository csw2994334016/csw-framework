package com.three.zuulserver.config;

import com.netflix.client.ClientException;
import com.netflix.zuul.exception.ZuulException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * feignClient调用异常，将服务的异常和http状态码解析
     *
     * @param exception
     * @param response
     * @return
     */
    @ExceptionHandler({FeignException.class})
    public Map<String, Object> feignException(FeignException exception, HttpServletResponse response) {
        int httpStatus = exception.status();
        if (httpStatus >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            log.error("feignClient调用异常", exception);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("msg", exception.getMessage());
        data.put("code", httpStatus);
        response.setStatus(httpStatus);
        return data;
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequestException(IllegalArgumentException exception) {
        Map<String, Object> data = new HashMap<>();
        data.put("code", HttpStatus.BAD_REQUEST.value());
        data.put("msg", exception.getMessage());
        return data;
    }

    @ExceptionHandler({ClientException.class, Throwable.class, ZuulException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> serverException(Throwable throwable) {
        log.error("服务端异常", throwable);
        Map<String, Object> data = new HashMap<>();
        data.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        data.put("msg", "服务端异常，请联系管理员:" + throwable);
        return data;
    }

//	@ExceptionHandler({ZuulException.class})
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//	public Map<String, Object> zuulException(ZuulException zuulException) {
//		log.error("网关连接服务异常", zuulException);
//		Map<String, Object> data = new HashMap<>();
//		data.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
//		data.put("msg", "网关无法找到服务，请联系管理员");
//		return data;
//	}

}
