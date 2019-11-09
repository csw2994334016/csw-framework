package com.three.points.service;

import cn.hutool.core.date.DateUtil;
import com.three.common.utils.DateUtils;
import com.three.commonclient.exception.BusinessException;
import com.three.commonclient.exception.ParameterException;
import com.three.points.entity.ManagerTask;
import com.three.points.entity.ManagerTaskEmp;
import com.three.points.param.ManagerTaskEmpParam;
import com.three.points.param.ManagerTaskParam1;
import com.three.points.repository.ManagerTaskEmpRepository;
import com.three.points.repository.ManagerTaskRepository;
import com.three.points.param.ManagerTaskParam;
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
import java.util.*;

/**
 * Created by csw on 2019-11-06.
 * Description:
 */

@Service
public class ManagerTaskService extends BaseService<ManagerTask, String> {

    @Autowired
    private ManagerTaskRepository managerTaskRepository;

    @Autowired
    private ManagerTaskEmpRepository managerTaskEmpRepository;

    private String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();

    @Transactional
    public void create(ManagerTaskParam managerTaskParam) {
        BeanValidator.check(managerTaskParam);

        // 任务名称已经存在
        if (checkTaskNameExist(managerTaskParam.getTaskName(), firstOrganizationId)) {
            throw new ParameterException("任务名称已经存在");
        }

        ManagerTask managerTask = new ManagerTask();
        managerTask = (ManagerTask) BeanCopyUtil.copyBean(managerTaskParam, managerTask);

        managerTask.setOrganizationId(firstOrganizationId);

        List<ManagerTask> managerTaskList = new ArrayList<>();

        // 当前月份第一天
        Date taskDate = DateUtil.parse(DateUtils.getMonthFirstDay(DateUtil.date()));
        // 当前月任务
        managerTask.setTaskDate(taskDate);
        managerTaskList.add(managerTask);
        // 依次生成未来12个月的任务
        for (int i = 0; i < 1; i++) {
            taskDate = DateUtil.offsetMonth(taskDate, 1);
            ManagerTask managerTask1 = new ManagerTask();
            managerTask1 = (ManagerTask) BeanCopyUtil.copyBean(managerTaskParam, managerTask1);
            managerTask1.setTaskDate(taskDate);
            managerTaskList.add(managerTask1);
        }

        managerTaskList = managerTaskRepository.saveAll(managerTaskList);
        // 添加参与人员
        if (managerTaskParam.getManagerTaskEmpParamList().size() > 0) {
            for (ManagerTask managerTask1 : managerTaskList) {
                updateEmp(managerTask1, managerTaskParam.getManagerTaskEmpParamList());
            }
        }
    }

    private boolean checkTaskNameExist(String taskName, String organizationId) {
        return managerTaskRepository.countByTaskNameAndOrganizationId(taskName, organizationId) > 0;
    }

    @Transactional
    public void update(ManagerTaskParam managerTaskParam) {
        BeanValidator.check(managerTaskParam);

        ManagerTask managerTask = getEntityById(managerTaskRepository, managerTaskParam.getId());
        // 同一个月的所有任务名称不能相同
        if (managerTaskRepository.countByTaskNameAndOrganizationIdAndTaskDate(managerTaskParam.getTaskName(), firstOrganizationId, managerTask.getTaskDate()) > 0) {
            throw new ParameterException("任务名称已经存在");
        }
        managerTask = (ManagerTask) BeanCopyUtil.copyBean(managerTaskParam, managerTask);

        managerTask = managerTaskRepository.save(managerTask);
        // 修改参与人员
        if (managerTaskParam.getManagerTaskEmpParamList().size() > 0) {
            updateEmp(managerTask, managerTaskParam.getManagerTaskEmpParamList());
        }

//        List<ManagerTask> managerTaskList = new ArrayList<>();
//        managerTaskList.add(managerTask);
//
//        // 根据任务名称，查找该月份之后的任务，然后修改
//        List<ManagerTask> managerTasks = managerTaskRepository.findAllByTaskNameAndOrganizationIdAndTaskDateAfter(managerTask.getTaskName(), firstOrganizationId, managerTask.getTaskDate());
//        for (ManagerTask managerTask1 : managerTasks) {
//            managerTask1 = (ManagerTask) BeanCopyUtil.copyBean(managerTaskParam, managerTask1, Arrays.asList("id"));
//            managerTaskList.add(managerTask1);
//        }
//
//        managerTaskList = managerTaskRepository.saveAll(managerTaskList);
//        // 添加配置人员
//        if (managerTaskParam.getManagerTaskEmpParamList().size() > 0) {
//            for (ManagerTask managerTask1 : managerTaskList) {
//                updateEmp(managerTask1, managerTaskParam.getManagerTaskEmpParamList());
//            }
//        }
    }

