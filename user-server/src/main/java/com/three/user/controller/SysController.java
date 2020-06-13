package com.three.user.controller;

import com.three.common.auth.*;
import com.three.common.enums.AdminEnum;
import com.three.common.enums.StatusEnum;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.vo.JsonData;
import com.three.common.vo.PageResult;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import com.three.user.entity.*;
import com.three.user.repository.AuthorityRepository;
import com.three.user.service.EmployeeService;
import com.three.user.service.OrganizationService;
import com.three.user.service.RedisService;
import com.three.user.service.UserService;
import com.three.common.vo.JsonResult;
import com.three.user.vo.MenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
    private RedisService redisService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EmployeeService employeeService;

    @ApiOperation(value = "获取个人信息")
    @GetMapping("/sys/userInfo")
    public JsonData<LoginUser> userInfo() {
        return new JsonData<>(LoginUserUtil.getLoginUser()).success();
    }

    @ApiOperation(value = "获取左侧菜单信息")
    @GetMapping("/sys/menuInfo")
    public JsonData<List<Map>> menuInfo() {
        List<MenuVo> menuVo1List = userService.getMenuInfo();
        List<Map> mapList = new ArrayList<>();
        changeToMap(menuVo1List, mapList);
        return new JsonData<>(mapList).success();
    }

    private void changeToMap(List<MenuVo> menuVoList, List<Map> mapList) {
        for (MenuVo menuVo : menuVoList) {
            Map<Object, Object> map = new HashMap<>();
            map.put("path", menuVo.getPath());
            map.put("name", menuVo.getName());
            map.put("icon", menuVo.getIcon());
            if (menuVo.getCompName() != null) {
                map.put("compName", menuVo.getCompName());
            }
            if (menuVo.getCompPath() != null) {
                map.put("compPath", menuVo.getCompPath());
            }
            if (menuVo.getChildren().size() > 0) {
                List<Map> mapList1 = new ArrayList<>();
                changeToMap(menuVo.getChildren(), mapList1);
                map.put("children", mapList1);
            }
            mapList.add(map);
        }
    }

    @ApiOperation(value = "重新加载组织机构-人员redis缓存")
    @GetMapping("/sys/reLoadOrgEmpRedis")
    public JsonResult reLoadOrgEmpRedis() {
        redisService.reLoadOrgEmpRedis();
        return JsonResult.ok("成功重新加载组织机构-人员redis缓存");
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
    public LoginUser findByAdmin(String firstOrganizationId) {
        User user = userService.findByAdmin(AdminEnum.YES.getCode(), firstOrganizationId);

        LoginUser loginUser = new LoginUser();
        loginUser = (LoginUser) BeanCopyUtil.copyBean(user, loginUser);

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

    @ApiOperation(value = "根据组织机构Id查找用户Id（内部接口）")
    @GetMapping(value = "/internal/findEmpIdSetByOrgId")
    Set<String> findSysEmployeeSet(@RequestParam() String orgId, @RequestParam() String containChildFlag) {
        Set<String> empIdSet = new HashSet<>();
        PageResult<Employee> pageResult = employeeService.query(null, StatusEnum.OK.getCode(), orgId, null, containChildFlag, "0", "0");
        for (Employee employee : pageResult.getData()) {
            empIdSet.add(employee.getId());
        }
        return empIdSet;
    }
}
