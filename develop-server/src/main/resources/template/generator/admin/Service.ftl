package ${package}.service;

import ${package}.entity.${className};
import ${package}.repository.${className}Repository;
import ${package}.param.${className}Param;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.exception.ParameterException;
import com.three.commonclient.utils.BeanValidator;
import com.three.commonjpa.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

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

    public PageResult<${className}> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return query(${changeClassName}Repository, pageQuery, sort, code, searchKey, searchValue);
    }

}