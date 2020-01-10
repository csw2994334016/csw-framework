package com.three.points.repository;

import com.three.points.entity.Theme;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2019-10-24.
 * Description:
 */
public interface ThemeRepository extends BaseRepository<Theme, String> {

    Theme findByRelationThemeIdAndThemeName(String id, String themeName);

    List<Theme> findAllByThemeTypeAndAttnIdAndThemeStatusAndThemeDateBetween(int themeType, String empId, int code, Date st, Date et);

}