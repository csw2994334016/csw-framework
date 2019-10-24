package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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



    @ApiModelProperty("主题名")
    private String themeName; // 主题名

    @ApiModelProperty("奖扣时间")
    private Date themeDate; // 奖扣时间

    @ApiModelProperty("A分（汇总分）")
    private Integer aScore; // A分（汇总分）

    @ApiModelProperty("B分（汇总分）")
    private Integer bScore; // B分（汇总分）

    @ApiModelProperty("初审人ID")
    private String attnId; // 初审人ID

    @ApiModelProperty("初审人姓名")
    private String attnName; // 初审人姓名

    @ApiModelProperty("终审人ID")
    private String auditId; // 终审人ID

    @ApiModelProperty("终审人姓名")
    private String auditName; // 终审人姓名

    @ApiModelProperty("抄送人ID")
    private String copyPersonId; // 抄送人ID

    @ApiModelProperty("抄送人姓名")
    private String copyPersonName; // 抄送人姓名

    @ApiModelProperty("主题事件List")
    private List<ThemeEventParam> themeEventParamList = new ArrayList<>(); // 主题事件List

    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

}