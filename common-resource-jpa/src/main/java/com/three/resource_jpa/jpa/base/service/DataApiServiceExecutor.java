package com.three.resource_jpa.jpa.base.service;

import com.three.common.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataApiServiceExecutor {

    @Autowired
    private GroovyService groovyService;

    public JsonResult submitRequest(Map<Object, Object> params, String scriptName) {
        JsonResult jsonResult = JsonResult.ok();

        Object object = groovyService.exec(scriptName, "execute", params);

        return jsonResult.put("data", object);
    }
}
