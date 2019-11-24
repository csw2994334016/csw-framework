package com.three.user.feign;

import com.three.common.constants.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019/07/17.
 * Description:
 */
@FeignClient(name = ServiceConstant.POINTS_SERVICE)
public interface PointsClient {

    @GetMapping(value = "/internal/findCurMonthTaskEmp")
    Set<String> findCurMonthTaskEmp(@RequestParam() String firstOrganizationId);

    @GetMapping(value = "/internal/findAuditor")
    Set<String> findAuditor(@RequestParam() String firstOrganizationId, @RequestParam() String attnOrAuditFlag, @RequestParam() String attnId, @RequestParam() Integer aPosScoreMax, @RequestParam() Integer aNegScoreMin, @RequestParam() Integer bPosScoreMax, @RequestParam() Integer bNegScoreMin);

    @GetMapping(value = "/internal/findAwardPrivilegeEmp")
    Set<String> findAwardPrivilegeEmp(@RequestParam() String firstOrganizationId);
}
