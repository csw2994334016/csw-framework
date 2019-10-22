package com.three.points.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.points.entity.AwardPrivilege;
import com.three.points.param.AwardPrivilegeEmployeeParam;
import com.three.points.param.AwardPrivilegeParam;
import com.three.points.service.AwardPrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by csw on 2019-09-29.
 * Description:
 */

@Api(value = "奖扣权限设置", tags = "奖扣权限设置")
@RestController
@RequestMapping("/points/awardPrivileges")
public class AwardPrivilegeController {

    @Autowired
    private AwardPrivilegeService awardPrivilegeService;

    @LogAnnotation(module = "添加奖扣权限设置")
    @ApiOperation(value = "添加奖扣权限设置")
    @ApiImplicitParam(name = "awardPrivilegeParam", value = "奖扣权限设置信息", required = true, dataType = "AwardPrivilegeParam")
    @PostMapping()
    public JsonResult create(@RequestBody AwardPrivilegeParam awardPrivilegeParam) {
        awardPrivilegeService.create(awardPrivilegeParam);
        return JsonResult.ok("奖扣权限设置添加成功");
    }

    @LogAnnotation(module = "修改奖扣权限设置")
    @ApiOperation(value = "修改奖扣权限设置")
    @ApiImplicitParam(name = "awardPrivilegeParam", value = "奖扣权限设置信息", required = true, dataType = "AwardPrivilegeParam")
    @PutMapping()
    public JsonResult update(@RequestBody AwardPrivilegeParam awardPrivilegeParam) {
        awardPrivilegeService.update(awardPrivilegeParam);
        return JsonResult.ok("奖扣权限设置修改成功");
    }

    @LogAnnotation(module = "删除奖扣权限设置")
    @ApiOperation(value = "删除奖扣权限设置")
    @ApiImplicitParam(name = "ids", value = "奖扣权限设置信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(String ids) {
        awardPrivilegeService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("奖扣权限设置删除成功");
    }

    @ApiOperation(value = "查询奖扣权限设置（分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<AwardPrivilege> query(Integer page, Integer limit, String searchKey, String searchValue) {
        PageQuery pageQuery = new PageQuery(page, limit);
        BeanValidator.check(pageQuery);
        return awardPrivilegeService.query(pageQuery, StatusEnum.OK.getCode(), searchKey, searchValue);
    }

    @LogAnnotation(module = "添加人员")
    @ApiOperation(value = "添加人员")
    @ApiImplicitParam(name = "awardPrivilegeParam", value = "添加人员信息", required = true, dataType = "AwardPrivilegeEmployeeParam")
    @PostMapping("/bindEmployee")
    public JsonResult bindEmployee(@RequestBody AwardPrivilegeEmployeeParam awardPrivilegeEmployeeParam) {
        awardPrivilegeService.bindEmployee(awardPrivilegeEmployeeParam);
        return JsonResult.ok("添加人员成功");
    }
}