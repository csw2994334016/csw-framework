package com.three.commonjpa.script.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonjpa.base.service.BaseService;
import com.three.commonjpa.script.entity.Script;
import com.three.commonjpa.script.param.ScriptParam;
import com.three.commonjpa.script.repository.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019/09/07.
 * Description:
 */
@Service
public class ScriptService extends BaseService<Script, Long> {

    @Autowired
    private ScriptRepository scriptRepository;

    @Transactional
    public void create(ScriptParam param) {

        Script script = new Script();
        script = (Script) BeanCopyUtil.copyBean(param, script);

        scriptRepository.save(script);
    }

    @Transactional
    public void update(ScriptParam param) {
        Preconditions.checkNotNull(param.getId(), "修改记录Id不可以为null");

        Script script = getEntityById(scriptRepository, param.getId());
        script.setName(param.getName());
        script.setCode(param.getCode());
        script.setPlainTxt(param.getPlainTxt());
        script.setVersion(param.getVersion());
        script.setRemark(param.getRemark());

        scriptRepository.save(script);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<Long> idSet = StringUtil.getStrToIdSet(ids);
        List<Script> scriptList = new ArrayList<>();
        for (Long id : idSet) {
            Script script = getEntityById(scriptRepository, id);
            script.setStatus(code);
            scriptList.add(script);
        }

        scriptRepository.saveAll(scriptList);
    }

    public PageResult<Script> query(PageQuery pageQuery, int code, String searchValue) {
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        Specification specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = Lists.newArrayList();
            predicateList.add(criteriaBuilder.equal(root.get("status"), code));
            if (StringUtil.isNotBlank(searchValue)) {
                predicateList.add(criteriaBuilder.like(root.get("name"), "%" + searchValue + "%"));
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(predicates));
        };
        return query(scriptRepository, pageQuery, sort, specification);
    }
}
