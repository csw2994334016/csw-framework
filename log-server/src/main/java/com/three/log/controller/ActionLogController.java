package com.three.log.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.log.Log;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.log.entity.ActionLog;
import com.three.log.service.ActionLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "操作日志", tags = "操作日志")
@RestController
@RequestMapping()
public class ActionLogController {

	@Autowired
	private ActionLogService actionLogService;

	@ApiOperation(value = "保存日志（内部接口）")
	@PostMapping("/internal/saveLog")
	public void save(@RequestBody Log log) {
		actionLogService.saveLog(log);
	}

	@ApiOperation(value = "查询日志（分页）", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "searchKey", value = "筛选条件字段(船名)", dataType = "String"),
			@ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String"),
			@ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
			@ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String"),
			@ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String")
	})
	@PostMapping("/log/actionLogs/query")
	public PageResult<ActionLog> query(Integer page, Integer limit, String searchKey, String searchValue, String startDate, String endDate) {
		PageQuery pageQuery = new PageQuery(page, limit);
		BeanValidator.check(pageQuery);
		return actionLogService.query(pageQuery, StatusEnum.OK.getCode(), searchKey, searchValue, startDate, endDate);
	}

	@LogAnnotation(module = "删除日志")
	@ApiOperation(value = "删除日志（强制/批量）")
	@ApiImplicitParam(name = "ids", value = "日志id（1,2）", required = true, dataType = "String")
	@DeleteMapping("/log/actionLogs/deleteBatch")
	public JsonResult deleteBatch(String ids) {
		actionLogService.delete(ids);
		return JsonResult.ok("删除成功");
	}

}
