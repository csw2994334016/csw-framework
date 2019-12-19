package com.three.points.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ThemeDetailDailyVo {

    @ApiModelProperty("奖扣时间")
    private Date themeDate; // 奖扣时间

    @ApiModelProperty("事件名称")
    private String eventName; // 事件名称

    @ApiModelProperty("人员Id")
    private String empId; // 人员Id

    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @ApiModelProperty("人员部门Id")
    private String empOrgId; // 人员部门Id

    @ApiModelProperty("人员部门")
    private String empOrgName; // 人员部门

    @ApiModelProperty("A分")
    private Integer ascore = 0; // A分

    @ApiModelProperty("B分")
    private Integer bscore = 0; // B分

    @ApiModelProperty("初审人姓名")
    private String attnName; // 初审人姓名

    @ApiModelProperty("终审人姓名")
    private String auditName; // 终审人姓名

    public ThemeDetailDailyVo(Date themeDate, String eventName, String empId, String empFullName, String empOrgId, String empOrgName, Integer ascore, Integer bscore, String attnName, String auditName) {
        this.themeDate = themeDate;
        this.eventName = eventName;
        this.empId = empId;
        this.empFullName = empFullName;
        this.empOrgId = empOrgId;
        this.empOrgName = empOrgName;
        this.ascore = ascore;
        this.bscore = bscore;
        this.attnName = attnName;
        this.auditName = auditName;
    }
}
