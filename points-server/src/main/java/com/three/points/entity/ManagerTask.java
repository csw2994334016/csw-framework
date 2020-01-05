package com.three.points.entity;

import com.three.common.enums.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
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
 * Created by csw on 2019-11-06.
 * Description: 管理任务设置
 */

@Getter
@Setter
@Entity
@Table(name = "points_manager_task")
@EntityListeners(AuditingEntityListener.class)
public class ManagerTask implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "organization_id", nullable = false, columnDefinition = "varchar(36) comment '组织机构（一级公司）'")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @Column(name = "task_name", nullable = false, columnDefinition = "varchar(255) comment '任务名称'")
    @ApiModelProperty("任务名称")
    private String taskName; // 任务名称，相同任务名称的分为一组，每组下面每月一条记录

    @Column(name = "task_date", nullable = false, columnDefinition = "datetime comment '任务日期（按月算）'")
    @ApiModelProperty("任务日期（按月算）")
    private Date taskDate; // 任务日期（按月算）

    @Column(name = "next_task_id", columnDefinition = "varchar(36) comment '下一个月管理任务的id'")
    @ApiModelProperty("下一个月管理任务的id")
    private String nextTaskId; // 下一个月管理任务的id

    @Column(name = "score_task_flag", nullable = false, columnDefinition = "int(1) default 2 comment '奖扣分考核任务（1=是；2=否）'")
    @ApiModelProperty("奖扣分考核任务（1=是；2=否）")
    private Integer scoreTaskFlag = 2; // 奖扣分考核任务（1=是；2=否）

    @Column(name = "score_cycle", columnDefinition = "int(1) comment '奖扣分周期（1=日任务；2=周任务；3=月任务）'")
    @ApiModelProperty("奖扣分周期（1=日任务；2=周任务；3=月任务）")
    private Integer scoreCycle; // 奖扣分周期（1=日任务；2=周任务；3=月任务）

    @Column(name = "score_award_min", columnDefinition = "int(11) default 0 comment '奖分下限'")
    @ApiModelProperty("奖分下限")
    private Integer scoreAwardMin = 0; // 奖分下限

    @Column(name = "score_deduct_min", columnDefinition = "int(11) default 0 comment '扣分下限'")
    @ApiModelProperty("扣分下限")
    private Integer scoreDeductMin = 0; // 扣分下限

    @Column(name = "score_neg_score", nullable = false, columnDefinition = "int(11) default 0 comment '未完成奖扣分任务扣分，默认0表示差额扣分'")
    @ApiModelProperty("未完成奖扣分任务扣分，默认0表示差额扣分")
    private Integer scoreNegScore = 0; // 未完成奖扣分任务扣分，默认0表示差额扣分

    @Column(name = "ratio_task_flag", nullable = false, columnDefinition = "int(1) default 2 comment '奖扣比例考核任务（1=是；2=否）'")
    @ApiModelProperty("奖扣比例考核任务（1=是；2=否）")
    private Integer ratioTaskFlag = 2; // 奖扣比例考核任务（1=是；2=否）

    @Column(name = "ratio_cycle", columnDefinition = "int(1) comment '奖扣比例周期（1=日任务；2=周任务；3=月任务）'")
    @ApiModelProperty("奖扣比例周期（1=日任务；2=周任务；3=月任务）")
    private Integer ratioCycle; // 奖扣比例周期（1=日任务；2=周任务；3=月任务）

    @Column(name = "ratio_value", columnDefinition = "double default 1.0 comment '奖扣比例值'")
    @ApiModelProperty("奖扣比例值")
    private Double ratioValue = 1.0; // 奖扣比例值

    @Column(name = "ratio_neg_score", nullable = false, columnDefinition = "int(11) default 0 comment '未完成奖扣比例任务扣分，默认0表示差额扣分'")
    @ApiModelProperty("未完成奖扣比例任务扣分，默认0表示差额扣分")
    private Integer ratioNegScore = 0; // 未完成奖扣比例任务扣分，默认0表示差额扣分

    @Column(name = "emp_count_task_flag", nullable = false, columnDefinition = "int(1) default 2 comment '奖扣人次考核任务（1=是；2=否）'")
    @ApiModelProperty("奖扣人次考核任务（1=是；2=否）")
    private Integer empCountTaskFlag = 2; // 奖扣人次考核任务（1=是；2=否）

    @Column(name = "emp_count_cycle", columnDefinition = "int(1) comment '奖扣人次周期（1=日任务；2=周任务；3=月任务）'")
    @ApiModelProperty("奖扣人次周期（1=日任务；2=周任务；3=月任务）")
    private Integer empCountCycle; // 奖扣人次周期（1=日任务；2=周任务；3=月任务）

    @Column(name = "emp_count_value", columnDefinition = "int(11) default 0 comment '奖扣人次值'")
    @ApiModelProperty("奖扣人次值")
    private Integer empCountValue = 0; // 奖扣人次值

    @Column(name = "emp_count_neg_score", nullable = false, columnDefinition = "int(11) default 0 comment '未完成奖扣人次任务扣分，默认0表示不扣分'")
    @ApiModelProperty("未完成奖扣人次任务扣分，默认0表示不扣分")
    private Integer empCountNegScore = 0; // 未完成奖扣人次任务扣分，默认0表示不扣分


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
    @ApiModelProperty("管理任务下的考核人员")
    private List<ManagerTaskEmp> managerTaskEmpList; // 管理任务下的考核人员

    @Transient
    @ApiModelProperty("key值，跟id同值")
    private String key; // key值，跟id同值

    @Transient
    @ApiModelProperty("title值，跟taskName同值")
    private String title; // title值，跟taskName同值

    public String getKey() {
        return id;
    }

    public String getTitle() {
        return taskName;
    }
}