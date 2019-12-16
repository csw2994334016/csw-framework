package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2019-11-06.
 * Description:
 */
@Data
public class ManagerTaskParam {

    private String id;


    @NotBlank(message = "任务名称不可以为空")
    @ApiModelProperty("任务名称")
    private String taskName; // 任务名称

    @NotNull(message = "奖扣分考核任务（1=是；2=否）不可以为空")
    @ApiModelProperty("奖扣分考核任务（1=是；2=否）")
    private Integer scoreTaskFlag; // 奖扣分考核任务（1=是；2=否）

    @ApiModelProperty("奖扣分周期（1=日任务；2=周任务；3=月任务）")
    private Integer scoreCycle; // 奖扣分周期（1=日任务；2=周任务；3=月任务）

    @ApiModelProperty("奖分下限")
    private Integer scoreAwardMin; // 奖分下限

    @ApiModelProperty("扣分下限")
    private Integer scoreDeductMin; // 扣分下限

    @NotNull(message = "奖扣比例考核任务（1=是；2=否）不可以为空")
    @ApiModelProperty("奖扣比例考核任务（1=是；2=否）")
    private Integer ratioTaskFlag; // 奖扣比例考核任务（1=是；2=否）

    @ApiModelProperty("奖扣比例周期（1=日任务；2=周任务；3=月任务）")
    private Integer ratioCycle; // 奖扣比例周期（1=日任务；2=周任务；3=月任务）

    @ApiModelProperty("奖扣比例值")
    private Double ratioValue; // 奖扣比例值

    @NotNull(message = "奖扣人次考核任务（1=是；2=否）不可以为空")
    @ApiModelProperty("奖扣人次考核任务（1=是；2=否）")
    private Integer empCountTaskFlag; // 奖扣人次考核任务（1=是；2=否）

    @ApiModelProperty("奖扣人次周期（1=日任务；2=周任务；3=月任务）")
    private Integer empCountCycle; // 奖扣人次周期（1=日任务；2=周任务；3=月任务）

    @ApiModelProperty("奖扣人次值")
    private Integer empCountValue; // 奖扣人次值

    @NotNull(message = "未完成奖扣人次任务扣分不可以为空")
    @Max(value = 0, message = "未完成奖扣人次任务扣分只能是小于0的数值")
    @ApiModelProperty("未完成奖扣人次任务扣分")
    private Integer empCountNegScore; // 未完成奖扣人次任务扣分


    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

    @ApiModelProperty("任务考核人员列表")
    List<ManagerTaskEmpParam> managerTaskEmpParamList = new ArrayList<>(); // 任务考核人员列表

}