package com.three.user.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by csw on 2020-07-06.
 * Description: 服务信息
 */

@Getter
@Setter
@Entity
@Table(name = "sys_server")
@EntityListeners(AuditingEntityListener.class)
public class Server implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    @ApiModelProperty("主键ID")
    private String id;


    @Column(name = "server_id", unique = true, nullable = false, columnDefinition = "varchar(50) comment '服务ID'")
    @ApiModelProperty("服务ID")
    private String serverId;

    @Column(name = "server_name", nullable = false, columnDefinition = "varchar(255) comment '服务名称'")
    @ApiModelProperty("服务名称")
    private String serverName;

    @Column(name = "server_type", nullable = false, columnDefinition = "int(2) default 1 comment '服务类型：1=业务服务；2=基础服务'")
    @ApiModelProperty("服务类型：1=业务服务；2=基础服务")
    private Integer serverType = 1;


    @Column(name = "remark", columnDefinition = "varchar(500) comment '描述/备注'")
    @ApiModelProperty("描述/备注")
    private String remark;

    @Column(name = "status", nullable = false, columnDefinition = "int(2) default 1 comment '记录状态：1=正常；2=锁定；3=删除'")
    @ApiModelProperty("记录状态：1=正常；2=锁定；3=删除")
    private Integer status = 1;

    @CreatedDate
    @ApiModelProperty("创建时间")
    private Date createDate;

    @LastModifiedDate
    @ApiModelProperty("修改时间")
    private Date updateDate;


}