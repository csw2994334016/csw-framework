package com.three.develop.entity;

import com.three.common.enums.StatusEnum;
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
 * Created by csw on 2020-04-06.
 * Description: 堆场-倍位
 */

@Getter
@Setter
@Entity
@Table(name = "nps_yard_bay")
@EntityListeners(AuditingEntityListener.class)
public class Bay implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    @ApiModelProperty("主键ID")
    private String id;

    @Column(name = "area_no", nullable = false, columnDefinition = "varchar(4) comment '所属箱区号'")
    @ApiModelProperty("所属箱区号")
    private String areaNo; // 所属箱区号


    @Column(name = "bay_no", nullable = false, columnDefinition = "varchar(36) comment '倍位号'")
    @ApiModelProperty("倍位号")
    private String bayNo; // 倍位号

    @Column(name = "row_num", columnDefinition = "int(2) comment '排数'")
    @ApiModelProperty("排数")
    private Integer rowNum; // 排数

    @Column(name = "tier_num", columnDefinition = "int(2) comment '层数'")
    @ApiModelProperty("层数")
    private Integer tierNum; // 层数




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