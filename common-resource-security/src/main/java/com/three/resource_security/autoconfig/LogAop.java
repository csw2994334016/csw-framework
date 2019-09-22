package com.three.resource_security.autoconfig;

import com.alibaba.fastjson.JSONObject;
import com.three.common.log.Log;
import com.three.common.log.LogAnnotation;
import com.three.common.log.LogQueue;
import com.three.common.utils.LogUtil;
import com.three.resource_security.utils.LoginUserUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * aop实现日志
 */
@Aspect
public class LogAop {

    private static final Logger logger = LoggerFactory.getLogger(LogAop.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 环绕带注解 @LogAnnotation的方法做aop
     */
    @Around(value = "@annotation(com.three.common.log.LogAnnotation)")
    public Object logSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Log log = new Log();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        LogAnnotation logAnnotation = methodSignature.getMethod().getDeclaredAnnotation(LogAnnotation.class);
        log.setModule(logAnnotation.module());
        // 用户名
        log.setUsername(LoginUserUtil.getLoginUsername());

        String methodName = joinPoint.getTarget().getClass().getName() + "." + methodSignature.getName() + "()";
        log.setMethod(methodName);

        if (logAnnotation.recordParam()) { // 是否要记录方法的参数数据
            String[] paramNames = methodSignature.getParameterNames();// 参数名
            if (paramNames != null && paramNames.length > 0) {
                Object[] args = joinPoint.getArgs();// 参数值

                Map<String, Object> params = new HashMap<>();
                for (int i = 0; i < paramNames.length; i++) {
                    Object value = args[i];
                    params.put(paramNames[i], value);
                }

                try {
                    log.setParams(JSONObject.toJSONString(params)); // 以json的形式记录参数
                } catch (Exception e) {
                    logger.error("记录参数失败：{}", e.getMessage());
                }
            }
        }

        LogUtil.setLogRequestInfo(log);

        long currentTime = System.currentTimeMillis();
        try {
            Object object = joinPoint.proceed();// 执行原方法
            log.setFlag(Boolean.TRUE);
            log.setMessage("successfully");
            currentTime = System.currentTimeMillis() - currentTime;
            return object;
        } catch (Exception e) { // 方法执行失败
            log.setFlag(Boolean.FALSE);
            log.setMessage(e.getMessage()); // 备注记录失败原因
            currentTime = System.currentTimeMillis() - currentTime;
            throw e;
        } finally {
            log.setTime(currentTime);
            // 异步将Log对象发送到队列
            CompletableFuture.runAsync(() -> {
                try {
                    amqpTemplate.convertAndSend(LogQueue.LOG_QUEUE, log);
                    logger.info("通过LogAnnotation，发送日志到队列：{}", log);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            });

        }

    }
}
