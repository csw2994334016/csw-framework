package com.three.points.entity;

import com.three.common.enums.StatusEnum;
import com.three.points.enums.ThemeTypeEnum;
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
 * Description: 积分奖扣主题详情
 */

@Getter
@Setter
@Entity
@Table(name = "points_theme_detail")
@EntityListeners(AuditingEntityListener.class)
public class ThemeDetail implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "organization_id", nullable = false, columnDefinition = "varchar(36) comment '组织机构（一级公司）'")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @Column(name = "theme_id", nullable = false, columnDefinition = "varchar(36) comment '主题ID'")
    @ApiModelProperty("主题ID")
    private String themeId; // 主题ID

    @Column(name = "theme_name", nullable = false, columnDefinition = "varchar(255) comment '主题名'")
    @ApiModelProperty("主题名")
    private String themeName; // 主题名

    @Column(name = "theme_date", nullable = false, columnDefinition = "datetime comment '奖扣时间'")
    @ApiModelProperty("奖扣时间")
    private Date themeDate; // 奖扣时间

    @Column(name = "theme_type", nullable = false, columnDefinition = "int(2) default 1 comment '积分类型：1=日常奖扣；2=管理任务；3=固定积分；4=其他得分'")
    @ApiModelProperty("积分类型：1=日常奖扣；2=管理任务；3=固定积分；4=其他得分")
    private Integer themeType = ThemeTypeEnum.DAILY_POINTS.getCode(); // 积分类型：1=日常奖扣；2=管理任务；3=固定积分；4=其他得分


    // 管理任务特有字段
    @Column(name = "manager_task_id", columnDefinition = "varchar(36) comment '管理任务id'")
    @ApiModelProperty("管理任务id")
    private String managerTaskId; // 管理任务id

    @Column(name = "manager_task_name", columnDefinition = "varchar(255) comment '任务名称'")
    @ApiModelProperty("任务名称")
    private String managerTaskName; // 任务名称

    @Column(name = "manager_task_date", columnDefinition = "datetime comment '任务日期（按月算）'")
    @ApiModelProperty("任务日期（按月算）")
    private Date managerTaskDate; // 任务日期（按月算）

    @Column(name = "manager_task_index", columnDefinition = "varchar(255) comment '任务指标'")
    @ApiModelProperty("任务指标")
    private String managerTaskIndex; // 任务指标

    @Column(name = "manager_task_type", columnDefinition = "varchar(255) comment '任务类型'")
    @ApiModelProperty("任务类型")
    private String managerTaskType; // 任务类型

    @Column(name = "score_neg_type", columnDefinition = "varchar(255) comment '未完成扣分类型'")
    @ApiModelProperty("未完成扣分类型")
    private String scoreNegType; // 未完成扣分类型


    // 日常奖扣（包括考勤）
    @Column(name = "event_type_id", columnDefinition = "varchar(36) comment '事件分类ID'")
    @ApiModelProperty("事件分类ID")
    private String eventTypeId; // 事件分类ID

    @Column(name = "event_type_name", columnDefinition = "varchar(255) comment '事件分类名称'")
    @ApiModelProperty("事件分类名称")
    private String eventTypeName; // 事件分类名称

    @Column(name = "event_id", columnDefinition = "varchar(36) comment '事件ID'")
    @ApiModelProperty("事件ID")
    private String eventId; // 事件ID

    @Column(name = "event_name", nullable = false, columnDefinition = "varchar(255) comment '事件名称'")
    @ApiModelProperty("事件名称")
    private String eventName; // 事件名称

    @Column(name = "event_flag", columnDefinition = "int(1) default 0 comment '事件类型：1=标准事件；0=临时事件'")
    @ApiModelProperty("事件类型：1=标准事件；0=临时事件")
    private Integer eventFlag = 0; // 事件类型：1=标准事件；0=临时事件

    @Column(name = "prize_flag", columnDefinition = "int(1) default 0 comment '奖票事件：1=是；0=否'")
    @ApiModelProperty("奖票事件：1=是；0=否")
    private Integer prizeFlag = 0; // 奖票事件：1=是；0=否

    @Column(name = "count_flag", columnDefinition = "int(1) default 0 comment '记件事件：1=是；0=否'")
    @ApiModelProperty("记件事件：1=是；0=否")
    private Integer countFlag = 0; // 记件事件：1=是；0=否

    @Column(name = "audit_flag", columnDefinition = "int(1) default 0 comment '专人审核：1=是；0=否'")
    @ApiModelProperty("专人审核：1=是；0=否")
    private Integer auditFlag = 0; // 专人审核：1=是；0=否

    // 奖扣对象字段
    @Column(name = "emp_id", nullable = false, columnDefinition = "varchar(36) comment '人员ID'")
    @ApiModelProperty("人员ID")
    private String empId; // 人员ID

    @Column(name = "emp_num", columnDefinition = "varchar(100) comment '员工工号'")
    @ApiModelProperty("员工工号")
    private String empNum; // 员工工号

    @Column(name = "emp_full_name", nullable = false, columnDefinition = "varchar(255) comment '人员姓名'")
    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @Column(name = "emp_org_id", columnDefinition = "varchar(36) comment '人员所属组织Id'")
    @ApiModelProperty("人员所属组织Id")
    private String empOrgId; // 人员所属组织Id

    @Column(name = "emp_org_name", columnDefinition = "varchar(100) comment '人员所属组织名称'")
    @ApiModelProperty("人员所属组织名称")
    private String empOrgName; // 人员所属组织名称

    @Column(name = "a_score", nullable = false, columnDefinition = "int(11) default 0 comment 'A分'")
    @ApiModelProperty("A分")
    private Integer ascore = 0; // A分

    @Column(name = "b_score", nullable = false, columnDefinition = "int(11) default 0 comment '日常奖扣B分/管理任务实际得分/固定积分B分'")
    @ApiModelProperty("B分")
    private Integer bscore = 0; // B分：日常奖扣/管理任务（完成情况实际得分）/固定积分

    @Column(name = "modify_flag", nullable = false, columnDefinition = "int(1) default 0 comment '修改（是否修改过分值）：0=否（默认）；1=是'")
    @ApiModelProperty("修改（是否修改过分值）：0=否（默认）；1=是")
    private Integer modifyFlag = 0; // 修改（是否修改过分值）：0=否（默认）；1=是


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

    public ThemeDetail() {
    }

    public ThemeDetail(String organizationId, String themeId, String themeName, Date themeDate, Integer themeType, String eventTypeName, Integer eventFlag) {
        this.organizationId = organizationId;
        this.themeId = themeId;
        this.themeName = themeName;
        this.themeDate = themeDate;
        this.themeType = themeType;
        this.eventTypeName = eventTypeName;
        this.eventFlag = eventFlag;
    }
}