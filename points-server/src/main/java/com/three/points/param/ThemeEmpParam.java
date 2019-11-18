package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by csw on 2019/10/24.
 * Description:
 */
@Data
public class ThemeEmpParam {


    @NotNull(message = "人员ID不可以为空")
    @ApiModelProperty("人员ID")
    private String empId; // 人员ID

    @NotNull(message = "人员姓名不可以为空")
    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @ApiModelProperty("A分")
    private Integer ascore = 0; // A分

    @ApiModelProperty("B分")
    private Integer bscore = 0; // B分

}
