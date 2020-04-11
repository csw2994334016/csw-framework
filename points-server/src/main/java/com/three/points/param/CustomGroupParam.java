package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by csw on 2020-04-06.
 * Description:
 */
@Data
public class CustomGroupParam {

    @ApiModelProperty("主键ID")
    private String id;

    @NotBlank(message = "分组名称不可以为空")
    @ApiModelProperty("分组名称")
    private String groupName; // 分组名称


    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

}