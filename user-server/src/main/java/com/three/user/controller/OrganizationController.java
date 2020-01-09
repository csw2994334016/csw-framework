package com.three.user.controller;

import com.three.common.vo.JsonData;
import com.three.user.entity.Organization;
import com.three.user.param.OrganizationParam;
import com.three.user.service.OrganizationService;
import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.user.vo.OrgVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ApiImplicitParam(name = "ids", value = "组织机构信息ids,多个用逗号,隔开", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        organizationService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("组织机构删除成功");
    }

    @ApiOperation(value = "查询组织机构（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<Organization> query(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page != null && limit != null) {
            return organizationService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), searchKey, searchValue);
        }
        return organizationService.query(null, StatusEnum.OK.getCode(), searchKey, searchValue);
    }

    @ApiOperation(value = "查询所有组织机构(树形结构)")
    @GetMapping("/tree")
    public JsonData<List<OrgVo>> findAllWithTree() {
        return new JsonData<>(organizationService.findAllWithTree(StatusEnum.OK.getCode())).success();
    }

    @ApiOperation(value = "上移")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "组织机构信息id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "upId", value = "同级上一个组织机构信息id", required = true, dataType = "String")
    })
    @PutMapping("/moveUp")
    public JsonResult moveUp(@RequestParam(required = true) String id, @RequestParam(required = true) String upId) {
        organizationService.move(id, upId);
        return JsonResult.ok("上移成功");
    }

    @ApiOperation(value = "下移")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "组织机构信息id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "downId", value = "同级下一个组织机构信息id", required = true, dataType = "String")
    })
    @GetMapping("/moveDown")
    public JsonResult moveDown(@RequestParam(required = true) String id, @RequestParam(required = true) String downId) {
        organizationService.move(id, downId);
        return JsonResult.ok("下移成功");
    }
}