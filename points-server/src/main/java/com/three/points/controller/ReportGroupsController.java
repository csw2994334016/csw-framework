package com.three.points.controller;

import com.three.points.entity.ReportGroups;
import com.three.points.param.ReportGroupsParam;
import com.three.points.service.ReportGroupsService;
import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.JsonData;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by csw on 2020-01-11.
 * Description:
 */

@Api(value = "自定义分组", tags = "自定义分组")
@RestController
@RequestMapping("/points/reportGroups")
public class ReportGroupsController {

    @Autowired
    private ReportGroupsService reportGroupsService;

    @LogAnnotation(module = "添加自定义分组")
    @ApiOperation(value = "添加自定义分组")
    @ApiImplicitParam(name = "reportGroupsParam", value = "自定义分组信息", required = true, dataType = "ReportGroupsParam")
    @PostMapping()
    public JsonResult create(@RequestBody ReportGroupsParam reportGroupsParam) {
        reportGroupsService.create(reportGroupsParam);
        return JsonResult.ok("自定义分组添加成功");
    }

    @LogAnnotation(module = "修改自定义分组")
    @ApiOperation(value = "修改自定义分组")
    @ApiImplicitParam(name = "reportGroupsParam", value = "自定义分组信息", required = true, dataType = "ReportGroupsParam")
    @PutMapping()
    public JsonResult update(@RequestBody ReportGroupsParam reportGroupsParam) {
        reportGroupsService.update(reportGroupsParam);
        return JsonResult.ok("自定义分组修改成功");
    }

    @LogAnnotation(module = "删除自定义分组")
    @ApiOperation(value = "删除自定义分组")
    @ApiImplicitParam(name = "ids", value = "自定义分组信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        reportGroupsService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("自定义分组删除成功");
    }

    @ApiOperation(value = "查询自定义分组（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<ReportGroups> query(Integer page, Integer limit, String searchValue) {
        if (page != null && limit != null) {
            return reportGroupsService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), searchValue);
        } else {
            return reportGroupsService.query(null, StatusEnum.OK.getCode(), searchValue);
        }
    }

    @ApiOperation(value = "查询自定义分组（根据ID查找）", notes = "")
    @ApiImplicitParam(name = "id", value = "自定义分组信息id", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonData<ReportGroups> findById(@RequestParam(required = true) String id) {
        return new JsonData<>(reportGroupsService.findById(id)).success();
    }
}