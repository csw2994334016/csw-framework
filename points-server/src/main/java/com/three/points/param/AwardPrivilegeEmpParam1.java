package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2019-09-29.
 * Description:
 */
@Data
public class AwardPrivilegeEmpParam1 {

    @NotBlank(message = "员工id不可以为空")
    @ApiModelProperty("员工id")
    private String empId; // 员工id

    @ApiModelProperty("员工工号")
    private String empNum; // 员工工号

    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

}