package com.three.points.service;

import com.three.points.entity.EventType;
import com.three.points.repository.EventTypeRepository;
import com.three.points.param.EventTypeParam;
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
public class EventTypeService extends BaseService<EventType,  String> {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Transactional
    public void create(EventTypeParam eventTypeParam) {
        BeanValidator.check(eventTypeParam);

        EventType eventType = new EventType();
        eventType = (EventType) BeanCopyUtil.copyBean(eventTypeParam, eventType);

        eventTypeRepository.save(eventType);
    }

    @Transactional
    public void update(EventTypeParam eventTypeParam) {
        BeanValidator.check(eventTypeParam);

        EventType eventType = getEntityById(eventTypeRepository, eventTypeParam.getId());
        eventType = (EventType) BeanCopyUtil.copyBean(eventTypeParam, eventType);

        eventTypeRepository.save(eventType);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<EventType> eventTypeList = new ArrayList<>();
        for (String id : idSet) {
            EventType eventType = getEntityById(eventTypeRepository, String.valueOf(id));
            eventType.setStatus(code);
            eventTypeList.add(eventType);
        }

        eventTypeRepository.saveAll(eventTypeList);
    }

    public PageResult<EventType> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return query(eventTypeRepository, pageQuery, sort, code, searchKey, searchValue);
    }

    public List<EventType> findAllWithTree(int code) {
        return null;
    }
}