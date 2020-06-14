package com.three.resource_jpa.autoconfig;


import com.three.common.auth.SysEmployee;
import com.three.common.auth.SysOrganization;
import com.three.common.log.Log;
import com.three.common.log.LogQueue;
import com.three.common.log.SysQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;

import java.util.concurrent.CompletableFuture;

/**
 * 通过mq发送日志<br>
 * 在LogAutoConfiguration中将该类声明成Bean，用时直接@Autowired即可
 */
public class SysMqClient {

    private static final Logger logger = LoggerFactory.getLogger(SysMqClient.class);

    private AmqpTemplate amqpTemplate;

    public SysMqClient(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
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

    public void sendEmployeeMsg(SysEmployee sysEmployee) {
        CompletableFuture.runAsync(() -> {
            try {
                amqpTemplate.convertAndSend(SysQueue.EMPLOYEE_QUEUE, sysEmployee);
                logger.info("通过MqClient，发送更新用户信息到队列：{}", sysEmployee);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        });
    }

    public void sendOrganizationMsg(SysOrganization sysOrganization) {
        CompletableFuture.runAsync(() -> {
            try {
                amqpTemplate.convertAndSend(SysQueue.ORGANIZATION_QUEUE, sysOrganization);
                logger.info("通过MqClient，发送更新组织机构信息到队列：{}", sysOrganization);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        });
    }
}
