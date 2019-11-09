package com.three.points.controller;

import com.three.common.vo.JsonData;
import com.three.points.entity.ManagerTask;
import com.three.points.entity.ManagerTaskEmp;
import com.three.points.param.ManagerTaskParam;
import com.three.points.param.ManagerTaskParam1;
import com.three.points.service.ManagerTaskService;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @LogAnnotation(module = "添加管理任务")
    @ApiOperation(value = "添加管理任务")
    @ApiImplicitParam(name = "managerTaskParam", value = "管理任务信息", required = true, dataType = "ManagerTaskParam")
    @PostMapping()
    public JsonResult create(@RequestBody ManagerTaskParam managerTaskParam) {
        managerTaskService.create(managerTaskParam);
        return JsonResult.ok("管理任务添加成功");
    }

    @LogAnnotation(module = "变更管理任务配置")
    @ApiOperation(value = "变更管理任务配置")
    @ApiImplicitParam(name = "managerTaskParam", value = "管理任务信息", required = true, dataType = "ManagerTaskParam")
    @PutMapping()
    public JsonResult update(@RequestBody ManagerTaskParam managerTaskParam) {
        managerTaskService.update(managerTaskParam);
        return JsonResult.ok("管理任务变更成功");
    }

    @LogAnnotation(module = "变更考核人员配置")
    @ApiOperation(value = "变更考核人员配置")
    @ApiImplicitParam(name = "managerTaskParam1", value = "管理任务信息", required = true, dataType = "ManagerTaskParam1")
    @PostMapping("/updateEmp")
    public JsonResult updateEmp(@RequestBody ManagerTaskParam1 managerTaskParam1) {
        managerTaskService.updateEmpOnly(managerTaskParam1);
        return JsonResult.ok("考核人员变更成功");
    }

    @ApiOperation(value = "预览下月任务配置", notes = "")
    @ApiImplicitParam(name = "id", value = "当前月管理任务ID", required = true, dataType = "String")
    @GetMapping("/findNextTask")
    public JsonData<ManagerTask> findNextTask(@RequestParam(required = true) String id) {
        return new JsonData<>(managerTaskService.findNextTask(id)).success();
    }

    @ApiOperation(value = "预览下月任务人员配置", notes = "")
    @ApiImplicitParam(name = "id", value = "当前月管理任务ID", required = true, dataType = "String")
    @GetMapping("/findNextEmp")
    public JsonData<List<ManagerTaskEmp>> findNextEmp(@RequestParam(required = true) String id) {
        return new JsonData<>(managerTaskService.findNextEmp(id)).success();
    }

//    @LogAnnotation(module = "删除管理任务")
//    @ApiOperation(value = "删除管理任务")
//    @ApiImplicitParam(name = "ids", value = "管理任务信息ids", required = true, dataType = "String")
//    @DeleteMapping()
//    public JsonResult delete(@RequestParam(required = true) String ids) {
//        managerTaskService.delete(ids, StatusEnum.DELETE.getCode());
//        return JsonResult.ok("管理任务删除成功");
//    }
}