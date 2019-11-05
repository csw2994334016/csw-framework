package com.three.points.repository;

import com.three.points.entity.ThemeDetail;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by csw on 2019-10-24.
 * Description:
 */
public interface ThemeDetailRepository extends BaseRepository<ThemeDetail, String> {

    void deleteByThemeId(String id);

    List<ThemeDetail> findAllByThemeIdAndStatus(String themeId, int code);

    @Query("select distinct t.themeId from ThemeDetail t where t.empId = :loginUserEmpId")
    List<String> findThemeIdByEmpId(@Param("loginUserEmpId") String loginUserEmpId);

    List<ThemeDetail> findAllByThemeId(String id);

    void deleteByThemeIdAndEmpIdAndEventName(String id, String recorderId, String message);
}