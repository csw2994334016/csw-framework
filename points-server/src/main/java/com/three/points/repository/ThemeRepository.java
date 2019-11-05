package com.three.points.repository;

import com.three.points.entity.Theme;
import com.three.points.enums.EventEnum;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

/**
 * Created by csw on 2019-10-24.
 * Description:
 */
public interface ThemeRepository extends BaseRepository<Theme, String> {

    Theme findByRelationThemeIdAndThemeName(String id, String themeName);
}