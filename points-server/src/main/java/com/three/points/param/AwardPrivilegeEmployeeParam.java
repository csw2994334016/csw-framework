package com.three.points.param;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2019-09-29.
 * Description:
 */
@Builder
@Data
public class AwardPrivilegeEmployeeParam {


    @NotBlank(message = "奖扣权限id不可以为空")
    private String awardPrivilegeId; // 奖扣权限id

    @Builder.Default
    private List<String> employeeIdList = new ArrayList<>(); // 员工idList

}