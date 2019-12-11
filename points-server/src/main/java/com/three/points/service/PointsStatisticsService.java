package com.three.points.service;

import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.repository.ManagerTaskScoreRepository;
import com.three.points.vo.PointsStatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PointsStatisticsService{

    @Autowired
    private ManagerTaskScoreRepository managerTaskScoreRepository;

    public PageResult<PointsStatisticsVo> themeDetailStatistics(PageQuery pageQuery, int code, String orgId, Long themeDateSt, Long themeDateEt, String searchValue) {
        return null;
    }
}
