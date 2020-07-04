package com.three.resource_jpa.jpa.entity.service;

import com.three.common.enums.StatusEnum;
import com.three.common.exception.BusinessException;
import com.three.common.exception.ParameterException;
import com.three.common.utils.BeanValidator;
import com.three.resource_jpa.jpa.base.service.PojoService;
import com.three.resource_jpa.jpa.entity.entity.EntityField;
import com.three.resource_jpa.jpa.entity.entity.EntityPojo;
import com.three.resource_jpa.jpa.entity.enums.MetaEnum;
import com.three.resource_jpa.jpa.entity.param.ColumnInfo;
import com.three.resource_jpa.jpa.entity.param.EntityFieldParam;
import com.three.resource_jpa.jpa.entity.param.GenConfig;
import com.three.resource_jpa.jpa.entity.repository.EntityFieldRepository;
import com.three.resource_jpa.jpa.entity.repository.EntityPojoRepository;
import com.three.resource_jpa.jpa.entity.param.EntityPojoParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.jpa.entity.utils.EntityGenUtil;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    private PojoService pojoService;

    @Transactional
    public void create(EntityPojoParam entityPojoParam) {
        BeanValidator.check(entityPojoParam);

        // 类名首字母必须大写
        if (!Character.isUpperCase(entityPojoParam.getEntityName().charAt(0))) {
            throw new ParameterException("类名首字母必须大写");
        }
        // 类名不能重复
        if (entityPojoRepository.countByEntityNameAndStatus(entityPojoParam.getEntityName(), StatusEnum.OK.getCode()) > 0) {
            throw new ParameterException("数据库中已存在相同类名");
        }
        // 表名不能重复
        if (entityPojoRepository.countByEntityTableNameAndStatus(entityPojoParam.getEntityTableName(), StatusEnum.OK.getCode()) > 0) {
            throw new ParameterException("数据库中已存在相同表名");
        }

        EntityPojo entityPojo = new EntityPojo();
        entityPojo = (EntityPojo) BeanCopyUtil.copyBean(entityPojoParam, entityPojo);

        entityPojo.setVersion(1);

        entityPojo = entityPojoRepository.save(entityPojo);

        // 默认生成字段：id，status，createDate，updateDate
        List<EntityField> entityFieldList = new ArrayList<>();
        EntityField entityField = new EntityField();
        entityField.setEntityPojoId(entityPojo.getId());
        entityField.setColumnName("id");
        entityField.setColumnType("String");
        entityField.setColumnKey(EntityGenUtil.PK);
        entityField.setColumnLength(36);
        entityField.setColumnComment("主键");
        entityFieldList.add(entityField);
        EntityField entityField1 = new EntityField();
        entityField1.setEntityPojoId(entityPojo.getId());
        entityField1.setColumnName("status");
        entityField1.setColumnType("Integer");
        entityField1.setNullFlag(0);
        entityField1.setColumnLength(2);
        entityField1.setDefaultValue(1);
        entityField1.setColumnComment("记录状态：1=正常；2=锁定；3=删除");
        entityFieldList.add(entityField1);
        EntityField entityField2 = new EntityField();
        entityField2.setEntityPojoId(entityPojo.getId());
        entityField2.setColumnName("createDate");
        entityField2.setColumnType("Date");
        entityField2.setColumnComment("创建时间");
        entityFieldList.add(entityField2);
        EntityField entityField3 = new EntityField();
        entityField3.setEntityPojoId(entityPojo.getId());
        entityField3.setColumnName("updateDate");
        entityField3.setColumnType("Date");
        entityField3.setColumnComment("修改时间");
        entityFieldList.add(entityField3);

        entityFieldRepository.saveAll(entityFieldList);
    }

    @Transactional
    public void update(EntityPojoParam entityPojoParam) {
        BeanValidator.check(entityPojoParam);

        // 类名首字母必须大写
        if (!Character.isUpperCase(entityPojoParam.getEntityName().charAt(0))) {
            throw new ParameterException("类名首字母必须大写");
        }

        EntityPojo entityPojo = getEntityById(entityPojoRepository, entityPojoParam.getId());
        // 类名不能重复
        if (entityPojoRepository.countByEntityNameAndStatusAndIdNot(entityPojoParam.getEntityName(), StatusEnum.OK.getCode(), entityPojoParam.getId()) > 0) {
            throw new ParameterException("数据库中已存在相同类名");
        }
        // 表名不能重复
        if (entityPojoRepository.countByEntityTableNameAndStatusAndIdNot(entityPojoParam.getEntityTableName(), StatusEnum.OK.getCode(), entityPojoParam.getId()) > 0) {
            throw new ParameterException("数据库中已存在相同表名");
        }
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
        PageResult<EntityPojo> pageResult;
        if (page != null && limit != null) {
            pageResult = query(entityPojoRepository, new PageQuery(page, limit), sort, code, "entityName", searchValue);
        } else {
            pageResult = query(entityPojoRepository, sort, code, "entityName", searchValue);
        }
        for (EntityPojo entityPojo : pageResult.getData()) {
            entityPojo.setEntityCode(null);
        }
        return pageResult;
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

        // 字段是否存在
        if (StringUtils.isNotBlank(entityFieldParam.getId())) {
            if (entityFieldRepository.countByColumnNameAndIdNot(entityFieldParam.getColumnName(), entityFieldParam.getId()) > 0) {
                throw new ParameterException("数据库中已存在相同字段名");
            }
        } else {
            if (entityFieldRepository.countByColumnName(entityFieldParam.getColumnName()) > 0) {
                throw new ParameterException("数据库中已存在相同字段名");
            }
        }

        EntityPojo entityPojo = findById(entityFieldParam.getEntityPojoId());
        entityPojo.setVersion(entityPojo.getVersion() + 1);
        entityPojoRepository.save(entityPojo);

        EntityField entityField = new EntityField();
        entityField = (EntityField) BeanCopyUtil.copyBean(entityFieldParam, entityField);

        entityFieldRepository.save(entityField);
    }

    @Transactional
    public void deleteField(String ids) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<EntityField> entityFieldList = new ArrayList<>();
        List<String> nameList = Arrays.asList("id", "status", "createDate", "updateDate");
        List<String> doNotDeletedList = new ArrayList<>();
        for (String id : idSet) {
            EntityField entityField = entityFieldRepository.findById(id).get();
            if (!nameList.contains(entityField.getColumnName())) {
                entityFieldList.add(entityField);
            } else {
                doNotDeletedList.add(entityField.getColumnName());
            }
        }
        if (doNotDeletedList.size() > 0) {
            throw new BusinessException("字段名称不允许删除：" + doNotDeletedList.toString());
        }
        entityFieldRepository.deleteAll(entityFieldList);
    }

    @Transactional
    public void generateCode(String id) {

        EntityPojo entityPojo = getEntityById(entityPojoRepository, id);
        List<EntityField> entityFieldList = entityFieldRepository.findAllByEntityPojoId(entityPojo.getId());

        String packPath = entityPojo.getEntityPackageName().substring(0, entityPojo.getEntityPackageName().lastIndexOf("."));

        GenConfig genConfig = GenConfig.builder()
                .packPath(packPath)
                .author(LoginUserUtil.getLoginUsername())
                .description(entityPojo.getRemark())
                .className(entityPojo.getEntityName())
                .tableName(entityPojo.getEntityTableName()).build();

        List<ColumnInfo> columnInfoList = new ArrayList<>();
        for (EntityField entityField : entityFieldList) {
            ColumnInfo columnInfo = ColumnInfo.builder().columnName(entityField.getColumnName())
                    .columnComment(entityField.getColumnComment())
                    .columnType(entityField.getColumnType())
                    .isNullable(entityField.getNullFlag() == 1)
                    .columnKey(entityField.getColumnKey())
                    .columnLength(entityField.getColumnLength())
                    .defaultValue(entityField.getDefaultValue()).build();
            columnInfoList.add(columnInfo);
        }

        String code;
        if (MetaEnum.ENTITY.getCode() == entityPojo.getMetaFlag()) {
            code = EntityGenUtil.generateCode(genConfig, columnInfoList, "Entity");
            // 添加Entity class、更新数据库Schema
            pojoService.addEntity(entityPojo.getEntityPackageName(), code, entityPojo.getVersion());
        } else if (MetaEnum.POJO.getCode() == entityPojo.getMetaFlag()) {
            code = EntityGenUtil.generateCode(genConfig, columnInfoList, "Pojo");
            // 添加Pojo class
            pojoService.addPojo(entityPojo.getEntityPackageName(), code, entityPojo.getVersion());
        } else {
            throw new BusinessException("该方法只能生成实体或者虚拟实体的Java代码");
        }
        entityPojo.setEntityCode(code);
        entityPojoRepository.save(entityPojo);
    }

    public EntityPojo findCode(String id) {
        return getEntityById(entityPojoRepository, id);
    }
}