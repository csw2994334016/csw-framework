package com.three.develop.service;

import com.three.common.vo.JsonResult;
import com.three.resource_jpa.jpa.base.service.DynamicQueryService;
import com.three.resource_jpa.jpa.base.service.GroovyService;
import com.three.resource_jpa.jpa.base.service.PojoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by csw on 2019/09/09.
 * Description:
 */
@Service
public class TestService {

    @Autowired
    private GroovyService groovyService;

//    @Autowired
//    private EntityPojoRepository entityPojoRepository;

    @Autowired
    private PojoService pojoService;

    @Autowired
    private DynamicQueryService dynamicQueryService;

    public JsonResult test(String id, String sql) {

//        EntityPojo entityPojo = entityPojoRepository.findById(id).get();
//
//        pojoService.addPojo(entityPojo.getEntityCode(), entityPojo.getVersion());
//
//        Class clazz = pojoService.getPojo(entityPojo.getEntityPackageName());


//        List resultList = dynamicQueryService.nativeQueryMap(sql);

        return JsonResult.ok("执行成功").put("data", null);
    }
}
