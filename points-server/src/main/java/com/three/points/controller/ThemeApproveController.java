package com.three.points.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.entity.Theme;
import com.three.points.service.ThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by csw on 2019/10/25.
 * Description:
 */

@Api(value = "积分审批", tags = "积分审批")
@RestController
@RequestMapping("/points/approvals")
public class ThemeApproveController {

    @Autowired
    private ThemeService themeService;

    @LogAnnotation(module = "驳回积分奖扣")
    @ApiOperation(value = "驳回积分奖扣", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "积分奖扣主题id(多条记录用英文逗号隔开)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "opinion", value = "驳回意见", required = true, dataType = "String"),
            @ApiImplicitParam(name = "recorderBScore", value = "记录人(B-)", dataType = "Integer"),
            @ApiImplicitParam(name = "attnBScore", value = "初审人(B-)", dataType = "Integer")
    })
    @GetMapping("/reject")
    public JsonResult reject(@RequestParam(required = true) String id, @RequestParam(required = true) String opinion, Integer recorderBScore, Integer attnBScore) {
        themeService.reject(id, opinion, recorderBScore, attnBScore);
        return JsonResult.ok("驳回成功");
    }

    @LogAnnotation(module = "通过积分奖扣")
    @ApiOperation(value = "通过积分奖扣", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "积分奖扣主题id(多条记录用英文逗号隔开)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "opinion", value = "通过意见", dataType = "String"),
            @ApiImplicitParam(name = "recorderBScore", value = "记录人(B+)", dataType = "Integer"),
            @ApiImplicitParam(name = "attnBScore", value = "初审人(B+)", dataType = "Integer")
    })
    @GetMapping("/approve")
    public JsonResult approve(@RequestParam(required = true) String id, String opinion, Integer recorderBScore, Integer attnBScore) {
        themeService.approve(id, opinion, recorderBScore, attnBScore);
        return JsonResult.ok("通过成功");
    }

    @LogAnnotation(module = "撤回积分奖扣（审核人）")
    @ApiOperation(value = "撤回积分奖扣（审核人）", notes = "")
    @ApiImplicitParam(name = "id", value = "积分奖扣主题id(多条记录用英文逗号隔开)", required = true, dataType = "String")
    @GetMapping("/retreat")
    public JsonResult retreat(@RequestParam(required = true) String id) {
        themeService.retreat(id);
        return JsonResult.ok("撤回成功");
    }

    @ApiOperation(value = "查询积分审批（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "whoFlag", value = "待我审核/我已审核/抄送给我,1=待我审核;2=我已审核;3=抄送给我(默认1)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "themeName", value = "主题关键词", dataType = "String"),
            @ApiImplicitParam(name = "recordDateSt", value = "记录时间开始(时间戳，毫秒", dataType = "Long"),
            @ApiImplicitParam(name = "recordDateEt", value = "记录时间结束(时间戳，毫秒),默认为当前时间", dataType = "Long"),
            @ApiImplicitParam(name = "themeDateSt", value = "奖扣时间开始(时间戳，毫秒)", dataType = "Long"),
            @ApiImplicitParam(name = "themeDateEt", value = "奖扣时间结束(时间戳，毫秒),默认为当前时间", dataType = "Long"),
            @ApiImplicitParam(name = "attnName", value = "初审人姓名", dataType = "String"),
            @ApiImplicitParam(name = "auditName", value = "终审人姓名", dataType = "String"),
            @ApiImplicitParam(name = "recorderName", value = "记录人姓名", dataType = "String"),
            @ApiImplicitParam(name = "themeStatus", value = "状态：0=草稿;1=保存;2=待初审;3=待终审;4=驳回;5=审核通过;6=锁定", dataType = "Integer")
    })
    @GetMapping("/query")
    public PageResult<Theme> query(Integer page, Integer limit, @RequestParam(defaultValue = "1") String whoFlag, String themeName,
                                   Long recordDateSt, Long recordDateEt, Long themeDateSt, Long themeDateEt,
                                   String attnName, String auditName, String recorderName, Integer themeStatus) {
        if (page != null && limit != null) {
            return themeService.queryApproval(new PageQuery(page, limit), StatusEnum.OK.getCode(), whoFlag, themeName,
                    recordDateSt, recordDateEt, themeDateSt, themeDateEt, attnName, auditName, recorderName, themeStatus);
        } else {
            return themeService.queryApproval(null, StatusEnum.OK.getCode(), whoFlag, themeName,
                    recordDateSt, recordDateEt, themeDateSt, themeDateEt, attnName, auditName, recorderName, themeStatus);
        }
    }
}
