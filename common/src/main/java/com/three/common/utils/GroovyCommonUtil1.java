package com.three.common.utils;

import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by csw on 2019/09/08.
 * Description:
 */
public class GroovyCommonUtil1 {

    private static final Logger log = LoggerFactory.getLogger(GroovyCommonUtil1.class);

    private static Map<String, Class> groovyClassCache = new ConcurrentHashMap<>();


    public static void removeClass(String scriptName) {
        groovyClassCache.remove(scriptName);
    }

    public static Class getClass(String scriptName) {
        return groovyClassCache.get(scriptName);
    }

    public static void addClass(String scriptName, String code) {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        Class clazz = groovyClassLoader.parseClass(code);
        groovyClassCache.put(scriptName, clazz);
    }

    /**
     * 用于调用指定Groovy脚本中的指定方法
     *
     * @param scriptName 脚本名称
     * @param methodName 方法名称
     * @param params     方法参数
     * @return Object       方法返回值
     */
//    public Object invokeMethod(String scriptName, String methodName, String code, Object... params) throws Exception {
//        Object res = null;
//        Class clazz = null;
//        GroovyObject instance = null;
//
//        try {
//            clazz = getClass(scriptName);
//            instance = (GroovyObject) clazz.newInstance();
//        } catch (Exception e1) {
//            log.warn("加载脚本[" + scriptName + "]出现异常", e1);
//        }
//
//        try {
//            res = instance.invokeMethod(methodName, params);
//        } catch (IllegalArgumentException e) {
//            log.warn("执行方法" + methodName + "参数出现异常, 参数为" + params, e);
//        } catch (Exception e) {
//            log.warn("执行方法" + methodName + "出现异常", e);
//        }
//
//        return res;
//    }
}
