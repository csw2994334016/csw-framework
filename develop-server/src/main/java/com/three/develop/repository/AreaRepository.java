package com.three.develop.repository;

import com.three.develop.entity.Area;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

import java.util.List;

/**
 * Created by csw on 2019/03/31.
 * Description:
 */
public interface AreaRepository extends BaseRepository<Area, String> {
    List<Area> findAllByStatusOrderByAreaNo(int code);
}
