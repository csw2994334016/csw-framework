package com.three.user.controller;

import com.three.common.log.LogAnnotation;
import com.three.user.entity.Role;
import com.three.user.param.RoleParam;
import com.three.user.service.RoleService;
import com.three.common.enums.StatusEnum;
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

import java.util.List;

/**
 * Created by csw on 2019/03/30.
 * Description:
 */

@Api(value = "角色管理", tags = "角色管理")
@RestController
@RequestMapping("/sys/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "查询角色（分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段(船名)", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String")
    })
    @PostMapping("/query")
    public PageResult<Role> query(Integer page, Integer limit, String searchKey, String searchValue) {
        PageQuery pageQuery = new PageQuery(page, limit);
        BeanValidator.check(pageQuery);
        return roleService.query(pageQuery, StatusEnum.OK.getCode(), searchKey, searchValue);
    }

    @ApiOperation(value = "查询所有角色")
    @GetMapping()
    public JsonResult queryAll() {
        List<Role> roleList = roleService.findAll(StatusEnum.OK.getCode());
        return JsonResult.ok().put("data", roleList);
    }

    @LogAnnotation(module = "添加角色")
    @ApiOperation(value = "添加角色")
    @ApiImplicitParam(name = "roleParam", value = "角色信息", required = true, dataType = "RoleParam")
    @PostMapping()
    public JsonResult create(@RequestBody RoleParam roleParam) {
        roleService.create(roleParam);
        return JsonResult.ok("添加成功");
    }

    @LogAnnotation(module = "修改角色")
    @ApiOperation(value = "修改角色")
    @ApiImplicitParam(name = "roleParam", value = "角色信息", required = true, dataType = "RoleParam")
    @PutMapping()
    public JsonResult update(@RequestBody RoleParam roleParam) {
        roleService.update(roleParam);
        return JsonResult.ok("修改成功");
    }

    @LogAnnotation(module = "删除角色")
    @ApiOperation(value = "删除角色")
    @ApiImplicitParam(name = "ids", value = "角色ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(String ids) {
        roleService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("删除成功");
    }

    @ApiOperation(value = "查找权限树（根据角色）")
    @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "String")
    @GetMapping("/findAuthTree")
    public JsonResult findAuthTree(String roleId) {
        return JsonResult.ok().put("data", roleService.findAuthTree(roleId));
    }

    @LogAnnotation(module = "角色绑定权限")
    @ApiOperation(value = "角色绑定权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "authIds", value = "权限ids", dataType = "String")
    })
    @PutMapping("/assignRoleAuth")
    public JsonResult assignRoleAuth(String roleId, String authIds) {
        roleService.assignRoleAuth(roleId, authIds);
        return JsonResult.ok();
    }

    @LogAnnotation(module = "角色绑定用户")
    @ApiOperation(value = "角色绑定用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "userIds", value = "用户ids", dataType = "String")
    })
    @PutMapping("/assignRoleUser")
    public JsonResult assignRoleUser(String roleId, String userIds) {
        roleService.assignRoleUser(roleId, userIds);
        return JsonResult.ok();
    }
}
