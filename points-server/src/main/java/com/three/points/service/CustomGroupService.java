package com.three.points.service;

import com.three.common.enums.StatusEnum;
import com.three.commonclient.exception.ParameterException;
import com.three.points.entity.CustomGroup;
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

    @Transactional
    public void create(CustomGroupParam customGroupParam) {
        BeanValidator.check(customGroupParam);

        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();

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

        CustomGroup customGroup = getEntityById(customGroupRepository, customGroupParam.getId());
        customGroup = (CustomGroup) BeanCopyUtil.copyBean(customGroupParam, customGroup);

        customGroupRepository.save(customGroup);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<CustomGroup> customGroupList = new ArrayList<>();
        for (String id : idSet) {
            CustomGroup customGroup = getEntityById(customGroupRepository, String.valueOf(id));
            customGroup.setStatus(code);
            customGroupList.add(customGroup);
        }

        customGroupRepository.saveAll(customGroupList);
    }

    public PageResult<CustomGroup> query(PageQuery pageQuery, int code, String searchValue) {
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<CustomGroup> specification = (root, criteriaQuery, criteriaBuilder) -> {

            Specification<CustomGroup> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, firstOrganizationId);
            Predicate predicate = codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder);

            if (StringUtil.isNotBlank(searchValue)) {
                List<Predicate> predicateList1 = new ArrayList<>();
                Predicate p1 = criteriaBuilder.like(root.get("name"), "%" + searchValue + "%");
                predicateList1.add(criteriaBuilder.or(p1));
                Predicate[] predicates1 = new Predicate[predicateList1.size()];
                Predicate predicate1 = criteriaBuilder.or(predicateList1.toArray(predicates1));

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
}