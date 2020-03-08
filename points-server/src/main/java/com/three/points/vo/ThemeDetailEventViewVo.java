package com.three.points.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ThemeDetailEventViewVo {

    @ApiModelProperty("主题详情Id")
    private String id; // 主题详情Id

    @ApiModelProperty("人员Id")
    private String empId; // 人员Id

    @ApiModelProperty("奖扣对象/参与人")
    private String empFullName; // 奖扣对象/参与人

    @ApiModelProperty("奖扣时间")
    private Date themeDate; // 奖扣时间

    @ApiModelProperty("主题名")
    private String themeName; // 主题名

    @ApiModelProperty("事件名称")
    private String eventName; // 事件名称

    @ApiModelProperty("修改（是否修改过分值）：0=否（默认）；1=是")
    private Integer modifyFlag = 0; // 修改（是否修改过分值）：0=否（默认）；1=是

    @ApiModelProperty("A分")
    private Integer ascore = 0; // A分

    @ApiModelProperty("B分")
    private Integer bscore = 0; // B分

    @ApiModelProperty("奖票事件：1=是；0=否")
    private Integer prizeFlag = 0; // 奖票事件：1=是；0=否

    @ApiModelProperty("记录人姓名")
    private String recorderName; // 记录人姓名

    @ApiModelProperty("初审人姓名")
    private String attnName; // 初审人姓名

    @ApiModelProperty("终审人姓名")
    private String auditName; // 终审人姓名

    @ApiModelProperty("主题状态：0=草稿;1=保存;2=待初审;3=待终审;4=驳回;5=审核通过;6=锁定;7=已作废")
    private Integer themeStatus; // 主题状态：0=草稿;1=保存;2=待初审;3=待终审;4=驳回;5=审核通过;6=锁定;7=已作废

    @ApiModelProperty("主题ID")
    private String themeId; // 主题ID

    @ApiModelProperty("描述")
    private String remark; // 描述

    @ApiModelProperty("事件分类名称")
    private String eventTypeName; // 事件分类名称

    @ApiModelProperty("A分（汇总正分）")
    private Integer aposScore = 0; // A分（汇总正分）

    @ApiModelProperty("A分（汇总负分）")
    private Integer anegScore = 0; // A分（汇总负分）

    @ApiModelProperty("B分（汇总正分）")
    private Integer bposScore = 0; // B分（汇总正分）

    @ApiModelProperty("B分（汇总负分）")
    private Integer bnegScore = 0; // B分（汇总负分）

    public ThemeDetailEventViewVo(String id, String empId, String empFullName, Date themeDate, String themeName, String eventName,
                                  Integer modifyFlag, Integer ascore, Integer bscore, Integer prizeFlag, String recorderName,
                                  String attnName, String auditName, Integer themeStatus, String themeId, String remark,
                                  String eventTypeName, Integer aposScore, Integer anegScore, Integer bposScore, Integer bnegScore) {
        this.id = id;
        this.empId = empId;
        this.empFullName = empFullName;
        this.themeDate = themeDate;
        this.themeName = themeName;
        this.eventName = eventName;
        this.modifyFlag = modifyFlag;
        this.ascore = ascore;
        this.bscore = bscore;
        this.prizeFlag = prizeFlag;
        this.recorderName = recorderName;
        this.attnName = attnName;
        this.auditName = auditName;
        this.themeStatus = themeStatus;
        this.themeId = themeId;
        this.remark = remark;
        this.eventTypeName = eventTypeName;
        this.aposScore = aposScore;
        this.anegScore = anegScore;
        this.bposScore = bposScore;
        this.bnegScore = bnegScore;
    }
}
