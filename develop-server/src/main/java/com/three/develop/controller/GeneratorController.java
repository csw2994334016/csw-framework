package com.three.develop.controller;

import com.three.common.vo.JsonResult;
import com.three.develop.param.GeneratorParam;
import com.three.develop.service.GeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by csw on 2019/09/19.
 * Description:
 */
@Api(value = "代码生成", tags = "代码生成")
@RestController
@RequestMapping("/develop/generators")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    @ApiOperation(value = "生成代码")
    @ApiImplicitParam(name = "generatorParam", value = "代码生成配置", required = true, dataType = "GeneratorParam")
    @PostMapping("/generate")
    public JsonResult create(@RequestBody GeneratorParam generatorParam) {
        generatorService.generate(generatorParam.getGenConfig(), generatorParam.getColumnInfoList(), generatorParam.getTemplateList());
        return JsonResult.ok("代码生成成功");
    }
}
