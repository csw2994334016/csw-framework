package com.three.resource_jpa.jpa.script.entity;

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
 * Created by csw on 2019/03/30.
 * Description:
 */
@Getter
@Setter
@Entity
@Table(name = "three_script")
@EntityListeners(AuditingEntityListener.class)
public class Script implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    @ApiModelProperty("主键ID")
    private String id;


    @Column(name = "script_name", nullable = false, columnDefinition = "varchar(255) comment '脚本名称'")
    @ApiModelProperty("脚本名称")
    private String scriptName;

    @Lob
    @Column(name = "script_code", columnDefinition = "text comment '代码'")
    @ApiModelProperty("代码")
    private String scriptCode;

    @Column(name = "version", nullable = false, columnDefinition = "int default 1 comment '版本'")
    @ApiModelProperty("版本")
    private Integer version = 1;


    @Column(name = "remark", columnDefinition = "varchar(500) comment '描述/备注'")
    @ApiModelProperty("描述/备注")
    private String remark;

    @Column(name = "status", nullable = false, columnDefinition = "int(2) default 1 comment '记录状态：1=正常；2=锁定；3=删除'")
    @ApiModelProperty("记录状态：1=正常；2=锁定；3=删除")
    private Integer status = 1;

    @CreatedDate
    @ApiModelProperty("创建时间")
    @Column(name = "create_date", columnDefinition = "datetime comment '创建时间'")
    private Date createDate;

    @LastModifiedDate
    @ApiModelProperty("修改时间")
    @Column(name = "update_date", columnDefinition = "datetime comment '修改时间'")
    private Date updateDate;

}
