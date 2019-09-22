package com.three.develop.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by csw on 2019/09/14.
 * Description:
 */
@Slf4j
@Component
public class TestTask {
    public void run() {
        log.info("执行成功");
    }

    public void run1(String str) {
        log.info("执行成功，参数为： {}" + str);
    }
}
