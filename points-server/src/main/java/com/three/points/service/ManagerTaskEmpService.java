package com.three.points.service;

import cn.hutool.core.date.DateUtil;
import com.three.points.entity.ManagerTask;
import com.three.points.entity.ManagerTaskEmp;
import com.three.points.enums.ManagerTaskEnum;
import com.three.points.repository.ManagerTaskEmpRepository;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.repository.ManagerTaskRepository;
import com.three.points.vo.ManagerTaskEmpVo;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * Created by csw on 2019-11-07.
 * Description:
 */

@Service
public class ManagerTaskEmpService extends BaseService<ManagerTaskEmp, String> {

    @Autowired
    private ManagerTaskEmpRepository managerTaskEmpRepository;

    @Autowired
    private ManagerTaskRepository managerTaskRepository;

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

    public PageResult<ManagerTaskEmpVo> query(PageQuery pageQuery, int code, String orgId, String taskId, String taskName, Long taskDate, String empFullName) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<ManagerTaskEmp> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
            Specification<ManagerTaskEmp> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, firstOrganizationId);
            predicateList.add(codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder));
            if (StringUtil.isNotBlank(orgId)) {
                predicateList.add(criteriaBuilder.equal(root.get("empOrgId"), orgId));
            }
            if (StringUtil.isNotBlank(taskId)) {
                predicateList.add(criteriaBuilder.equal(root.get("taskId"), taskId));
            }
            if (StringUtil.isNotBlank(taskName)) {
                predicateList.add(criteriaBuilder.like(root.get("taskName"), "%" + taskName + "%"));
            }
            if (taskDate != null) {
                Date taskDate1 = DateUtil.beginOfMonth(new Date(taskDate));
                predicateList.add(criteriaBuilder.equal(root.get("taskDate"), taskDate1));
            }
            if (StringUtil.isNotBlank(empFullName)) {
                predicateList.add(criteriaBuilder.like(root.get("empFullName"), "%" + empFullName + "%"));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        PageResult<ManagerTaskEmp> pageResult;
        if (pageQuery != null) {
            pageResult = query(managerTaskEmpRepository, pageQuery, sort, specification);
        } else {
            pageResult = query(managerTaskEmpRepository, sort, specification);
        }
        List<ManagerTaskEmpVo> managerTaskEmpVoList = new ArrayList<>();
        Map<String, ManagerTask> managerTaskMap = new HashMap<>();
        for (ManagerTaskEmp managerTaskEmp : pageResult.getData()) {
            ManagerTaskEmpVo managerTaskEmpVo = new ManagerTaskEmpVo();
            managerTaskEmpVo = (ManagerTaskEmpVo) BeanCopyUtil.copyBean(managerTaskEmp, managerTaskEmpVo);
            managerTaskMap.computeIfAbsent(managerTaskEmp.getTaskId(), k -> managerTaskRepository.findById(managerTaskEmp.getTaskId()).orElse(null));
            managerTaskEmpVoList.add(managerTaskEmpVo);
        }
        // 任务指标、完成得分
        for (ManagerTaskEmpVo managerTaskEmpVo : managerTaskEmpVoList) {
            ManagerTask managerTask = managerTaskMap.get(managerTaskEmpVo.getTaskId());
            if (managerTask != null) {
                managerTaskEmpVo.setTaskIndex(getTaskIndex(managerTask));
            }
        }

        return new PageResult<>(managerTaskEmpVoList.size(), managerTaskEmpVoList);
    }

    private String getTaskIndex(ManagerTask managerTask) {
        String re = "";
        if (managerTask.getScoreCycle() != null) {
            re += "奖分:" + managerTask.getScoreAwardMin() + "/" + ManagerTaskEnum.getMessageByCode(managerTask.getScoreCycle()) +
                    " 扣分:" + managerTask.getScoreDeductMin() + "/" + ManagerTaskEnum.getMessageByCode(managerTask.getScoreCycle());
        }
        if (managerTask.getEmpCountCycle() != null) {
            re += " 人次:" + managerTask.getEmpCountValue() + "/" + ManagerTaskEnum.getMessageByCode(managerTask.getEmpCountCycle());
        }
        if (managerTask.getRatioCycle() != null) {
            re += " 比例:" + managerTask.getRatioValue() + "/" + ManagerTaskEnum.getMessageByCode(managerTask.getRatioCycle());
        }
        return re;
    }

    public ManagerTaskEmp findById(String id) {
        return getEntityById(managerTaskEmpRepository, id);
    }
}