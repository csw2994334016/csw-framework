package ${package}.controller;

import ${package}.entity.${className};
import ${package}.param.${className}Param;
import ${package}.service.${className}Service;
import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ${author} on ${date}.
 * Description:
 */

@Api(value = "${menuName}", tags = "${menuName}")
@RestController
@RequestMapping("${controllerUrl}")
public class ${className}Controller {

    @Autowired
    private ${className}Service ${changeClassName}Service;

    @LogAnnotation(module = "添加${menuName}")
    @ApiOperation(value = "添加${menuName}")
    @ApiImplicitParam(name = "${changeClassName}Param", value = "${menuName}信息", required = true, dataType = "${className}Param")
    @PostMapping()
    public JsonResult create(@RequestBody ${className}Param ${changeClassName}Param) {
        ${changeClassName}Service.create(${changeClassName}Param);
        return JsonResult.ok("${menuName}添加成功");
    }

    @LogAnnotation(module = "修改${menuName}")
    @ApiOperation(value = "修改${menuName}")
    @ApiImplicitParam(name = "${changeClassName}Param", value = "${menuName}信息", required = true, dataType = "${className}Param")
    @PutMapping()
    public JsonResult update(@RequestBody ${className}Param ${changeClassName}Param) {
        ${changeClassName}Service.update(${changeClassName}Param);
        return JsonResult.ok("${menuName}修改成功");
    }

    @LogAnnotation(module = "删除${menuName}")
    @ApiOperation(value = "删除${menuName}")
    @ApiImplicitParam(name = "ids", value = "${menuName}信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        ${changeClassName}Service.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("${menuName}删除成功");
    }

    @ApiOperation(value = "查询${menuName}（分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<${className}> query(Integer page, Integer limit, String searchValue) {
        if (page != null && limit != null) {
            return ${changeClassName}Service.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), searchValue);
        } else {
            return ${changeClassName}Service.query(null, StatusEnum.OK.getCode(), searchValue);
        }
    }

    @ApiOperation(value = "查询${menuName}（根据ID查找）")
    @ApiImplicitParam(name = "id", value = "${menuName}信息id", required = true, dataType = "String")
    @GetMapping()
    public JsonResult findById(@RequestParam(required = true) String id) {
        return JsonResult.ok().put("data", ${changeClassName}Service.findById(id));
    }
}