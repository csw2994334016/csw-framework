package com.three.points.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * Created by csw on 2019/10/25.
 * Description:
 */
@Data
public class ThemeApprovalVo {

    @ApiModelProperty("流程名称")
    private String processName; // 流程名称

    @ApiModelProperty("状态")
    private String status; // 状态

    @ApiModelProperty("处理人ID")
    private String processEmpId; // 处理人ID

    @ApiModelProperty("处理人姓名")
    private String processEmpName; // 处理人姓名

    @ApiModelProperty("处理时间")
    private Date processDate; // 处理时间

    public ThemeApprovalVo() {}

    public ThemeApprovalVo(String processName, String status, String processEmpId, String processEmpName, Date processDate) {
        this.processName = processName;
        this.status = status;
        this.processEmpId = processEmpId;
        this.processEmpName = processEmpName;
        this.processDate = processDate;
    }
}
