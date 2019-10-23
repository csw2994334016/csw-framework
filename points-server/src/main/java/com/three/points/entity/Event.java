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
 * Description: 事件
 */

@Getter
@Setter
@Entity
@Table(name = "points_event")
@EntityListeners(AuditingEntityListener.class)
public class Event implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "organization_id", nullable = false, columnDefinition = "varchar(36) comment '组织机构（一级公司）'")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @Column(name = "type_id", nullable = false, columnDefinition = "varchar(36) comment '事件分类ID'")
    @ApiModelProperty("事件分类ID")
    private String typeId; // 事件分类ID

    @Column(name = "type_name", columnDefinition = "varchar(255) comment '事件分类'")
    @ApiModelProperty("事件分类")
    private String typeName; // 事件分类

    @Column(name = "event_name", nullable = false, columnDefinition = "varchar(1000) comment '事件名称'")
    @ApiModelProperty("事件名称")
    private String eventName; // 事件名称

    @Column(name = "a_score_min", nullable = false, columnDefinition = "int(11) comment 'A分最小值'")
    @ApiModelProperty("A分最小值")
    private Integer aScoreMin; // A分最小值

    @Column(name = "a_score_max", nullable = false, columnDefinition = "int(11) comment 'A分最大值'")
    @ApiModelProperty("A分最大值")
    private Integer aScoreMax; // A分最大值

    @Column(name = "b_score_min", nullable = false, columnDefinition = "int(11) comment 'B分最小值'")
    @ApiModelProperty("B分最小值")
    private Integer bScoreMin; // B分最小值

    @Column(name = "b_score_max", nullable = false, columnDefinition = "int(11) comment 'B分最大值'")
    @ApiModelProperty("B分最大值")
    private Integer bScoreMax; // B分最大值

    @Column(name = "prize_flag", columnDefinition = "int(1) comment '奖票事件：1=是；0=否'")
    @ApiModelProperty("奖票事件：1=是；0=否")
    private Integer prizeFlag; // 奖票事件：1=是；0=否

    @Column(name = "count_flag", columnDefinition = "int(1) comment '记件事件：1=是；0=否'")
    @ApiModelProperty("记件事件：1=是；0=否")
    private Integer countFlag; // 记件事件：1=是；0=否

    @Column(name = "audit_flag", columnDefinition = "int(1) comment '专人审核：1=是；0=否'")
    @ApiModelProperty("专人审核：1=是；0=否")
    private Integer auditFlag; // 专人审核：1=是；0=否

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