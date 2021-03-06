package com.three.develop.entity;

import com.three.common.enums.StatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by csw on 2019/03/30.
 * Description:
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "log_action_log")
@EntityListeners(AuditingEntityListener.class)
public class ActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 操作用户
     */
    private String username;

    /**
     * 描述
     */
    private String description;

    /**
     * 方法名
     */
    private String method;

    /**
     * 参数
     */
    @Lob
    @Column(columnDefinition = "text")
    private String params;

    /**
     * 日志状态：1成功，2失败
     */
    @Column(name = "log_type")
    private Integer logType;

    private String message;

    /**
     * 请求耗时
     */
    private Long time;

    /**
     * 请求ip
     */
    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "os_name")
    private String osName;

    private String device;

    @Column(name = "browser_type")
    private String browserType;

    /**
     * 异常详细
     */
    @Lob
    @Column(name = "exception_detail", columnDefinition = "text")
    private String exceptionDetail;


    @Column(nullable = false, length = 2, columnDefinition = "int default 1")
    private Integer status = StatusEnum.OK.getCode();

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime comment '创建时间'")
    private Date createDate;

    public ActionLog(Integer logType, String message, Long time) {
        this.logType = logType;
        this.message = message;
        this.time = time;
    }
}
