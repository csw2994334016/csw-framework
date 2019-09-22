package com.three.develop.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.develop.entity.QuartzJob;
import com.three.develop.param.QuartzJobParam;
import com.three.develop.service.QuartzJobService;
import com.three.develop.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "定时任务", tags = "定时任务")
@RestController
@RequestMapping("/develop/quartzJobs")
public class QuartzJobController {

    @Autowired
    private QuartzJobService quartzJobService;

    @LogAnnotation(module = "添加定时任务")
    @ApiOperation(value = "添加定时任务")
    @ApiImplicitParam(name = "quartzJobParam", value = "定时任务信息", required = true, dataType = "QuartzJobParam")
    @PostMapping()
    public JsonResult create(@RequestBody QuartzJobParam quartzJobParam) {
        quartzJobService.create(quartzJobParam);
        return JsonResult.ok("添加成功");
    }

    @LogAnnotation(module = "修改定时任务")
    @ApiOperation(value = "修改定时任务")
    @ApiImplicitParam(name = "quartzJobParam", value = "定时任务信息", required = true, dataType = "QuartzJobParam")
    @PutMapping()
    public JsonResult update(@RequestBody QuartzJobParam quartzJobParam) {
        quartzJobService.update(quartzJobParam);
        return JsonResult.ok("修改成功");
    }

    @LogAnnotation(module = "删除定时任务")
    @ApiOperation(value = "删除定时任务")
    @ApiImplicitParam(name = "ids", value = "定时任务ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(String ids) {
        quartzJobService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("删除成功");
    }

    @ApiOperation(value = "查询定时任务（分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段(任务名称)", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String")
    })
    @PostMapping("/query")
    public PageResult<QuartzJob> query(Integer page, Integer limit, String searchKey, String searchValue) {
        PageQuery pageQuery = new PageQuery(page, limit);
        BeanValidator.check(pageQuery);
        return quartzJobService.query(pageQuery, StatusEnum.OK.getCode(), searchKey, searchValue);
    }

    @LogAnnotation(module = "修改任务状态")
    @ApiOperation(value = "修改任务状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "任务ids", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态：1运行中，2已暂停", required = true, dataType = "Integer")
    })
    @PutMapping("/jobStatus")
    public JsonResult updateJobStatus(String ids, Integer status) {
        quartzJobService.updateJobStatus(ids, status);
        return JsonResult.ok();
    }

    @LogAnnotation(module = "执行定时任务")
    @ApiOperation(value = "执行定时任务", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "任务ids", required = true, dataType = "String")
    })
    @PutMapping("/execute")
    public JsonResult executeJob(String ids) {
        quartzJobService.executeJob(ids);
        return JsonResult.ok();
    }

    @Autowired
    private TestService testService;

    @PostMapping("/test")
    public JsonResult test(String name, String method) {
        String re = (String) testService.test(name, method);
        return JsonResult.ok("执行成功：" + re);
    }

}
