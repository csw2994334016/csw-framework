package com.three.common.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by csw on 2019/07/17.
 * Description:
 */
@Getter
@Setter
public class LoginUser {

    private String id;
    private String username;
    private String password;
    private String fullName; // 姓名
    private String cellNum; // 手机号
    private Integer isAdmin;
    private String employeeId;
    /**
     * 状态
     */
    private Integer status;
    private Boolean enabled;
    private Date createDate;
    private Date updateDate;

    private Set<SysRole> sysRoles = new HashSet<>();

    private Set<SysAuthority> sysAuthorities = new HashSet<>();

    private SysOrganization sysOrganization;

    private SysEmployee sysEmployee;

    public LoginUser() {
    }

    public LoginUser(String id, String username, String password, String fullName, String cellNum, String employeeId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.cellNum = cellNum;
        this.employeeId = employeeId;
    }
}
