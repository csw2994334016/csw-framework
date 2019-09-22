package com.three.resource_security.autoconfig;


import com.three.common.log.Log;
import com.three.common.log.LogQueue;
import com.three.resource_security.utils.LoginUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;

import java.util.concurrent.CompletableFuture;

/**
 * 通过mq发送日志<br>
 * 在LogAutoConfiguration中将该类声明成Bean，用时直接@Autowired即可
 *
 */
public class LogMqClient {

    private static final Logger logger = LoggerFactory.getLogger(LogMqClient.class);

    private AmqpTemplate amqpTemplate;

    public LogMqClient(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendLogMsg(String module, String username, String params, String message, boolean flag) {
        Log log = new Log();
        if (StringUtils.isNotBlank(username)) {
            log.setUsername(username);
        } else {
            log.setUsername(LoginUserUtil.getLoginUsername());
        }

        log.setFlag(flag);
        log.setModule(module);
        log.setParams(params);
        log.setMessage(message);
        log.setTime(1L);
        sendLogMsg(log);
    }

    public void sendLogMsg(Log log) {
        CompletableFuture.runAsync(() -> {
            try {
                amqpTemplate.convertAndSend(LogQueue.LOG_QUEUE, log);
                logger.info("通过LogMqClient，发送日志到队列：{}", log);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        });
    }
}
