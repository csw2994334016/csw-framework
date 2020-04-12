package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by csw on 2019-09-29.
 * Description:
 */
@Data
public class CustomGroupEmpParam1 {

    @NotBlank(message = "员工id不可以为空")
    @ApiModelProperty("员工id")
    private String empId; // 员工id

    @ApiModelProperty("员工工号")
    private String empNum; // 员工工号

    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @NotBlank(message = "人员部门Id不可以为空")
    @ApiModelProperty("人员部门Id")
    private String empOrgId; // 人员部门Id

    @ApiModelProperty("人员部门")
    private String empOrgName; // 人员部门

}