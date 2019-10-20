package com.three.points.entity;

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
 * Created by csw on 2019-10-20.
 * Description: 事件分类
 */

@Getter
@Setter
@Entity
@Table(name = "points_event_type")
@EntityListeners(AuditingEntityListener.class)
public class EventType implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "organization_id", nullable = false, columnDefinition = "varchar(36) comment '组织机构（一级公司）'")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @Column(name = "parent_id", nullable = false, columnDefinition = "varchar(36) default '-1' comment '父级分类ID'")
    @ApiModelProperty("父级分类ID")
    private String parentId = "-1"; // 父级分类ID

    @Column(name = "parent_name", columnDefinition = "varchar(100) comment '父级分类名称'")
    @ApiModelProperty("父级分类名称")
    private String parentName; // 父级分类名称

    @Column(name = "type_name", nullable = false, columnDefinition = "varchar(100) comment '分类名称'")
    @ApiModelProperty("分类名称")
    private String typeName; // 分类名称

    @Column(name = "sort", nullable = false, columnDefinition = "int(11) default 100 comment '排序'")
    @ApiModelProperty("排序")
    private Integer sort = 100; // 排序


    @Column(name = "remark", columnDefinition = "varchar(500) comment '描述/备注'")
    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

    @Column(nullable = false, columnDefinition = "int(2) default 1 comment '记录状态：1=正常；2=锁定；3=删除'")
    @ApiModelProperty("记录状态：1=正常；2=锁定；3=删除")
    private Integer status = StatusEnum.OK.getCode(); // 记录状态：1=正常；2=锁定；3=删除

    @CreatedDate
    private Date createDate; // 创建时间

    @LastModifiedDate
    private Date updateDate; // 修改时间


}