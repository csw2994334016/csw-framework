package com.three.user.controller;

import com.three.common.auth.*;
import com.three.common.enums.AdminEnum;
import com.three.common.enums.StatusEnum;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.vo.JsonData;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import com.three.user.entity.Authority;
import com.three.user.entity.Organization;
import com.three.user.entity.Role;
import com.three.user.entity.User;
import com.three.user.repository.AuthorityRepository;
import com.three.user.service.EmployeeService;
import com.three.user.service.OrganizationService;
import com.three.user.service.UserService;
import com.three.user.vo.MenuVo;
import com.three.common.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2019/04/05.
 * Description:
 */
@Api(value = "系统管理", tags = "系统管理")
@RestController
@RequestMapping()
public class SysController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private OrganizationService organizationService;

    @ApiOperation(value = "获取个人信息")
    @GetMapping("/sys/userInfo")
    public JsonData<LoginUser> userInfo() {
        return new JsonData<>(LoginUserUtil.getLoginUser()).success();
    }

    @ApiOperation(value = "获取左侧菜单信息")
    @GetMapping("/sys/menuInfo")
    public JsonData<List<MenuVo>> menuInfo() {
        return new JsonData<>(userService.getMenuInfo()).success();
    }

    @ApiOperation(value = "重新加载组织机构-人员redis缓存")
    @GetMapping("/sys/reLoadOrgEmpRedis")
    public JsonResult reLoadOrgEmpRedis() {
        employeeService.reLoadOrgEmpRedis();
        return JsonResult.ok();
    }


    @ApiOperation(value = "按用户名查找用户（内部接口）")
    @GetMapping(value = "/internal/findByUsername")
    public LoginUser findByUsername(String username) {
        User user = userService.findByUsername(username);

        LoginUser loginUser = new LoginUser();
        loginUser = (LoginUser) BeanCopyUtil.copyBean(user, loginUser);

        for (Role role : user.getRoles()) {
            SysRole sysRole = new SysRole();
            sysRole = (SysRole) BeanCopyUtil.copyBean(role, sysRole);
            loginUser.getSysRoles().add(sysRole);
            for (Authority authority : role.getAuthorities()) {
                SysAuthority sysAuthority = new SysAuthority();
                sysAuthority = (SysAuthority) BeanCopyUtil.copyBean(authority, sysAuthority);
                loginUser.getSysAuthorities().add(sysAuthority);
            }
        }

        if (user.getEmployee() != null) { // 除非admin用户，不然一般都会有员工信息
            SysEmployee sysEmployee = new SysEmployee();
            sysEmployee = (SysEmployee) BeanCopyUtil.copyBean(user.getEmployee(), sysEmployee);
            loginUser.setSysEmployee(sysEmployee);

            Organization organization = organizationService.findById(user.getEmployee().getOrganizationId());
            SysOrganization sysOrganization = new SysOrganization();
            sysOrganization = (SysOrganization) BeanCopyUtil.copyBean(organization, sysOrganization);
            loginUser.setSysOrganization(sysOrganization);
        }
        return loginUser;
    }

    @ApiOperation(value = "查找系统管理员（内部接口）")
    @GetMapping(value = "/internal/findByAdmin")
    public LoginUser findByAdmin() {
        User user = userService.findByIsAdmin(AdminEnum.YES.getCode());

        LoginUser loginUser = new LoginUser();
        loginUser = (LoginUser) BeanCopyUtil.copyBean(user, loginUser);

//        for (Role role : user.getRoles()) {
//            SysRole sysRole = new SysRole();
//            sysRole = (SysRole) BeanCopyUtil.copyBean(role, sysRole);
//            loginUser.getSysRoles().add(sysRole);
//            for (Authority authority : role.getAuthorities()) {
//                SysAuthority sysAuthority = new SysAuthority();
//                sysAuthority = (SysAuthority) BeanCopyUtil.copyBean(authority, sysAuthority);
//                loginUser.getSysAuthorities().add(sysAuthority);
//            }
//        }
//
//        if (user.getEmployee() != null) { // 除非admin用户，不然一般都会有员工信息
//            SysOrganization sysOrganization = new SysOrganization();
//            sysOrganization = (SysOrganization) BeanCopyUtil.copyBean(user.getEmployee().getOrganization(), sysOrganization);
//            loginUser.setSysOrganization(sysOrganization);
//
//            SysEmployee sysEmployee = new SysEmployee();
//            sysEmployee = (SysEmployee) BeanCopyUtil.copyBean(user.getEmployee(), sysEmployee);
//            loginUser.setSysEmployee(sysEmployee);
//        }
        return loginUser;
    }

    @ApiOperation(value = "查找所有权限（内部接口）")
    @GetMapping(value = "/internal/findAllAuthorities")
    List<SysAuthority> findAllAuthorities() {
        List<SysAuthority> sysAuthorityList = new ArrayList<>();
        List<Authority> authorityList = authorityRepository.findAllByStatus(StatusEnum.OK.getCode());
        for (Authority authority : authorityList) {
            SysAuthority sysAuthority = new SysAuthority();
            sysAuthority = (SysAuthority) BeanCopyUtil.copyBean(authority, sysAuthority);
            sysAuthorityList.add(sysAuthority);
        }
        return sysAuthorityList;
    }
}
