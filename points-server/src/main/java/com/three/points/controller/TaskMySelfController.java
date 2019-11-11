package com.three.points.controller;

import com.three.common.vo.JsonData;
import com.three.points.entity.ManagerTaskEmp;
import com.three.points.service.ManagerTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by csw on 2019-11-06.
 * Description:
 */

@Api(value = "我的管理任务", tags = "我的管理任务")
@RestController
@RequestMapping("/points/taskMySelf")
public class TaskMySelfController {

    @Autowired
    private ManagerTaskService managerTaskService;

    @ApiOperation(value = "查询我的管理任务（个人奖扣任务完成情况）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskDate", value = "任务日期（毫秒），不传则表示当前月任务完成情况", dataType = "Long"),
            @ApiImplicitParam(name = "empId", value = "人员ID，不传则表示当前登录用户", dataType = "String")
    })
    @GetMapping("/queryTaskMySelf")
    public JsonData<ManagerTaskEmp> queryTaskMySelf(Long taskDate, String empId) {
        return new JsonData<>(managerTaskService.queryTaskMySelf(taskDate, empId)).success();
    }
}