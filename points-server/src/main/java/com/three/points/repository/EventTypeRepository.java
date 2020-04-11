package com.three.points.repository;

import com.three.points.entity.EventType;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by csw on 2019-10-20.
 * Description:
 */
public interface EventTypeRepository extends BaseRepository<EventType, String> {

    List<EventType> findAllByOrganizationIdAndStatus(String firstParentId, int code);

    List<EventType> findAllByOrganizationIdAndStatusAndParentId(String firstOrganizationId, int code, String parentId);

    List<EventType> findAllByStatusAndParentId(int code, String parentId);

    int countByTypeNameAndOrganizationIdAndStatus(String typeName, String organizationId, int code);

    int countByTypeNameAndOrganizationIdAndStatusAndIdNot(String typeName, String organizationId, int code, String id);

    @Query("select max(e.sort) from EventType e where e.parentId = :parentId")
    Integer findMaxSortByParentId(String parentId);
}