package com.three.points.service;

import com.three.points.entity.Event;
import com.three.points.repository.EventRepository;
import com.three.points.param.EventParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.exception.ParameterException;
import com.three.commonclient.utils.BeanValidator;
import com.three.resource_jpa.jpa.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019-10-20.
 * Description:
 */

@Service
public class EventService extends BaseService<Event,  String> {

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public void create(EventParam eventParam) {
        BeanValidator.check(eventParam);

        Event event = new Event();
        event = (Event) BeanCopyUtil.copyBean(eventParam, event);

        eventRepository.save(event);
    }

    @Transactional
    public void update(EventParam eventParam) {
        BeanValidator.check(eventParam);

        Event event = getEntityById(eventRepository, eventParam.getId());
        event = (Event) BeanCopyUtil.copyBean(eventParam, event);

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

    public PageResult<Event> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return query(eventRepository, pageQuery, sort, code, searchKey, searchValue);
    }

}