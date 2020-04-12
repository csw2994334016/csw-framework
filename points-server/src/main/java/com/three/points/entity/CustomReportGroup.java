package com.three.points.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by csw on 2020-04-11.
 * Description: 自定义报表
 */

@Getter
@Setter
@Entity
@Table(name = "points_custom_report_group")
@EntityListeners(AuditingEntityListener.class)
public class CustomReportGroup implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    @ApiModelProperty("主键ID")
    private String id;


    @Column(name = "report_id", nullable = false, columnDefinition = "varchar(36) comment '报表Id'")
    @ApiModelProperty("报表Id")
    private String reportId; // 报表Id

    @Column(name = "group_id", nullable = false, columnDefinition = "varchar(36) comment '分组Id'")
    @ApiModelProperty("分组Id")
    private String groupId; // 分组Id

    @CreatedDate
    @ApiModelProperty("创建时间")
    private Date createDate; // 创建时间

    @LastModifiedDate
    @ApiModelProperty("修改时间")
    private Date updateDate; // 修改时间


}