package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by csw on 2019-09-29.
 * Description:
 */
@Data
public class AwardPrivilegeParam {

    private String id;

    @NotBlank(message = "奖扣权限名称不可以为空")
    @ApiModelProperty("奖扣权限名称")
    private String name; // 奖扣权限名称

    @NotBlank(message = "A分权限不可以为空")
    @ApiModelProperty("分A权限（正数）")
    private Integer aScore; // 分A权限（正数）

    @NotBlank(message = "B分权限不可以为空")
    @ApiModelProperty("分B权限（正数）")
    private Integer bScore; // 分B权限（正数）


    private String remark;

}