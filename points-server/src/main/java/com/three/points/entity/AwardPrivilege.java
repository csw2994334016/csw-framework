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
 * Created by csw on 2019-09-29.
 * Description: 奖扣权限设置
 */

@Getter
@Setter
@Entity
@Table(name = "points_award_privilege")
@EntityListeners(AuditingEntityListener.class)
public class AwardPrivilege implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    private String id;


    @Column(name = "organization_id", nullable = false, columnDefinition = "varchar(36) comment '组织机构（一级公司）'")
    @ApiModelProperty("组织机构（一级公司）")
    private String organizationId; // 组织机构（一级公司）

    @Column(name = "award_privilege_name", nullable = false, columnDefinition = "varchar(255) comment '奖扣权限名称'")
    @ApiModelProperty("奖扣权限名称")
    private String awardPrivilegeName; // 奖扣权限名称

    @Column(name = "a_score", nullable = false, columnDefinition = "int(2) default 0 comment 'A分权限'")
    @ApiModelProperty("分A权限（正数）")
    private Integer aScore = 0; // 分A权限（正数）

    @Column(name = "b_score", nullable = false, columnDefinition = "int(2) default 0 comment 'B分权限'")
    @ApiModelProperty("分B权限（正数）")
    private Integer bScore = 0; // 分B权限（正数）

    @Column(name = "emp_num", columnDefinition = "int(2) default 0 comment '人数'")
    @ApiModelProperty("人数")
    private Integer empNum = 0; // 人数


    @Column(name = "remark", columnDefinition = "varchar(500) comment '描述/备注'")
    @ApiModelProperty("描述/备注")
    private String remark; // 描述/备注

    @Column(nullable = false, columnDefinition = "int(2) default 1 comment '记录状态：1=正常；2=锁定；3=删除'")
    private Integer status = StatusEnum.OK.getCode(); // 记录状态：1=正常；2=锁定；3=删除

    @CreatedDate
    private Date createDate; // 创建时间

    @LastModifiedDate
    private Date updateDate; // 修改时间


}