package com.three.commonjpa.script.entity;

import com.three.common.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by csw on 2019/03/30.
 * Description:
 */
@Getter
@Setter
@Entity
@Table(name = "script")
@EntityListeners(AuditingEntityListener.class)
public class Script implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 类名
     */
    private String name;

    /**
     * 代码
     */
    @Lob
    @Column(columnDefinition = "text")
    private String code;

    /**
     * 代码
     */
    @Lob
    @Column(columnDefinition = "text")
    private String plainTxt;

    private String version;

    private String remark;

    @Column(nullable = false, length = 2, columnDefinition = "int default 1")
    private Integer status = StatusEnum.OK.getCode();

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date updateDate;

}
