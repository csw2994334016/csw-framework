package com.three.develop.param;

import com.three.develop.enums.JobStatusEnum;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * Created by csw on 2019/09/07.
 * Description:
 */
@Builder
@Data
public class QuartzJobParam {

    private Long id;

    @Length(min = 2, max = 20, message = "权限名称长度需要在2-20个字之间")
    private String jobName; // 任务名称

    @Length(max = 20, message = "任务组名长度最多20个字")
    private String jobGroup; // 任务组名

    @Length(min = 2, max = 50, message = "Bean名称长度需要在2-50个字之间")
    private String beanName; // Bean名称

    @Length(min = 2, max = 50, message = "方法名称长度需要在2-50个字之间")
    private String methodName; // 方法名称

    private String params; // 参数

    @NotBlank
    private String cronExpression; // cron表达式

    @Builder.Default
    private int jobStatus = JobStatusEnum.PAUSE.getCode();

    private String remark; // 描述
}
