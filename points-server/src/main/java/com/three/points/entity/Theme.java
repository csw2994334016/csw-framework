package com.three.points.entity;

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

/**
 * Created by csw on 2019-10-24.
 * Description: 积分奖扣主题
 */

@Getter
@Setter
@Entity
@Table(name = "points_theme")
@EntityListeners(AuditingEntityListener.class)
public class Theme implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "organization_id", nullable = false, columnDefinition = "varchar(36) comment '组织机构（一级公司）'")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @Column(name = "theme_name", nullable = false, columnDefinition = "varchar(255) comment '主题名'")
    @ApiModelProperty("主题名")
    private String themeName; // 主题名

    @Column(name = "theme_date", nullable = false, columnDefinition = "datetime comment '奖扣时间'")
    @ApiModelProperty("奖扣时间")
    private Date themeDate; // 奖扣时间

    @Column(name = "theme_status", nullable = false, columnDefinition = "int(2) comment '主题状态：0=草稿;1=保存;2=待初审;3=待终审;4=审核不通过;5=审核通过'")
    @ApiModelProperty("主题状态：0=草稿;1=保存;2=待初审;3=待终审;4=审核不通过;5=审核通过")
    private Integer themeStatus; // 主题状态：0=草稿;1=保存;2=待初审;3=待终审;4=审核不通过;5=审核通过

    @Column(name = "a_pos_score", columnDefinition = "int(11) default 0 comment 'A分（汇总正分）'")
    @ApiModelProperty("A分（汇总正分）")
    private Integer aPosScore = 0; // A分（汇总正分）

    @Column(name = "a_neg_score", columnDefinition = "int(11) default 0 comment 'A分（汇总负分）'")
    @ApiModelProperty("A分（汇总负分）")
    private Integer aNegScore = 0; // A分（汇总负分）

    @Column(name = "b_pos_score", columnDefinition = "int(11) default 0 comment 'B分（汇总正分）'")
    @ApiModelProperty("B分（汇总正分）")
    private Integer bPosScore = 0; // B分（汇总正分）

    @Column(name = "b_neg_score", columnDefinition = "int(11) default 0 comment 'B分（汇总负分）'")
    @ApiModelProperty("B分（汇总负分）")
    private Integer bNegScore = 0; // B分（汇总负分）

    @Column(name = "emp_count", nullable = false, columnDefinition = "int(11) comment '人次'")
    @ApiModelProperty("人次")
    private Integer empCount; // 人次

    @Column(name = "recorder_id", columnDefinition = "varchar(36) comment '记录人ID'")
    @ApiModelProperty("记录人ID")
    private String recorderId; // 记录人ID

    @Column(name = "recorder_name", columnDefinition = "varchar(255) comment '记录人姓名'")
    @ApiModelProperty("记录人姓名")
    private String recorderName; // 记录人姓名

    @Column(name = "submitter_id", columnDefinition = "varchar(36) comment '提交人ID'")
    @ApiModelProperty("提交人ID")
    private String submitterId; // 提交人ID

    @Column(name = "submitter_name", columnDefinition = "varchar(255) comment '提交人姓名'")
    @ApiModelProperty("提交人姓名")
    private String submitterName; // 提交人姓名

    @Column(name = "attn_id", columnDefinition = "varchar(36) comment '初审人ID'")
    @ApiModelProperty("初审人ID")
    private String attnId; // 初审人ID

    @Column(name = "attn_name", columnDefinition = "varchar(255) comment '初审人姓名'")
    @ApiModelProperty("初审人姓名")
    private String attnName; // 初审人姓名

    @Column(name = "attn_opinion", columnDefinition = "varchar(255) comment '初审意见'")
    @ApiModelProperty("初审意见")
    private String attnOpinion; // 初审意见

    @Column(name = "attn_date", columnDefinition = "datetime comment '初审时间'")
    @ApiModelProperty("初审时间")
    private Date attnDate; // 初审时间

    @Column(name = "audit_id", columnDefinition = "varchar(36) comment '终审人ID'")
    @ApiModelProperty("终审人ID")
    private String auditId; // 终审人ID

    @Column(name = "audit_name", columnDefinition = "varchar(255) comment '终审人姓名'")
    @ApiModelProperty("终审人姓名")
    private String auditName; // 终审人姓名

    @Column(name = "audit_opinion", columnDefinition = "varchar(255) comment '终审意见'")
    @ApiModelProperty("终审意见")
    private String auditOpinion; // 终审意见

    @Column(name = "audit_date", columnDefinition = "datetime comment '终审时间'")
    @ApiModelProperty("终审时间")
    private Date auditDate; // 终审时间

    @Column(name = "copy_person_id", columnDefinition = "varchar(36) comment '抄送人ID'")
    @ApiModelProperty("抄送人ID")
    private String copyPersonId; // 抄送人ID

    @Column(name = "copy_person_name", columnDefinition = "varchar(255) comment '抄送人姓名'")
    @ApiModelProperty("抄送人姓名")
    private String copyPersonName; // 抄送人姓名

    @Column(name = "last_edit_user_id", columnDefinition = "varchar(36) comment '最后编辑用户ID'")
    @ApiModelProperty("最后编辑用户ID")
    private String lastEditUserID; // 最后编辑用户ID

    @Column(name = "last_edit_user_name", columnDefinition = "varchar(255) comment '最后编辑用户姓名'")
    @ApiModelProperty("最后编辑用户姓名")
    private String lastEditUserName; // 最后编辑用户姓名


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


}