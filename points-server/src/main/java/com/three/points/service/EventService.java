package com.three.points.service;

import com.google.common.collect.Lists;
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

    @Transactional
    public void create(EventParam eventParam) {
        BeanValidator.check(eventParam);

        Event event = new Event();
        event = (Event) BeanCopyUtil.copyBean(eventParam, event);

        event.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());

        EventType eventType = eventTypeService.getEventTypeById(event.getTypeId());
        event.setTypeName(eventType.getTypeName());

        eventRepository.save(event);
    }

    @Transactional
    public void update(EventParam eventParam) {
        BeanValidator.check(eventParam);

        Event event = getEntityById(eventRepository, eventParam.getId());
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

    public PageResult<Event> query(PageQuery pageQuery, int code, String typeId, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<Event> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = Lists.newArrayList();

            Specification<Event> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code);
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
        Event event = getEntityById(eventRepository, moveEventParam.getId());
        event.setTypeId(moveEventParam.getTypeId());
        eventRepository.save(event);
    }

    public Event findById(String id) {
        return getEntityById(eventRepository, id);
    }
}