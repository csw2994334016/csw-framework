package com.three.resource_jpa.jpa.entity.service;

import com.three.common.utils.BeanValidator;
import com.three.resource_jpa.jpa.entity.entity.EntityField;
import com.three.resource_jpa.jpa.entity.entity.EntityPojo;
import com.three.resource_jpa.jpa.entity.enums.MetaEnum;
import com.three.resource_jpa.jpa.entity.param.EntityFieldParam;
import com.three.resource_jpa.jpa.entity.repository.EntityFieldRepository;
import com.three.resource_jpa.jpa.entity.repository.EntityPojoRepository;
import com.three.resource_jpa.jpa.entity.param.EntityPojoParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.resource_jpa.jpa.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by  on 2020-06-26.
 * Description:
 */

@Service
public class EntityPojoService extends BaseService<EntityPojo, String> {

    @Autowired
    private EntityPojoRepository entityPojoRepository;

    @Autowired
    private EntityFieldRepository entityFieldRepository;

    @Transactional
    public void create(EntityPojoParam entityPojoParam) {
        BeanValidator.check(entityPojoParam);

        EntityPojo entityPojo = new EntityPojo();
        entityPojo = (EntityPojo) BeanCopyUtil.copyBean(entityPojoParam, entityPojo);

        entityPojo.setVersion(1);

        entityPojoRepository.save(entityPojo);
    }

    @Transactional
    public void update(EntityPojoParam entityPojoParam) {
        BeanValidator.check(entityPojoParam);

        EntityPojo entityPojo = getEntityById(entityPojoRepository, entityPojoParam.getId());
        entityPojo = (EntityPojo) BeanCopyUtil.copyBean(entityPojoParam, entityPojo);

        entityPojo.setVersion(entityPojo.getVersion() + 1);

        entityPojoRepository.save(entityPojo);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<EntityPojo> entityPojoList = new ArrayList<>();
        for (String id : idSet) {
            EntityPojo entityPojo = getEntityById(entityPojoRepository, id);
            entityPojo.setStatus(code);
            entityPojoList.add(entityPojo);
        }
        entityPojoRepository.saveAll(entityPojoList);
    }

    public PageResult<EntityPojo> query(Integer page, Integer limit, int code, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        if (page != null && limit != null) {
            return query(entityPojoRepository, new PageQuery(page, limit), sort, code, "entityName", searchValue);
        } else {
            return query(entityPojoRepository, sort, code, "entityName", searchValue);
        }
    }

    public EntityPojo findById(String id) {
        return getEntityById(entityPojoRepository, id);
    }

    public List<EntityField> findFieldsByEntityPojoId(String entityPojoId) {
        return entityFieldRepository.findAllByEntityPojoId(entityPojoId);
    }

    @Transactional
    public void saveField(EntityFieldParam entityFieldParam) {
        BeanValidator.check(entityFieldParam);

        EntityField entityField = new EntityField();
        entityField = (EntityField) BeanCopyUtil.copyBean(entityFieldParam, entityField);

        entityFieldRepository.save(entityField);
    }

    @Transactional
    public void deleteField(String ids) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<EntityField> entityFieldList = new ArrayList<>();
        for (String id : idSet) {
            EntityField entityField = entityFieldRepository.findById(id).get();
            entityFieldList.add(entityField);
        }
        entityFieldRepository.deleteAll(entityFieldList);
    }

    @Transactional
    public void generateCode(String id) {

        EntityPojo entityPojo = getEntityById(entityPojoRepository, id);
        List<EntityField> entityFieldList = entityFieldRepository.findAllByEntityPojoId(entityPojo.getId());
        if (MetaEnum.ENTITY.getCode() == entityPojo.getMetaFlag()) {

        } else if (MetaEnum.POJO.getCode() == entityPojo.getMetaFlag()){

        } else {

        }
    }
}