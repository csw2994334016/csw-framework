package com.three.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Created by csw on 2019-09-22.
 * Description:
 */
@Data
public class EmployeeParam {

    private String id;

    @ApiModelProperty("用户名")
    private String username; // 用户名

    public String getUsername() {
        return empNum;
    }

    @NotBlank(message = "姓名不可以为空")
    @ApiModelProperty("姓名，必填字段")
    private String fullName; // 姓名

    @NotBlank(message = "手机号不可以为空")
    @ApiModelProperty("手机号，必填字段")
    private String cellNum; // 手机号

    @NotBlank(message = "员工工号不可以为空")
    @ApiModelProperty("员工工号，必填字段")
    private String empNum; // 员工工号

    @NotBlank(message = "所在组织机构不可以为空")
    @ApiModelProperty("所在组织（部门），必填字段")
    private String organizationId; // 所在组织

    private String roleIds;

    @ApiModelProperty("身份证号码")
    private String idCardNum; // 身份证号码

    @ApiModelProperty("性别")
    private String gender; // 性别

    @ApiModelProperty("出生日期")
    private Date birthday; // 出生日期

    @ApiModelProperty("民族")
    private String nation; // 民族

    @ApiModelProperty("固定电话")
    private String tellNum; // 固定电话

    @ApiModelProperty("办公传真")
    private String officeFax; // 办公传真

    @ApiModelProperty("电子邮箱")
    private String email; // 电子邮箱

    @ApiModelProperty("通讯地址")
    private String address; // 通讯地址

    @ApiModelProperty("邮编")
    private String postalCode; // 邮编

    @ApiModelProperty("微信号")
    private String weChatId; // 微信号

    @ApiModelProperty("QQ号")
    private String qqId; // QQ号

    @ApiModelProperty("最后学历")
    private String eduLevelId; // 最后学历

    @ApiModelProperty("最后学位")
    private String eduDegreeId; // 最后学位

    @ApiModelProperty("毕业院校信息")
    private String graduateSchool; // 毕业院校信息

    @ApiModelProperty("专业")
    private String major; // 专业

    @ApiModelProperty("特长")
    private String learnSpecialty; // 特长

    @ApiModelProperty("个人信息完善状态：1=未完善；2=完善")
    private Integer infoCompleteStatus; // 个人信息完善状态：1=未完善；2=完善

    @ApiModelProperty("入职日期")
    private Date joinDate; // 入职日期

    @ApiModelProperty("职称/职位")
    private String titleLevel; // 职称/职位

    @ApiModelProperty("职位状态：1=在职；2=不在职")
    private Integer empStatus; // 职位状态：1=在职；2=不在职

    @ApiModelProperty("工龄分")
    private Integer workingAgeScore; // 工龄分

    @ApiModelProperty("基础分")
    private Integer baseScore; // 基础分

    @ApiModelProperty("当前薪资")
    private Double baseSalary; // 当前薪资

    @ApiModelProperty("排序")
    private Integer sort; // 排序

    @ApiModelProperty("备注")
    private String remark;

}