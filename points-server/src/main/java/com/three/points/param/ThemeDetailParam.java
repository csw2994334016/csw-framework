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
public class ThemeDetailParam {

    private String id;


    @NotBlank(message = "组织机构（一级公司）不可以为空")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @NotBlank(message = "主题ID不可以为空")
    @ApiModelProperty("主题ID")
    private String themeId; // 主题ID

    @NotBlank(message = "主题名不可以为空")
    @ApiModelProperty("主题名")
    private String themeName; // 主题名

    @NotNull(message = "奖扣时间不可以为空")
    @ApiModelProperty("奖扣时间")
    private Date themeDate; // 奖扣时间

    @NotBlank(message = "人员ID不可以为空")
    @ApiModelProperty("人员ID")
    private String empId; // 人员ID

    @NotBlank(message = "人员姓名不可以为空")
    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @ApiModelProperty("人员工号")
    private String empUsername; // 人员工号

    @ApiModelProperty("事件分类ID")
    private String eventTypeId; // 事件分类ID

    @ApiModelProperty("事件分类名称")
    private String eventTypeName; // 事件分类名称

    @ApiModelProperty("事件ID")
    private String eventId; // 事件ID

    @NotBlank(message = "事件名称不可以为空")
    @ApiModelProperty("事件名称")
    private String eventName; // 事件名称

    @ApiModelProperty("记件事件：1=是；0=否")
    private Integer countFlag; // 记件事件：1=是；0=否

    @NotNull(message = "A分不可以为空")
    @ApiModelProperty("A分")
    private Integer aScore; // A分

    @NotNull(message = "B分不可以为空")
    @ApiModelProperty("B分")
    private Integer bScore; // B分


    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

}