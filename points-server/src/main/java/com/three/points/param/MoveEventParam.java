package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by csw on 2019-10-20.
 * Description:
 */
@Data
public class MoveEventParam {

    @NotBlank(message = "事件ID不可以为空")
    @ApiModelProperty("事件ID")
    private String id; // 事件ID


    @NotBlank(message = "事件分类ID不可以为空")
    @ApiModelProperty("事件分类ID")
    private String typeId; // 事件分类ID

}