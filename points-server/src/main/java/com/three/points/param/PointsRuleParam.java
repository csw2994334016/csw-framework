package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by csw on 2019-11-12.
 * Description:
 */
@Data
public class PointsRuleParam {

    private String id;



    @NotNull(message = "发布积分任务奖分设置，奖?分不可以为空")
    @ApiModelProperty("发布积分任务奖分设置，奖?分")
    private Integer pointsTaskAwardScore; // 发布积分任务奖分设置，奖?分

    @NotNull(message = "A/B分比例设置，1A分=?B分不可以为空")
    @ApiModelProperty("A/B分比例设置，1A分=?B分")
    private Integer abScoreValue; // A/B分比例设置，1A分=?B分

    @NotNull(message = "管理任务结算日期设置，每月?日不可以为空")
    @ApiModelProperty("管理任务结算日期设置，每月?日")
    private Integer managerTaskDay; // 管理任务结算日期设置，每月?日

    @ApiModelProperty("记录人奖分设置")
    private List<PointsRuleEmpCountParam> pointsRuleEmpCountParamList; // 记录人奖分设置


    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

}