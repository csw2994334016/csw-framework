package com.three.develop.entity;

import com.three.common.enums.StatusEnum;
import com.three.develop.enums.JobStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by csw on 2019/09/13.
 * Description:
 */
@Getter
@Setter
@Entity
@Table(name = "quartz_job")
@EntityListeners(AuditingEntityListener.class)
public class QuartzJob implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String jobName; // 任务名称

    private String jobGroup; // 任务组名

    @Column(unique = true, nullable = false)
    private String beanName; // Bean名称

    @Column(nullable = false)
    private String methodName; // 执行方法

    private String params; // 参数

    @Column(nullable = false)
    private String cronExpression; // cron表达式

    /**
     * 任务执行状态，1：运行中；2：暂停
     */
    @Column(nullable = false, length = 2, columnDefinition = "int default 2")
    private Integer jobStatus = JobStatusEnum.PAUSE.getCode();


    private String remark; // 描述

    @Column(nullable = false, length = 2, columnDefinition = "int default 1")
    private Integer status = StatusEnum.OK.getCode();

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date updateDate;
}
