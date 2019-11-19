package com.three.points.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ManagerTaskEmpVo {

    private String id;

    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @ApiModelProperty("管理任务ID")
    private String taskId; // 管理任务ID

    @ApiModelProperty("任务名称")
    private String taskName; // 任务名称

    @ApiModelProperty("任务日期（按月算）")
    private Date taskDate; // 任务日期（按月算）

    @ApiModelProperty("人员Id")
    private String empId; // 人员Id

    @ApiModelProperty("员工工号")
    private String empNum; // 员工工号

    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @ApiModelProperty("人员手机号")
    private String empCellName; // 人员手机号

    @ApiModelProperty("人员部门Id")
    private String empOrgId; // 人员部门Id

    @ApiModelProperty("人员部门")
    private String empOrgName; // 人员部门

    @ApiModelProperty("职位")
    private String titleLevel; // 职位

    @ApiModelProperty("性别")
    private String gender; // 性别


    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

    @ApiModelProperty("记录状态：1=正常；2=锁定；3=删除")
    private Integer status; // 记录状态：1=正常；2=锁定；3=删除

    @ApiModelProperty("任务指标：奖分/扣分/人次/比例")
    private String taskIndex; // 任务指标：奖分/扣分/人次/比例

    @ApiModelProperty("完成得分")
    private String completedScore; // 完成得分

}
