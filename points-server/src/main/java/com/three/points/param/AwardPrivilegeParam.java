package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Created by csw on 2019-09-29.
 * Description:
 */
@Getter
@Setter
public class AwardPrivilegeParam {

    private String id;

    @NotBlank(message = "奖扣权限名称不可以为空")
    @ApiModelProperty("奖扣权限名称")
    private String awardPrivilegeName; // 奖扣权限名称

    @ApiModelProperty("分A权限（正数）")
    private Integer ascore = 0; // 分A权限（正数）

    @ApiModelProperty("分B权限（正数）")
    private Integer bscore = 0; // 分B权限（正数）


    private String remark;

}