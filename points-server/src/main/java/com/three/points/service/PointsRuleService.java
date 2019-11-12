package com.three.points.service;

import com.three.common.enums.StatusEnum;
import com.three.commonclient.exception.BusinessException;
import com.three.points.entity.PointsRule;
import com.three.points.entity.PointsRuleEmpCount;
import com.three.points.param.PointsRuleEmpCountParam;
import com.three.points.repository.PointsRuleEmpCountRepository;
import com.three.points.repository.PointsRuleRepository;
import com.three.points.param.PointsRuleParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.commonclient.utils.BeanValidator;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2019-11-12.
 * Description:
 */

@Service
public class PointsRuleService extends BaseService<PointsRule, String> {

    @Autowired
    private PointsRuleRepository pointsRuleRepository;

    @Autowired
    private PointsRuleEmpCountRepository pointsRuleEmpCountRepository;

    @Transactional
    public void create(PointsRuleParam pointsRuleParam) {
        BeanValidator.check(pointsRuleParam);

        PointsRule pointsRule = new PointsRule();
        pointsRule = (PointsRule) BeanCopyUtil.copyBean(pointsRuleParam, pointsRule);

        pointsRule.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());

        pointsRule = pointsRuleRepository.save(pointsRule);

        // 保存记录人奖扣人次奖分设置
        savePointsRuleEmpCount(pointsRule, pointsRuleParam);
    }

    private void savePointsRuleEmpCount(PointsRule pointsRule, PointsRuleParam pointsRuleParam) {
        List<PointsRuleEmpCount> pointsRuleEmpCountList = new ArrayList<>();
        for (PointsRuleEmpCountParam pointsRuleEmpCountParam : pointsRuleParam.getPointsRuleEmpCountParamList()) {
            BeanValidator.check(pointsRuleEmpCountParam);
            PointsRuleEmpCount pointsRuleEmpCount = new PointsRuleEmpCount();
            pointsRuleEmpCount = (PointsRuleEmpCount) BeanCopyUtil.copyBean(pointsRuleEmpCountParam, pointsRuleEmpCount);
            pointsRuleEmpCount.setPointsRuleId(pointsRule.getId());
            pointsRuleEmpCountList.add(pointsRuleEmpCount);
        }
        pointsRuleEmpCountRepository.saveAll(pointsRuleEmpCountList);
    }

    @Transactional
    public void update(PointsRuleParam pointsRuleParam) {
        BeanValidator.check(pointsRuleParam);

        PointsRule pointsRule = getEntityById(pointsRuleRepository, pointsRuleParam.getId());
        pointsRule = (PointsRule) BeanCopyUtil.copyBean(pointsRuleParam, pointsRule);

        pointsRuleRepository.save(pointsRule);

        pointsRuleEmpCountRepository.deleteByPointsRuleId(pointsRule.getId());
        // 保存记录人奖扣人次奖分设置
        savePointsRuleEmpCount(pointsRule, pointsRuleParam);

    }

    public PointsRule findPointsRule() {
        PointsRule pointsRule = pointsRuleRepository.findByOrganizationIdAndStatus(LoginUserUtil.getLoginUserFirstOrganizationId(), StatusEnum.OK.getCode());
        if (pointsRule == null) {
            throw new BusinessException("系统当前没有积分规则设置，请新加规则");
        }
        List<PointsRuleEmpCount> pointsRuleEmpCountList = pointsRuleEmpCountRepository.findAllByPointsRuleId(pointsRule.getId());
        pointsRule.setPointsRuleEmpCountList(pointsRuleEmpCountList);
        return pointsRule;
    }
}