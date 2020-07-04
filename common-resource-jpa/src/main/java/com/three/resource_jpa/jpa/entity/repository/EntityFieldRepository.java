package com.three.resource_jpa.jpa.entity.repository;

import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import com.three.resource_jpa.jpa.entity.entity.EntityField;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by  on 2020-06-26.
 * Description:
 */
@Repository
public interface EntityFieldRepository extends BaseRepository<EntityField, String> {

    List<EntityField> findAllByEntityPojoId(String entityPojoId);

    int countByColumnNameAndIdNot(String columnName, String id);

    int countByColumnName(String columnName);
}