package com.three.user.entity;

import com.three.common.enums.AuthorityEnum;
import com.three.common.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by csw on 2019/03/22.
 * Description: 菜单/接口权限
 */

@Getter
@Setter
@Entity
@Table(name = "sys_authority")
@EntityListeners(AuditingEntityListener.class)
public class Authority implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "organization_id", columnDefinition = "varchar(36) comment '组织机构（一级公司）'")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @Column(unique = true, columnDefinition = "varchar(255) comment '菜单/接口权限路径'")
    @ApiModelProperty("菜单/接口权限路径")
    private String authorityUrl;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(255) comment '菜单/接口权限名称'")
    @ApiModelProperty("菜单/接口权限名称")
    private String authorityName;

    @Column(nullable = false, length = 2, columnDefinition = "int default 2 comment '菜单/接口权限类型'")
    @ApiModelProperty("菜单/接口权限类型")
    private Integer authorityType = AuthorityEnum.BUTTON.getCode();

    @Column(columnDefinition = "varchar(255) comment '菜单/接口权限图标'")
    @ApiModelProperty("菜单/接口权限图标")
    private String authorityIcon;

    @Column(columnDefinition = "varchar(255) comment '组件名称'")
    @ApiModelProperty("组件名称")
    private String compName;

    @Column(columnDefinition = "varchar(255) comment '组件路径'")
    @ApiModelProperty("组件路径")
    private String compPath;

    @Column(nullable = false, columnDefinition = "varchar(36) default '-1' comment '菜单/接口父权限ID'")
    @ApiModelProperty("菜单/接口父权限ID")
    private String parentId;

    @Column(columnDefinition = "varchar(2000) comment '菜单/接口父权限IDS'")
    @ApiModelProperty("菜单/接口父权限IDS")
    private String parentIds;

    @Column(columnDefinition = "varchar(255) comment '菜单/接口父权限名称'")
    @ApiModelProperty("菜单/接口父权限名称")
    private String parentName;

    @Column(name = "sort", nullable = false, columnDefinition = "int(11) default 1 comment '排序'")
    @ApiModelProperty("排序")
    private Integer sort = 1;


    @Column(name = "remark", columnDefinition = "varchar(500) comment '描述/备注'")
    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

    @Column(nullable = false, columnDefinition = "int(2) default 1 comment '记录状态：1=正常；2=锁定；3=删除'")
    @ApiModelProperty("记录状态：1=正常；2=锁定；3=删除")
    private Integer status = StatusEnum.OK.getCode(); // 记录状态：1=正常；2=锁定；3=删除

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date updateDate;

    @ManyToMany(mappedBy = "authorities")
    @JsonIgnore
    private Set<Role> roles = new HashSet<>(0);

    @Transient
    private List<Authority> children = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return id.equals(authority.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
