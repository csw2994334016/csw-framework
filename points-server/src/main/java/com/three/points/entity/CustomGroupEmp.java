package com.three.points.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by csw on 2020-04-06.
 * Description: 自定义分组-人员
 */

@Getter
@Setter
@Entity
@Table(name = "points_custom_group_emp")
@EntityListeners(AuditingEntityListener.class)
public class CustomGroupEmp implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    @ApiModelProperty("主键ID")
    private String id;

    @Column(name = "group_id", nullable = false, columnDefinition = "varchar(36) comment '分组Id'")
    @ApiModelProperty("分组Id")
    private String groupId; // 分组Id


    @Column(name = "emp_id", nullable = false, columnDefinition = "varchar(36) comment '员工id'")
    @ApiModelProperty("员工id")
    private String empId; // 员工id

    @Column(name = "emp_num", columnDefinition = "varchar(100) comment '员工工号'")
    @ApiModelProperty("员工工号")
    private String empNum; // 员工工号

    @Column(name = "emp_full_name", columnDefinition = "varchar(50) comment '人员姓名'")
    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @Column(name = "emp_org_id", nullable = false, columnDefinition = "varchar(36) comment '人员部门Id'")
    @ApiModelProperty("人员部门Id")
    private String empOrgId; // 人员部门Id

    @Column(name = "emp_org_name", nullable = false, columnDefinition = "varchar(100) comment '人员部门'")
    @ApiModelProperty("人员部门")
    private String empOrgName; // 人员部门


}