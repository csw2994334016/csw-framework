package com.three.points.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CustomReportVo {

    @ApiModelProperty("主键ID")
    private String id;


    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @ApiModelProperty("报表名称")
    private String reportName; // 报表名称

    @ApiModelProperty("报表类型：0=积分排名；1=平均分排名")
    private Integer reportType = 0; // 报表类型：0=积分排名；1=平均分排名


    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

    @ApiModelProperty("记录状态：1=正常；2=锁定；3=删除")
    private Integer status; // 记录状态：1=正常；2=锁定；3=删除

    @ApiModelProperty("自定义报表分组信息")
    private List<ReportGroupVo> reportGroupVoList; // 自定义报表分组信息
}
