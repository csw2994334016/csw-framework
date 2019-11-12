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
import java.util.List;

/**
 * Created by csw on 2019-11-12.
 * Description: 积分规则设置
 */

@Getter
@Setter
@Entity
@Table(name = "points_points_rule")
@EntityListeners(AuditingEntityListener.class)
public class PointsRule implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "organization_id", nullable = false, columnDefinition = "varchar(36) comment '组织机构（一级公司）'")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @Column(name = "points_task_award_score", nullable = false, columnDefinition = "int(11) comment '发布积分任务奖分设置，奖?分'")
    @ApiModelProperty("发布积分任务奖分设置，奖?分")
    private Integer pointsTaskAwardScore; // 发布积分任务奖分设置，奖?分

    @Column(name = "ab_score_value", nullable = false, columnDefinition = "int(11) comment 'A/B分比例设置，1A分=?B分'")
    @ApiModelProperty("A/B分比例设置，1A分=?B分")
    private Integer abScoreValue; // A/B分比例设置，1A分=?B分

    @Column(name = "manager_task_day", nullable = false, columnDefinition = "int(11) comment '管理任务结算日期设置，每月?日'")
    @ApiModelProperty("管理任务结算日期设置，每月?日")
    private Integer managerTaskDay; // 管理任务结算日期设置，每月?日


    @Column(name = "remark", columnDefinition = "varchar(500) comment '描述/备注'")
    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

    @Column(nullable = false, columnDefinition = "int(2) default 1 comment '记录状态：1=正常；2=锁定；3=删除'")
    @ApiModelProperty("记录状态：1=正常；2=锁定；3=删除")
    private Integer status = StatusEnum.OK.getCode(); // 记录状态：1=正常；2=锁定；3=删除

    @CreatedDate
    @ApiModelProperty("创建时间")
    private Date createDate; // 创建时间

    @LastModifiedDate
    @ApiModelProperty("修改时间")
    private Date updateDate; // 修改时间

    @Transient
    @ApiModelProperty("记录人奖扣人次奖分设置")
    private List<PointsRuleEmpCount> pointsRuleEmpCountList; // 记录人奖扣人次奖分设置


}