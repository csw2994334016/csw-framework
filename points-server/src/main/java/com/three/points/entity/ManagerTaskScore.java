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
 * Created by csw on 2019-12-11.
 * Description: 管理任务得分
 */

@Getter
@Setter
@Entity
@Table(name = "points_manager_task_score")
@EntityListeners(AuditingEntityListener.class)
public class ManagerTaskScore implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    @ApiModelProperty("主键ID")
    private String id;


    @Column(name = "organization_id", nullable = false, columnDefinition = "varchar(36) comment '组织机构（一级公司）'")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @Column(name = "task_id", nullable = false, columnDefinition = "varchar(36) comment '任务id'")
    @ApiModelProperty("任务id")
    private String taskId; // 任务id

    @Column(name = "task_name", nullable = false, columnDefinition = "varchar(255) comment '任务名称'")
    @ApiModelProperty("任务名称")
    private String taskName; // 任务名称

    @Column(name = "task_date", nullable = false, columnDefinition = "datetime comment '任务日期（按月算）'")
    @ApiModelProperty("任务日期（按月算）")
    private Date taskDate; // 任务日期（按月算）

    @Column(name = "emp_id", nullable = false, columnDefinition = "varchar(36) comment '人员Id'")
    @ApiModelProperty("人员Id")
    private String empId; // 人员Id

    @Column(name = "emp_full_name", nullable = false, columnDefinition = "varchar(255) comment '人员姓名'")
    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名


    @Column(name = "task_index", nullable = false, columnDefinition = "varchar(255) comment '任务指标'")
    @ApiModelProperty("任务指标")
    private String taskIndex; // 任务指标

    @Column(name = "task_type", nullable = false, columnDefinition = "varchar(255) comment '任务类型'")
    @ApiModelProperty("任务类型")
    private String taskType; // 任务类型

    @Column(name = "score_neg_type", nullable = false, columnDefinition = "varchar(255) comment '未完成扣分类型'")
    @ApiModelProperty("未完成扣分类型")
    private String scoreNegType; // 未完成扣分类型

    @Column(name = "bscore", nullable = false, columnDefinition = "int(11) default 1 comment '实际得分（B分）'")
    @ApiModelProperty("实际得分（B分）")
    private Integer bscore = 0; // 实际得分（B分）


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


}