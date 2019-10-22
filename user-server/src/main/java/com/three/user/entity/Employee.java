package com.three.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Date;
import java.util.Set;

/**
 * Created by csw on 2019-09-22.
 * Description: 员工信息
 */

@Getter
@Setter
@Entity
@Table(name = "sys_employee")
@EntityListeners(AuditingEntityListener.class)
public class Employee implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(nullable = false, unique = true, columnDefinition = "varchar(36) comment '账号'")
    @ApiModelProperty("账号")
    private String username; // 账号

    @Column(name = "full_name", nullable = false, columnDefinition = "varchar(50) comment '姓名'")
    @ApiModelProperty("姓名")
    private String fullName; // 姓名

    @Column(name = "cell_num", nullable = false, columnDefinition = "varchar(30) comment '手机号'")
    @ApiModelProperty("手机号")
    private String cellNum; // 手机号

    @Column(name = "emp_num", columnDefinition = "varchar(100) comment '员工工号'")
    @ApiModelProperty("员工工号")
    private String empNum; // 员工工号

    @Column(name = "id_card_num", unique = true, columnDefinition = "varchar(255) comment '身份证号码'")
    @ApiModelProperty("身份证号码")
    private String idCardNum; // 身份证号码

    @Column(columnDefinition = "varchar(255) comment '头像'")
    @ApiModelProperty("头像")
    private String picture; // 头像

    @Column(name = "gender", columnDefinition = "varchar(2) comment '性别'")
    @ApiModelProperty("性别")
    private String gender; // 性别

    @Column(name = "birthday", columnDefinition = "datetime comment '出生日期'")
    @ApiModelProperty("出生日期")
    private Date birthday; // 出生日期

    @Column(name = "nation", columnDefinition = "varchar(30) comment '民族'")
    @ApiModelProperty("民族")
    private String nation; // 民族

    @Column(name = "tell_num", columnDefinition = "varchar(30) comment '固定电话'")
    @ApiModelProperty("固定电话")
    private String tellNum; // 固定电话

    @Column(name = "office_fax", columnDefinition = "varchar(30) comment '办公传真'")
    @ApiModelProperty("办公传真")
    private String officeFax; // 办公传真

    @Column(name = "email", columnDefinition = "varchar(30) comment '电子邮箱'")
    @ApiModelProperty("电子邮箱")
    private String email; // 电子邮箱

    @Column(name = "address", columnDefinition = "varchar(255) comment '通讯地址'")
    @ApiModelProperty("通讯地址")
    private String address; // 通讯地址

    @Column(name = "postal_code", columnDefinition = "varchar(30) comment '邮编'")
    @ApiModelProperty("邮编")
    private String postalCode; // 邮编

    @Column(name = "we_chat_id", unique = true, columnDefinition = "varchar(255) comment '微信号'")
    @ApiModelProperty("微信号")
    private String weChatId; // 微信号

    @Column(name = "qq_id", columnDefinition = "varchar(255) comment 'QQ号'")
    @ApiModelProperty("QQ号")
    private String qqId; // QQ号

    @Column(name = "edu_level_id", columnDefinition = "varchar(100) comment '最后学历'")
    @ApiModelProperty("最后学历")
    private String eduLevelId; // 最后学历

    @Column(name = "edu_degree_id", columnDefinition = "varchar(100) comment '最后学位'")
    @ApiModelProperty("最后学位")
    private String eduDegreeId; // 最后学位

    @Column(name = "graduate_school", columnDefinition = "varchar(255) comment '毕业院校信息'")
    @ApiModelProperty("毕业院校信息")
    private String graduateSchool; // 毕业院校信息

    @Column(name = "major", columnDefinition = "varchar(255) comment '专业'")
    @ApiModelProperty("专业")
    private String major; // 专业

    @Column(name = "learn_specialty", columnDefinition = "varchar(255) comment '特长'")
    @ApiModelProperty("特长")
    private String learnSpecialty; // 特长

    @Column(name = "info_complete_status", columnDefinition = "int(2) comment '个人信息完善状态：1=未完善；2=完善'")
    @ApiModelProperty("个人信息完善状态：1=未完善；2=完善")
    private Integer infoCompleteStatus; // 个人信息完善状态：1=未完善；2=完善

    @Column(name = "join_date", columnDefinition = "datetime comment '入职日期'")
    @ApiModelProperty("入职日期")
    private Date joinDate; // 入职日期

    @Column(name = "title_level", columnDefinition = "varchar(255) comment '职位'")
    @ApiModelProperty("职位")
    private String titleLevel; // 职位

    @Column(name = "emp_status", columnDefinition = "int(2) comment '职位状态：1=在职；2=不在职'")
    @ApiModelProperty("职位状态：1=在职；2=不在职")
    private Integer empStatus; // 职位状态：1=在职；2=不在职

    @Column(name = "working_age_score", columnDefinition = "int(11) comment '工龄分'")
    @ApiModelProperty("工龄分")
    private Integer workingAgeScore; // 工龄分

    @Column(name = "base_score", columnDefinition = "int(11) comment '基础分'")
    @ApiModelProperty("基础分")
    private Integer baseScore; // 基础分

    @Column(name = "base_salary", columnDefinition = "double comment '当前薪资'")
    @ApiModelProperty("当前薪资")
    private Double baseSalary; // 当前薪资

    @Column(name = "sort", columnDefinition = "int(11) comment '排序'")
    @ApiModelProperty("排序")
    private Integer sort; // 排序

    @Column(name = "create_user_id", columnDefinition = "varchar(36) comment '创建用户ID'")
    @ApiModelProperty("创建用户ID")
    private String createUserID; // 创建用户ID

    @Column(name = "create_user_name", columnDefinition = "varchar(255) comment '创建用户姓名'")
    @ApiModelProperty("创建用户姓名")
    private String createUserName; // 创建用户姓名

    @Column(name = "last_edit_user_id", columnDefinition = "varchar(36) comment '最后编辑用户ID'")
    @ApiModelProperty("最后编辑用户ID")
    private String lastEditUserID; // 最后编辑用户ID

    @Column(name = "last_edit_user_name", columnDefinition = "varchar(255) comment '最后编辑用户姓名'")
    @ApiModelProperty("最后编辑用户姓名")
    private String lastEditUserName; // 最后编辑用户姓名

    @OneToOne()
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    @ApiModelProperty("组织机构")
    private Organization organization; // 组织机构

    @OneToOne(mappedBy = "employee", cascade = CascadeType.MERGE, optional = false) // 关联关系被谁维护，非必填，只有关系维护方才能操作两者的关系，被维护方即使设置了维护方的属性进行存储也不会更新外键关联
    @JsonIgnore
    private User user;

    @Transient
    private Set<Role> roles;


    @Column(name = "remark", columnDefinition = "varchar(500) comment '描述/备注'")
    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

    @Column(nullable = false, columnDefinition = "int(2) default 1 comment '记录状态：1=正常；2=锁定；3=删除'")
    @ApiModelProperty("记录状态：1=正常；2=锁定；3=删除")
    private Integer status = StatusEnum.OK.getCode(); // 记录状态：1=正常；2=锁定；3=删除

    @CreatedDate
    @ApiModelProperty("创建时间")
    private Date createDate; // 创建时间

    @LastModifiedDate
    @ApiModelProperty("修改时间")
    private Date updateDate; // 修改时间

    public Set<Role> getRoles() {
        return user.getRoles();
    }

}