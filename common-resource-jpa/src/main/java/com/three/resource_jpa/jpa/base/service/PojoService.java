package com.three.resource_jpa.jpa.base.service;

import com.three.common.exception.BusinessException;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovySystem;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class PojoService {

    private Map<String, Integer> pojoVersionMap = new ConcurrentHashMap<>();

    private GroovyClassLoader groovyClassLoader = new GroovyClassLoader();

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Class addPojo(String className, String code, Integer version) {
        if (!version.equals(pojoVersionMap.get(className))) {
            removePojo(className);
            Class clazz = groovyClassLoader.parseClass(code);
            pojoVersionMap.put(clazz.getName(), version);
            return clazz;
        } else {
            return getPojo(className);
        }
    }

    public Class addEntity(String className, String code, Integer version) {
        if (!version.equals(pojoVersionMap.get(className))) {
            removePojo(className);
            Class clazz = groovyClassLoader.parseClass(code);
            pojoVersionMap.put(clazz.getName(), version);
            executeSchema(clazz);
            return clazz;
        } else {
            return getPojo(className);
        }
    }

    private void executeSchema(Class clazz) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        StandardServiceRegistry serviceRegistry = sessionFactory.getSessionFactoryOptions().getServiceRegistry();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        // 添加注解实体类
        metadataSources.addAnnotatedClass(clazz);
        Thread.currentThread().setContextClassLoader(clazz.getClassLoader());
        Metadata metadata = metadataSources.buildMetadata();
        // 更新数据库Schema，如果不存在就创建表，存在就更新字段
        SchemaUpdate schemaUpdate = new SchemaUpdate();
        schemaUpdate.execute(EnumSet.of(TargetType.DATABASE), metadata, serviceRegistry);
    }

    private void removePojo(String className) {
        Class[] loadedClasses = groovyClassLoader.getLoadedClasses();
        if (className != null && loadedClasses != null && loadedClasses.length > 0) {
            for (Class clazz : loadedClasses) {
                if (className.trim().equals(clazz.getName())) {
                    GroovySystem.getMetaClassRegistry().removeMetaClass(clazz);
                }
            }
        }
        pojoVersionMap.remove(className);
    }

    public Class getPojo(String className) {
        try {
            return groovyClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new BusinessException(className + " class not found exception");
        }
    }
}
