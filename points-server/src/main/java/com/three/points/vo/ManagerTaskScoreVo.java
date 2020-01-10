package com.three.points.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by csw on 2019-12-11.
 * Description: 管理任务得分
 */

@Data
public class ManagerTaskScoreVo {


    @ApiModelProperty("任务名称")
    private String managerTaskName; // 任务名称

    @ApiModelProperty("任务日期（按月算）")
    private Date managerTaskDate; // 任务日期（按月算）

    @ApiModelProperty("任务指标")
    private String managerTaskIndex; // 任务指标

    @ApiModelProperty("任务类型")
    private String managerTaskType; // 任务类型

    @ApiModelProperty("未完成扣分类型")
    private String scoreNegType; // 未完成扣分类型

    @ApiModelProperty("B分")
    private Integer bscore = 0; // B分：日常奖扣/管理任务（完成情况实际得分）/固定积分

    public ManagerTaskScoreVo(String managerTaskName, Date managerTaskDate, String managerTaskIndex, String managerTaskType, String scoreNegType, Integer bscore) {
        this.managerTaskName = managerTaskName;
        this.managerTaskDate = managerTaskDate;
        this.managerTaskIndex = managerTaskIndex;
        this.managerTaskType = managerTaskType;
        this.scoreNegType = scoreNegType;
        this.bscore = bscore;
    }
}