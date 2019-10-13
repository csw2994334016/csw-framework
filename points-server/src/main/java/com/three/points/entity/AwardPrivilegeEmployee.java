package com.three.points.entity;

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
@Table(name = "points_award_privilege_employee")
@EntityListeners(AuditingEntityListener.class)
public class AwardPrivilegeEmployee implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "award_privilege_id", nullable = false, columnDefinition = "varchar(36) comment '奖扣权限id'")
    private String awardPrivilegeId; // 奖扣权限id

    @Column(name = "employee_id", nullable = false, columnDefinition = "varchar(36) comment '员工id'")
    private String employeeId; // 员工id

}