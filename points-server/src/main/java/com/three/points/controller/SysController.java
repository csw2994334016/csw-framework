package com.three.points.controller;

import cn.hutool.core.date.DateUtil;
import com.three.common.utils.DateUtils;
import com.three.common.vo.JsonResult;
import com.three.points.service.AwardPrivilegeService;
import com.three.points.service.ManagerTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Api(value = "积分制管理", tags = "积分制管理")
@RestController
@RequestMapping()
public class SysController {

    @Autowired
    private ManagerTaskService managerTaskService;

    @Autowired
    private AwardPrivilegeService awardPrivilegeService;

    @ApiOperation(value = "查询当前月任务已分配人员（内部接口）")
    @GetMapping(value = "/internal/findCurMonthTaskEmp")
    Set<String> findCurMonthTaskEmp(String firstOrganizationId) {
        Date date = DateUtil.beginOfMonth(new Date());
        Date taskDateNext = DateUtil.offsetMonth(date, 1);
        return managerTaskService.findCurMonthTaskEmp(firstOrganizationId, taskDateNext);
    }

    @ApiOperation(value = "查询奖扣权限设置已分配人员（内部接口）")
    @GetMapping(value = "/internal/findAwardPrivilegeEmp")
    Set<String> findAwardPrivilegeEmp(String firstOrganizationId) {
        return awardPrivilegeService.findAwardPrivilegeEmp(firstOrganizationId);
    }

    @ApiOperation(value = "查找积分奖扣审核人员列表（内部接口）")
    @GetMapping(value = "/internal/findAuditor")
    Set<String> findAuditor(String firstOrganizationId, String attnOrAuditFlag, String attnId, Integer aPosScoreMax, Integer aNegScoreMin, Integer bPosScoreMax, Integer bNegScoreMin) {
        return awardPrivilegeService.findAuditor(firstOrganizationId, attnOrAuditFlag, attnId, aPosScoreMax, aNegScoreMin, bPosScoreMax, bNegScoreMin);
    }

    @ApiOperation(value = "生成管理任务下月配置（内部接口）")
    @GetMapping(value = "/internal/generateNextManagerTask")
    JsonResult generateNextManagerTask(String name, String method, String firstOrganizationId) {
        managerTaskService.generateNextManagerTask(name, method, firstOrganizationId);
        return JsonResult.ok("生成管理任务下月配置成功");
    }

    @ApiOperation(value = "管理任务结算（内部接口）")
    @GetMapping(value = "/internal/settleManagerTask")
    JsonResult settleManagerTask(String name, String method, String firstOrganizationId) {
        managerTaskService.settleManagerTask(name, method, firstOrganizationId, null);
        return JsonResult.ok("管理任务结算成功");
    }
}
