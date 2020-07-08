package com.three.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by csw on 2020-07-06.
 * Description:
 */
@Data
public class ServerParam {

    @ApiModelProperty("主键ID")
    private String id;


    @NotBlank(message = "服务ID不可以为空")
    @ApiModelProperty("服务ID")
    private String serverId;

    @NotBlank(message = "服务名称不可以为空")
    @ApiModelProperty("服务名称")
    private String serverName;


    @Size(max = 500, message = "描述/备注不超过500个字")
    @ApiModelProperty("描述/备注(不超过500个字)")
    private String remark;

}