package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by csw on 2019-11-06.
 * Description:
 */
@Data
public class ManagerTaskEmpParam {

    @NotBlank(message = "人员Id")
    @ApiModelProperty("人员Id")
    private String empId; // 人员Id

    @NotBlank(message = "员工工号")
    @ApiModelProperty("员工工号")
    private String empNum; // 员工工号

    @NotBlank(message = "人员姓名")
    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @NotBlank(message = "人员手机号")
    @ApiModelProperty("人员手机号")
    private String empCellName; // 人员手机号

    @NotBlank(message = "人员部门Id")
    @ApiModelProperty("人员部门Id")
    private String empOrgId; // 人员部门Id

    @NotBlank(message = "人员部门")
    @ApiModelProperty("人员部门")
    private String empOrgName; // 人员部门

    @ApiModelProperty("职位")
    private String titleLevel; // 职位

}