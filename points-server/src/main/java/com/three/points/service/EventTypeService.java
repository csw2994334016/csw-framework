package com.three.points.service;

import com.google.common.collect.Lists;
import com.three.common.auth.LoginUser;
import com.three.common.enums.AdminEnum;
import com.three.commonclient.exception.BusinessException;
import com.three.points.entity.EventType;
import com.three.points.repository.EventRepository;
import com.three.points.repository.EventTypeRepository;
import com.three.points.param.EventTypeParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.points.vo.EventTypeVo;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * Created by csw on 2019-10-20.
 * Description:
 */

@Service
public class EventTypeService extends BaseService<EventType, String> {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public EventType create(EventTypeParam eventTypeParam) {
        BeanValidator.check(eventTypeParam);

        EventType eventType = new EventType();
        eventType = (EventType) BeanCopyUtil.copyBean(eventTypeParam, eventType);

        eventType.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());

        eventType = eventTypeRepository.save(eventType);
        return eventType;
    }

    @Transactional
    public EventType update(EventTypeParam eventTypeParam) {
        BeanValidator.check(eventTypeParam);

        EventType eventType = getEntityById(eventTypeRepository, eventTypeParam.getId());
        eventType = (EventType) BeanCopyUtil.copyBean(eventTypeParam, eventType);

        eventType = eventTypeRepository.save(eventType);
        return eventType;
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<EventType> eventTypeList = new ArrayList<>();
        List<String> eventTypeNameList = new ArrayList<>();
        for (String id : idSet) {
            EventType eventType = getEntityById(eventTypeRepository, String.valueOf(id));
            eventType.setStatus(code);
            eventTypeList.add(eventType);
            int count = eventRepository.countByTypeId(id);
            if (count > 0) {
                eventTypeNameList.add(eventType.getTypeName());
            }
        }
        if (eventTypeNameList.size() > 0) {
            throw new BusinessException("事件分类" + eventTypeNameList.toString() + "下绑定了具体事件，不能删除！");
        }
        eventTypeRepository.saveAll(eventTypeList);
    }

    public PageResult<EventType> query(PageQuery pageQuery, int code, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<EventType> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = Lists.newArrayList();

            predicateList.add(criteriaBuilder.equal(root.get("status"), code));

            String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
            if (firstOrganizationId != null) {
                predicateList.add(criteriaBuilder.equal(root.get("organizationId"), firstOrganizationId));
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            Predicate predicate = criteriaBuilder.and(predicateList.toArray(predicates));

            if (StringUtil.isNotBlank(searchValue)) {
                List<Predicate> predicateList1 = Lists.newArrayList();
                Predicate p1 = criteriaBuilder.like(root.get("typeName"), "%" + searchValue + "%");
                predicateList1.add(criteriaBuilder.or(p1));
                Predicate[] predicates1 = new Predicate[predicateList1.size()];
                Predicate predicate1 = criteriaBuilder.or(predicateList1.toArray(predicates1));

                return criteriaQuery.where(predicate, predicate1).getRestriction();
            }
            return predicate;
        };
        if (pageQuery != null) {
            return query(eventTypeRepository, pageQuery, sort, specification);
        } else {
            return query(eventTypeRepository, sort, specification);
        }
    }

    public List<EventTypeVo> findAllWithTree(int code) {
        List<EventType> eventTypeList = new ArrayList<>();

        LoginUser loginUser = LoginUserUtil.getLoginUser();
        if (loginUser != null) {
            if (loginUser.getSysOrganization() != null) {
                eventTypeList = eventTypeRepository.findAllByOrganizationIdAndStatus(loginUser.getSysOrganization().getFirstParentId(), code);
            } else {
                if (loginUser.getIsAdmin().equals(AdminEnum.YES.getCode())) {
                    eventTypeList = eventTypeRepository.findAllByStatus(code); // 默认admin账号可以查看全库所有组织机构
                }
            }
        }

        Map<String, EventTypeVo> voMap = new HashMap<>();
        for (EventType eventType : eventTypeList) {
            EventTypeVo vo = EventTypeVo.builder().title(eventType.getTypeName()).id(eventType.getId()).parentId(eventType.getParentId()).sort(eventType.getSort()).build();
            voMap.put(vo.getId(), vo);
        }
        List<EventTypeVo> parentVoList = new ArrayList<>();
        for (EventTypeVo vo : voMap.values()) {
            EventTypeVo parentVo = voMap.get(vo.getParentId());
            if (parentVo != null) {
                parentVo.getChildren().add(vo);
            } else {
                parentVoList.add(vo);
            }
        }
        // 排序
        sortBySort(parentVoList);
        return parentVoList;
    }

    private void sortBySort(List<EventTypeVo> voList) {
        voList.sort(Comparator.comparing(EventTypeVo::getSort));
        for (EventTypeVo vo : voList) {
            if (vo.getChildren().size() > 0) {
                sortBySort(vo.getChildren());
            }
        }
    }

    public EventType getEventTypeById(String typeId) {
        return getEntityById(eventTypeRepository, typeId);
    }

    public List<EventType> findChildren(int code, String id) {
        List<EventType> eventTypeList;
        String parentId = StringUtil.isNotBlank(id) ? id : "-1";
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        if (firstOrganizationId != null) {
            eventTypeList = eventTypeRepository.findAllByOrganizationIdAndStatusAndParentId(firstOrganizationId, code, parentId);
        } else {
            eventTypeList = eventTypeRepository.findAllByStatusAndParentId(code, parentId);
        }
        return eventTypeList;
    }
}