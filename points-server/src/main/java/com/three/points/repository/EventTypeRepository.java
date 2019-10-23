package com.three.points.repository;

import com.three.points.entity.EventType;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

import java.util.List;

/**
 * Created by csw on 2019-10-20.
 * Description:
 */
public interface EventTypeRepository extends BaseRepository<EventType, String> {

    List<EventType> findAllByOrganizationIdAndStatus(String firstParentId, int code);
}