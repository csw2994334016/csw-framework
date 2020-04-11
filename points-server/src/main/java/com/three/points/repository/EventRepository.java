package com.three.points.repository;

import com.three.points.entity.Event;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by csw on 2019-10-20.
 * Description:
 */
public interface EventRepository extends BaseRepository<Event, String> {

    List<Event> findAllByTypeId(String id);

    int countByEventNameAndOrganizationIdAndStatus(String eventName, String organizationId, int code);

    int countByEventNameAndOrganizationIdAndStatusAndIdNot(String eventName, String organizationId, int code, String id);

    @Modifying
    @Query("update Event e set e.typeName = :typeName  where e.typeId = :typeId")
    void updateTypeNameByTypeId(String typeName, String typeId);
}