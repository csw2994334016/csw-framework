package com.three.points.repository;

import com.three.points.entity.Theme;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2019-10-24.
 * Description:
 */
public interface ThemeRepository extends BaseRepository<Theme, String> {

    Theme findByRelationThemeIdAndThemeName(String id, String themeName);

    List<Theme> findAllByThemeTypeAndAttnIdAndThemeStatusAndThemeDateBetween(int themeType, String empId, int code, Date st, Date et);

    @Modifying(clearAutomatically = true)
    @Query("update Theme t set t.recorderName = :fullName where t.recorderId = :id")
    void updateRecorderName(String fullName, String id);

    @Modifying(clearAutomatically = true)
    @Query("update Theme t set t.attnName = :fullName where t.attnId = :id")
    void updateAttnName(String fullName, String id);

    @Modifying(clearAutomatically = true)
    @Query("update Theme t set t.auditName = :fullName where t.auditId = :id")
    void updateAuditName(String fullName, String id);
}