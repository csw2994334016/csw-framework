package com.three.user.feign;

import com.three.common.constants.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by csw on 2019/07/17.
 * Description:
 */
@FeignClient(name = ServiceConstant.POINTS_SERVICE)
public interface PointsClient {

    @GetMapping(value = "/internal/findCurMonthTaskEmp")
    List<String> findCurMonthTaskEmp();
}
