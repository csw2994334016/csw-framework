package com.three.develop.task

import com.three.commonclient.utils.SpringContextHolder
import com.three.develop.entity.QuartzJob
import com.three.develop.repository.QuartzJobRepository

/**
 * Created by csw on 2019/09/14.
 * Description: 
 */
class TestTask1 {

    private QuartzJobRepository quartzJobRepository = (QuartzJobRepository) SpringContextHolder.getBean("quartzJobRepository");

    public void run() {
//        log.info("执行成功");

        println "执行成功"
    }

    public String run1(String str) {
//        log.info("执行成功，参数为： {}" + str);
        QuartzJob quartzJob = quartzJobRepository.findById(1L).orElse(null);
        println "执行成功：" + quartzJob.toString()
        return "执行成功，参数为：" + str
    }

}
