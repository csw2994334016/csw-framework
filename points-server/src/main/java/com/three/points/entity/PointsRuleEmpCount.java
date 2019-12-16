package com.three.points.entity;

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
 * Created by csw on 2019-11-12.
 * Description: 积分规则设置，对积分奖扣主题记录人奖分设置
 */

@Getter
@Setter
@Entity
@Table(name = "points_points_rule_emp_count")
@EntityListeners(AuditingEntityListener.class)
public class PointsRuleEmpCount implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "points_rule_id", nullable = false, columnDefinition = "varchar(36) comment '积分规则ID'")
    @ApiModelProperty("积分规则ID")
    private String pointsRuleId; // 积分规则ID

    @Column(name = "theme_emp_count", nullable = false, columnDefinition = "int(11) comment '奖扣人次（人次到？人次，奖多少分）'")
    @ApiModelProperty("奖扣人次（人次到？人次，奖多少分）")
    private Integer themeEmpCount; // 奖扣人次（人次到？人次，奖多少分）

    @Column(name = "award_score_value", nullable = false, columnDefinition = "int(11) comment '奖?分（记录人奖分） '")
    @ApiModelProperty("奖?分（对应奖分） ")
    private Integer awardScoreValue; // 奖?分（对应奖分）


    @CreatedDate
    @ApiModelProperty("创建时间")
    private Date createDate; // 创建时间

    @LastModifiedDate
    @ApiModelProperty("修改时间")
    private Date updateDate; // 修改时间


}