package com.three.user.entity;

import com.three.common.enums.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by  on 2019-09-22.
 * Description: 组织机构（公司/部门父级关系结构）
 */

@Getter
@Setter
@Entity
@Table(name = "sys_organization")
@EntityListeners(AuditingEntityListener.class)
public class Organization implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "org_name", nullable = false, columnDefinition = "varchar(100) comment '组织机构名称'")
    @ApiModelProperty("组织机构名称")
    private String orgName; // 组织机构名称

    @Column(name = "org_code", nullable = false, unique = true, columnDefinition = "varchar(100) comment '组织机构编码'")
    @ApiModelProperty("组织机构编码")
    private String orgCode; // 组织机构编码

    @Column(name = "org_type", columnDefinition = "int(2) default 2 comment '组织机构类型'")
    @ApiModelProperty("组织机构类型，1=公司；2=部门")
    private Integer orgType = 2; // 组织机构类型，1=公司；2=部门

    @Column(name = "company_id", columnDefinition = "varchar(36) comment '公司ID(公司详细信息)'")
    @ApiModelProperty("公司ID(公司详细信息)")
    private String companyId; // 公司ID(公司详细信息)

    @Column(name = "dept_id", columnDefinition = "varchar(36) comment '部门ID(部门详细信息)'")
    @ApiModelProperty("部门ID(部门详细信息)")
    private String deptId; // 部门ID(部门详细信息)

    @Column(name = "parent_id", nullable = false, columnDefinition = "varchar(36) default '-1' comment '父级机构ID'")
    @ApiModelProperty("父级机构ID")
    private String parentId = "-1"; // 父级机构ID

    @Column(name = "first_parent_id", nullable = false, columnDefinition = "varchar(36) comment '一级父级机构ID'")
    @ApiModelProperty("一级父级机构ID")
    private String firstParentId; // 一级父级机构ID

    @Column(name = "parent_name", columnDefinition = "varchar(100) comment '父级机构名称'")
    @ApiModelProperty("父级机构名称")
    private String parentName; // 父级机构名称

    @Column(name = "parent_ids", columnDefinition = "varchar(800) comment '所有父级ID'")
    @ApiModelProperty("所有父级ID")
    private String parentIds; // 所有父级ID

    @Column(name = "sort", nullable = false, columnDefinition = "int(11) default 100 comment '排序'")
    @ApiModelProperty("排序")
    private Integer sort = 100; // 排序

    @Column(name = "attn_list", columnDefinition = "varchar(1000) comment '初审人，多个用,隔开'")
    @ApiModelProperty("初审人，多个用,隔开")
    private String attnList; // 初审人，多个用,隔开

    @Column(name = "audit_list", columnDefinition = "varchar(1000) comment '终审人，多个用,隔开'")
    @ApiModelProperty("终审人，多个用,隔开")
    private String auditList; // 终审人，多个用,隔开


    @Column(name = "remark", columnDefinition = "varchar(500) comment '描述/备注'")
    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

    @Column(nullable = false, columnDefinition = "int(2) default 1 comment '记录状态，1：正常、2：锁定、3：删除'")
    @ApiModelProperty("记录状态：1=正常；2=锁定；3=删除")
    private Integer status = StatusEnum.OK.getCode(); // 记录状态，1：正常、2：锁定、3：删除

    @CreatedDate
    private Date createDate; // 创建时间

    @LastModifiedDate
    private Date updateDate; // 修改时间

    @Transient
    private List<Organization> children = new ArrayList<>();


}