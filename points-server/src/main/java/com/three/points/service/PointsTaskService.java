package com.three.points.service;

import com.three.commonclient.exception.BusinessException;
import com.three.points.entity.PointsTask;
import com.three.points.enums.PointsTaskEnum;
import com.three.points.repository.PointsTaskRepository;
import com.three.points.param.PointsTaskParam;
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
 * Created by csw on 2019-11-04.
 * Description:
 */

@Service
public class PointsTaskService extends BaseService<PointsTask, String> {

    @Autowired
    private PointsTaskRepository pointsTaskRepository;

    @Transactional
    public void create(PointsTaskParam pointsTaskParam) {
        BeanValidator.check(pointsTaskParam);

        PointsTask pointsTask = new PointsTask();
        pointsTask = (PointsTask) BeanCopyUtil.copyBean(pointsTaskParam, pointsTask);
        pointsTask.setDeadline(new Date(pointsTaskParam.getDeadline()));

        pointsTask.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());

        pointsTask.setCreateId(LoginUserUtil.getLoginUserEmpId());
        pointsTask.setCreateName(LoginUserUtil.getLoginUserEmpFullName());

        pointsTaskRepository.save(pointsTask);
    }

    @Transactional
    public void update(PointsTaskParam pointsTaskParam) {
        BeanValidator.check(pointsTaskParam);

        PointsTask pointsTask = getEntityById(pointsTaskRepository, pointsTaskParam.getId());
        pointsTask = (PointsTask) BeanCopyUtil.copyBean(pointsTaskParam, pointsTask);
        pointsTask.setDeadline(new Date(pointsTaskParam.getDeadline()));

        pointsTaskRepository.save(pointsTask);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<PointsTask> pointsTaskList = new ArrayList<>();
        for (String id : idSet) {
            PointsTask pointsTask = getEntityById(pointsTaskRepository, String.valueOf(id));
            pointsTask.setStatus(code);
            pointsTaskList.add(pointsTask);
        }

        pointsTaskRepository.saveAll(pointsTaskList);
    }

    public PageResult<PointsTask> query(PageQuery pageQuery, int code, String whoFlag, String sortKey, String chargePersonId) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        if ("deadline".equals(sortKey)) {
            sort = new Sort(Sort.Direction.DESC, "deadline");
        }
        Specification<PointsTask> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            Specification<PointsTask> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code);
            predicateList.add(codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder));

            String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
            if (loginUserEmpId != null) {
                if ("1".equals(whoFlag)) { // 我参与的
                    predicateList.add(criteriaBuilder.like(root.get("taskEmpId"), "%" + loginUserEmpId + "%"));
                } else if ("2".equals(whoFlag)) { // 我负责的
                    predicateList.add(criteriaBuilder.equal(root.get("chargePersonId"), loginUserEmpId));
                } else if ("3".equals(whoFlag)) { // 抄送给我的
                    predicateList.add(criteriaBuilder.like(root.get("copyPersonId"), "%" + loginUserEmpId + "%"));
                } else if ("4".equals(whoFlag)) { // 我发布的
                    predicateList.add(criteriaBuilder.equal(root.get("createId"), loginUserEmpId));
                }
            }

            if (StringUtil.isNotBlank(chargePersonId)) {
                predicateList.add(criteriaBuilder.equal(root.get("chargePersonId"), chargePersonId));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        if (pageQuery != null) {
            return query(pointsTaskRepository, pageQuery, sort, specification);
        } else {
            return query(pointsTaskRepository, sort, specification);
        }
    }

    public PointsTask findById(String id) {
        return getEntityById(pointsTaskRepository, id);
    }

    @Transactional
    public void completeTask(String id) {
        PointsTask pointsTask = getEntityById(pointsTaskRepository, id);
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        if (loginUserEmpId != null && loginUserEmpId.equals(pointsTask.getChargePersonId())) {
            pointsTask.setTaskStatus(PointsTaskEnum.FINISHED.getCode());
            pointsTask.setCompleteDate(new Date());
            pointsTaskRepository.save(pointsTask);
        } else {
            throw new BusinessException("登录用户不是负责人，操作失败");
        }
    }
}