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
public class CompanyParam {

    private String id;


    @NotBlank(message = "公司简称不可以为空")
    private String shorterName; // 公司简称

    @NotBlank(message = "公司全称不可以为空")
    private String fullName; // 公司全称

    @NotBlank(message = "公司编码不可以为空")
    private String code; // 公司编码

    private String logo; // 企业logo（存放图片名称）

    private String location; // 所属地

    private String address; // 公司地址

    private String authenticationData; // 认证资料（存放图片名称）

    private Integer authenticationFlag; // 认证标记：0=未完善资料，1=已完善资料，2=已认证

    private String industry; // 所属行业

    private String contact; // 联系人

    private String contactGender; // 联系人性别

    private String contactTel; // 联系人电话

    private String contactCell; // 联系人手机号码

    private String contactPost; // 联系人职务

    private Integer employeeNumber; // 人数

    private Date settlementDate; // 报表结算日期 (1- 28)

    private Date endDate; // 公司使用截止日期

    private Integer sort; // 排序值

    private String agentId; // 微信应用编号

    private String corpSecret; // 微信企业号编码

    private String corpId; // 微信企业ID

    private String email; // 邮箱

    private String fax; // 传真

    private String companyType; // 公司类型

    private Date companyCreateDate; // 公司创立时间

    private String postCode; // 邮编

    private String website; // 网址

    private String businessLicence; // 企业法人营业执照

    private String organizeFrame; // 组织机构代码证

    private String taxRegister; // 税务登记证

    private String certificateCardZm; // 企业法人身份证正面

    private String certificateCardFm; // 企业法人身份证反面

    private String province; // 省

    private String city; // 市

    private String area; // 区/县

    private String managerName; // 客服专员姓名

    private String managerTel; // 客服专员固定电话

    private String managerCell; // 客服专员手机

    private String managerEmail; // 客服专员邮箱

    private String longitude; // 经度

    private String latitude; // 纬度


    private String remark;

}