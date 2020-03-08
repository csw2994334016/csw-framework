package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by csw on 2019-11-04.
 * Description:
 */
@Data
public class PointsTaskParam {

    private String id;


    @NotBlank(message = "任务内容不可以为空")
    @ApiModelProperty("任务内容")
    private String taskContent; // 任务内容

    @NotNull(message = "延期扣分不可以为空，负数")
    @ApiModelProperty("延期扣分")
    private Integer delayNegScore; // 延期扣分

    @NotNull(message = "扣分上限不可以为空，负数")
    @ApiModelProperty("扣分上限")
    private Integer negScoreMax; // 扣分上限

    @NotNull(message = "积分翻倍（1=不翻倍；2=翻倍）不可以为空")
    @ApiModelProperty("积分翻倍（1=不翻倍；2=翻倍）")
    private Integer timesNum; // 积分翻倍（1=不翻倍；2=翻倍）

    @NotNull(message = "截止时间不可以为空")
    @ApiModelProperty("截止时间")
    private Long deadline; // 截止时间

    @NotBlank(message = "责任人ID不可以为空")
    @ApiModelProperty("责任人ID")
    private String chargePersonId; // 责任人ID

    @NotBlank(message = "责任人姓名不可以为空")
    @ApiModelProperty("责任人姓名")
    private String chargePersonName; // 责任人姓名

    @ApiModelProperty("任务成员ID")
    private String taskEmpId; // 任务成员ID

    @ApiModelProperty("任务成员姓名")
    private String taskEmpName; // 任务成员姓名

    @ApiModelProperty("抄送人ID")
    private String copyPersonId; // 抄送人ID

    @ApiModelProperty("抄送人姓名")
    private String copyPersonName; // 抄送人姓名

    @ApiModelProperty("图片")
    private String picture; // 图片

    @ApiModelProperty("附件")
    private String enclosure; // 附件


    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

}