package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by csw on 2019-10-20.
 * Description:
 */
@Builder
@Data
public class EventParam {

    private String id;


    @NotBlank(message = "事件分类ID不可以为空")
    @ApiModelProperty("事件分类ID")
    private String typeId; // 事件分类ID

    @NotBlank(message = "事件名称不可以为空")
    @ApiModelProperty("事件名称")
    private String eventName; // 事件名称

    @NotBlank(message = "A分最小值不可以为空")
    @ApiModelProperty("A分最小值")
    private Integer aScoreMin; // A分最小值

    @NotBlank(message = "A分最大值不可以为空")
    @ApiModelProperty("A分最大值")
    private Integer aScoreMax; // A分最大值

    @NotBlank(message = "B分最小值不可以为空")
    @ApiModelProperty("B分最小值")
    private Integer bScoreMin; // B分最小值

    @NotBlank(message = "B分最大值不可以为空")
    @ApiModelProperty("B分最大值")
    private Integer bScoreMax; // B分最大值

    @ApiModelProperty("奖票事件：1=是；0=否")
    private Integer prizeFlag; // 奖票事件：1=是；0=否

    @ApiModelProperty("记件事件：1=是；0=否")
    private Integer countFlag; // 记件事件：1=是；0=否

    @ApiModelProperty("专人审核：1=是；0=否")
    private Integer auditFlag; // 专人审核：1=是；0=否

    @NotBlank(message = "排序不可以为空")
    @ApiModelProperty("排序")
    private Integer sort; // 排序


    private String remark;

}