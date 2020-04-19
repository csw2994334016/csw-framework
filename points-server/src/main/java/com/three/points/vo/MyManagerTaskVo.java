package com.three.points.vo;

import com.three.points.entity.ManagerTask;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class MyManagerTaskVo {

    @ApiModelProperty("人员Id")
    private String empId; // 人员Id

    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @ApiModelProperty("奖分任务已完成值")
    private Integer scoreAwardCompleted = 0; // 奖分任务已完成值

    @ApiModelProperty("奖分任务要求值")
    private Integer scoreAwardNeeded = 0; // 奖分任务要求值

    @ApiModelProperty("扣分任务已完成值")
    private Integer scoreDeductCompleted = 0; // 扣分任务已完成值

    @ApiModelProperty("扣分任务要求值")
    private Integer scoreDeductNeeded = 0; // 扣分任务要求值

    @ApiModelProperty("人次任务已完成值")
    private Integer empCountValueCompleted = 0; // 人次任务已完成值

    @ApiModelProperty("人次任务要求值")
    private Integer empCountValueNeeded = 0; // 人次任务要求值

    @ApiModelProperty("比例任务已完成值")
    private Double ratioValueCompleted = 0.0; // 比例任务已完成值

    @ApiModelProperty("比例任务要求值")
    private Double ratioValueNeeded = 1.0; // 比例任务要求值

    @ApiModelProperty("比例任务奖分值")
    private Integer ratioTaskAwardScore = 0; // 比例任务奖分值

    @ApiModelProperty("比例任务扣分值")
    private Integer ratioTaskDeductScore = 0; // 比例任务扣分值

    @ApiModelProperty("任务信息对象，关于管理任务信息都从这个对象里面取")
    private ManagerTask managerTask; // 任务信息对象，关于管理任务信息都从这个对象里面取
}
