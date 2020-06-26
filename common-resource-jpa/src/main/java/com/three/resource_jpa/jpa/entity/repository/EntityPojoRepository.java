package com.three.resource_jpa.jpa.entity.repository;

import com.three.resource_jpa.jpa.entity.entity.EntityPojo;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by  on 2020-06-26.
 * Description:
 */
@Repository
public interface EntityPojoRepository extends BaseRepository<EntityPojo, String> {

}