package com.three.points.service;

import com.three.points.entity.Theme;
import com.three.points.param.UpdateThemeParam;
import com.three.points.repository.ThemeRepository;
import com.three.points.param.ThemeParam;
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
public class ThemeService extends BaseService<Theme,  String> {

    @Autowired
    private ThemeRepository themeRepository;

    @Transactional
    public void createDraft(ThemeParam themeParam) {

    }

    @Transactional
    public void create(UpdateThemeParam updateThemeParam) {
        BeanValidator.check(updateThemeParam);

        Theme theme = new Theme();
        theme = (Theme) BeanCopyUtil.copyBean(updateThemeParam, theme);

        theme.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());

        themeRepository.save(theme);
    }

    @Transactional
    public void update(UpdateThemeParam updateThemeParam) {
        BeanValidator.check(updateThemeParam);

        Theme theme = getEntityById(themeRepository, updateThemeParam.getId());
        theme = (Theme) BeanCopyUtil.copyBean(updateThemeParam, theme);

        themeRepository.save(theme);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<Theme> themeList = new ArrayList<>();
        for (String id : idSet) {
            Theme theme = getEntityById(themeRepository, String.valueOf(id));
            theme.setStatus(code);
            themeList.add(theme);
        }

        themeRepository.saveAll(themeList);
    }

    public PageResult<Theme> query(PageQuery pageQuery, int code, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<Theme> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            Specification<Theme> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code);
            Predicate predicate = codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder);

            if (StringUtil.isNotBlank(searchValue)) {
                List<Predicate> predicateList1 = new ArrayList<>();
                Predicate p1 = criteriaBuilder.like(root.get("themeName"), "%" + searchValue + "%");
                predicateList1.add(criteriaBuilder.or(p1));
                Predicate predicate1 = criteriaBuilder.or(predicateList1.toArray(new Predicate[0]));
                return criteriaQuery.where(predicate, predicate1).getRestriction();
            }
            return predicate;
        };
        if (pageQuery != null) {
            return query(themeRepository, pageQuery, sort, specification);
        } else {
            return query(themeRepository, sort, specification);
        }
    }

    public Theme findById(String id) {
        return getEntityById(themeRepository, id);
    }
}