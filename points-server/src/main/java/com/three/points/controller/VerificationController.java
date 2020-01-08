package com.three.points.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.service.ThemeDetailService;
import com.three.points.vo.ThemeDetailEventViewVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "核查结算", tags = "核查结算")
@RestController
@RequestMapping("/points/verifications")
public class VerificationController {

    @Autowired
    private ThemeDetailService themeDetailService;

    @ApiOperation(value = "事件视图（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "themeDateSt", value = "奖扣时间(最近两月/上月/本月换算成月份，时间戳，毫秒)开始，默认为本月", dataType = "Long"),
            @ApiImplicitParam(name = "themeDateEt", value = "奖扣时间(最近两月/上月/本月换算成月份，时间戳，毫秒)结束，默认为本月", dataType = "Long"),
            @ApiImplicitParam(name = "themeDate", value = "奖扣时间(最近两月/上月/本月换算成月份，时间戳，毫秒)", dataType = "Long"),
            @ApiImplicitParam(name = "themeStatus", value = "状态：5=审核通过;7=已作废", dataType = "Integer"),
            @ApiImplicitParam(name = "themeName", value = "主题名称关键词", dataType = "String"),
            @ApiImplicitParam(name = "eventName", value = "事件名称关键词", dataType = "String"),
            @ApiImplicitParam(name = "empFullName", value = "奖扣对象", dataType = "String"),
            @ApiImplicitParam(name = "modifyFlag", value = "是否修改：0=否（默认）；1=是", dataType = "Integer"),
    })
    @GetMapping("/eventView")
    public PageResult<ThemeDetailEventViewVo> eventView(Integer page, Integer limit, Long themeDateSt, Long themeDateEt, Integer themeStatus, String themeName, String eventName, String empFullName, @RequestParam(defaultValue = "0") Integer modifyFlag) {
        if (page != null && limit != null) {
            return themeDetailService.eventView(new PageQuery(page, limit), StatusEnum.OK.getCode(), themeDateSt, themeDateEt, themeStatus, themeName, eventName, empFullName, modifyFlag);
        } else {
            return themeDetailService.eventView(null, StatusEnum.OK.getCode(), themeDateSt, themeDateEt, themeStatus, themeName, eventName, empFullName, modifyFlag);
        }
    }
}
