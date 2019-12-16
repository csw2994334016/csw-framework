package com.three.develop.task;

import com.three.common.utils.StringUtil;
import com.three.common.vo.JsonResult;
import com.three.develop.feign.PointsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ManagerTaskSettlement {

    @Autowired
    private PointsClient pointsClient;

    // 结算管理任务完成情况
    public void settleManagerTask(String params) {
        if (StringUtil.isNotBlank(params) && params.contains(",")) {
            String scriptName = params.split(",")[0];
            String scriptMethod = params.split(",")[1];
            JsonResult jsonResult = pointsClient.settleManagerTask(scriptName, scriptMethod, null);
            log.info("generateManagerTask result: {}", jsonResult.get("msg"));
        } else {
            log.error("generateManagerTask none params!");
        }
    }
}
