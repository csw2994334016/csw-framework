package com.three.common.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by csw on 2019/09/28.
 * Description:
 */
@Data
public class SysOrganization implements Serializable {

    private String id;

    private String orgName; // 组织机构名称

    private String orgCode; // 组织机构编码

    private Integer orgType = 2; // 组织机构类型，1=公司；2=部门

    private String companyId; // 公司ID(公司详细信息)

    private String deptId; // 部门ID(部门详细信息)

    private String parentId; // 父级机构ID

    private String parentName; // 父级机构名称

    private String parentIds; // 所有父级ID

    private String organizationId; // 一级组织机构（一级公司）

    private Integer sort = 100; // 排序

    private String attnList; // 初审人

    private String auditList; // 终审人
}
