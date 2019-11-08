package com.three.points.controller;

import cn.hutool.core.date.DateUtil;
import com.three.common.utils.DateUtils;
import com.three.points.service.ManagerTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(value = "积分制管理", tags = "积分制管理")
@RestController
@RequestMapping()
public class SysController {

    @Autowired
    private ManagerTaskService managerTaskService;

    @ApiOperation(value = "查询当前月任务已分配人员（内部接口）")
    @GetMapping(value = "/internal/findCurMonthTaskEmp")
    List<String> findCurMonthTaskEmp() {
        Date date = DateUtil.parse(DateUtils.getMonthFirstDay(new Date()));
        Date taskDateNext = DateUtil.offsetMonth(date, 1);
        return new ArrayList<>(managerTaskService.findCurMonthTaskEmp(taskDateNext));
    }
}
