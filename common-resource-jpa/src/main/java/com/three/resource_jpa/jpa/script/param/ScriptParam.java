package com.three.resource_jpa.jpa.script.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by csw on 2019/09/07.
 * Description:
 */
@Builder
@Data
public class ScriptParam {

    @ApiModelProperty("主键ID")
    private String id;

    @NotBlank(message = "脚本名称不可以为空")
    @ApiModelProperty("脚本名称")
    private String scriptName;

    @ApiModelProperty("代码")
    private String scriptCode;

    @ApiModelProperty("描述/备注")
    private String remark;
}
