package com.three.points.vo;

import com.three.points.param.ThemeEmpParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2019/10/25.
 * Description:
 */
@Data
public class ThemeDetailVo {

    @ApiModelProperty("事件分类ID")
    private String eventTypeId; // 事件分类ID

    @ApiModelProperty("事件分类名称")
    private String eventTypeName; // 事件分类名称

    @ApiModelProperty("事件ID")
    private String eventId; // 事件ID

    @ApiModelProperty("事件名称(自定义临时事件一定要名称)")
    private String eventName; // 事件名称(自定义临时事件一定要名称)

    @ApiModelProperty("奖票事件：1=是；0=否")
    private Integer prizeFlag = 0; // 奖票事件：1=是；0=否

    @ApiModelProperty("记件事件：1=是；0=否")
    private Integer countFlag = 0; // 记件事件：1=是；0=否

    @ApiModelProperty("专人审核：1=是；0=否")
    private Integer auditFlag = 0; // 专人审核：1=是；0=否

    @ApiModelProperty("事件参与人员List")
    private List<ThemeEmpParam> themeEmpParamList = new ArrayList<>(); // 事件参与人员List


    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注
}
