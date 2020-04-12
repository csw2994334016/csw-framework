package com.three.points.controller;

import com.three.points.entity.CustomGroup;
import com.three.points.entity.CustomGroupEmp;
import com.three.points.param.CustomGroupEmpParam;
import com.three.points.param.CustomGroupParam;
import com.three.points.service.CustomGroupService;
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

import java.util.List;

/**
 * Created by csw on 2020-04-06.
 * Description:
 */

@Api(value = "自定义分组", tags = "自定义分组")
@RestController
@RequestMapping("/points/customGroups")
public class CustomGroupController {

    @Autowired
    private CustomGroupService customGroupService;

    @LogAnnotation(module = "添加自定义分组")
    @ApiOperation(value = "添加自定义分组")
    @ApiImplicitParam(name = "customGroupParam", value = "自定义分组信息", required = true, dataType = "CustomGroupParam")
    @PostMapping()
    public JsonResult create(@RequestBody CustomGroupParam customGroupParam) {
        customGroupService.create(customGroupParam);
        return JsonResult.ok("自定义分组添加成功");
    }

    @LogAnnotation(module = "修改自定义分组")
    @ApiOperation(value = "修改自定义分组")
    @ApiImplicitParam(name = "customGroupParam", value = "自定义分组信息", required = true, dataType = "CustomGroupParam")
    @PutMapping()
    public JsonResult update(@RequestBody CustomGroupParam customGroupParam) {
        customGroupService.update(customGroupParam);
        return JsonResult.ok("自定义分组修改成功");
    }

    @LogAnnotation(module = "删除自定义分组")
    @ApiOperation(value = "删除自定义分组")
    @ApiImplicitParam(name = "ids", value = "自定义分组信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        customGroupService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("自定义分组删除成功");
    }

    @ApiOperation(value = "查询自定义分组（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<CustomGroup> query(Integer page, Integer limit, String searchValue) {
        if (page != null && limit != null) {
            return customGroupService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), searchValue);
        } else {
            return customGroupService.query(null, StatusEnum.OK.getCode(), searchValue);
        }
    }

    @ApiOperation(value = "查询自定义分组（根据ID查找）", notes = "")
    @ApiImplicitParam(name = "id", value = "自定义分组信息id", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonData<CustomGroup> findById(@RequestParam(required = true) String id) {
        return new JsonData<>(customGroupService.findById(id)).success();
    }

    @LogAnnotation(module = "配置分组人员")
    @ApiOperation(value = "配置分组人员")
    @ApiImplicitParam(name = "customGroupEmpParam", value = "配置人员信息", required = true, dataType = "CustomGroupEmpParam")
    @PostMapping("/bindEmployee")
    public JsonResult bindEmployee(@RequestBody CustomGroupEmpParam customGroupEmpParam) {
        customGroupService.bindEmployee(customGroupEmpParam);
        return JsonResult.ok("配置分组人员");
    }

    @ApiOperation(value = "查找分组已配置的人员", notes = "")
    @ApiImplicitParam(name = "customGroupId", value = "分组id", required = true, dataType = "String")
    @GetMapping("/findCustomGroupEmpList")
    public JsonData<List<CustomGroupEmp>> findCustomGroupEmpList(@RequestParam(required = true) String customGroupId) {
        return new JsonData<>(customGroupService.findCustomGroupEmpList(customGroupId));
    }
}