    @Transactional
    public void updateEmpOnly(ManagerTaskParam1 managerTaskParam1) {
        BeanValidator.check(managerTaskParam1);

        ManagerTask managerTask = getEntityById(managerTaskRepository, managerTaskParam1.getTaskId());

        updateEmp(managerTask, managerTaskParam1.getManagerTaskEmpParamList());
    }

    @Transactional
    public void updateEmp(ManagerTask managerTask, List<ManagerTaskEmpParam> managerTaskEmpParamList) {
        Set<String> empIdSet = new HashSet<>();
        for (ManagerTaskEmpParam managerTaskEmpParam : managerTaskEmpParamList) {
            BeanValidator.check(managerTaskEmpParam);
            empIdSet.add(managerTaskEmpParam.getEmpId());
        }
        // 人员在当前月的其它任务中不能出现
        List<ManagerTaskEmp> managerTaskEmpList = managerTaskEmpRepository.findAllByOrganizationIdAndTaskIdNotAndTaskDateAndEmpIdIn(firstOrganizationId, managerTask.getId(), managerTask.getTaskDate(), empIdSet);
        if (managerTaskEmpList.size() > 0) {
            List<String> errorList = new ArrayList<>();
            for (ManagerTaskEmp managerTaskEmp : managerTaskEmpList) {
                errorList.add(managerTaskEmp.getEmpFullName() + "在" + managerTaskEmp.getTaskName() + "中存在");
            }
            throw new BusinessException("人员在其它任务中出现：" + errorList.toString());
        }
        // 删除该任务原有的人员信息
        managerTaskEmpRepository.deleteByTaskId(managerTask.getId());
        // 重新添加人员
        managerTaskEmpList.clear();
        for (ManagerTaskEmpParam managerTaskEmpParam : managerTaskEmpParamList) {
            ManagerTaskEmp managerTaskEmp = new ManagerTaskEmp();
            managerTaskEmp = (ManagerTaskEmp) BeanCopyUtil.copyBean(managerTaskEmpParam, managerTaskEmp);
            managerTaskEmp.setOrganizationId(firstOrganizationId);
            managerTaskEmp.setTaskId(managerTask.getId());
            managerTaskEmp.setTaskName(managerTask.getTaskName());
            managerTaskEmp.setTaskDate(managerTask.getTaskDate());
            managerTaskEmpList.add(managerTaskEmp);
        }
        managerTaskEmpRepository.saveAll(managerTaskEmpList);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<ManagerTask> managerTaskList = new ArrayList<>();
        for (String id : idSet) {
            ManagerTask managerTask = getEntityById(managerTaskRepository, String.valueOf(id));
            managerTask.setStatus(code);
            managerTaskList.add(managerTask);
        }

        managerTaskRepository.saveAll(managerTaskList);
    }

    public PageResult<ManagerTask> query(PageQuery pageQuery, int code, Long taskDate) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<ManagerTask> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            Specification<ManagerTask> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, firstOrganizationId);
            predicateList.add(codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder));
            Date taskDate1 = DateUtil.parse(DateUtils.getMonthFirstDay(new Date()));
            if (taskDate != null) {
                taskDate1 = DateUtil.parse(DateUtils.getMonthFirstDay(new Date(taskDate)));
            }
            predicateList.add(criteriaBuilder.equal(root.get("taskDate"), taskDate1));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        if (pageQuery != null) {
            return query(managerTaskRepository, pageQuery, sort, specification);
        } else {
            return query(managerTaskRepository, sort, specification);
        }
    }

    public ManagerTask findById(String id) {
        return getEntityById(managerTaskRepository, id);
    }

    public ManagerTask findNextTask(String id) {
        ManagerTask managerTask = getEntityById(managerTaskRepository, id);
        Date taskDateNext = DateUtil.offsetMonth(managerTask.getTaskDate(), 1);
        ManagerTask managerTaskNext = managerTaskRepository.findByOrganizationIdAndTaskNameAndTaskDate(firstOrganizationId, managerTask.getTaskName(), taskDateNext);
        if (managerTaskNext == null) {
            throw new BusinessException("系统中不存在该任务的下个月配置信息");
        }
        return managerTaskNext;
    }

    public List<ManagerTaskEmp> findNextEmp(String id) {
        ManagerTask managerTaskNext = findNextTask(id);
        return managerTaskEmpRepository.findAllByTaskId(managerTaskNext.getId());
    }

    public Set<String> findCurMonthTaskEmp(Date taskDate) {
        List<ManagerTaskEmp> managerTaskEmpList = managerTaskEmpRepository.findAllByOrganizationIdAndTaskDate(firstOrganizationId, taskDate);
        Set<String> empIdSet = new HashSet<>();
        managerTaskEmpList.forEach(e -> {
            empIdSet.add(e.getEmpId());
        });
        return empIdSet;
    }
}