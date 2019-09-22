package com.three.log.consumer;

import com.three.common.log.Log;
import com.three.common.log.LogQueue;
import com.three.log.service.ActionLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 从mq队列消费日志数据
 * 
 *
 */
@Component
@RabbitListener(queues = LogQueue.LOG_QUEUE) // 监听队列
public class LogConsumer {

	private static final Logger logger = LoggerFactory.getLogger(LogConsumer.class);

	@Autowired
	private ActionLogService actionLogService;

	/**
	 * 处理消息
	 * 
	 * @param log
	 */
	@RabbitHandler
	public void logHandler(Log log) {
		try {
			actionLogService.saveLog(log);
		} catch (Exception e) {
			logger.error("保存日志失败，日志：{}，异常：{}", log, e);
		}
	}
}
