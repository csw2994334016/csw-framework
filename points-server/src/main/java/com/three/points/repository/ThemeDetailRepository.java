package com.three.points.repository;

import com.three.points.entity.ThemeDetail;
import com.three.points.vo.ThemeDetailDailyVo;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019-10-24.
 * Description:
 */
public interface ThemeDetailRepository extends BaseRepository<ThemeDetail, String> {

    void deleteByThemeId(String id);

    List<ThemeDetail> findAllByThemeIdAndStatus(String themeId, int code);

    @Query("select distinct t.themeId from ThemeDetail t where t.empId = :loginUserEmpId")
    List<String> findThemeIdByEmpId(@Param("loginUserEmpId") String loginUserEmpId);

    @Query("select distinct td.themeId from ThemeDetail td, Theme t where td.empId = :loginUserEmpId and t.themeStatus = :code and td.themeId = t.id")
    List<String> findThemeIdByEmpIdAndThemeStatus(String loginUserEmpId, int code);

    List<ThemeDetail> findAllByThemeId(String id);

    int countByEventIdAndStatus(String id, int code);

    @Query("select new com.three.points.vo.ThemeDetailDailyVo(td.themeDate, td.eventName, td.empFullName, td.ascore, td.bscore, t.attnName, t.auditName) " +
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.status = :status and td.empId = :empId and t.themeStatus = :themeStatus and td.themeDate >= :stM and td.themeDate <= :etM")
    Page<ThemeDetailDailyVo> findAllByStatusAndEmpIdAndThemeStatusAndThemeDatePageable(int status, String empId, int themeStatus, Date stM, Date etM, Pageable pageable);

    @Query("select new com.three.points.vo.ThemeDetailDailyVo(td.themeDate, td.eventName, td.empFullName, td.ascore, td.bscore, t.attnName, t.auditName) " +
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.status = :status and td.empId = :empId and t.themeStatus = :themeStatus and td.themeDate >= :stM and td.themeDate <= :etM")
    List<ThemeDetailDailyVo> findAllByStatusAndEmpIdAndThemeStatusAndThemeDateSort(int status, String empId, int themeStatus, Date stM, Date etM, Sort sort);
}