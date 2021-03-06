package com.three.points.feign;

import com.three.common.auth.LoginUser;
import com.three.common.constants.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * Created by csw on 2019/07/17.
 * Description:
 */
@FeignClient(name = ServiceConstant.USER_SERVICE)
public interface UserClient {

    @GetMapping(value = "/internal/findByAdmin")
    LoginUser findByAdmin(@RequestParam() String firstOrganizationId);

    @GetMapping(value = "/internal/findEmpIdSetByOrgId")
    Set<String> findSysEmployeeSet(@RequestParam() String orgId, @RequestParam() String containChildFlag);
}
