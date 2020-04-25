package com.three.zuulserver.filter;

import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;


@Component
@Slf4j
public class MySendErrorFilter extends SendErrorFilter {

    @Override
    public Object run() {
        // 重写 run方法
        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            // 直接复用异常处理类
            ExceptionHolder exception = findZuulException(ctx.getThrowable());
            log.info("异常信息:{}", exception.getThrowable().getMessage());
            // 这里可对不同异常返回不同的错误码
            ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            ctx.getResponse().setStatus(HttpStatus.OK.value());
            ctx.getResponse().getOutputStream().write(("{\"code\":500,\"msg\":\"服务异常：" + exception.getThrowable().getCause().getMessage() + "\"}").getBytes());

        } catch (Exception ex) {
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }
}
