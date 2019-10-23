package com.three.authserver.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.three.common.auth.SysAuthority;
import com.three.common.auth.SysEmployee;
import com.three.common.auth.SysOrganization;
import com.three.common.auth.SysRole;
import com.three.common.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * spring security当前登录对象
 */
@Getter
@Setter
public class SysUser implements UserDetails {

    private static final long serialVersionUID = 1753977564987556640L;

    private String id;
    private String username;
    private String password;
    private String fullName; // 姓名
    private String headImgUrl;
    private String phone;
    private Integer isAdmin;
    /**
     * 状态
     */
    private Integer status;
    private Boolean enabled;
    private String type;
    private Date createDate;
    private Date updateDate;

    private Set<SysRole> sysRoles;

    private Set<SysAuthority> sysAuthorities;

    private SysOrganization sysOrganization;

    private SysEmployee sysEmployee;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new HashSet<>();
//        if (!CollectionUtils.isEmpty(sysRoles)) {
//            sysRoles.forEach(role -> {
//                if (role.getCode().startsWith("ROLE_")) {
//                    collection.add(new SimpleGrantedAuthority(role.getCode()));
//                } else {
//                    collection.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
//                }
//            });
//        }

        if (!CollectionUtils.isEmpty(sysAuthorities)) {
            sysAuthorities.forEach(sysAuthority -> {
                collection.add(new SimpleGrantedAuthority(sysAuthority.getAuthorityUrl()));
            });
        }

        return collection;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getStatus() != StatusEnum.FROZEN.getCode();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getStatus() == StatusEnum.OK.getCode();
    }
}
