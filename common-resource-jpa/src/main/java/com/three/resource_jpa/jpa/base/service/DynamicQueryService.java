package com.three.resource_jpa.jpa.base.service;

import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class DynamicQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PojoService pojoService;

    public List nativeQueryListMap(String nativeSql, Map<Object, Object> params) {
        Query query = entityManager.createNativeQuery(nativeSql);
        setParameters(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List result = query.getResultList();

        return result;
    }

    public List nativeQueryListModel(String className, String nativeSql, Map<Object, Object> params) {
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
