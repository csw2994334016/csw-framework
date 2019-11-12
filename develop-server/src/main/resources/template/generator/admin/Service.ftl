package ${package}.service;

import ${package}.entity.${className};
import ${package}.repository.${className}Repository;
import ${package}.param.${className}Param;
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
 * Created by ${author} on ${date}.
 * Description:
 */

@Service
public class ${className}Service extends BaseService<${className},  ${pkColumnType}> {

    @Autowired
    private ${className}Repository ${changeClassName}Repository;

    @Transactional
    public void create(${className}Param ${changeClassName}Param) {
        BeanValidator.check(${changeClassName}Param);

        ${className} ${changeClassName} = new ${className}();
        ${changeClassName} = (${className}) BeanCopyUtil.copyBean(${changeClassName}Param, ${changeClassName});

        ${changeClassName}.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());

        ${changeClassName}Repository.save(${changeClassName});
    }

    @Transactional
    public void update(${className}Param ${changeClassName}Param) {
        BeanValidator.check(${changeClassName}Param);

        ${className} ${changeClassName} = getEntityById(${changeClassName}Repository, ${changeClassName}Param.getId());
        ${changeClassName} = (${className}) BeanCopyUtil.copyBean(${changeClassName}Param, ${changeClassName});

        ${changeClassName}Repository.save(${changeClassName});
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<${className}> ${changeClassName}List = new ArrayList<>();
        for (String id : idSet) {
            ${className} ${changeClassName} = getEntityById(${changeClassName}Repository, ${pkColumnType}.valueOf(id));
            ${changeClassName}.setStatus(code);
            ${changeClassName}List.add(${changeClassName});
        }

        ${changeClassName}Repository.saveAll(${changeClassName}List);
    }

    public PageResult<${className}> query(PageQuery pageQuery, int code, String searchValue) {
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<${className}> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            Specification<${className}> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, firstOrganizationId);
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
            return query(${changeClassName}Repository, pageQuery, sort, specification);
        } else {
            return query(${changeClassName}Repository, sort, specification);
        }
    }

    public ${className} findById(String id) {
        return getEntityById(${changeClassName}Repository, id);
    }
}