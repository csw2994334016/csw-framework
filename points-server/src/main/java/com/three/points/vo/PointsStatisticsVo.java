package com.three.points.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PointsStatisticsVo {

    @ApiModelProperty("奖扣时间")
    private Date themeDate; // 奖扣时间

    @ApiModelProperty("员工工号")
    private String empNum; // 员工工号

    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @ApiModelProperty("固定积分")
    private Integer fixedScore = 0; // 固定积分

    @ApiModelProperty("奖扣任务")
    private Integer managerTaskScore = 0; // 奖扣任务

    @ApiModelProperty("考勤得分")
    private Integer attendanceScore = 0; // 考勤得分

    @ApiModelProperty("累计得分")
    private Integer cumulativeScore = 0; // 累计得分

    @ApiModelProperty("A分总分")
    private Integer ascoreAll = 0; // A分总分

    @ApiModelProperty("B分总分/当月得分")
    private Integer bscoreAll = 0; // B分总分/当月得分
}
