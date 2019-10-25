package com.three.points.param;

import com.three.common.utils.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2019-10-24.
 * Description:
 */
@Data
public class ThemeParam {

    private String id;


    @NotBlank(message = "主题名不可以为空")
    @ApiModelProperty("主题名")
    private String themeName = StringUtil.getDateStr() + "未设置主题"; // 主题名

    @NotNull(message = "奖扣时间不可以为空")
    @ApiModelProperty("奖扣时间")
    private Date themeDate = new Date(); // 奖扣时间

    @NotBlank(message = "初审人ID不可以为空")
    @ApiModelProperty("初审人ID")
    private String attnId; // 初审人ID

    @NotBlank(message = "初审人姓名不可以为空")
    @ApiModelProperty("初审人姓名")
    private String attnName; // 初审人姓名

    @NotBlank(message = "终审人ID不可以为空")
    @ApiModelProperty("终审人ID")
    private String auditId; // 终审人ID

    @NotBlank(message = "终审人姓名不可以为空")
    @ApiModelProperty("终审人姓名")
    private String auditName; // 终审人姓名

    @ApiModelProperty("抄送人ID")
    private String copyPersonId; // 抄送人ID

    @ApiModelProperty("抄送人姓名")
    private String copyPersonName; // 抄送人姓名

    @NotEmpty(message = "主题事件List不可以为空")
    @ApiModelProperty("主题事件List")
    private List<ThemeEventParam> themeEventParamList = new ArrayList<>(); // 主题事件List

    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

}