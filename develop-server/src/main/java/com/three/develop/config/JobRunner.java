package com.three.develop.config;

import com.three.common.enums.StatusEnum;
import com.three.develop.entity.QuartzJob;
import com.three.develop.enums.JobStatusEnum;
import com.three.develop.repository.QuartzJobRepository;
import com.three.develop.utils.QuartzManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by csw on 2019/09/13.
 * Description:
 */
@Component
public class JobRunner implements ApplicationRunner {

    @Autowired
    private QuartzJobRepository quartzJobRepository;

    @Autowired
    private QuartzManager quartzManager;

    /**
     * 项目启动时重新激活启用的定时任务
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("--------------------注入定时任务---------------------");
        List<QuartzJob> quartzJobs = quartzJobRepository.findByJobStatusAndStatus(JobStatusEnum.RUNNING.getCode(), StatusEnum.OK.getCode());
        quartzJobs.forEach(quartzJob -> {
            quartzManager.addJob(quartzJob);
        });
        System.out.println("--------------------定时任务注入完成---------------------");
    }
}
