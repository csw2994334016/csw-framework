package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by csw on 2019-11-12.
 * Description:
 */
@Data
public class PointsRuleEmpCountParam {

    private String id;



    @NotNull(message = "奖扣人次（积分奖扣主题中对记录人奖分）")
    @ApiModelProperty("奖扣人次（积分奖扣主题中对记录人奖分）")
    private Integer themeEmpCount; // 奖扣人次（积分奖扣主题中对记录人奖分）

    @NotNull(message = "奖?分（记录人奖分）")
    @ApiModelProperty("奖?分（记录人奖分）")
    private Integer awardScoreValue; // 奖?分（记录人奖分）

}