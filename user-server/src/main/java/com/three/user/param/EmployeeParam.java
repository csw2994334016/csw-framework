package com.three.user.param;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Created by csw on 2019-09-22.
 * Description:
 */
@Builder
@Data
public class EmployeeParam {

    private String id;

    @NotBlank(message = "用户名不可以为空")
    private String username; // 用户名

    @NotBlank(message = "姓名不可以为空")
    private String fullName; // 姓名

    @NotBlank(message = "手机号不可以为空")
    private String cellNum; // 手机号

    private String empNum; // 员工工号

    private String idCardNum; // 身份证号码

    @NotBlank(message = "所在组织机构不可以为空")
    private String organizationId; // 所在组织

    private String roleIds;


    private String gender; // 性别

    private Date birthday; // 出生日期

    private String nation; // 民族

    private String tellNum; // 固定电话

    private String officeFax; // 办公传真

    private String email; // 电子邮箱

    private String address; // 通讯地址

    private String postalCode; // 邮编

    private String weChatId; // 微信号

    private String qqId; // QQ号

    private String eduLevelId; // 最后学历

    private String eduDegreeId; // 最后学位

    private String graduateSchool; // 毕业院校信息

    private String major; // 专业

    private String learnSpecialty; // 特长

    private Integer infoCompleteStatus; // 个人信息完善状态：1=未完善；2=完善

    private Date joinDate; // 入职日期

    private String titleLevel; // 职称

    private Integer empStatus; // 职位状态：1=在职；2=不在职

    private Integer workingAgeScore; // 工龄分

    private Integer baseScore; // 基础分

    private Double baseSalary; // 当前薪资

    private Integer sort; // 排序

    private String createUserID; // 创建用户ID

    private String createUserName; // 创建用户姓名

    private String lastEditUserID; // 最后编辑用户ID

    private String lastEditUserName; // 最后编辑用户姓名


    private String remark;

}