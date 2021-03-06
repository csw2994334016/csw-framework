package com.three.resource_jpa.jpa.base.service;

import com.google.common.base.Preconditions;
import com.three.common.enums.StatusEnum;
import com.three.common.exception.BusinessException;
import com.three.resource_jpa.jpa.script.entity.Script;
import com.three.resource_jpa.jpa.script.repository.ScriptRepository;
import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by csw on 2019/09/08.
 * Description:
 */
@Slf4j
@Component
public class GroovyService {

    private static Map<String, Class> groovyClassCache = new ConcurrentHashMap<>();

    @Autowired
    private ScriptRepository scriptRepository;

    @Autowired
    private BeanManager beanManager;

    private Script getScriptByName(String name) {
        Preconditions.checkNotNull(name, "查找脚本，脚本名称不可以为：" + name);
        Script script = scriptRepository.findByScriptNameAndStatus(name, StatusEnum.OK.getCode());
        Preconditions.checkNotNull(script, "查找脚本（脚本名称：" + name + ")不存在");
        return script;
    }

    /**
     * 用于调用指定Groovy脚本中的指定方法
     *
     * @param scriptName 脚本名称
     * @param params     方法参数
     * @return Object       方法返回值
     */
    public Object exec(String scriptName, Map<Object, Object> params) {
        Object res;

        Script script = getScriptByName(scriptName);

        try {
            Class clazz = getClass(scriptName, script.getScriptCode(), script.getVersion());
            DataApiService dataApiService = (DataApiService) beanManager.getBeanByName(clazz.getSimpleName());

            if (dataApiService == null) {
                throw new BusinessException("脚本没有实现DataApiService接口");
            }

            res = dataApiService.execute(params);

        } catch (Exception e1) {
            log.error("执行脚本[{}]出现异常：{}", scriptName, e1.getMessage());
            throw new RuntimeException("执行脚本[" + scriptName + "]出现异常：" + e1.getMessage());
        }

        return res;
    }

    private void removeClass(String prefix) {
        for (String key : groovyClassCache.keySet()) {
            if (key.startsWith(prefix)) {
                beanManager.removeBean(groovyClassCache.get(key).getSimpleName());
                groovyClassCache.remove(prefix);
            }
        }
    }

    private Class getClass(String scriptName, String code, Integer version) {
        addClass(scriptName, code, version);
        return groovyClassCache.get(scriptName);
    }

    private void addClass(String scriptName, String code, Integer version) {
        String currentKey = generateKey(scriptName, version);
        if (groovyClassCache.get(currentKey) == null) {
            GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
            Class clazz = groovyClassLoader.parseClass(code);
            removeClass(generateKeyPrefix(scriptName));
            groovyClassCache.put(scriptName, clazz);
            beanManager.registerBean(clazz.getSimpleName(), clazz);
        }
    }

    private String generateKey(String scriptName, Integer version) {
        return generateKeyPrefix(scriptName) + version;
    }

    private String generateKeyPrefix(String id) {
        return "groovy-" + id + "-";
    }
}
