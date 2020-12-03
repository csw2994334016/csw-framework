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
 * Description: 箱区-在场箱
 */

@Getter
@Setter
@Entity
@Table(name = "nps_container")
@EntityListeners(AuditingEntityListener.class)
public class Container implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    @ApiModelProperty("主键ID")
    private String id;

    @Column(name = "area_no", nullable = false, columnDefinition = "varchar(4) comment '所属箱区号'")
    @ApiModelProperty("所属箱区号")
    private String areaNo; // 所属箱区号


    @Column(name = "container_no", nullable = false, columnDefinition = "varchar(36) comment '箱号'")
    @ApiModelProperty("箱号")
    private String containerNo; // 箱号

    @Column(name = "y_location", nullable = false, columnDefinition = "varchar(6) comment '场箱位'")
    @ApiModelProperty("场箱位")
    private String yLocation; // 场箱位


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