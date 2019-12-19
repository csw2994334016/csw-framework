package com.three.points.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.three.common.enums.StatusEnum;
import com.three.common.enums.YesNoEnum;
import com.three.common.utils.DateUtils;
import com.three.commonclient.exception.BusinessException;
import com.three.commonclient.exception.ParameterException;
import com.three.points.entity.ManagerTask;
import com.three.points.entity.ManagerTaskEmp;
import com.three.points.entity.ManagerTaskScore;
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
import com.three.points.repository.ManagerTaskScoreRepository;
import com.three.points.repository.ThemeRepository;
import com.three.points.vo.DateVo;
import com.three.points.vo.MyManagerTaskVo;
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

    @Autowired
    private ManagerTaskScoreRepository managerTaskScoreRepository;

    @Transactional
    public void create(ManagerTaskParam managerTaskParam) {
        BeanValidator.check(managerTaskParam);
        checkValueCorrect(managerTaskParam);

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
        // 依次生成未来1个月的任务
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

    private void checkValueCorrect(ManagerTaskParam managerTaskParam) {
        if (managerTaskParam.getScoreAwardMin() != null && managerTaskParam.getScoreAwardMin() < 0) {
            throw new ParameterException("奖分下限只能是正数");
        }
        if (managerTaskParam.getScoreDeductMin() != null && managerTaskParam.getScoreDeductMin() > 0) {
            throw new ParameterException("扣分下限只能是负数");
        }
        if (managerTaskParam.getEmpCountValue() != null && managerTaskParam.getEmpCountValue() < 0) {
            throw new ParameterException("奖扣人次只能是正数");
        }
    }

    private boolean checkTaskNameExist(String taskName, String organizationId) {
        return managerTaskRepository.countByTaskNameAndOrganizationId(taskName, organizationId) > 0;
    }

    @Transactional
    public void update(ManagerTaskParam managerTaskParam) {
        BeanValidator.check(managerTaskParam);
        checkValueCorrect(managerTaskParam);

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

    /**
     * 我的管理任务，查找（当前登录）用户作为初审人审核通过的积分奖扣主题，统计b分
     * @param taskDateM
     * @param empId
     * @return
     */
    public MyManagerTaskVo queryTaskMySelf(Long taskDateM, String empId) {
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

        MyManagerTaskVo myManagerTaskVo = new MyManagerTaskVo();
        myManagerTaskVo.setEmpId(managerTaskEmp.getEmpId());
        myManagerTaskVo.setEmpFullName(managerTaskEmp.getEmpFullName());
        myManagerTaskVo.setManagerTask(managerTask);

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
            myManagerTaskVo.setScoreAwardCompleted(myManagerTaskVo.getScoreAwardCompleted() + theme.getBposScore());
            myManagerTaskVo.setScoreDeductCompleted(myManagerTaskVo.getScoreDeductCompleted() + theme.getBnegScore());
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
            myManagerTaskVo.setEmpCountValueCompleted(myManagerTaskVo.getEmpCountValueCompleted() + theme.getEmpCount());
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
            myManagerTaskVo.setRatioTaskAwardScore(myManagerTaskVo.getRatioTaskAwardScore() + theme.getBposScore());
            myManagerTaskVo.setRatioTaskDeductScore(myManagerTaskVo.getRatioTaskDeductScore() + theme.getBnegScore());
        }
        return myManagerTaskVo;
    }

    /**
     * 管理任务统计，查找（当前登录）用户作为初审人审核通过的积分奖扣主题，按日/周/月统计b分
     * @param taskDate
     * @param statisticsFlag
     * @param empId
     * @return
     */
    public TaskStatisticsVo queryTaskStatistics(Long taskDate, String statisticsFlag, String empId) {
        TaskStatisticsVo taskStatisticsVo = new TaskStatisticsVo();
        Date date = new Date();
        if (taskDate != null) {
            date = new Date(taskDate);
        }
        if (StringUtil.isBlank(empId)) {
            empId = LoginUserUtil.getLoginUserEmpId(); // 当前登录用户，作为初审人审核通过了多少分
        }
        if (StringUtil.isBlank(empId)) {
            throw new BusinessException("没有用户信息，无法查管理任务统计");
        }
        taskStatisticsVo.setEmpId(empId);
        Date st, et;
        Map<String, DateVo> dateMap = new LinkedHashMap<>();
        Map<String, Integer> awardValueMap = new LinkedHashMap<>();
        Map<String, Integer> deductValueMap = new LinkedHashMap<>();
        Map<String, Integer> empCountValueMap = new LinkedHashMap<>();
        Map<String, Double> ratioValueMap = new LinkedHashMap<>();
        if ("3".equals(statisticsFlag)) { // 月统计
            st = DateUtil.beginOfYear(date);
            et = DateUtil.endOfYear(date);
            // 一年有多少个月
            for (int i = DateUtil.month(st); i <= DateUtil.month(et); i++) {
                Date date1 = DateUtil.offsetMonth(st, i);
                DateVo dateVo = new DateVo(DateUtil.beginOfMonth(date1), DateUtil.endOfMonth(date1));
                initMap(dateVo, i, dateMap, awardValueMap, deductValueMap, empCountValueMap, ratioValueMap);
            }
        } else if ("2".equals(statisticsFlag)) { // 周统计
            st = DateUtil.beginOfMonth(date);
            et = DateUtil.endOfMonth(date);
            // 这个月有几周
            for (int i = DateUtil.weekOfMonth(st); i <= DateUtil.weekOfMonth(et); i++) {
                Date date1 = DateUtil.offsetWeek(st, i - 1);
                DateVo dateVo = new DateVo(DateUtil.beginOfWeek(date1), DateUtil.endOfWeek(date1));
                initMap(dateVo, i, dateMap, awardValueMap, deductValueMap, empCountValueMap, ratioValueMap);
            }
        } else if ("4".equals(statisticsFlag)) { // 只统计这个月份
            st = DateUtil.beginOfMonth(date);
            et = DateUtil.endOfMonth(date);
            DateVo dateVo = new DateVo(st, et);
            initMap(dateVo, DateUtil.month(st), dateMap, awardValueMap, deductValueMap, empCountValueMap, ratioValueMap);
        } else { // 默认日统计
            st = DateUtil.beginOfMonth(date);
            et = DateUtil.endOfMonth(date);
            // 一个月有多少天
            for (int i = DateUtil.dayOfMonth(st); i <= DateUtil.dayOfMonth(et); i++) {
                Date date1 = DateUtil.offsetDay(st, i - 1);
                DateVo dateVo = new DateVo(DateUtil.beginOfDay(date1), DateUtil.endOfDay(date1));
                initMap(dateVo, i, dateMap, awardValueMap, deductValueMap, empCountValueMap, ratioValueMap);
            }
        }
        // 统计结果
        List<Theme> themeList = themeRepository.findAllByAttnIdAndThemeStatusAndThemeDateBetween(empId, ThemeStatusEnum.SUCCESS.getCode(), st, et);
        for (Theme theme : themeList) {
            taskStatisticsVo.setAllAwardScore(taskStatisticsVo.getAllAwardScore() + theme.getBposScore());
            taskStatisticsVo.setAllDeductScore(taskStatisticsVo.getAllDeductScore() + theme.getBnegScore());
            taskStatisticsVo.setAllEmpCount(taskStatisticsVo.getAllEmpCount() + theme.getEmpCount());
            for (Map.Entry<String, DateVo> entry : dateMap.entrySet()) {
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
        taskStatisticsVo.setSeriesList(new ArrayList<>(dateMap.keySet()));
        taskStatisticsVo.setAwardValueTrendList(new ArrayList<>(awardValueMap.values()));
        taskStatisticsVo.setDeductValueTrendList(new ArrayList<>(deductValueMap.values()));
        taskStatisticsVo.setEmpCountValueTrendList(new ArrayList<>(empCountValueMap.values()));
        taskStatisticsVo.setRatioValueTrendList(new ArrayList<>(ratioValueMap.values()));
        if (taskStatisticsVo.getAllAwardScore() > 0) {
            taskStatisticsVo.setAllRatio(NumberUtil.div(taskStatisticsVo.getAllDeductScore().doubleValue(), taskStatisticsVo.getAllAwardScore().doubleValue(), 2));
        }
        return taskStatisticsVo;
    }

    private void initMap(DateVo dateVo, int i, Map<String, DateVo> dateMap, Map<String, Integer> awardValueMap, Map<String, Integer> deductValueMap, Map<String, Integer> empCountValueMap, Map<String, Double> ratioValueMap) {
        dateMap.put(String.format("%02d", i), dateVo);
        awardValueMap.put(String.format("%02d", i), 0);
        deductValueMap.put(String.format("%02d", i), 0);
        empCountValueMap.put(String.format("%02d", i), 0);
        ratioValueMap.put(String.format("%02d", i), 0.0);
    }

    /**
     * 被定时任务调用
     *
     * @param name
     * @param method
     * @param firstOrganizationId
     */
    @Transactional
    public void generateNextManagerTask(String name, String method, String firstOrganizationId) {
        Date curTaskDate = DateUtil.beginOfMonth(new Date());
        // 查看当前月的下一个月管理任务是否存在，如果不存在，则根据当前月的管理任务生成下个月的任务
        List<ManagerTask> curManagerTaskList = findManagerTaskListByTaskDate(curTaskDate, firstOrganizationId);
        Date nextTaskDate = DateUtil.offsetMonth(curTaskDate, 1);
        for (ManagerTask curManagerTask : curManagerTaskList) {
            ManagerTask nextManagerTask = managerTaskRepository.findByOrganizationIdAndTaskNameAndTaskDate(curManagerTask.getOrganizationId(), curManagerTask.getTaskName(), nextTaskDate);
            if (nextManagerTask == null) {
                // 生成下个月管理任务
                nextManagerTask = new ManagerTask();
                nextManagerTask = (ManagerTask) BeanCopyUtil.copyBean(curManagerTask, nextManagerTask, Arrays.asList("id"));
                nextManagerTask.setTaskDate(nextTaskDate);
                nextManagerTask = managerTaskRepository.save(nextManagerTask);
                // 生成下个月管理任务的人员配置
                List<ManagerTaskEmp> managerTaskEmpList = managerTaskEmpRepository.findAllByTaskId(curManagerTask.getId());
                List<ManagerTaskEmp> nextManagerTaskEmpList = new ArrayList<>();
                for (ManagerTaskEmp managerTaskEmp : managerTaskEmpList) {
                    ManagerTaskEmp nextManagerTaskEmp = new ManagerTaskEmp();
                    nextManagerTaskEmp = (ManagerTaskEmp) BeanCopyUtil.copyBean(managerTaskEmp, nextManagerTaskEmp, Arrays.asList("id"));
                    nextManagerTaskEmp.setTaskId(nextManagerTask.getId());
                    nextManagerTaskEmpList.add(nextManagerTaskEmp);
                }
                managerTaskEmpRepository.saveAll(nextManagerTaskEmpList);
            }
        }
    }

    private List<ManagerTask> findManagerTaskListByTaskDate(Date curTaskDate, String firstOrganizationId) {
        List<ManagerTask> managerTaskList;
        if (StringUtil.isBlank(firstOrganizationId)) {
            firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        }
        if (StringUtil.isNotBlank(firstOrganizationId)) {
            managerTaskList = managerTaskRepository.findAllByStatusAndOrganizationIdAndTaskDate(StatusEnum.OK.getCode(), firstOrganizationId, curTaskDate);
        } else {
            managerTaskList = managerTaskRepository.findAllByStatusAndTaskDate(StatusEnum.OK.getCode(), curTaskDate);
        }
        return managerTaskList;
    }

    /**
     * 被定时任务调用
     *
     * @param name
     * @param method
     * @param firstOrganizationId
     */
    @Transactional
    public void settleManagerTask(String name, String method, String firstOrganizationId) {
        Date frontTaskDate = DateUtil.beginOfMonth(DateUtil.offsetMonth(new Date(), -1));
        // 查找管理任务、及配置的人员，根据管理任务设置：天/周/月，统计每个人的任务完成情况，进行考核结算
        List<ManagerTaskScore> managerTaskScoreList = new ArrayList<>();
        List<ManagerTask> managerTaskList = findManagerTaskListByTaskDate(frontTaskDate, firstOrganizationId);
        for (ManagerTask managerTask : managerTaskList) {
            List<ManagerTaskEmp> managerTaskEmpList = managerTaskEmpRepository.findAllByTaskId(managerTask.getId());
            for (ManagerTaskEmp managerTaskEmp : managerTaskEmpList) {
                ManagerTaskScore managerTaskScore = new ManagerTaskScore();
                managerTaskScore.setOrganizationId(managerTask.getOrganizationId());
                managerTaskScore.setTaskId(managerTask.getId());
                managerTaskScore.setTaskName(managerTask.getTaskName());
                managerTaskScore.setTaskDate(managerTask.getTaskDate());
                managerTaskScore.setEmpId(managerTaskEmp.getEmpId());
                managerTaskScore.setEmpFullName(managerTaskEmp.getEmpFullName());
                if (YesNoEnum.YES.getCode() == managerTask.getScoreTaskFlag()) {
                    ManagerTaskScore managerTaskScore1 = new ManagerTaskScore();
                    managerTaskScore1 = (ManagerTaskScore) BeanCopyUtil.copyBean(managerTaskScore, managerTaskScore1);
                    managerTaskScore1.setTaskType(ManagerTaskEnum.TASK_TYPE_AWARD.getMessage());
                    managerTaskScore1.setTaskIndex(getTaskIndex(managerTask.getScoreAwardMin(), managerTask.getScoreCycle()));
                    TaskStatisticsVo taskStatisticsVo;
                    if (ManagerTaskEnum.TASK_DAY.getCode() == managerTask.getScoreCycle()) { // 这个月有多少天，按天统计
                        taskStatisticsVo = queryTaskStatistics(frontTaskDate.getTime(), "1", managerTaskEmp.getEmpId());
                    } else if (ManagerTaskEnum.TASK_WEEK.getCode() == managerTask.getScoreCycle()) { // 这个月有多少周，按周统计
                        taskStatisticsVo = queryTaskStatistics(frontTaskDate.getTime(), "2", managerTaskEmp.getEmpId());
                    } else { // 按整个月统计
                        taskStatisticsVo = queryTaskStatistics(frontTaskDate.getTime(), "4", managerTaskEmp.getEmpId());
                    }
                    for (Integer awardValue : taskStatisticsVo.getAwardValueTrendList()) {
                        if (managerTask.getScoreNegScore() == 0) {
                            managerTaskScore1.setScoreNegType("差额扣分");
                            if (awardValue < managerTask.getScoreAwardMin()) {
                                managerTaskScore1.setBscore(managerTaskScore1.getBscore() + awardValue - managerTask.getScoreAwardMin());
                            }
                        } else {
                            managerTaskScore1.setScoreNegType("未完成扣分：" + managerTask.getScoreNegScore()); // 扣分是负分
                            if (awardValue < managerTask.getScoreAwardMin()) {
                                managerTaskScore1.setBscore(managerTaskScore1.getBscore() + managerTask.getScoreNegScore());
                            }
                        }
                    }
                    managerTaskScoreList.add(managerTaskScore1);
                    ManagerTaskScore managerTaskScore2 = new ManagerTaskScore();
                    managerTaskScore2 = (ManagerTaskScore) BeanCopyUtil.copyBean(managerTaskScore, managerTaskScore2);
                    managerTaskScore2.setTaskType(ManagerTaskEnum.TASK_TYPE_NEG.getMessage());
                    managerTaskScore2.setTaskIndex(getTaskIndex(managerTask.getScoreDeductMin(), managerTask.getScoreCycle()));
                    for (Integer deductValue : taskStatisticsVo.getDeductValueTrendList()) {
                        if (managerTask.getScoreNegScore() == 0) {
                            managerTaskScore2.setScoreNegType("差额扣分");
                            if (deductValue > managerTask.getScoreDeductMin()) {
                                managerTaskScore2.setBscore(managerTaskScore2.getBscore() + managerTask.getScoreDeductMin() - deductValue);
                            }
                        } else {
                            managerTaskScore2.setScoreNegType("未完成扣分：" + managerTask.getScoreNegScore());
                            if (deductValue > managerTask.getScoreAwardMin()) {
                                managerTaskScore2.setBscore(managerTaskScore2.getBscore() + managerTask.getScoreNegScore());
                            }
                        }
                    }
                    managerTaskScoreList.add(managerTaskScore2);
                }
                if (YesNoEnum.YES.getCode() == managerTask.getEmpCountTaskFlag()) {
                    ManagerTaskScore managerTaskScore1 = new ManagerTaskScore();
                    managerTaskScore1 = (ManagerTaskScore) BeanCopyUtil.copyBean(managerTaskScore, managerTaskScore1);
                    managerTaskScore1.setTaskType(ManagerTaskEnum.TASK_TYPE_EMP_COUNT.getMessage());
                    managerTaskScore1.setTaskIndex(getTaskIndex(managerTask.getEmpCountValue(), managerTask.getEmpCountCycle()));
                    managerTaskScore1.setScoreNegType("未完成扣分：" + managerTask.getEmpCountNegScore());
                    TaskStatisticsVo taskStatisticsVo;
                    if (ManagerTaskEnum.TASK_DAY.getCode() == managerTask.getEmpCountCycle()) { // 这个月有多少天，按天统计
                        taskStatisticsVo = queryTaskStatistics(frontTaskDate.getTime(), "1", managerTaskEmp.getEmpId());
                    } else if (ManagerTaskEnum.TASK_WEEK.getCode() == managerTask.getEmpCountCycle()) { // 这个月有多少周，按周统计
                        taskStatisticsVo = queryTaskStatistics(frontTaskDate.getTime(), "2", managerTaskEmp.getEmpId());
                    } else { // 按整个月统计
                        taskStatisticsVo = queryTaskStatistics(frontTaskDate.getTime(), "4", managerTaskEmp.getEmpId());
                    }
                    for (Integer empCountValue : taskStatisticsVo.getEmpCountValueTrendList())
                        if (empCountValue < managerTask.getEmpCountValue()) {
                            managerTaskScore1.setBscore(managerTaskScore1.getBscore() + managerTask.getEmpCountNegScore());
                        }
                    managerTaskScoreList.add(managerTaskScore1);
                }
                if (YesNoEnum.YES.getCode() == managerTask.getRatioTaskFlag()) {
                    ManagerTaskScore managerTaskScore1 = new ManagerTaskScore();
                    managerTaskScore1 = (ManagerTaskScore) BeanCopyUtil.copyBean(managerTaskScore, managerTaskScore1);
                    managerTaskScore1.setTaskType(ManagerTaskEnum.TASK_TYPE_RATIO.getMessage());
                    managerTaskScore1.setTaskIndex(managerTask.getRatioValue() + "%/" + ManagerTaskEnum.getMessageByCode(managerTask.getRatioCycle()));
                    TaskStatisticsVo taskStatisticsVo;
                    if (ManagerTaskEnum.TASK_DAY.getCode() == managerTask.getRatioCycle()) { // 这个月有多少天，按天统计
                        taskStatisticsVo = queryTaskStatistics(frontTaskDate.getTime(), "1", managerTaskEmp.getEmpId());
                    } else if (ManagerTaskEnum.TASK_WEEK.getCode() == managerTask.getRatioCycle()) { // 这个月有多少周，按周统计
                        taskStatisticsVo = queryTaskStatistics(frontTaskDate.getTime(), "2", managerTaskEmp.getEmpId());
                    } else { // 按整个月统计
                        taskStatisticsVo = queryTaskStatistics(frontTaskDate.getTime(), "4", managerTaskEmp.getEmpId());
                    }
                    for (int i = 0; i < taskStatisticsVo.getRatioValueTrendList().size(); i++) {
                        double ratioValue = taskStatisticsVo.getRatioValueTrendList().get(i);
                        if (managerTask.getRatioNegScore() == 0) {
                            managerTaskScore1.setScoreNegType("差额扣分");
                            if (ratioValue > 0 && ratioValue < managerTask.getRatioValue() / 100) { // （扣分/奖分）< 目标比例值  表示比例任务未完成，需扣分
                                // 扣分规则：已奖分 * 目标比例 – 已扣分值
                                double deduct = taskStatisticsVo.getAwardValueTrendList().get(i) * managerTask.getRatioValue() / 100;
                                int d = (int) (taskStatisticsVo.getDeductValueTrendList().get(i) - deduct);
                                managerTaskScore1.setBscore(managerTaskScore1.getBscore() + d);
                            }
                        } else {
                            managerTaskScore1.setScoreNegType("未完成扣分：" + managerTask.getRatioNegScore());
                            if (ratioValue > 0 && ratioValue < managerTask.getRatioValue()) {
                                managerTaskScore1.setBscore(managerTaskScore1.getBscore() + managerTask.getRatioNegScore());
                            }
                        }
                    }
                    managerTaskScoreList.add(managerTaskScore1);
                }
            }
        }
        managerTaskScoreRepository.saveAll(managerTaskScoreList);
    }

    private String getTaskIndex(Integer scoreAwardMin, Integer scoreCycle) {
        return scoreAwardMin + "/" + ManagerTaskEnum.getMessageByCode(scoreCycle);
    }
}