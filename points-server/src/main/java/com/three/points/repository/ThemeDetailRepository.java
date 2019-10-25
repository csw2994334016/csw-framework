package com.three.points.repository;

import com.three.points.entity.ThemeDetail;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

import java.util.List;

/**
 * Created by csw on 2019-10-24.
 * Description:
 */
public interface ThemeDetailRepository extends BaseRepository<ThemeDetail, String> {

    void deleteByThemeId(String id);

    List<ThemeDetail> findAllByThemeIdAndStatus(String themeId, int code);
}