package com.three.user.param;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by  on 2019-09-22.
 * Description:
 */
@Builder
@Data
public class OrganizationParam {

    private String id;


    @NotBlank(message = "组织机构名称不可以为空")
    private String name; // 组织机构名称

    @NotBlank(message = "组织机构编码不可以为空")
    private String code; // 组织机构编码

    private String type; // 组织机构类型，1=部门；2=公司

    @NotBlank(message = "父级编号不可以为空")
    private String pId; // 父级编号

    private String pName; // 父级名称

    @NotBlank(message = "排序不可以为空")
    private Integer sort; // 排序


    private String remark;

}