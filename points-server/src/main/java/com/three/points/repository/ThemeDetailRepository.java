package com.three.points.repository;

import com.three.points.entity.ThemeDetail;
import com.three.points.vo.ThemeDetailDailyVo;
import com.three.points.vo.ThemeDetailEventViewVo;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
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

    @Query("select distinct td.themeId from ThemeDetail td, Theme t where td.empId = :loginUserEmpId and t.themeStatus = :code and td.themeId = t.id")
    List<String> findThemeIdByEmpIdAndThemeStatus(String loginUserEmpId, int code);

    List<ThemeDetail> findAllByThemeId(String id);

    int countByEventIdAndStatus(String id, int code);

    @Query("select new com.three.points.vo.ThemeDetailDailyVo(td.themeDate, td.eventName, td.empId, td.empFullName, td.empOrgId, td.empOrgName, td.ascore, td.bscore, t.attnName, t.auditName) " +
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.status = :status and td.empId = :empId and t.themeStatus = :themeStatus and td.themeDate >= :stM and td.themeDate <= :etM")
    Page<ThemeDetailDailyVo> findAllByStatusAndEmpIdAndThemeStatusAndThemeDatePageable(int status, String empId, int themeStatus, Date stM, Date etM, Pageable pageable);

    @Query("select new com.three.points.vo.ThemeDetailDailyVo(td.themeDate, td.eventName, td.empId, td.empFullName, td.empOrgId, td.empOrgName, td.ascore, td.bscore, t.attnName, t.auditName) " +
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.status = :status and td.empId = :empId and t.themeStatus = :themeStatus and td.themeDate >= :stM and td.themeDate <= :etM")
    List<ThemeDetailDailyVo> findAllByStatusAndEmpIdAndThemeStatusAndThemeDateSort(int status, String empId, int themeStatus, Date stM, Date etM, Sort sort);

//    @Query("select new com.three.points.vo.ThemeDetailEventViewVo(td.empId, td.empFullName, td.themeDate, td.themeName, td.eventName, td.modifyFlag, td.ascore, td.bscore, td.prizeFlag, t.recorderName, t.attnName, t.auditName, t.themeStatus) " +
//            "from ThemeDetail td, Theme t where td.themeId = t.id and td.status = :status and t.themeStatus = :themeStatus and td.themeDate >= :stM and td.themeDate <= :etM and" +
//            " if(:modifyFlag is not null,td.modifyFlag = :modifyFlag, 1=1) and if(:empFullName is not null and :empFullName != '', td.empFullName like contact('%',:empFullName,'%'), 1=1) " +
//            "and if(:themeName is not null and :themeName != '', td.themeName like contact('%',:themeName,'%'), 1=1) and if(:eventName is not null and :eventName != '', td.eventName like contact('%',:eventName,'%'), 1=1)")
//    Page<ThemeDetailEventViewVo> findAllByEventView(int status, Integer themeStatus, Date stM, Date etM, Integer modifyFlag, String empFullName, String themeName, String eventName, Pageable pageable);

    @Query("select new com.three.points.vo.ThemeDetailEventViewVo(td.empId, td.empFullName, td.themeDate, td.themeName, td.eventName, td.modifyFlag, td.ascore, td.bscore, td.prizeFlag, t.recorderName, t.attnName, t.auditName, t.themeStatus, t.id) " +
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.status = :status and t.themeStatus = :themeStatus and td.themeDate >= :stM and td.themeDate <= :etM and" +
            " (:modifyFlag is null or td.modifyFlag = :modifyFlag) and (:empFullName is null or td.empFullName like :empFullName) and" +
            " (:themeName is null or td.themeName like :themeName) and (:eventName is null or td.eventName like :eventName)")
    Page<ThemeDetailEventViewVo> findAllByEventView(int status, Integer themeStatus, Date stM, Date etM, Integer modifyFlag, String empFullName, String themeName, String eventName, Pageable pageable);

    @Query("select new com.three.points.vo.ThemeDetailEventViewVo(td.empId, td.empFullName, td.themeDate, td.themeName, td.eventName, td.modifyFlag, td.ascore, td.bscore, td.prizeFlag, t.recorderName, t.attnName, t.auditName, t.themeStatus, t.id) " +
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.status = :status and t.themeStatus = :themeStatus and td.themeDate >= :stM and td.themeDate <= :etM and" +
            " (:modifyFlag is null or td.modifyFlag = :modifyFlag) and (:empFullName is null or td.modifyFlag = :empFullName) and" +
            "(:themeName is null or td.themeName = :themeName) and (:eventName is null or td.eventName = :eventName) order by td.themeDate")
    List<ThemeDetailEventViewVo> findAllByEventViewOrderByThemeDate(int status, Integer themeStatus, Date stM, Date etM, Integer modifyFlag, String empFullName, String themeName, String eventName);
}