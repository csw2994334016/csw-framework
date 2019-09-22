package com.three.zuulserver.feign;

import com.three.common.constants.ServiceConstant;
import com.three.common.log.Log;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(ServiceConstant.LOG_SERVER)
public interface LogClient {

    @PostMapping("/internal/saveLog")
    public void save(@RequestBody Log log);
}
