package com.three.commonjpa.base.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonjpa.base.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * Created by csw on 2019/03/29.
 * Description:
 */
public class BaseService<T, ID> {

    // 分页，按code、关键字
    public PageResult<T> query(BaseRepository baseRepository, PageQuery pageQuery, Sort sort, int code, String searchKey, String searchValue) {
        Pageable pageable = PageRequest.of(pageQuery.getPageNo(), pageQuery.getPageSize(), sort);
        Specification<T> codeAndSearchKeySpec = getCodeAndSearchKeySpec(code, searchKey, searchValue);
        Page<T> resultPage = baseRepository.findAll(codeAndSearchKeySpec, pageable);
        return new PageResult<>(resultPage.getTotalElements(), resultPage.getContent());
    }

    // 不分页，按code、关键字
    protected PageResult<T> findAll(BaseRepository baseRepository, Sort sort, int code, String searchKey, String searchValue) {
        Specification<T> codeAndSearchKeySpec = getCodeAndSearchKeySpec(code, searchKey, searchValue);
        List<T> resultList = baseRepository.findAll(codeAndSearchKeySpec, sort);
        return new PageResult<T>(resultList);
    }

    // 分页，按code、关键字、创建时间
    public PageResult<T> query(BaseRepository baseRepository, PageQuery pageQuery, Sort sort, int code, String searchKey, String searchValue, String startDate, String endDate) {
        Pageable pageable = PageRequest.of(pageQuery.getPageNo(), pageQuery.getPageSize(), sort);
        Specification specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = Lists.newArrayList();
            Specification spec = getCodeAndSearchKeySpec(code, searchKey, searchValue);
            predicateList.add(spec.toPredicate(root, criteriaQuery, criteriaBuilder));
            if (StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), StringUtil.getStrToDate(startDate)));
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), StringUtil.getStrToDate(endDate)));
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(predicates));
        };
        Page<T> resultPage = baseRepository.findAll(specification, pageable);
        return new PageResult<>(resultPage.getTotalElements(), resultPage.getContent());
    }

    // 分页，添加查询条件
    public PageResult<T> query(BaseRepository baseRepository, PageQuery pageQuery, Sort sort, Specification<T> specification) {
        Pageable pageable = PageRequest.of(pageQuery.getPageNo(), pageQuery.getPageSize(), sort);
        Page<T> resultPage = baseRepository.findAll(specification, pageable);
        return new PageResult<>(resultPage.getTotalElements(), resultPage.getContent());
    }

    // 不分页，添加查询条件
    public PageResult<T> query(BaseRepository baseRepository, Sort sort, Specification<T> specification) {
        List<T> resultList = baseRepository.findAll(specification, sort);
        return new PageResult<T>(resultList);
    }

    protected Specification<T> getCodeAndSearchKeySpec(int code, String searchKey, String searchValue) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = Lists.newArrayList();
            predicateList.add(criteriaBuilder.equal(root.get("status"), code));
            if (StringUtil.isNotBlank(searchKey) && StringUtil.isNotBlank(searchValue)) {
                predicateList.add(criteriaBuilder.like(root.get(searchKey), "%" + searchValue + "%"));
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(predicates));
        };
    }

    protected T getEntityById(BaseRepository baseRepository, ID id) {
        Preconditions.checkNotNull(id, "查找记录，id不可以为：" + id);
        T entity = (T) baseRepository.findById(id).orElse(null);
        Preconditions.checkNotNull(entity, "记录（id：" + id + ")不存在");
        return entity;
    }
}
