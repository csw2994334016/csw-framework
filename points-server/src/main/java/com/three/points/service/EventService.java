package com.three.points.service;

import com.google.common.collect.Lists;
import com.three.common.enums.StatusEnum;
import com.three.commonclient.exception.BusinessException;
import com.three.commonclient.exception.ParameterException;
import com.three.points.entity.Event;
import com.three.points.entity.EventType;
import com.three.points.param.MoveEventParam;
import com.three.points.repository.EventRepository;
import com.three.points.param.EventParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.points.repository.ThemeDetailRepository;
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
 * Created by csw on 2019-10-20.
 * Description:
 */

@Service
public class EventService extends BaseService<Event, String> {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventTypeService eventTypeService;

    @Autowired
    private ThemeDetailRepository themeDetailRepository;

    @Transactional
    public void create(EventParam eventParam) {
        BeanValidator.check(eventParam);

        // 事件名称不能相同
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        if (eventRepository.countByEventNameAndOrganizationIdAndStatus(eventParam.getEventName(), firstOrganizationId, StatusEnum.OK.getCode()) > 0) {
            throw new ParameterException("已存在同名[" + eventParam.getEventName() + "]的事件");
        }

        Event event = new Event();
        event = (Event) BeanCopyUtil.copyBean(eventParam, event);

        event.setOrganizationId(firstOrganizationId);

        EventType eventType = eventTypeService.getEventTypeById(event.getTypeId());
        event.setTypeName(eventType.getTypeName());

        eventRepository.save(event);
    }

    @Transactional
    public void update(EventParam eventParam) {
        BeanValidator.check(eventParam);

        Event event = getEntityById(eventRepository, eventParam.getId());
        // 事件名称不能相同
        if (eventRepository.countByEventNameAndOrganizationIdAndStatusAndIdNot(eventParam.getEventName(), event.getOrganizationId(), StatusEnum.OK.getCode(), event.getId()) > 0) {
            throw new ParameterException("已存在同名[" + eventParam.getEventName() + "]的事件");
        }
        event = (Event) BeanCopyUtil.copyBean(eventParam, event);

        EventType eventType = eventTypeService.getEventTypeById(event.getTypeId());
        event.setTypeName(eventType.getTypeName());

        eventRepository.save(event);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<Event> eventList = new ArrayList<>();
        for (String id : idSet) {
            Event event = getEntityById(eventRepository, String.valueOf(id));
            event.setStatus(code);
            eventList.add(event);
        }
        eventRepository.saveAll(eventList);
    }

    public void validateUsed(String ids) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<String> errorList = new ArrayList<>();
        for (String id : idSet) {
            Event event = getEntityById(eventRepository, id);
            // 事件是否被使用
            int count = themeDetailRepository.countByEventIdAndStatus(event.getId(), StatusEnum.OK.getCode());
            if (count > 0) {
                errorList.add(event.getEventName());
            }
        }
        if (errorList.size() > 0) {
            throw new BusinessException("事件被积分奖扣详情使用，请确认是否删除？");
        }
    }

    public PageResult<Event> query(PageQuery pageQuery, int code, String typeId, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<Event> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = Lists.newArrayList();

            String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
            Specification<Event> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, firstOrganizationId);
            predicateList.add(codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder));

            // 按事件分类查找事件
            if (StringUtil.isNotBlank(typeId)) {
                predicateList.add(criteriaBuilder.equal(root.get("typeId"), typeId));
            }

            Predicate predicate = criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

            if (StringUtil.isNotBlank(searchValue)) {
                List<Predicate> predicateList1 = Lists.newArrayList();
                Predicate p1 = criteriaBuilder.like(root.get("eventName"), "%" + searchValue + "%");
                predicateList1.add(criteriaBuilder.or(p1));
                Predicate predicate1 = criteriaBuilder.or(predicateList1.toArray(new Predicate[0]));
                return criteriaQuery.where(predicate, predicate1).getRestriction();
            }
            return predicate;
        };
        if (pageQuery != null) {
            return query(eventRepository, pageQuery, sort, specification);
        } else {
            return query(eventRepository, sort, specification);
        }
    }

    @Transactional
    public void moveEvent(MoveEventParam moveEventParam) {
        BeanValidator.check(moveEventParam);
        EventType eventType = eventTypeService.findById(moveEventParam.getTypeId());
        Set<String> ids = StringUtil.getStrToIdSet1(moveEventParam.getId());
        List<Event> eventList = new ArrayList<>();
        ids.forEach(id -> {
            Event event = getEntityById(eventRepository, id);
            event.setTypeId(eventType.getId());
            event.setTypeName(eventType.getTypeName());
            eventList.add(event);
        });
        eventRepository.saveAll(eventList);
    }

    public Event findById(String id) {
        return getEntityById(eventRepository, id);
    }
}