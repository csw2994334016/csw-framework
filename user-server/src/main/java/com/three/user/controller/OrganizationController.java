package com.three.user.controller;

import com.three.user.entity.Organization;
import com.three.user.param.OrganizationParam;
import com.three.user.service.OrganizationService;
import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by csw on 2019-09-25.
 * Description:
 */

@Api(value = "组织机构", tags = "组织机构")
@RestController
@RequestMapping("/sys/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @LogAnnotation(module = "添加组织机构")
    @ApiOperation(value = "添加组织机构")
    @ApiImplicitParam(name = "organizationParam", value = "组织机构信息", required = true, dataType = "OrganizationParam")
    @PostMapping()
    public JsonResult create(@RequestBody OrganizationParam organizationParam) {
        organizationService.create(organizationParam);
        return JsonResult.ok("组织机构添加成功");
    }

    @LogAnnotation(module = "修改组织机构")
    @ApiOperation(value = "修改组织机构")
    @ApiImplicitParam(name = "organizationParam", value = "组织机构信息", required = true, dataType = "OrganizationParam")
    @PutMapping()
    public JsonResult update(@RequestBody OrganizationParam organizationParam) {
        organizationService.update(organizationParam);
        return JsonResult.ok("组织机构修改成功");
    }

    @LogAnnotation(module = "删除组织机构")
    @ApiOperation(value = "删除组织机构")
    @ApiImplicitParam(name = "ids", value = "组织机构信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(String ids) {
        organizationService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("组织机构删除成功");
    }

    @ApiOperation(value = "查询组织机构（分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @PostMapping("/query")
    public PageResult<Organization> query(Integer page, Integer limit, String searchKey, String searchValue) {
        PageQuery pageQuery = new PageQuery(page, limit);
        BeanValidator.check(pageQuery);
        return organizationService.query(pageQuery, StatusEnum.OK.getCode(), searchKey, searchValue);
    }

    @ApiOperation(value = "查询所有组织机构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchKey", value = "筛选条件", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping()
    public PageResult<Organization> findAll(String searchKey, String searchValue) {
        return organizationService.findAll(StatusEnum.OK.getCode(), searchKey, searchValue);
    }

    @ApiOperation(value = "查询所有组织机构(树形结构)")
    @GetMapping("/tree")
    public JsonResult findAllWithTree() {
        return JsonResult.ok("查找成功").put("data", organizationService.findAllWithTree(StatusEnum.OK.getCode()));
    }

    @ApiOperation(value = "上移")
    @ApiImplicitParam(name = "id", value = "组织机构信息id", required = true, dataType = "String")
    @PutMapping("/moveUp")
    public JsonResult moveUp(String id) {
        organizationService.moveUp(id);
        return JsonResult.ok("上移成功");
    }

    @ApiOperation(value = "下移")
    @ApiImplicitParam(name = "id", value = "组织机构信息id", required = true, dataType = "String")
    @GetMapping("/moveDown")
    public JsonResult moveDown(String id) {
        organizationService.moveDown(id);
        return JsonResult.ok("下移成功");
    }
}