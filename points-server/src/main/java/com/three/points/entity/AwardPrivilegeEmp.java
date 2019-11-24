package com.three.points.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by csw on 2019-09-29.
 * Description: 奖扣权限-人员关系表
 */

@Getter
@Setter
@Entity
@Table(name = "points_award_privilege_emp")
@EntityListeners(AuditingEntityListener.class)
public class AwardPrivilegeEmp implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "organization_id", nullable = false, columnDefinition = "varchar(36) comment '组织机构（一级公司）'")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @Column(name = "award_privilege_id", nullable = false, columnDefinition = "varchar(36) comment '奖扣权限id'")
    @ApiModelProperty("奖扣权限id")
    private String awardPrivilegeId; // 奖扣权限id

    @Column(name = "award_privilege_name", columnDefinition = "varchar(255) comment '奖扣权限名称'")
    @ApiModelProperty("奖扣权限名称")
    private String awardPrivilegeName; // 奖扣权限名称

    @Column(name = "emp_id", nullable = false, columnDefinition = "varchar(36) comment '员工id'")
    @ApiModelProperty("员工id")
    private String empId; // 员工id

    @Column(name = "emp_num", columnDefinition = "varchar(100) comment '员工工号'")
    @ApiModelProperty("员工工号")
    private String empNum; // 员工工号

    @Column(name = "emp_full_name", columnDefinition = "varchar(50) comment '人员姓名'")
    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

}