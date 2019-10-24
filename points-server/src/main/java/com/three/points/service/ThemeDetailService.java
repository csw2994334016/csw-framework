package com.three.points.service;

import com.three.points.entity.ThemeDetail;
import com.three.points.repository.ThemeDetailRepository;
import com.three.points.param.ThemeDetailParam;
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
 * Created by csw on 2019-10-24.
 * Description:
 */

@Service
public class ThemeDetailService extends BaseService<ThemeDetail,  String> {

    @Autowired
    private ThemeDetailRepository themeDetailRepository;

    @Transactional
    public void create(ThemeDetailParam themeDetailParam) {
        BeanValidator.check(themeDetailParam);

        ThemeDetail themeDetail = new ThemeDetail();
        themeDetail = (ThemeDetail) BeanCopyUtil.copyBean(themeDetailParam, themeDetail);

        themeDetail.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());

        themeDetailRepository.save(themeDetail);
    }

    @Transactional
    public void update(ThemeDetailParam themeDetailParam) {
        BeanValidator.check(themeDetailParam);

        ThemeDetail themeDetail = getEntityById(themeDetailRepository, themeDetailParam.getId());
        themeDetail = (ThemeDetail) BeanCopyUtil.copyBean(themeDetailParam, themeDetail);

        themeDetailRepository.save(themeDetail);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<ThemeDetail> themeDetailList = new ArrayList<>();
        for (String id : idSet) {
            ThemeDetail themeDetail = getEntityById(themeDetailRepository, String.valueOf(id));
            themeDetail.setStatus(code);
            themeDetailList.add(themeDetail);
        }

        themeDetailRepository.saveAll(themeDetailList);
    }

    public PageResult<ThemeDetail> query(PageQuery pageQuery, int code, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<ThemeDetail> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            predicateList.add(criteriaBuilder.equal(root.get("status"), code));

            String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
            if (firstOrganizationId != null) {
                predicateList.add(criteriaBuilder.equal(root.get("organizationId"), firstOrganizationId));
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            Predicate predicate = criteriaBuilder.and(predicateList.toArray(predicates));

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
            return query(themeDetailRepository, pageQuery, sort, specification);
        } else {
            return query(themeDetailRepository, sort, specification);
        }
    }

    public ThemeDetail findById(String id) {
        return getEntityById(themeDetailRepository, id);
    }
}