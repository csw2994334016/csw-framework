package com.three.points.service;

import com.three.common.enums.StatusEnum;
import com.three.commonclient.exception.ParameterException;
import com.three.points.entity.CustomGroup;
import com.three.points.entity.CustomGroupEmp;
import com.three.points.param.CustomGroupEmpParam;
import com.three.points.param.CustomGroupEmpParam1;
import com.three.points.repository.CustomGroupEmpRepository;
import com.three.points.repository.CustomGroupRepository;
import com.three.points.param.CustomGroupParam;
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
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2020-04-06.
 * Description:
 */

@Service
public class CustomGroupService extends BaseService<CustomGroup, String> {

    @Autowired
    private CustomGroupRepository customGroupRepository;

    @Autowired
    private CustomGroupEmpRepository customGroupEmpRepository;

    @Transactional
    public void create(CustomGroupParam customGroupParam) {
        BeanValidator.check(customGroupParam);

        String firstOrganizationId = getLoginUserFirstOrganizationId();

        if (customGroupRepository.countByOrganizationIdAndGroupNameAndStatus(firstOrganizationId, customGroupParam.getGroupName(), StatusEnum.OK.getCode()) > 0) {
            throw new ParameterException("已存在同名[" + customGroupParam.getGroupName() + "]的分组");
        }

        CustomGroup customGroup = new CustomGroup();
        customGroup = (CustomGroup) BeanCopyUtil.copyBean(customGroupParam, customGroup);

        customGroup.setOrganizationId(firstOrganizationId);

        customGroupRepository.save(customGroup);
    }

    @Transactional
    public void update(CustomGroupParam customGroupParam) {
        BeanValidator.check(customGroupParam);

        String firstOrganizationId = getLoginUserFirstOrganizationId();

        CustomGroup customGroup = getEntityById(customGroupRepository, customGroupParam.getId());

        if (customGroupRepository.countByOrganizationIdAndGroupNameAndStatusAndIdNot(firstOrganizationId, customGroupParam.getGroupName(), StatusEnum.OK.getCode(), customGroup.getId()) > 0) {
            throw new ParameterException("已存在同名[" + customGroupParam.getGroupName() + "]的分组");
        }

        customGroup = (CustomGroup) BeanCopyUtil.copyBean(customGroupParam, customGroup);

        customGroupRepository.save(customGroup);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<CustomGroup> customGroupList = new ArrayList<>();
        for (String id : idSet) {
            CustomGroup customGroup = getEntityById(customGroupRepository, id);
            customGroup.setStatus(code);
            customGroupList.add(customGroup);
        }

        customGroupRepository.saveAll(customGroupList);

        // 删除分组人员配置
        for (CustomGroup customGroup : customGroupList) {
            customGroupEmpRepository.deleteByGroupId(customGroup.getId());
        }
    }

    public PageResult<CustomGroup> query(PageQuery pageQuery, int code, String searchValue) {
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<CustomGroup> specification = (root, criteriaQuery, criteriaBuilder) -> {
            Specification<CustomGroup> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, firstOrganizationId);
            Predicate predicate = codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder);
            if (StringUtil.isNotBlank(searchValue)) {
                List<Predicate> predicateList1 = new ArrayList<>();
                Predicate p1 = criteriaBuilder.like(root.get("groupName"), "%" + searchValue + "%");
                predicateList1.add(criteriaBuilder.or(p1));
                Predicate predicate1 = criteriaBuilder.or(predicateList1.toArray(new Predicate[0]));

                return criteriaQuery.where(predicate, predicate1).getRestriction();
            }
            return predicate;
        };
        if (pageQuery != null) {
            return query(customGroupRepository, pageQuery, sort, specification);
        } else {
            return query(customGroupRepository, sort, specification);
        }
    }

    public CustomGroup findById(String id) {
        return getEntityById(customGroupRepository, id);
    }

    @Transactional
    public void bindEmployee(CustomGroupEmpParam customGroupEmpParam) {
        BeanValidator.check(customGroupEmpParam);

        CustomGroup customGroup = getEntityById(customGroupRepository, customGroupEmpParam.getCustomGroupId());

        List<CustomGroupEmp> customGroupEmpList = new ArrayList<>();
        for (CustomGroupEmpParam1 customGroupEmpParam1 : customGroupEmpParam.getCustomGroupEmpParam1List()) {
            CustomGroupEmp customGroupEmp = new CustomGroupEmp();
            customGroupEmp.setGroupId(customGroup.getId());
            customGroupEmp.setEmpId(customGroupEmpParam1.getEmpId());
            customGroupEmp.setEmpNum(customGroupEmpParam1.getEmpNum());
            customGroupEmp.setEmpPicture(customGroupEmpParam1.getEmpPicture());
            customGroupEmp.setEmpFullName(customGroupEmpParam1.getEmpFullName());
            customGroupEmp.setEmpOrgId(customGroupEmpParam1.getEmpOrgId());
            customGroupEmp.setEmpOrgName(customGroupEmpParam1.getEmpOrgName());
            customGroupEmpList.add(customGroupEmp);
        }

        customGroupEmpRepository.deleteByGroupId(customGroup.getId());

        if (customGroupEmpList.size() > 0) {
            customGroupEmpRepository.saveAll(customGroupEmpList);
        }
    }

    public List<CustomGroupEmp> findCustomGroupEmpList(String customGroupId) {
        if (StringUtil.isNotBlank(customGroupId)) {
            return customGroupEmpRepository.findAllByGroupId(customGroupId);
        }
        return new ArrayList<>();
    }
}