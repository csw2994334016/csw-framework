package com.three.develop.repository;

import com.three.develop.entity.Bay;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

import java.util.List;

/**
 * Created by csw on 2019/03/31.
 * Description:
 */
public interface BayRepository extends BaseRepository<Bay, String> {
    List<Bay> findAllByAreaNoAndStatusOrderByBayNo(String areaNo, int code);
}
