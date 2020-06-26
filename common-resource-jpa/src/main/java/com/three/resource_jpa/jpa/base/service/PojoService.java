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
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class PojoService {

    private Map<String, Integer> pojoVersionMap = new ConcurrentHashMap<>();

    private GroovyClassLoader groovyClassLoader = new GroovyClassLoader();

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Class addPojo(String code, Integer version) {
        Class clazz = groovyClassLoader.parseClass(code);
        pojoVersionMap.put(clazz.getName(), version);

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        StandardServiceRegistry serviceRegistry = sessionFactory.getSessionFactoryOptions().getServiceRegistry();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        sessionFactory.getSessionFactoryOptions();
        // 添加注解实体类
        metadataSources.addAnnotatedClass(clazz);
        Thread.currentThread().setContextClassLoader(clazz.getClassLoader());
        Metadata metadata = metadataSources.buildMetadata();
        // 更新数据库Schema，如果不存在就创建表，存在就更新字段
        SchemaUpdate schemaUpdate = new SchemaUpdate();
        schemaUpdate.execute(EnumSet.of(TargetType.DATABASE), metadata, serviceRegistry);

        return clazz;
    }

    public void removePojo(String className) {
        Class clazz = null;
        Class[] loadedClasses = groovyClassLoader.getLoadedClasses();
        if (className != null && loadedClasses != null && loadedClasses.length > 0) {
            for (Class c : loadedClasses) {
                if (className.trim().equals(c.getName())) {
                    clazz = c;
                    break;
                }
            }
        }

        if (clazz != null) {
            GroovySystem.getMetaClassRegistry().removeMetaClass(clazz);
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
