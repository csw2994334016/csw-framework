package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by csw on 2019-10-20.
 * Description:
 */
@Data
public class EventTypeParam {

    private String id;

    @ApiModelProperty("父级分类ID，默认\"-1\"")
    private String parentId = "-1"; // 父级分类ID，默认"-1"

    private String parentName; // 父级分类名称

    @NotBlank(message = "分类名称不可以为空")
    @ApiModelProperty("分类名称")
    private String typeName; // 分类名称


    private String remark;

}