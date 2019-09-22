package com.three.user.entity;

import com.three.common.enums.AdminEnum;
import com.three.common.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by csw on 2019/03/22.
 * Description:
 */

@Getter
@Setter
@Entity
@Table(name = "sys_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "organization_id", columnDefinition = "varchar(36) comment '组织机构ID，关联sys_organization.id'")
    private String organizationId; // 组织机构ID，关联sys_organization.id

    @Column(name = "employee_id", columnDefinition = "varchar(36) comment '员工信息ID，关联sys_employee.id'")
    private String employeeId; // 员工信息ID，关联sys_employee.id

    @Column(nullable = false, unique = true, columnDefinition = "varchar(36) comment '账号'")
    private String username; // 账号

    @JsonIgnore
    @Column(nullable = false)
    private String password; // 密码

    private String salt; // 加密子

    @Column(name = "full_name", columnDefinition = "varchar(50) comment '姓名'")
    private String fullName; // 姓名

    @Column(name = "phone", unique = true, columnDefinition = "varchar(11) comment '手机号'")
    private String phone; // 手机号

    @Column(nullable = false, length = 2, columnDefinition = "int default 2")
    private Integer isAdmin = AdminEnum.NO.getCode();

    @Column(name = "remark", columnDefinition = "varchar(500) comment '描述/备注'")
    private String remark; // 描述/备注

    @Column(nullable = false, columnDefinition = "int(2) default 1 comment '记录状态：1=正常；2=锁定；3=删除'")
    private Integer status = StatusEnum.OK.getCode(); // 记录状态：1=正常；2=锁定；3=删除

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date updateDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>(0);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
