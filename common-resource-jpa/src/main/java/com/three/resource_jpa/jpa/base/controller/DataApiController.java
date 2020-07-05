package com.three.resource_jpa.jpa.base.controller;

import com.three.common.utils.HttpServletUtil;
import com.three.common.vo.JsonResult;
import com.three.resource_jpa.jpa.base.service.DataApiServiceExecutor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DataApiController {

    @Autowired
    private DataApiServiceExecutor dataApiServiceExecutor;

    @PostMapping("/dataApi/{scriptName}")
    public JsonResult execute(@PathVariable(name = "scriptName", required = true) String scriptName) {
        Map<Object, Object> params = HttpServletUtil.getParamMap();
        return dataApiServiceExecutor.submitRequest(scriptName, params);
    }
}
