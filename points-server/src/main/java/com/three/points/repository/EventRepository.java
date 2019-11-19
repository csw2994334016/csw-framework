package com.three.points.repository;

import com.three.points.entity.Event;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

import java.util.List;

/**
 * Created by csw on 2019-10-20.
 * Description:
 */
public interface EventRepository extends BaseRepository<Event, String> {

    int countByTypeId(String id);

    List<Event> findAllByTypeId(String id);
}