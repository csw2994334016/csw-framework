package com.three.user.controller;

import com.three.common.vo.JsonData;
import com.three.commonclient.exception.BusinessException;
import com.three.resource_jpa.jpa.file.entity.FileInfo;
import com.three.resource_jpa.jpa.file.service.FileInfoService;
import com.three.user.entity.Employee;
import com.three.user.entity.Role;
import com.three.user.param.EmployeeParam;
import com.three.user.service.EmployeeService;
import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.user.service.UploadFileService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

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

    @Autowired
    private FileInfoService fileInfoService;

    @Autowired
    private UploadFileService uploadFileService;

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
    public JsonResult delete(@RequestParam(required = true) String ids) {
        employeeService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("员工信息删除成功");
    }

    @ApiOperation(value = "查找所有员工信息（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "organizationId", value = "组织/公司/部门ID", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "搜索值（姓名/手机号）", dataType = "String"),
            @ApiImplicitParam(name = "containChildFlag", value = "包含子部门人员标记：0=不包含；1=包含（默认0）", defaultValue = "0", dataType = "String"),
            @ApiImplicitParam(name = "taskFilterFlag", value = "过滤已分配管理任务的人员：0=不过滤；1=过滤（默认0）", defaultValue = "0", dataType = "String"),
            @ApiImplicitParam(name = "awardPrivilegeFilterFlag", value = "过滤已分配积分奖扣权限的人员：0=不过滤；1=过滤（默认0）", defaultValue = "0", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<Employee> query(Integer page, Integer limit, String organizationId, String searchValue, @RequestParam(defaultValue = "0") String containChildFlag, @RequestParam(defaultValue = "0") String taskFilterFlag, @RequestParam(defaultValue = "0") String awardPrivilegeFilterFlag) {
        if (page != null && limit != null) {
            PageQuery pageQuery = new PageQuery(page, limit);
            BeanValidator.check(pageQuery);
            return employeeService.query(pageQuery, StatusEnum.OK.getCode(), organizationId, searchValue, containChildFlag, taskFilterFlag, awardPrivilegeFilterFlag);
        } else {
            return employeeService.query(null, StatusEnum.OK.getCode(), organizationId, searchValue, containChildFlag, taskFilterFlag, awardPrivilegeFilterFlag);
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
    @GetMapping("/psw")
    public JsonResult updatePsw(@RequestParam(required = true) String oldPsw, @RequestParam(required = true) String newPsw) {
        employeeService.updatePsw(oldPsw, newPsw);
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

    @ApiOperation(value = "查找员工信息（根据Id）", notes = "")
    @ApiImplicitParam(name = "id", value = "员工ID", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonData<Employee> findById(String id) {
        return new JsonData<>(employeeService.findById(id)).success();
    }

    @ApiOperation(value = "查找员工已绑定的角色信息（根据Id）", notes = "")
    @ApiImplicitParam(name = "id", value = "员工ID", required = true, dataType = "String")
    @GetMapping("/findRolesById")
    public JsonData<Set<Role>> findRolesById(@RequestParam(required = true) String id) {
        return new JsonData<>(employeeService.findRolesById(id)).success();
    }

    @ApiOperation(value = "查找积分奖扣审核人员列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attnOrAuditFlag", value = "查初审人/终审人标记：0=查初审人；1=查终审人（默认0）", defaultValue = "0", dataType = "String"),
            @ApiImplicitParam(name = "attnId", value = "初审人Id", dataType = "String"),
            @ApiImplicitParam(name = "aPosScoreMax", value = "临时事件下,参与人员中奖A分(正分)最大值", dataType = "Integer"),
            @ApiImplicitParam(name = "aNegScoreMin", value = "临时事件下,参与人员中扣A分(负分)最小值", dataType = "Integer"),
            @ApiImplicitParam(name = "bPosScoreMax", value = "临时事件下,参与人员中奖B分(正分)最大值", dataType = "Integer"),
            @ApiImplicitParam(name = "bNegScoreMin", value = "临时事件下,参与人员中扣B分(负分)最小值", dataType = "Integer"),
            @ApiImplicitParam(name = "themeEmpIds", value = "奖扣对象，多个对象用英文逗号隔开，目的是终审人不能是奖扣对象自己", dataType = "String"),
    })
    @GetMapping("/findAuditor")
    public JsonData<List<Employee>> findAuditor(@RequestParam(defaultValue = "0") String attnOrAuditFlag, String attnId, Integer aPosScoreMax, Integer aNegScoreMin, Integer bPosScoreMax, Integer bNegScoreMin, String themeEmpIds) {
        return new JsonData<>(employeeService.findAuditor(attnOrAuditFlag, attnId, aPosScoreMax, aNegScoreMin, bPosScoreMax, bNegScoreMin, themeEmpIds));
    }

    @LogAnnotation(module = "上传头像")
    @ApiOperation(value = "上传头像", notes = "")
    @ApiImplicitParam(name = "file", value = "图片", required = true, dataType = "MultipartFile")
    @PostMapping("/uploadHeadPortrait")
    public JsonData<FileInfo> uploadHeadPortrait(@RequestParam(value = "file", required = true) MultipartFile file) {
        FileInfo fileInfo;
        try {
            fileInfo = uploadFileService.upload(file);
        } catch (Exception e) {
            throw new BusinessException("上传头像失败：" + e.getMessage());
        }
        return new JsonData<>(fileInfo);
    }
}