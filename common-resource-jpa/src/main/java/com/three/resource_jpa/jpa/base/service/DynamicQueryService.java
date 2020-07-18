package com.three.resource_jpa.jpa.base.service;

import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class DynamicQueryService {

    // TRANSACTION: 当调用事务范围bean上的方法时，容器将自动启动事务，并为您创建新的持久性上下文。当方法结束事务结束并且持久化上下文将关闭时，您的实体将变为分离。
    // EXTENDED: 只能用于有状态会话bean，并且与bean的生命周期相关联。持久化上下文可以在多个事务中产生，这意味着扩展bean中的方法共享相同的持久性上下文。

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PojoService pojoService;

    public List nativeQueryMap(String nativeSql, Map<Object, Object> params) {
        Query query = entityManager.createNativeQuery(nativeSql);
        setParameters(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Transactional
    public List nativeQueryMapWithTransactional(String nativeSql, Map<Object, Object> params) {
        Query query = entityManager.createNativeQuery(nativeSql);
        setParameters(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Transactional
    public int nativeInsertMap(String nativeSql, Map<Object, Object> params) {
        Query query = entityManager.createNativeQuery(nativeSql);
        setParameters(query, params);
        return query.executeUpdate();
    }

    public List nativeQueryModel(String className, String nativeSql, Map<Object, Object> params) {
        Class clazz = pojoService.getPojo(className);
        Query query = entityManager.createNativeQuery(nativeSql);
        setParameters(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(clazz));
        List result = query.getResultList();

        return result;
    }

    private void setParameters(Query query, Map<Object, Object> params) {
        if (params != null) {
            for (Map.Entry<Object, Object> entry : params.entrySet()) {
                if (query.unwrap(NativeQueryImpl.class).getQueryParameters().getNamedParameters().get(entry.getKey().toString()) != null) {
                    query.setParameter(entry.getKey().toString(), entry.getValue());
                }
            }
            if (params.get("page") != null && params.get("limit") != null) {
                int pageNumber = Integer.valueOf(params.get("page").toString());
                int pageSize = Integer.valueOf(params.get("limit").toString());
                int startPosition = (pageNumber - 1) * pageSize;
                query.setFirstResult(startPosition).setMaxResults(pageSize);
            }
        }
    }
}
