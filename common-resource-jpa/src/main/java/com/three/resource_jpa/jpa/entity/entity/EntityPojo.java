package com.three.resource_jpa.jpa.entity.entity;

import com.three.common.enums.StatusEnum;
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
 * Created by  on 2020-06-26.
 * Description: 实体管理
 */

@Getter
@Setter
@Entity
@Table(name = "three_entity_pojo")
@EntityListeners(AuditingEntityListener.class)
public class EntityPojo implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    @ApiModelProperty("主键ID")
    private String id;


    @Column(name = "entity_name", nullable = false, columnDefinition = "varchar(255) comment '实体类名称'")
    @ApiModelProperty("实体类名称")
    private String entityName; // 实体类名称

    @Column(name = "entity_package_name", nullable = false, columnDefinition = "varchar(255) comment '实体类包全名称'")
    @ApiModelProperty("实体类包全名称")
    private String entityPackageName; // 实体类包全名称

    @Lob
    @Column(name = "entity_code", columnDefinition = "text comment '实体类代码'")
    @ApiModelProperty("实体类代码")
    private String entityCode; // 实体类代码

    @Column(name = "entity_table_name", columnDefinition = "varchar(255) comment '实体表名'")
    @ApiModelProperty("实体表名")
    private String entityTableName; // 实体表名

    @Column(name = "version", nullable = false, columnDefinition = "int(11) default 1 comment '版本'")
    @ApiModelProperty("版本")
    private Integer version = 1; // 版本

    @Column(name = "meta_flag", nullable = false, columnDefinition = "int(1) default 1 comment '实体标记：1=实体；0=虚拟实体'")
    @ApiModelProperty("实体标记：1=实体；0=虚拟实体")
    private Integer metaFlag = 1; // 实体标记：1=实体；0=虚拟实体


    @Column(name = "remark", columnDefinition = "varchar(500) comment '描述/备注'")
    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

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