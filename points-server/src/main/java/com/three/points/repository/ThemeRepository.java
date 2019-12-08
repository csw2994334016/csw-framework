package com.three.points.repository;

import com.three.points.entity.Theme;
import com.three.points.enums.EventEnum;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2019-10-24.
 * Description:
 */
public interface ThemeRepository extends BaseRepository<Theme, String> {

    Theme findByRelationThemeIdAndThemeName(String id, String themeName);

    List<Theme> findAllByAttnIdAndThemeStatusAndThemeDateBetween(String empId, int code, Date taskDate, Date taskDateNext);

    List<Theme> findAllByStatusAndThemeStatusAndThemeDateBetween(int code, int code1, Date stM, Date etM);
}