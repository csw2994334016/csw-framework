package com.three.develop.utils;

import com.three.commonclient.exception.BusinessException;
import com.three.develop.constants.Job;
import com.three.develop.entity.QuartzJob;
import com.three.develop.enums.JobStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by csw on 2019/09/13.
 * Description:
 */
@Slf4j
@Component
public class QuartzManager {

    private static final String JOB_NAME = "TASK_";

    @Resource(name = "scheduler")
    private Scheduler scheduler;

    public void addJob(QuartzJob quartzJob) {
        try {
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ExecutionJob.class).withIdentity(JOB_NAME + quartzJob.getId()).build();

            // 通过触发器名和cron 表达式创建 Trigger
            Trigger cronTrigger = newTrigger()
                    .withIdentity(JOB_NAME + quartzJob.getId())
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression()))
                    .build();

            cronTrigger.getJobDataMap().put(Job.JOB_KEY, quartzJob);

            // 重置启动时间
            ((CronTriggerImpl) cronTrigger).setStartTime(new Date());

            // 执行定时任务
            scheduler.scheduleJob(jobDetail, cronTrigger);

            // 暂停任务
            if (JobStatusEnum.PAUSE.getCode() == quartzJob.getJobStatus()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e) {
            log.error("创建定时任务失败", e);
            throw new BusinessException("创建定时任务失败");
        }
    }

    /**
     * 更新job cron表达式
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void updateJobCron(QuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(quartzJob);
                trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            }
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //重置启动时间
            ((CronTriggerImpl) trigger).setStartTime(new Date());
            trigger.getJobDataMap().put(Job.JOB_KEY, quartzJob);

            scheduler.rescheduleJob(triggerKey, trigger);
            // 暂停任务
            if (JobStatusEnum.PAUSE.getCode() == quartzJob.getJobStatus()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e) {
            log.error("更新定时任务失败", e);
            throw new BusinessException("更新定时任务失败");
        }

    }

    /**
     * 删除一个job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void deleteJob(QuartzJob quartzJob) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            log.error("删除定时任务失败", e);
            throw new BusinessException("删除定时任务失败");
        }
    }

    /**
     * 恢复一个job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void resumeJob(QuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null)
                addJob(quartzJob);
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            log.error("恢复定时任务失败", e);
            throw new BusinessException("恢复定时任务失败");
        }
    }

    /**
     * 立即执行job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void runAJobNow(QuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null)
                addJob(quartzJob);
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(Job.JOB_KEY, quartzJob);
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.triggerJob(jobKey, dataMap);
        } catch (Exception e) {
            log.error("定时任务执行失败", e);
            throw new BusinessException("定时任务执行失败");
        }
    }

    /**
     * 暂停一个job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void pauseJob(QuartzJob quartzJob) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            log.error("定时任务暂停失败", e);
            throw new BusinessException("定时任务暂停失败");
        }
    }
}
