package com.three.points.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.entity.ManagerTaskEmp;
import com.three.points.service.ManagerTaskEmpService;
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

@Api(value = "管理任务查询", tags = "管理任务查询")
@RestController
@RequestMapping("/points/taskQueries")
public class ManagerTaskQueryController {

    @Autowired
    private ManagerTaskEmpService managerTaskEmpService;

    @ApiOperation(value = "查询管理任务考核人员（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer"),
            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String"),
            @ApiImplicitParam(name = "taskName", value = "任务名称", dataType = "String"),
            @ApiImplicitParam(name = "taskDate", value = "任务日期（毫秒）", dataType = "Long"),
            @ApiImplicitParam(name = "empFullName", value = "员工姓名", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<ManagerTaskEmp> query(Integer page, Integer limit, String taskId, String taskName, Long taskDate, String empFullName) {
        if (page != null && limit != null) {
            return managerTaskEmpService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), taskId, taskName, taskDate, empFullName);
        } else {
            return managerTaskEmpService.query(null, StatusEnum.OK.getCode(), taskId, taskName, taskDate, empFullName);
        }
    }
}