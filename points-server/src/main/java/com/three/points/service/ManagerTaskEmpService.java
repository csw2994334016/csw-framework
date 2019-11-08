package com.three.points.service;

import cn.hutool.core.date.DateUtil;
import com.three.common.utils.DateUtils;
import com.three.points.entity.ManagerTaskEmp;
import com.three.points.repository.ManagerTaskEmpRepository;
import com.three.points.param.ManagerTaskEmpParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019-11-07.
 * Description:
 */

@Service
public class ManagerTaskEmpService extends BaseService<ManagerTaskEmp, String> {

    @Autowired
    private ManagerTaskEmpRepository managerTaskEmpRepository;

    private String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<ManagerTaskEmp> managerTaskEmpList = new ArrayList<>();
        for (String id : idSet) {
            ManagerTaskEmp managerTaskEmp = getEntityById(managerTaskEmpRepository, String.valueOf(id));
            managerTaskEmp.setStatus(code);
            managerTaskEmpList.add(managerTaskEmp);
        }

        managerTaskEmpRepository.saveAll(managerTaskEmpList);
    }

    public PageResult<ManagerTaskEmp> query(PageQuery pageQuery, int code, String taskId, String taskName, Long taskDate, String empFullName) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<ManagerTaskEmp> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            Specification<ManagerTaskEmp> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, firstOrganizationId);
            predicateList.add(codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder));

            if (StringUtil.isNotBlank(taskId)) {
                predicateList.add(criteriaBuilder.equal(root.get("taskId"), taskId));
            }
            if (StringUtil.isNotBlank(taskName)) {
                predicateList.add(criteriaBuilder.like(root.get("taskName"), "%" + taskName + "%"));
            }
            if (taskDate != null) {
                Date taskDate1 = DateUtil.parse(DateUtils.getMonthFirstDay(new Date(taskDate)));
                predicateList.add(criteriaBuilder.equal(root.get("taskDate"), taskDate1));
            }
            if (StringUtil.isNotBlank(empFullName)) {
                predicateList.add(criteriaBuilder.like(root.get("empFullName"), "%" + empFullName + "%"));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        if (pageQuery != null) {
            return query(managerTaskEmpRepository, pageQuery, sort, specification);
        } else {
            return query(managerTaskEmpRepository, sort, specification);
        }
    }

    public ManagerTaskEmp findById(String id) {
        return getEntityById(managerTaskEmpRepository, id);
    }
}