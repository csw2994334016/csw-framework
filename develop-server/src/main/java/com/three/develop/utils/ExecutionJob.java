package com.three.develop.utils;

import com.three.common.enums.LogEnum;
import com.three.commonclient.utils.SpringContextHolder;
import com.three.common.utils.ThrowableUtil;
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
    protected void executeInternal(JobExecutionContext context) {
        QuartzJob quartzJob = (QuartzJob) context.getMergedJobDataMap().get(Job.JOB_KEY);
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
            logger.info("任务准备执行，任务名称：{}", quartzJob.getJobName());
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(), quartzJob.getParams());
            Future<?> future = executorService.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态
            log.setLogType(LogEnum.INFO.getCode());
            logger.info("任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", quartzJob.getJobName(), times);
        } catch (Exception e) {
            logger.error("任务执行失败，任务名称：{}" + quartzJob.getJobName(), e);
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态 1：成功 2：失败
            log.setLogType(LogEnum.ERROR.getCode());
            log.setExceptionDetail(ThrowableUtil.getStackTrace(e));
            // 出错就暂停任务
            quartzManager.pauseJob(quartzJob);
            // 更新状态
            quartzJobService.updateIsPause(quartzJob);
        } finally {
            quartzJobLogRepository.save(log);
        }
    }
}
