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
 * Created by csw on 2019-11-07.
 * Description: 管理任务设置
 */

@Getter
@Setter
@Entity
@Table(name = "points_manager_task_emp")
@EntityListeners(AuditingEntityListener.class)
public class ManagerTaskEmp implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "organization_id", nullable = false, columnDefinition = "varchar(36) comment '组织机构（一级公司）'")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @Column(name = "task_id", nullable = false, columnDefinition = "varchar(36) comment '管理任务ID'")
    @ApiModelProperty("管理任务ID")
    private String taskId; // 管理任务ID

    @Column(name = "task_name", nullable = false, columnDefinition = "varchar(36) comment '任务名称'")
    @ApiModelProperty("任务名称")
    private String taskName; // 任务名称

    @Column(name = "task_date", nullable = false, columnDefinition = "datetime comment '任务日期（按月算）'")
    @ApiModelProperty("任务日期（按月算）")
    private Date taskDate; // 任务日期（按月算）

    @Column(name = "emp_id", nullable = false, columnDefinition = "varchar(36) comment '人员Id'")
    @ApiModelProperty("人员Id")
    private String empId; // 人员Id

    @Column(name = "emp_num", nullable = false, columnDefinition = "varchar(100) comment '员工工号'")
    @ApiModelProperty("员工工号")
    private String empNum; // 员工工号

    @Column(name = "emp_full_name", nullable = false, columnDefinition = "varchar(50) comment '人员姓名'")
    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @Column(name = "emp_cell_name", nullable = false, columnDefinition = "varchar(30) comment '人员手机号'")
    @ApiModelProperty("人员手机号")
    private String empCellName; // 人员手机号

    @Column(name = "emp_org_id", nullable = false, columnDefinition = "varchar(36) comment '人员部门Id'")
    @ApiModelProperty("人员部门Id")
    private String empOrgId; // 人员部门Id

    @Column(name = "emp_org_name", nullable = false, columnDefinition = "varchar(100) comment '人员部门'")
    @ApiModelProperty("人员部门")
    private String empOrgName; // 人员部门

    @Column(name = "title_level", columnDefinition = "varchar(255) comment '职位'")
    @ApiModelProperty("职位")
    private String titleLevel; // 职位


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
    @ApiModelProperty("任务信息对象")
    private ManagerTask managerTask; // 任务信息对象

    @Transient
    @ApiModelProperty("奖分任务值")
    private Integer scoreAwardMin; // 奖分任务值

    @Transient
    @ApiModelProperty("奖分任务已完成值")
    private Integer scoreAwardCompleted; // 奖分任务已完成值

    @Transient
    @ApiModelProperty("扣分任务值")
    private Integer scoreDeductMin; // 扣分任务值

    @Transient
    @ApiModelProperty("扣分任务已完成值")
    private Integer scoreDeductCompleted; // 扣分任务已完成值

    @Transient
    @ApiModelProperty("人次任务值")
    private Integer empCountValue; // 人次任务值

    @Transient
    @ApiModelProperty("人次任务已完成值")
    private Integer empCountValueCompleted; // 人次任务已完成值

    @Transient
    @ApiModelProperty("比例任务值")
    private Double ratioValue; // 比例任务值

    @Transient
    @ApiModelProperty("比例任务已完成值")
    private Double ratioValueCompleted; // 比例任务已完成值

}