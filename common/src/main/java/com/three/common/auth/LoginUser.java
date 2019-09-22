package com.three.common.auth;

import com.three.common.auth.SysAuthority;
import com.three.common.auth.SysRole;
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

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String headImgUrl;
    private String phone;
    private Integer sex;
    private Integer isAdmin;
    /**
     * 状态
     */
    private Integer status;
    private Boolean enabled;
    private String type;
    private Date createDate;
    private Date updateDate;

    private Set<SysRole> sysRoles = new HashSet<>();

    private Set<SysAuthority> sysAuthorities = new HashSet<>();
}
