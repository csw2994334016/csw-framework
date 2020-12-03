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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2020-04-06.
 * Description: 堆场-箱区
 */

@Getter
@Setter
@Entity
@Table(name = "nps_yard_area")
@EntityListeners(AuditingEntityListener.class)
public class Area implements Serializable {

    @Id
    @GeneratedValue(generator = "system-guid")
    @GenericGenerator(name = "system-guid", strategy = "guid")
    @Column(name = "id", columnDefinition = "varchar(36) comment '主键ID'")
    @ApiModelProperty("主键ID")
    private String id;


    @Column(name = "area_no", nullable = false, columnDefinition = "varchar(4) comment '箱区号'")
    @ApiModelProperty("箱区号")
    private String areaNo; // 箱区号

    @Lob
    @Column(name = "position", columnDefinition = "text comment '箱区位置信息'")
    @ApiModelProperty("箱区位置信息")
    private String position; // 箱区位置信息


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

    @Transient
    private List<Bay> bayList = new ArrayList<>();

}