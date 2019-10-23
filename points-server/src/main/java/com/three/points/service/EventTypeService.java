package com.three.points.service;

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
import com.three.commonclient.exception.ParameterException;
import com.three.commonclient.utils.BeanValidator;
import com.three.points.vo.EventTypeVo;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

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
    public void create(EventTypeParam eventTypeParam) {
        BeanValidator.check(eventTypeParam);

        EventType eventType = new EventType();
        eventType = (EventType) BeanCopyUtil.copyBean(eventTypeParam, eventType);

        eventType.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());

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

    public PageResult<EventType> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        if (pageQuery != null) {
            return query(eventTypeRepository, pageQuery, sort, code, searchKey, searchValue);
        } else {
            return query(eventTypeRepository, sort, code, searchKey, searchValue);
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
}