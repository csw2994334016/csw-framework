package com.three.points.consumer;

import com.three.common.auth.SysOrganization;
import com.three.common.log.SysQueue;
import com.three.points.service.PointsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 从mq队列消费日志数据
 * 
 *
 */
@Component
@RabbitListener(queues = SysQueue.ORGANIZATION_QUEUE) // 监听队列
public class OrganizationConsumer {

	private static final Logger logger = LoggerFactory.getLogger(OrganizationConsumer.class);

	@Autowired
	private PointsService pointsService;

	/**
	 * 处理消息
	 * 
	 * @param sysOrganization
	 */
	@RabbitHandler
	public void logHandler(SysOrganization sysOrganization) {
		CompletableFuture.runAsync(() -> {
			try {
				pointsService.changeCustomGroupOrgInfo(sysOrganization);
			} catch (Exception e) {
				logger.error("更新自定义分组人员信息失败，日志：{}，异常：{}", sysOrganization, e);
			}
		});
		CompletableFuture.runAsync(() -> {
			try {
				pointsService.changeManagerTaskOrgInfo(sysOrganization);
			} catch (Exception e) {
				logger.error("更新管理任务设置人员信息失败，日志：{}，异常：{}", sysOrganization, e);
			}
		});
		CompletableFuture.runAsync(() -> {
			try {
				pointsService.changeThemeDetailOrgInfo(sysOrganization);
			} catch (Exception e) {
				logger.error("更新积分奖扣主题详情人员信息失败，日志：{}，异常：{}", sysOrganization, e);
			}
		});
	}
}
