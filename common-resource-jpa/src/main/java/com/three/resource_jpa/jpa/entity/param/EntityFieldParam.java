package com.three.resource_jpa.jpa.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by  on 2020-06-26.
 * Description:
 */
@Data
public class EntityFieldParam {

    @ApiModelProperty("主键ID")
    private String id;


    @NotBlank(message = "实体类ID不可以为空")
    @ApiModelProperty("实体类ID")
    private String entityPojoId; // 实体类ID

    @NotBlank(message = "字段名不可以为空")
    @ApiModelProperty("字段名")
    private String columnName; // 字段名

    @NotBlank(message = "类型不可以为空")
    @ApiModelProperty("类型")
    private String columnType; // 类型

    @ApiModelProperty("关键字")
    private String columnKey; // 关键字

    @NotNull(message = "是否空值不可以为空")
    @ApiModelProperty("是否空值：1=可空；0=不可空")
    private Integer nullFlag; // 是否空值：1=可空；0=不可空

    @NotNull(message = "长度不可以为空")
    @ApiModelProperty("长度")
    private Integer columnLength; // 长度


    @Size(max = 500, message = "描述/备注不超过500个字")
    @ApiModelProperty("描述，不超过500个字")
    private String columnComment; // 描述

}