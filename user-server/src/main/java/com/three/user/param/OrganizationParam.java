package com.three.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by  on 2019-09-22.
 * Description:
 */
@Data
public class OrganizationParam {

    private String id;


    @NotBlank(message = "组织机构名称不可以为空")
    @ApiModelProperty("组织机构名称")
    private String orgName; // 组织机构名称

    @NotBlank(message = "组织机构编码不可以为空")
    @ApiModelProperty("组织机构编码")
    private String orgCode; // 组织机构编码

    @ApiModelProperty("父级id，不传默认为一级组织，或默认传字符串'-1'")
    private String parentId = "-1"; // 父级id


    @ApiModelProperty("备注")
    private String remark; // 备注

}