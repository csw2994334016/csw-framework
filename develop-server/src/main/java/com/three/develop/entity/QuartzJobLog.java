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
@Table(name = "quartz_job_log")
@EntityListeners(AuditingEntityListener.class)
public class QuartzJobLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobName; // 任务名称

    private String beanName; // Bean名称

    private String methodName; // 方法名称

    private String params; // 参数

    private String cronExpression; // cron表达式

    /**
     * 日志状态：1成功，2失败
     */
    private Integer logType;

    private String message; // 执行消息

    /**
     * 异常详细
     */
    @Lob
    @Column(columnDefinition = "text")
    private String exceptionDetail;

    /**
     * 请求耗时
     */
    private Long time;

    private String remark;

    @Column(nullable = false, length = 2, columnDefinition = "int default 1")
    private Integer status = StatusEnum.OK.getCode();

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date updateDate;
}
