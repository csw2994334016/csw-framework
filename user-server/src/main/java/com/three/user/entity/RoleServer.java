package com.three.user.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by csw on 2020-07-06.
 * Description: 服务信息
 */

@Getter
@Setter
@Entity
@Table(name = "sys_role_server")
@EntityListeners(AuditingEntityListener.class)
public class RoleServer implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    @ApiModelProperty("主键ID")
    private String id;

    @Column(name = "role_id", nullable = false, columnDefinition = "varchar(36) comment '角色ID'")
    @ApiModelProperty("角色ID")
    private String roleId;

    @Column(name = "server_id", nullable = false, columnDefinition = "varchar(36) comment '服务ID'")
    @ApiModelProperty("服务ID")
    private String serverId;

    @CreatedDate
    @ApiModelProperty("创建时间")
    private Date createDate;

    @LastModifiedDate
    @ApiModelProperty("修改时间")
    private Date updateDate;


}