package com.three.develop.utils;

import com.three.common.enums.LogEnum;
import com.three.commonclient.utils.SpringContextHolder;
import com.three.common.utils.ThrowableUtil;
import com.three.resource_jpa.jpa.base.service.DataApiServiceExecutor;
import com.three.develop.constants.Job;
import com.three.develop.entity.QuartzJob;
import com.three.develop.entity.QuartzJobLog;
import com.three.develop.repository.QuartzJobLogRepository;
import com.three.develop.service.QuartzJobService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by csw on 2019/09/13.
 * Description:
 */
@Async
public class ExecutionJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        QuartzJob quartzJob = (QuartzJob) jobExecutionContext.getMergedJobDataMap().get(Job.JOB_KEY);
        // 获取spring bean
        QuartzJobLogRepository quartzJobLogRepository = (QuartzJobLogRepository) SpringContextHolder.getBean("quartzJobLogRepository");
        QuartzJobService quartzJobService = (QuartzJobService) SpringContextHolder.getBean("quartzJobService");
        QuartzManager quartzManager = (QuartzManager) SpringContextHolder.getBean("quartzManager");

        QuartzJobLog log = new QuartzJobLog();
        log.setJobName(quartzJob.getJobName());
        log.setBeanName(quartzJob.getBeanName());
        log.setMethodName(quartzJob.getMethodName());
        log.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        log.setCronExpression(quartzJob.getCronExpression());
        try {
            // 执行任务
            logger.info("任务准备执行：{}", quartzJob.getJobName());
            try {
                QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(), quartzJob.getParams());
                Future<?> future = executorService.submit(task);
                future.get();
                log.setMessage("成功执行JavaBean任务");
            } catch (Exception e) {
                logger.info("定时任务[{}]在SpringContextHolder中不存在：{}，继续查找groovy脚本执行任务", quartzJob.getJobName(), e.getMessage());
                log.setMessage("定时任务[" + quartzJob.getJobName() + "]在SpringContextHolder中不存在：" + e.getMessage() + "，继续查找groovy脚本执行任务");
                DataApiServiceExecutor dataApiServiceExecutor = (DataApiServiceExecutor) SpringContextHolder.getBean("dataApiServiceExecutor");
                dataApiServiceExecutor.submitRequest(quartzJob.getBeanName(), new HashMap<>());
            }
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态
            log.setLogType(LogEnum.INFO.getCode());
            logger.info("任务执行完毕：{} 总共耗时：{} ms", quartzJob.getJobName(), times);
        } catch (Exception e) {
            logger.error("任务执行失败：{}，异常信息：{}", quartzJob.getJobName(), e.getMessage());
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态 1：成功 2：失败
            log.setLogType(LogEnum.ERROR.getCode());
            log.addMessage(e.getMessage());
            log.setExceptionDetail(ThrowableUtil.getStackTrace(e));
            // 出错就暂停任务
            quartzManager.pauseJob(quartzJob);
            // 出错，定时任务改成暂停状态
            quartzJobService.updateByPause(quartzJob);
        } finally {
            quartzJobLogRepository.save(log);
        }
    }
}
