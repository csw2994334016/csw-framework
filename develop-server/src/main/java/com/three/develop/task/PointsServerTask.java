package com.three.develop.task;

import com.three.common.utils.StringUtil;
import com.three.common.vo.JsonResult;
import com.three.develop.feign.PointsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PointsServerTask {

    @Autowired
    private PointsClient pointsClient;

    public void generateManagerTask(String params) {
        // 解析参数
        if (StringUtil.isNotBlank(params) && params.contains(",")) {
            String scriptName = params.split(",")[0];
            String scriptMethod = params.split(",")[1];
            JsonResult jsonResult = pointsClient.generateNextManagerTask(scriptName, scriptMethod, null);
            log.info("generateManagerTask result: {}", jsonResult.get("msg"));
        } else {
            log.error("generateManagerTask none params!");
        }
    }
}
