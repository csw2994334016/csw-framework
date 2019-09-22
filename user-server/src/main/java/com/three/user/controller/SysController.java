package com.three.user.controller;

import com.three.common.auth.LoginUser;
import com.three.common.auth.SysAuthority;
import com.three.common.auth.SysRole;
import com.three.common.enums.StatusEnum;
import com.three.common.utils.BeanCopyUtil;
import com.three.resource_security.utils.LoginUserUtil;
import com.three.user.entity.Authority;
import com.three.user.entity.Role;
import com.three.user.entity.User;
import com.three.user.repository.AuthorityRepository;
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

    @ApiOperation(value = "获取个人信息")
    @GetMapping("/sys/userInfo")
    public JsonResult userInfo() {
        return JsonResult.ok().put("user", LoginUserUtil.getLoginUser());
    }

    @ApiOperation(value = "获取左侧菜单信息")
    @GetMapping("/sys/menuInfo")
    public JsonResult menuInfo() {
        List<MenuVo> menuVoList = userService.getMenuInfo();
        return JsonResult.ok().put("data", menuVoList);
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
