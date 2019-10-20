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
public class EventTypeParam {

    private String id;

    @NotBlank(message = "父级分类ID不可以为空")
    @ApiModelProperty("父级分类ID")
    private String parentId; // 父级分类ID

    private String parentName; // 父级分类名称

    @NotBlank(message = "分类名称不可以为空")
    @ApiModelProperty("分类名称")
    private String typeName; // 分类名称

    @NotBlank(message = "排序不可以为空")
    @ApiModelProperty("排序")
    @Builder.Default
    private Integer sort = 100; // 排序


    private String remark;

}