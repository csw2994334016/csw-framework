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

    @Column(name = "job_name")
    private String jobName; // 任务名称

    @Column(name = "bean_name")
    private String beanName; // Bean名称

    @Column(name = "method_name")
    private String methodName; // 方法名称

    private String params; // 参数

    @Column(name = "cron_expression")
    private String cronExpression; // cron表达式

    /**
     * 日志状态：1成功，2失败
     */
    @Column(name = "log_type")
    private Integer logType;

    private String message; // 执行消息

    /**
     * 异常详细
     */
    @Lob
    @Column(name = "exception_detail", columnDefinition = "text")
    private String exceptionDetail;

    /**
     * 请求耗时
     */
    private Long time;

    private String remark;

    @Column(name = "status", nullable = false, length = 2, columnDefinition = "int default 1")
    private Integer status = StatusEnum.OK.getCode();

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime comment '创建时间'")
    private Date createDate;

    @LastModifiedDate
    @Column(name = "update_date", columnDefinition = "datetime comment '修改时间'")
    private Date updateDate;

    public void addMessage(String msg) {
        message += msg + "\n";
    }
}
