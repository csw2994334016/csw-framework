package com.three.points.entity;

import com.three.common.enums.StatusEnum;
import com.three.points.enums.PointsTaskEnum;
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
 * Created by csw on 2019-11-04.
 * Description: 积分任务
 */

@Getter
@Setter
@Entity
@Table(name = "points_points_task")
@EntityListeners(AuditingEntityListener.class)
public class PointsTask implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "organization_id", nullable = false, columnDefinition = "varchar(36) comment '组织机构（一级公司）'")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @Column(name = "task_content", nullable = false, columnDefinition = "varchar(500) comment '任务内容'")
    @ApiModelProperty("任务内容")
    private String taskContent; // 任务内容

    @Column(name = "task_status", nullable = false, columnDefinition = "int(2) default 1 comment '任务状态（1=未完成；2=完成）'")
    @ApiModelProperty("任务状态（1=未完成；2=完成）")
    private Integer taskStatus = PointsTaskEnum.UNFINISHED.getCode(); // 任务状态（1=未完成；2=完成）

    @Column(name = "delay_neg_score", nullable = false, columnDefinition = "int(11) default 0 comment '延期扣分'")
    @ApiModelProperty("延期扣分，负数")
    private Integer delayNegScore = 0; // 延期扣分

    @Column(name = "neg_score_max", nullable = false, columnDefinition = "int(11) default 0 comment '扣分上限'")
    @ApiModelProperty("扣分上限，负数")
    private Integer negScoreMax = 0; // 扣分上限

    @Column(name = "times_num", nullable = false, columnDefinition = "int(11) default 1 comment '积分翻倍（1=不翻倍；2=翻倍）'")
    @ApiModelProperty("积分翻倍（1=不翻倍；2=翻倍）")
    private Integer timesNum = 1; // 积分翻倍（1=不翻倍；2=翻倍）

    @Column(name = "deadline", nullable = false, columnDefinition = "datetime comment '截止时间'")
    @ApiModelProperty("截止时间")
    private Date deadline; // 截止时间

    @Column(name = "complete_date", columnDefinition = "datetime comment '完成时间'")
    @ApiModelProperty("完成时间")
    private Date completeDate; // 完成时间

    @Column(name = "create_id", nullable = false, columnDefinition = "varchar(255) comment '创建人ID'")
    @ApiModelProperty("创建人ID")
    private String createId; // 创建人ID

    @Column(name = "create_name", nullable = false, columnDefinition = "varchar(255) comment '创建人姓名'")
    @ApiModelProperty("创建人姓名")
    private String createName; // 创建人姓名

    @Column(name = "charge_person_id", nullable = false, columnDefinition = "varchar(255) comment '责任人ID'")
    @ApiModelProperty("责任人ID")
    private String chargePersonId; // 责任人ID

    @Column(name = "charge_person_name", nullable = false, columnDefinition = "varchar(255) comment '责任人姓名'")
    @ApiModelProperty("责任人姓名")
    private String chargePersonName; // 责任人姓名

    @Lob
    @Column(name = "task_emp_id", columnDefinition = "text comment '任务成员ID'")
    @ApiModelProperty("任务成员ID")
    private String taskEmpId; // 任务成员ID

    @Lob
    @Column(name = "task_emp_name", columnDefinition = "text comment '任务成员姓名'")
    @ApiModelProperty("任务成员姓名")
    private String taskEmpName; // 任务成员姓名

    @Lob
    @Column(name = "copy_person_id", columnDefinition = "text comment '抄送人ID'")
    @ApiModelProperty("抄送人ID")
    private String copyPersonId; // 抄送人ID

    @Lob
    @Column(name = "copy_person_name", columnDefinition = "text comment '抄送人姓名'")
    @ApiModelProperty("抄送人姓名")
    private String copyPersonName; // 抄送人姓名

    @Column(name = "picture", columnDefinition = "varchar(255) comment '图片'")
    @ApiModelProperty("图片")
    private String picture; // 图片

    @Column(name = "enclosure", columnDefinition = "varchar(255) comment '附件'")
    @ApiModelProperty("附件")
    private String enclosure; // 附件


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