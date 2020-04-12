package com.three.points.repository;

import com.three.points.entity.CustomReportGroup;
import com.three.points.vo.ReportGroupVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by csw on 2020/04/12.
 * Description:
 */
public interface CustomReportGroupRepository extends JpaRepository<CustomReportGroup, String> {

    void deleteByReportId(String reportId);

    @Query("select new com.three.points.vo.ReportGroupVo(rg.reportId, rg.groupId, g.groupName) " +
            "from CustomReportGroup rg left join CustomGroup g on rg.groupId = g.id " +
            "where rg.reportId = :id ")
    List<ReportGroupVo> findByReportId(String id);
}
