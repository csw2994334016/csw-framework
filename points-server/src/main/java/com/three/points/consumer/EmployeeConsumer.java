package com.three.points.consumer;

import com.three.common.auth.SysEmployee;
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
@RabbitListener(queues = SysQueue.EMPLOYEE_QUEUE) // 监听队列
public class EmployeeConsumer {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeConsumer.class);

	@Autowired
	private PointsService pointsService;

	/**
	 * 处理消息
	 * 
	 * @param sysEmployee
	 */
	@RabbitHandler
	public void logHandler(SysEmployee sysEmployee) {
		CompletableFuture.runAsync(() -> {
			try {
				pointsService.changeCustomGroupEmpInfo(sysEmployee);
			} catch (Exception e) {
				logger.error("更新自定义分组人员信息失败，日志：{}，异常：{}", sysEmployee, e);
			}
		});
		CompletableFuture.runAsync(() -> {
			try {
				pointsService.changeAwardPrivilegeEmpInfo(sysEmployee);
			} catch (Exception e) {
				logger.error("更新奖扣权限人员信息失败，日志：{}，异常：{}", sysEmployee, e);
			}
		});
		CompletableFuture.runAsync(() -> {
			try {
				pointsService.changeManagerTaskEmpInfo(sysEmployee);
			} catch (Exception e) {
				logger.error("更新管理任务设置人员信息失败，日志：{}，异常：{}", sysEmployee, e);
			}
		});
		CompletableFuture.runAsync(() -> {
			try {
				pointsService.changeThemeEmpInfo(sysEmployee);
			} catch (Exception e) {
				logger.error("更新积分奖扣主题人员信息失败，日志：{}，异常：{}", sysEmployee, e);
			}
		});
		CompletableFuture.runAsync(() -> {
			try {
				pointsService.changeThemeDetailEmpInfo(sysEmployee);
			} catch (Exception e) {
				logger.error("更新积分奖扣主题详情人员信息失败，日志：{}，异常：{}", sysEmployee, e);
			}
		});
	}
}
