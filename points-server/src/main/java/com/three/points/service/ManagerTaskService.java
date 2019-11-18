package com.three.points.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.three.common.enums.YesNoEnum;
import com.three.common.utils.DateUtils;
import com.three.commonclient.exception.BusinessException;
import com.three.commonclient.exception.ParameterException;
import com.three.points.entity.ManagerTask;
import com.three.points.entity.ManagerTaskEmp;
import com.three.points.entity.Theme;
import com.three.points.enums.ManagerTaskEnum;
import com.three.points.enums.ThemeStatusEnum;
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
import com.three.points.repository.ThemeRepository;
import com.three.points.vo.DateVo;
import com.three.points.vo.TaskStatisticsVo;
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

    @Autowired
    private ThemeRepository themeRepository;

    @Transactional
    public void create(ManagerTaskParam managerTaskParam) {
        BeanValidator.check(managerTaskParam);

        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
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
            managerTask1.setOrganizationId(firstOrganizationId);
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

        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();

        ManagerTask managerTask = getEntityById(managerTaskRepository, managerTaskParam.getId());
        // 同一个月的所有任务名称不能相同
        if (managerTaskRepository.countByTaskNameAndOrganizationIdAndTaskDateAndIdNot(managerTaskParam.getTaskName(), firstOrganizationId, managerTask.getTaskDate(), managerTask.getId()) > 0) {
            throw new ParameterException("任务名称已经存在");
        }
        managerTask = (ManagerTask) BeanCopyUtil.copyBean(managerTaskParam, managerTask);

        managerTask = managerTaskRepository.save(managerTask);
        // 修改参与人员
        if (managerTaskParam.getManagerTaskEmpParamList().size() > 0) {
            updateEmp(managerTask, managerTaskParam.getManagerTaskEmpParamList());
        }
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
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
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
        managerTaskEmpRepository.deleteAllByTaskId(managerTask.getId());
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

    public PageResult<ManagerTask> query(PageQuery pageQuery, int code, Long taskDate, String taskName) {
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
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
            if (StringUtil.isNotBlank(taskName)) {
                predicateList.add(criteriaBuilder.like(root.get("taskName"), "%" + taskName + "%"));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        if (pageQuery != null) {
            return query(managerTaskRepository, pageQuery, sort, specification);
        } else {
            return query(managerTaskRepository, sort, specification);
        }
    }

    public ManagerTask findById(String id) {
        ManagerTask managerTask = getEntityById(managerTaskRepository, id);
        List<ManagerTaskEmp> managerTaskEmpList = managerTaskEmpRepository.findAllByTaskId(managerTask.getId());
        managerTask.setManagerTaskEmpList(managerTaskEmpList);
        return managerTask;
    }

    public ManagerTask findNextTask(String id) {
        ManagerTask managerTask = getEntityById(managerTaskRepository, id);
        Date taskDateNext = DateUtil.offsetMonth(managerTask.getTaskDate(), 1);
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        ManagerTask managerTaskNext = managerTaskRepository.findByOrganizationIdAndTaskNameAndTaskDate(firstOrganizationId, managerTask.getTaskName(), taskDateNext);
        if (managerTaskNext == null) {
            throw new BusinessException("系统中不存在该任务的下个月配置信息");
        }
        List<ManagerTaskEmp> managerTaskEmpList = managerTaskEmpRepository.findAllByTaskId(managerTaskNext.getId());
        managerTaskNext.setManagerTaskEmpList(managerTaskEmpList);
        return managerTaskNext;
    }

    public List<ManagerTaskEmp> findNextEmp(String id) {
        ManagerTask managerTaskNext = findNextTask(id);
        return managerTaskEmpRepository.findAllByTaskId(managerTaskNext.getId());
    }

    public Set<String> findCurMonthTaskEmp(String firstOrganizationId, Date taskDate) {
        List<ManagerTaskEmp> managerTaskEmpList = managerTaskEmpRepository.findAllByOrganizationIdAndTaskDate(firstOrganizationId, taskDate);
        Set<String> empIdSet = new HashSet<>();
        managerTaskEmpList.forEach(e -> {
            empIdSet.add(e.getEmpId());
        });
        return empIdSet;
    }

    public ManagerTaskEmp queryTaskMySelf(Long taskDateM, String empId) {
        Date date = new Date();
        if (taskDateM != null) {
            date = new Date(taskDateM);
        }
        Date stD = DateUtil.beginOfDay(date);
        Date etD = DateUtil.endOfDay(date);
        Date stW = DateUtil.beginOfWeek(date);
        Date etW = DateUtil.endOfWeek(date);
        Date stM = DateUtil.beginOfMonth(date); // 查找任务日期月份第一天
        Date etM = DateUtil.endOfMonth(date); // 任务月份最后时间
        if (StringUtil.isBlank(empId)) {
            empId = LoginUserUtil.getLoginUserEmpId(); // 当前登录用户
        }
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        ManagerTaskEmp managerTaskEmp = managerTaskEmpRepository.findAllByOrganizationIdAndTaskDateAndEmpId(firstOrganizationId, stM, empId);
        ManagerTask managerTask = getEntityById(managerTaskRepository, managerTaskEmp.getTaskId());
        managerTaskEmp.setManagerTask(managerTask);

        // 设置相关任务完成情况
        // 查找主题，条件：初审人ID、审核通过、奖扣时间范围
        List<Theme> themeList = new ArrayList<>();
        // 奖/扣分任务
        if (YesNoEnum.YES.getCode() == managerTask.getScoreTaskFlag()) {
            if (ManagerTaskEnum.TASK_DAY.getCode() == managerTask.getScoreCycle()) {
                themeList = themeRepository.findAllByAttnIdAndThemeStatusAndThemeDateBetween(managerTaskEmp.getEmpId(), ThemeStatusEnum.SUCCESS.getCode(), stD, etD);
            } else if (ManagerTaskEnum.TASK_WEEK.getCode() == managerTask.getScoreCycle()) {
                themeList = themeRepository.findAllByAttnIdAndThemeStatusAndThemeDateBetween(managerTaskEmp.getEmpId(), ThemeStatusEnum.SUCCESS.getCode(), stW, etW);
            } else {
                themeList = themeRepository.findAllByAttnIdAndThemeStatusAndThemeDateBetween(managerTaskEmp.getEmpId(), ThemeStatusEnum.SUCCESS.getCode(), stM, etM);
            }
        }
        for (Theme theme : themeList) {
            managerTaskEmp.setScoreAwardCompleted(managerTaskEmp.getScoreAwardCompleted() + theme.getBposScore());
            managerTaskEmp.setScoreDeductCompleted(managerTaskEmp.getScoreDeductCompleted() + theme.getBnegScore());
        }
        // 人次任务
        themeList.clear();
        if (YesNoEnum.YES.getCode() == managerTask.getEmpCountTaskFlag()) {
            if (ManagerTaskEnum.TASK_DAY.getCode() == managerTask.getEmpCountCycle()) {
                themeList = themeRepository.findAllByAttnIdAndThemeStatusAndThemeDateBetween(managerTaskEmp.getEmpId(), ThemeStatusEnum.SUCCESS.getCode(), stD, etD);
            } else if (ManagerTaskEnum.TASK_WEEK.getCode() == managerTask.getEmpCountCycle()) {
                themeList = themeRepository.findAllByAttnIdAndThemeStatusAndThemeDateBetween(managerTaskEmp.getEmpId(), ThemeStatusEnum.SUCCESS.getCode(), stW, etW);
            } else {
                themeList = themeRepository.findAllByAttnIdAndThemeStatusAndThemeDateBetween(managerTaskEmp.getEmpId(), ThemeStatusEnum.SUCCESS.getCode(), stM, etM);
            }
        }
        for (Theme theme : themeList) {
            managerTaskEmp.setEmpCountValueCompleted(managerTaskEmp.getEmpCountValueCompleted() + theme.getEmpCount());
        }
        // 比例任务
        themeList.clear();
        if (YesNoEnum.YES.getCode() == managerTask.getRatioTaskFlag()) {
            if (ManagerTaskEnum.TASK_DAY.getCode() == managerTask.getRatioCycle()) {
                themeList = themeRepository.findAllByAttnIdAndThemeStatusAndThemeDateBetween(managerTaskEmp.getEmpId(), ThemeStatusEnum.SUCCESS.getCode(), stD, etD);
            } else if (ManagerTaskEnum.TASK_WEEK.getCode() == managerTask.getRatioCycle()) {
                themeList = themeRepository.findAllByAttnIdAndThemeStatusAndThemeDateBetween(managerTaskEmp.getEmpId(), ThemeStatusEnum.SUCCESS.getCode(), stW, etW);
            } else {
                themeList = themeRepository.findAllByAttnIdAndThemeStatusAndThemeDateBetween(managerTaskEmp.getEmpId(), ThemeStatusEnum.SUCCESS.getCode(), stM, etM);
            }
        }
        for (Theme theme : themeList) {
            managerTaskEmp.setRatioTaskAwardScore(managerTaskEmp.getRatioTaskAwardScore() + theme.getBposScore());
            managerTaskEmp.setRatioTaskDeductScore(managerTaskEmp.getRatioTaskDeductScore() + theme.getBnegScore());
        }
        return managerTaskEmp;
    }

    public TaskStatisticsVo queryTaskStatistics(Long taskDate, String statisticsFlag, String empId) {
        TaskStatisticsVo taskStatisticsVo = new TaskStatisticsVo();
        Date date = new Date();
        if (taskDate != null) {
            date = new Date(taskDate);
        }
        if (StringUtil.isBlank(empId)) {
            empId = LoginUserUtil.getLoginUserEmpId(); // 当前登录用户
        }
        taskStatisticsVo.setEmpId(empId);
        Date st, et;
        Map<String, DateVo> dayMap = new LinkedHashMap<>();
        Map<String, Integer> awardValueMap = new LinkedHashMap<>();
        Map<String, Integer> deductValueMap = new LinkedHashMap<>();
        Map<String, Integer> empCountValueMap = new LinkedHashMap<>();
        Map<String, Double> ratioValueMap = new LinkedHashMap<>();
        if ("2".equals(statisticsFlag)) { // 月统计
            st = DateUtil.beginOfYear(date);
            et = DateUtil.endOfYear(date);
            // 一年有多少个月
            for (int i = DateUtil.month(st); i <= DateUtil.month(et); i++) {
                Date date1 = DateUtil.offsetMonth(st, i);
                DateVo dateVo = new DateVo(DateUtil.beginOfMonth(date1), DateUtil.endOfMonth(date1));
                dayMap.put(String.format("%02d", i + 1), dateVo);
                awardValueMap.put(String.format("%02d", i + 1), 0);
                deductValueMap.put(String.format("%02d", i + 1), 0);
                empCountValueMap.put(String.format("%02d", i + 1), 0);
                ratioValueMap.put(String.format("%02d", i + 1), 0.0);
            }
        } else if ("3".equals(statisticsFlag)) { // 年统计
            st = DateUtil.parse("2019-01-01 00:00:00");
            et = DateUtil.endOfYear(date);
            // 系统使用了几年
            for (int i = DateUtil.year(st); i <= DateUtil.year(et); i++) {
                Date date1 = DateUtil.parse(i + "-01-01 00:00:00");
                DateVo dateVo = new DateVo(DateUtil.beginOfYear(date1), DateUtil.endOfYear(date1));
                dayMap.put(i + "", dateVo);
                awardValueMap.put(i + "", 0);
                deductValueMap.put(i + "", 0);
                empCountValueMap.put(i + "", 0);
                ratioValueMap.put(i + "", 0.0);
            }
        } else { // 默认日统计
            st = DateUtil.beginOfMonth(date);
            et = DateUtil.endOfMonth(date);
            // 一个月有多少天
            for (int i = DateUtil.dayOfMonth(st); i <= DateUtil.dayOfMonth(et); i++) {
                Date date1 = DateUtil.offsetDay(st, i - 1);
                DateVo dateVo = new DateVo(DateUtil.beginOfDay(date1), DateUtil.endOfDay(date1));
                dayMap.put(String.format("%02d", i), dateVo);
                awardValueMap.put(String.format("%02d", i), 0);
                deductValueMap.put(String.format("%02d", i), 0);
                empCountValueMap.put(String.format("%02d", i), 0);
                ratioValueMap.put(String.format("%02d", i), 0.0);
            }
        }
        // 统计结果
        List<Theme> themeList = themeRepository.findAllByAttnIdAndThemeStatusAndThemeDateBetween(empId, ThemeStatusEnum.SUCCESS.getCode(), st, et);
        for (Theme theme : themeList) {
            taskStatisticsVo.setAllAwardScore(taskStatisticsVo.getAllAwardScore() + theme.getBposScore());
            taskStatisticsVo.setAllDeductScore(taskStatisticsVo.getAllDeductScore() + theme.getBnegScore());
            taskStatisticsVo.setAllEmpCount(taskStatisticsVo.getAllEmpCount() + theme.getEmpCount());
            for (Map.Entry<String, DateVo> entry : dayMap.entrySet()) {
                if (theme.getThemeDate().compareTo(entry.getValue().getStartD()) >= 0 && theme.getThemeDate().compareTo(entry.getValue().getEndD()) <= 0) {
                    int value1 = awardValueMap.get(entry.getKey()) + theme.getBposScore();
                    awardValueMap.put(entry.getKey(), value1);
                    int value2 = deductValueMap.get(entry.getKey()) + theme.getBnegScore();
                    deductValueMap.put(entry.getKey(), value2);
                    int value3 = empCountValueMap.get(entry.getKey()) + theme.getEmpCount();
                    empCountValueMap.put(entry.getKey(), value3);
                    if (value1 > 0) {
                        ratioValueMap.put(entry.getKey(), NumberUtil.div(value2, value1, 2));
                    }
                    break;
                }
            }
        }
        // 得到结果
        taskStatisticsVo.setSeriesList(new ArrayList<>(dayMap.keySet()));
        taskStatisticsVo.setAwardValueTrendList(new ArrayList<>(awardValueMap.values()));
        taskStatisticsVo.setDeductValueTrendList(new ArrayList<>(deductValueMap.values()));
        taskStatisticsVo.setEmpCountValueTrendList(new ArrayList<>(empCountValueMap.values()));
        taskStatisticsVo.setRatioValueTrendList(new ArrayList<>(ratioValueMap.values()));
        if (taskStatisticsVo.getAllAwardScore() > 0) {
            taskStatisticsVo.setAllRatio(NumberUtil.div(taskStatisticsVo.getAllDeductScore().doubleValue(), taskStatisticsVo.getAllAwardScore().doubleValue(), 2));
        }
        return taskStatisticsVo;
    }
}