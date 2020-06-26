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
public class EntityPojoParam {

    @ApiModelProperty("主键ID")
    private String id;


    @NotBlank(message = "实体类名称不可以为空")
    @ApiModelProperty("实体类名称")
    private String entityName; // 实体类名称

    @NotBlank(message = "实体类包全名称不可以为空")
    @ApiModelProperty("实体类包全名称")
    private String entityPackageName; // 实体类包全名称

    @NotNull(message = "实体标记：1=实体；0=虚拟实体不可以为空")
    @ApiModelProperty("实体标记：1=实体；0=虚拟实体")
    private Integer metaFlag = 1; // 实体标记：1=实体；0=虚拟实体


    @Size(max = 500, message = "描述/备注不超过500个字")
    @ApiModelProperty("描述/备注(不超过500个字)")
    private String remark; // 描述/备注

}