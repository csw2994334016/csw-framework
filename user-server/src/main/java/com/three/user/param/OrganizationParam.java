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

    @ApiModelProperty("组织机构类型")
    private String orgType; // 组织机构类型，1=部门；2=公司

    @ApiModelProperty("父级编号")
    private String parentId = "-1"; // 父级编号

    @ApiModelProperty("一级父级机构ID")
    private String firstParentId; // 一级父级机构ID

    @ApiModelProperty("父级名称")
    private String parentName; // 父级名称

    @ApiModelProperty("排序")
    private Integer sort = 100; // 排序


    private String remark;

}