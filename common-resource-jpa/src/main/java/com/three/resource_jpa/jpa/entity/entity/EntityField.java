package com.three.resource_jpa.jpa.entity.entity;

import com.three.common.enums.StatusEnum;
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
 * Created by  on 2020-06-26.
 * Description: 实体字段
 */

@Getter
@Setter
@Entity
@Table(name = "three_entity_field")
@EntityListeners(AuditingEntityListener.class)
public class EntityField implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    @ApiModelProperty("主键ID")
    private String id;


    @Column(name = "entity_pojo_id", nullable = false, columnDefinition = "varchar(36) comment '实体类ID'")
    @ApiModelProperty("实体类ID")
    private String entityPojoId; // 实体类ID

    @Column(name = "column_name", nullable = false, columnDefinition = "varchar(255) comment '字段名'")
    @ApiModelProperty("字段名")
    private String columnName; // 字段名

    @Column(name = "column_type", nullable = false, columnDefinition = "varchar(255) comment '类型'")
    @ApiModelProperty("类型")
    private String columnType; // 类型

    @Column(name = "column_key", columnDefinition = "varchar(255) comment '关键字'")
    @ApiModelProperty("关键字")
    private String columnKey; // 关键字

    @Column(name = "null_flag", columnDefinition = "int(1) default 1 comment '是否空值：1=可空；0=不可空'")
    @ApiModelProperty("是否空值：1=可空；0=不可空")
    private Integer nullFlag = 1; // 是否空值：1=可空；0=不可空

    @Column(name = "column_length", columnDefinition = "int(11) comment '长度'")
    @ApiModelProperty("长度")
    private Integer columnLength; // 长度

    @Column(name = "column_comment", columnDefinition = "varchar(255) comment '描述'")
    @ApiModelProperty("描述")
    private String columnComment; // 描述

    @Column(name = "default_value", columnDefinition = "int(11) comment '默认值'")
    @ApiModelProperty("默认值")
    private Integer defaultValue; // 默认值

    @Column(name = "status", nullable = false, columnDefinition = "int(2) default 1 comment '记录状态：1=正常；2=锁定；3=删除'")
    @ApiModelProperty("记录状态：1=正常；2=锁定；3=删除")
    private Integer status = StatusEnum.OK.getCode(); // 记录状态：1=正常；2=锁定；3=删除

    @CreatedDate
    @ApiModelProperty("创建时间")
    @Column(name = "create_date", columnDefinition = "datetime comment '创建时间'")
    private Date createDate; // 创建时间

    @LastModifiedDate
    @ApiModelProperty("修改时间")
    @Column(name = "update_date", columnDefinition = "datetime comment '修改时间'")
    private Date updateDate; // 修改时间


}