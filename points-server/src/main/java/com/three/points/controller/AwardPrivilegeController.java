package com.three.points.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonData;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.entity.AwardPrivilege;
import com.three.points.entity.AwardPrivilegeEmp;
import com.three.points.param.AwardPrivilegeEmpParam;
import com.three.points.param.AwardPrivilegeParam;
import com.three.points.service.AwardPrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public JsonResult delete(@RequestParam(required = true) String ids) {
        awardPrivilegeService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("奖扣权限设置删除成功");
    }

    @ApiOperation(value = "查询奖扣权限设置（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<AwardPrivilege> query(Integer page, Integer limit, String searchValue) {
        if (page != null && limit != null) {
            return awardPrivilegeService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), searchValue);
        }
        return awardPrivilegeService.query(null, StatusEnum.OK.getCode(), searchValue);
    }

    @LogAnnotation(module = "配置奖扣权限人员")
    @ApiOperation(value = "配置奖扣权限人员")
    @ApiImplicitParam(name = "awardPrivilegeEmpParam", value = "配置人员信息", required = true, dataType = "AwardPrivilegeEmpParam")
    @PostMapping("/bindEmployee")
    public JsonResult bindEmployee(@RequestBody AwardPrivilegeEmpParam awardPrivilegeEmpParam) {
        awardPrivilegeService.bindEmployee(awardPrivilegeEmpParam);
        return JsonResult.ok("配置奖扣权限人员");
    }

    @ApiOperation(value = "查询奖扣权限设置（根据ID查找）", notes = "")
    @ApiImplicitParam(name = "id", value = "奖扣权限设置id", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonData<AwardPrivilege> findById(@RequestParam(required = true) String id) {
        return new JsonData<>(awardPrivilegeService.findById(id));
    }

    @ApiOperation(value = "查找奖扣权限设置的人员", notes = "")
    @ApiImplicitParam(name = "awardPrivilegeId", value = "奖扣权限id", required = true, dataType = "String")
    @GetMapping("/findAwardPrivilegeEmpList")
    public JsonData<List<AwardPrivilegeEmp>> findAwardPrivilegeEmpList(@RequestParam(required = true) String awardPrivilegeId) {
        return new JsonData<>(awardPrivilegeService.findAwardPrivilegeEmpList(awardPrivilegeId));
    }
}