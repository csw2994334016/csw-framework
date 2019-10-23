package com.three.user.controller;

import com.three.user.entity.Employee;
import com.three.user.param.EmployeeParam;
import com.three.user.service.EmployeeService;
import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by csw on 2019-09-27.
 * Description:
 */

@Api(value = "员工信息", tags = "员工信息")
@RestController
@RequestMapping("/sys/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @LogAnnotation(module = "添加员工信息")
    @ApiOperation(value = "添加员工信息")
    @ApiImplicitParam(name = "employeeParam", value = "员工信息", required = true, dataType = "EmployeeParam")
    @PostMapping()
    public JsonResult create(@RequestBody EmployeeParam employeeParam) {
        employeeService.create(employeeParam);
        return JsonResult.ok("员工信息添加成功");
    }

    @LogAnnotation(module = "修改员工信息")
    @ApiOperation(value = "修改员工信息")
    @ApiImplicitParam(name = "employeeParam", value = "员工信息", required = true, dataType = "EmployeeParam")
    @PutMapping()
    public JsonResult update(@RequestBody EmployeeParam employeeParam) {
        employeeService.update(employeeParam);
        return JsonResult.ok("员工信息修改成功");
    }

    @LogAnnotation(module = "删除员工信息")
    @ApiOperation(value = "删除员工信息")
    @ApiImplicitParam(name = "ids", value = "员工信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(String ids) {
        employeeService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("员工信息删除成功");
    }

    @ApiOperation(value = "查找所有员工信息（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "organizationId", value = "组织/公司/部门ID", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "搜索值（姓名/手机号）", dataType = "String"),
            @ApiImplicitParam(name = "containChildFlag", value = "包含子部门人员标记：1=包含；0=不包含（默认0）", defaultValue = "0", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<Employee> query(Integer page, Integer limit, String organizationId, String searchValue, @RequestParam(defaultValue = "0") String containChildFlag) {
        if (page != null && limit != null) {
            PageQuery pageQuery = new PageQuery(page, limit);
            BeanValidator.check(pageQuery);
            return employeeService.query(pageQuery, StatusEnum.OK.getCode(), organizationId, searchValue, containChildFlag);
        } else {
            return employeeService.query(null, StatusEnum.OK.getCode(), organizationId, searchValue, containChildFlag);
        }
    }

    @LogAnnotation(module = "分配角色")
    @ApiOperation(value = "分配角色", notes = "")
    @ApiImplicitParam(name = "EmployeeParam", value = "员工信息", required = true, dataType = "employeeParam")
    @PutMapping("/assignRole")
    public JsonResult assignRole(@RequestBody EmployeeParam employeeParam) {
        employeeService.assignRole(employeeParam);
        return JsonResult.ok("修改成功");
    }

    @LogAnnotation(module = "修改员工状态")
    @ApiOperation(value = "修改员工状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "员工信息ids", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态：1正常，2冻结，3删除", required = true, dataType = "Integer")
    })
    @PutMapping("/status")
    public JsonResult updateState(String ids, Integer status) {
        employeeService.updateState(ids, status);
        return JsonResult.ok();
    }

    @LogAnnotation(module = "修改密码")
    @ApiOperation(value = "修改密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPsw", value = "原密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPsw", value = "新密码", required = true, dataType = "String")
    })
    @PutMapping("/psw")
    public JsonResult updatePsw(String oldPsw, String newPsw) {
        String finalSecret = new BCryptPasswordEncoder().encode(oldPsw);
        employeeService.updatePsw(finalSecret, newPsw);
        return JsonResult.ok("密码修改成功");
    }

    @ApiOperation(value = "查找所有员工(按角色)(分页)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String")
    })
    @GetMapping("/findByRole")
    public PageResult<Employee> queryByRole(Integer page, Integer limit, String roleId, String searchKey, String searchValue) {
        PageQuery pageQuery = new PageQuery(page, limit);
        return employeeService.findByRole(pageQuery, StatusEnum.OK.getCode(), searchKey, searchValue, roleId);
    }

    @ApiOperation(value = "查找所有员工信息(不包括子部门人员)(分页,page/limit不给表示不分页)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "organizationId", value = "组织/公司/部门ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "搜索值（姓名/手机号）", dataType = "String")
    })
    @GetMapping("/findAllByOrgId")
    public PageResult<Employee> findAllByOrgId(Integer page, Integer limit, String organizationId, String searchValue) {
        if (page != null && limit != null) {
            PageQuery pageQuery = new PageQuery(page, limit);
            return employeeService.query(pageQuery, StatusEnum.OK.getCode(), organizationId, searchValue, "0");
        } else {
            return employeeService.query(null, StatusEnum.OK.getCode(), organizationId, searchValue, "0");
        }

    }

    @ApiOperation(value = "查找员工信息（根据Id）", notes = "")
    @ApiImplicitParam(name = "id", value = "员工ID", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonResult findById(String id) {
        return JsonResult.ok().put("data", employeeService.findById(id));
    }
}