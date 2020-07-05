package com.three.resource_jpa.jpa.base.service;

import com.three.common.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataApiServiceExecutor {

    @Autowired
    private GroovyService groovyService;

    public JsonResult submitRequest(String scriptName, Map<Object, Object> params) {
        JsonResult jsonResult = JsonResult.ok();

        Object result = groovyService.exec(scriptName, params);

        return jsonResult.put("data", result);
    }
}
