package com.three.authserver.service;

import com.three.authserver.feign.UserClient;
import com.three.common.auth.LoginUser;
import com.three.common.auth.SysAuthority;
import com.three.common.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by csw on 2019/07/13.
 * Description:
 */
@Service
public class SysUserService {

    @Autowired
    private UserClient userClient;

    public SysUser findByUsername(String username) {
        LoginUser loginUser = userClient.findByUsername(username);
        SysUser sysUser = new SysUser();
        sysUser = (SysUser) BeanCopyUtil.copyBean(loginUser, sysUser);

        sysUser.setSysRoles(sysUser.getSysRoles());
        sysUser.setSysAuthorities(sysUser.getSysAuthorities());

        return sysUser;
    }


    public List<SysAuthority> findAllSysAuthorities() {
        return userClient.findAllAuthorities();
    }
}
