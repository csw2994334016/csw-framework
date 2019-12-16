package com.three.develop.feign;

import com.three.common.constants.ServiceConstant;
import com.three.common.vo.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * Created by csw on 2019/07/17.
 * Description:
 */
@FeignClient(name = ServiceConstant.POINTS_SERVICE)
public interface PointsClient {

    @GetMapping(value = "/internal/generateNextManagerTask")
    JsonResult generateNextManagerTask(@RequestParam() String scriptName, @RequestParam() String method, @RequestParam() String firstOrganizationId);

    @GetMapping(value = "/internal/settleManagerTask")
    JsonResult settleManagerTask(@RequestParam() String scriptName, @RequestParam() String method, @RequestParam() String firstOrganizationId);
}
