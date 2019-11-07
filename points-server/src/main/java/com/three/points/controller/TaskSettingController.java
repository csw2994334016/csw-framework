package com.three.points.controller;

import com.three.common.vo.JsonData;
import com.three.points.entity.ManagerTask;
import com.three.points.param.ManagerTaskParam;
import com.three.points.service.ManagerTaskService;
import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
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

@Api(value = "管理任务设置", tags = "管理任务设置")
@RestController
@RequestMapping("/points/taskSettings")
public class TaskSettingController {

    @Autowired
    private ManagerTaskService managerTaskService;

    @LogAnnotation(module = "添加管理任务设置")
    @ApiOperation(value = "添加管理任务设置")
    @ApiImplicitParam(name = "managerTaskParam", value = "管理任务设置信息", required = true, dataType = "ManagerTaskParam")
    @PostMapping()
    public JsonResult create(@RequestBody ManagerTaskParam managerTaskParam) {
        managerTaskService.create(managerTaskParam);
        return JsonResult.ok("管理任务设置添加成功");
    }

    @LogAnnotation(module = "修改管理任务设置")
    @ApiOperation(value = "修改管理任务设置")
    @ApiImplicitParam(name = "managerTaskParam", value = "管理任务设置信息", required = true, dataType = "ManagerTaskParam")
    @PutMapping()
    public JsonResult update(@RequestBody ManagerTaskParam managerTaskParam) {
        managerTaskService.update(managerTaskParam);
        return JsonResult.ok("管理任务设置修改成功");
    }

    @LogAnnotation(module = "删除管理任务设置")
    @ApiOperation(value = "删除管理任务设置")
    @ApiImplicitParam(name = "ids", value = "管理任务设置信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        managerTaskService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("管理任务设置删除成功");
    }

    @ApiOperation(value = "查询管理任务设置（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<ManagerTask> query(Integer page, Integer limit, String searchValue) {
        if (page != null && limit != null) {
            return managerTaskService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), searchValue);
        } else {
            return managerTaskService.query(null, StatusEnum.OK.getCode(), searchValue);
        }
    }

    @ApiOperation(value = "查询管理任务设置（根据ID查找）", notes = "")
    @ApiImplicitParam(name = "id", value = "管理任务设置信息id", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonData<ManagerTask> findById(@RequestParam(required = true) String id) {
        return new JsonData<>(managerTaskService.findById(id)).success();
    }
}