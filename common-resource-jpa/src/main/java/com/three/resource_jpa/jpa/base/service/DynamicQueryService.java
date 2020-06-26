package com.three.resource_jpa.jpa.base.service;

import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class DynamicQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    public List nativeQueryPagingListModel(String nativeSql, Object... params) {
        int pageNumber = 0;
        int pageSize = 10;
        int startPosition = pageNumber * pageSize;
        Query query = createNativeQuery(nativeSql, params)
                .setFirstResult(startPosition).setMaxResults(pageSize);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    private Query createNativeQuery(String sql, Object... params) {
        Query query = entityManager.createNativeQuery(sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        return query;
    }
}
