package com.three.points.repository;

import com.three.points.entity.ThemeDetail;
import com.three.points.vo.ManagerTaskScoreVo;
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
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.status = :status and td.empId = :empId and " +
            "t.themeStatus = :themeStatus and td.themeDate >= :stM and td.themeDate <= :etM and td.themeType = :themeType")
    Page<ThemeDetailDailyVo> findAllByStatusAndEmpIdAndThemeStatusAndThemeDateAndThemeTypePageable(int status, String empId, int themeStatus, Date stM, Date etM, int themeType, Pageable pageable);

    @Query("select new com.three.points.vo.ThemeDetailDailyVo(td.themeDate, td.eventName, td.empId, td.empFullName, td.empOrgId, td.empOrgName, td.ascore, td.bscore, t.attnName, t.auditName) " +
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.status = :status and td.empId = :empId and " +
            "t.themeStatus = :themeStatus and td.themeDate >= :stM and td.themeDate <= :etM and td.themeType = :themeType")
    List<ThemeDetailDailyVo> findAllByStatusAndEmpIdAndThemeStatusAndThemeDateAndThemeTypeSort(int status, String empId, int themeStatus, Date stM, Date etM, int themeType, Sort sort);

    @Query("select new com.three.points.vo.ThemeDetailDailyVo(td.themeDate, td.eventName, td.empId, td.empFullName, td.empOrgId, td.empOrgName, td.ascore, td.bscore, t.attnName, t.auditName) " +
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.status = :status and td.empId = :empId and " +
            "t.themeStatus = :themeStatus and td.themeDate >= :stM and td.themeDate <= :etM")
    List<ThemeDetailDailyVo> findAllByStatusAndEmpIdAndThemeStatusAndThemeDateSort(int status, String empId, int themeStatus, Date stM, Date etM, Sort sort);

    @Query("select new com.three.points.vo.ThemeDetailEventViewVo(td.empId, td.empFullName, td.themeDate, td.themeName, td.eventName, td.modifyFlag, td.ascore, td.bscore, " +
            "td.prizeFlag, t.recorderName, t.attnName, t.auditName, t.themeStatus, t.id, td.remark, td.eventTypeName, t.aposScore, t.anegScore, t.bposScore, t.bnegScore) " +
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.themeType = :themeType and  td.status = :status and t.themeStatus = :themeStatus and td.themeDate >= :stM and td.themeDate <= :etM and" +
            " (td.modifyFlag = :modifyFlag) and (:empFullName is null or td.empFullName like concat('%', :empFullName, '%')) and" +
            " (:themeName is null or td.themeName like concat('%', :themeName, '%')) and (:eventName is null or td.eventName like concat('%', :eventName, '%'))")
    Page<ThemeDetailEventViewVo> findAllByEventView(int themeType, int status, int themeStatus, Date stM, Date etM, int modifyFlag, String empFullName, String themeName, String eventName, Pageable pageable);

    @Query("select new com.three.points.vo.ThemeDetailEventViewVo(td.empId, td.empFullName, td.themeDate, td.themeName, td.eventName, td.modifyFlag, td.ascore, td.bscore, " +
            "td.prizeFlag, t.recorderName, t.attnName, t.auditName, t.themeStatus, t.id, td.remark, td.eventTypeName, t.aposScore, t.anegScore, t.bposScore, t.bnegScore) " +
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.themeType = :themeType and td.status = :status and t.themeStatus = :themeStatus and td.themeDate >= :stM and td.themeDate <= :etM and" +
            " (td.modifyFlag = :modifyFlag) and (:empFullName is null or td.empFullName like concat('%', :empFullName, '%')) and" +
            "(:themeName is null or td.themeName like concat('%', :themeName, '%')) and (:eventName is null or td.eventName like concat('%', :eventName, '%')) order by td.themeDate")
    List<ThemeDetailEventViewVo> findAllByEventViewOrderByThemeDate(int themeType, int status, int themeStatus, Date stM, Date etM, int modifyFlag, String empFullName, String themeName, String eventName);

    @Query("select new com.three.points.vo.ManagerTaskScoreVo(td.managerTaskName, td.managerTaskDate, td.managerTaskIndex, td.managerTaskType, td.scoreNegType, td.bscore) " +
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.status = :status and td.empId = :empId and " +
            "t.themeStatus = :themeStatus and td.managerTaskDate = :stM and td.themeType = :themeType")
    Page<ManagerTaskScoreVo> findAllByManagerTaskScorePageable(int status, String empId, int themeStatus, Date stM, int themeType, Pageable pageable);

    @Query("select new com.three.points.vo.ManagerTaskScoreVo(td.managerTaskName, td.managerTaskDate, td.managerTaskIndex, td.managerTaskType, td.scoreNegType, td.bscore) " +
            "from ThemeDetail td, Theme t where td.themeId = t.id and td.status = :status and td.empId = :empId and " +
            "t.themeStatus = :themeStatus and td.managerTaskDate = :stM and td.themeType = :themeType")
    List<ManagerTaskScoreVo> findAllByManagerTaskScoreSort(int status, String empId, int themeStatus, Date stM, int themeType, Sort sort);

}