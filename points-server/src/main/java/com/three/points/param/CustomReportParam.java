package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by csw on 2020-04-11.
 * Description:
 */
@Data
public class CustomReportParam {

    @ApiModelProperty("主键ID")
    private String id;


    @NotBlank(message = "报表名称不可以为空")
    @ApiModelProperty("报表名称")
    private String reportName; // 报表名称

    @NotNull(message = "报表类型：0=积分排名；1=平均分排名不可以为空")
    @ApiModelProperty("报表类型：0=积分排名；1=平均分排名")
    private Integer reportType; // 报表类型：0=积分排名；1=平均分排名


    @ApiModelProperty("描述/备注")
    @Size(max = 500, message = "描述/备注不超过500个字")
    private String remark; // 描述/备注

    @ApiModelProperty("分组Ids，多个分组用英文逗号隔开")
    private String customGroupIds; // 分组Ids，多个分组用","隔开

}