package com.three.points.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.vo.JsonData;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.entity.ManagerTask;
import com.three.points.service.ManagerTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by csw on 2019-11-06.
 * Description:
 */

@Api(value = "管理任务", tags = "管理任务")
@RestController
@RequestMapping("/points/managerTasks")
public class ManagerTaskController {

    @Autowired
    private ManagerTaskService managerTaskService;

    @ApiOperation(value = "查询所有管理任务（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer"),
            @ApiImplicitParam(name = "taskDate", value = "任务日期（毫秒,为空默认返回当月任务）", dataType = "Long")
    })
    @GetMapping("/query")
    public PageResult<ManagerTask> query(Integer page, Integer limit, Long taskDate) {
        if (page != null && limit != null) {
            return managerTaskService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), taskDate);
        } else {
            return managerTaskService.query(null, StatusEnum.OK.getCode(), taskDate);
        }
    }

    @ApiOperation(value = "查询管理任务详情（根据ID查找）", notes = "")
    @ApiImplicitParam(name = "id", value = "管理任务信息id", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonData<ManagerTask> findById(@RequestParam(required = true) String id) {
        return new JsonData<>(managerTaskService.findById(id)).success();
    }
}