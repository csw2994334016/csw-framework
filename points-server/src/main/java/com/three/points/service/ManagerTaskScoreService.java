package com.three.points.service;

import cn.hutool.core.date.DateUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.exception.BusinessException;
import com.three.points.entity.ManagerTaskScore;
import com.three.points.repository.ManagerTaskScoreRepository;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ManagerTaskScoreService extends BaseService<ManagerTaskScore, String> {

    @Autowired
    private ManagerTaskScoreRepository managerTaskScoreRepository;

    public PageResult<ManagerTaskScore> managerTaskScore(PageQuery pageQuery, int code, Long taskDate, String empId) {
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        if (empId != null) {
            loginUserEmpId = empId;
        }
        if (StringUtil.isNotBlank(loginUserEmpId)) {
            Date date = new Date();
            if (taskDate != null) {
                date = new Date(taskDate);
            }
            Date stM = DateUtil.beginOfMonth(date); // 查找任务日期月份第一天
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            Specification<ManagerTaskScore> specification = (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = new ArrayList<>();
                Specification<ManagerTaskScore> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, LoginUserUtil.getLoginUserFirstOrganizationId());
                predicateList.add(codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder));
                predicateList.add(criteriaBuilder.equal(root.get("empId"), empId));
                predicateList.add(criteriaBuilder.equal(root.get("taskDate"), stM));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
            if (pageQuery != null) {
                return query(managerTaskScoreRepository, pageQuery, sort, specification);
            } else {
                return query(managerTaskScoreRepository, sort, specification);
            }
        } else {
            throw new BusinessException("用户没有登录，无法查找奖扣任务记录");
        }
    }
}
