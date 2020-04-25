package com.three.zuulserver.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.three.common.vo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 过滤uri<br>
 * 该类uri不需要登陆，但又不允许外网通过网关调用，只允许微服务间在内网调用，<br>
 * 为了方便拦截此场景的uri，我们自己约定一个规范，及uri中含有-anon/internal<br>
 * 如在oauth登陆的时候用到根据username查询用户，<br>
 * 用户系统提供的查询接口/users-anon/internal肯定不能做登录拦截，而该接口也不能对外网暴露<br>
 * 如果有此类场景的uri，请用这种命名格式，
 */
@Component
@Slf4j
public class InternalAccessFilter extends ZuulFilter {

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();

        //获取request对象
        HttpServletRequest request = requestContext.getRequest();
        //打印日志
        log.info("请求方式：{}, 地址：{}", request.getMethod(), request.getRequestURI());

        //避免中文乱码
        requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        requestContext.setResponseStatusCode(HttpStatus.OK.value());
        requestContext.setResponseBody("{\"code\":500,\"msg\":\"请求接口路径中禁止含有*/internal/*内容\"}");
        requestContext.setSendZuulResponse(false);

        //这返回值没啥用
        return null;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        return PatternMatchUtils.simpleMatch("*/internal/*", request.getRequestURI());
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

}
