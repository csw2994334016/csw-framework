package com.three.points.controller;

import com.three.common.vo.JsonData;
import com.three.points.entity.ManagerTaskEmp;
import com.three.points.service.ManagerTaskService;
import com.three.points.vo.TaskStatisticsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by csw on 2019-11-06.
 * Description:
 */

@Api(value = "任务统计", tags = "任务统计")
@RestController
@RequestMapping("/points/taskStatistics")
public class ManagerTaskStatisticsController {

    @Autowired
    private ManagerTaskService managerTaskService;

    @ApiOperation(value = "查询任务统计（个人任务统计情况）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskDate", value = "任务日期（毫秒），不传则表示当前时间任务完成情况", dataType = "Long"),
            @ApiImplicitParam(name = "statisticsFlag", value = "任务统计标记：1=日统计（默认）；2=月统计；3=年统计", dataType = "String"),
            @ApiImplicitParam(name = "empId", value = "人员ID，不传则表示当前登录用户", dataType = "String")
    })
    @GetMapping("/queryTaskStatistics")
    public JsonData<TaskStatisticsVo> queryTaskStatistics(Long taskDate, @RequestParam(defaultValue = "1") String statisticsFlag, String empId) {
        return new JsonData<>(managerTaskService.queryTaskStatistics(taskDate, statisticsFlag, empId));
    }
}