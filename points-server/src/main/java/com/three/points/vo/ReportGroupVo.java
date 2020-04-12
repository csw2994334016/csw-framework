package com.three.points.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * Created by csw on 2019/10/25.
 * Description:
 */
@Data
public class ReportGroupVo {

    @ApiModelProperty("报表Id")
    private String reportId; // 报表Id

    @ApiModelProperty("分组Id")
    private String groupId; // 分组Id

    @ApiModelProperty("分组名称")
    private String groupName; // 分组名称

    public ReportGroupVo() {}

    public ReportGroupVo(String reportId, String groupId, String groupName) {
        this.reportId = reportId;
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
