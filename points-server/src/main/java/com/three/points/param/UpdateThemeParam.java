package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by csw on 2019-10-24.
 * Description:
 */
@Data
public class UpdateThemeParam {

    private String id;


    @NotBlank(message = "组织机构（一级公司）不可以为空")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @NotBlank(message = "主题名不可以为空")
    @ApiModelProperty("主题名")
    private String themeName; // 主题名

    @NotNull(message = "奖扣时间不可以为空")
    @ApiModelProperty("奖扣时间")
    private Date themeDate; // 奖扣时间

    @NotNull(message = "主题状态：0=草稿;1=保存;2=已提交;3=初审人审核;4=终审人审核;5=审核通过不可以为空")
    @ApiModelProperty("主题状态：0=草稿;1=保存;2=已提交;3=初审人审核;4=终审人审核;5=审核通过")
    private Integer themeStatus; // 主题状态：0=草稿;1=保存;2=已提交;3=初审人审核;4=终审人审核;5=审核通过

    @NotNull(message = "A分（汇总分）不可以为空")
    @ApiModelProperty("A分（汇总分）")
    private Integer aScore; // A分（汇总分）

    @NotNull(message = "B分（汇总分）不可以为空")
    @ApiModelProperty("B分（汇总分）")
    private Integer bScore; // B分（汇总分）

    @NotNull(message = "人次不可以为空")
    @ApiModelProperty("人次")
    private Integer empCount; // 人次

    @NotBlank(message = "记录人ID不可以为空")
    @ApiModelProperty("记录人ID")
    private String recorderId; // 记录人ID

    @NotBlank(message = "记录人姓名不可以为空")
    @ApiModelProperty("记录人姓名")
    private String recorderName; // 记录人姓名

    @NotBlank(message = "初审人ID不可以为空")
    @ApiModelProperty("初审人ID")
    private String attnId; // 初审人ID

    @NotBlank(message = "初审人姓名不可以为空")
    @ApiModelProperty("初审人姓名")
    private String attnName; // 初审人姓名

    @ApiModelProperty("初审意见")
    private String attnOpinion; // 初审意见

    @ApiModelProperty("初审时间")
    private Date attnDate; // 初审时间

    @NotBlank(message = "终审人ID不可以为空")
    @ApiModelProperty("终审人ID")
    private String auditId; // 终审人ID

    @NotBlank(message = "终审人姓名不可以为空")
    @ApiModelProperty("终审人姓名")
    private String auditName; // 终审人姓名

    @ApiModelProperty("终审意见")
    private String auditOpinion; // 终审意见

    @ApiModelProperty("终审时间")
    private Date auditDate; // 终审时间

    @ApiModelProperty("抄送人ID")
    private String copyPersonId; // 抄送人ID

    @ApiModelProperty("抄送人姓名")
    private String copyPersonName; // 抄送人姓名

    @ApiModelProperty("创建用户ID")
    private String createUserID; // 创建用户ID

    @ApiModelProperty("创建用户姓名")
    private String createUserName; // 创建用户姓名

    @ApiModelProperty("最后编辑用户ID")
    private String lastEditUserID; // 最后编辑用户ID

    @ApiModelProperty("最后编辑用户姓名")
    private String lastEditUserName; // 最后编辑用户姓名


    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

}