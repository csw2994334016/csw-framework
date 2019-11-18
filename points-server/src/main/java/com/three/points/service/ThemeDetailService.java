package com.three.points.service;

import com.three.common.enums.StatusEnum;
import com.three.points.entity.Event;
import com.three.points.entity.ThemeDetail;
import com.three.points.param.ThemeEmpParam;
import com.three.points.repository.ThemeDetailRepository;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.vo.ThemeDetailVo;
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
 * Created by csw on 2019-10-24.
 * Description:
 */

@Service
public class ThemeDetailService extends BaseService<ThemeDetail, String> {

    @Autowired
    private ThemeDetailRepository themeDetailRepository;

    @Autowired
    private EventService eventService;

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<ThemeDetail> themeDetailList = new ArrayList<>();
        for (String id : idSet) {
            ThemeDetail themeDetail = getEntityById(themeDetailRepository, String.valueOf(id));
            themeDetail.setStatus(code);
            themeDetailList.add(themeDetail);
        }

        themeDetailRepository.saveAll(themeDetailList);
    }

    public PageResult<ThemeDetail> query(PageQuery pageQuery, int code, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<ThemeDetail> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
            Specification<ThemeDetail> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, firstOrganizationId);
            Predicate predicate = codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder);

            if (StringUtil.isNotBlank(searchValue)) {
                List<Predicate> predicateList1 = new ArrayList<>();
                Predicate p1 = criteriaBuilder.like(root.get("name"), "%" + searchValue + "%");
                predicateList1.add(criteriaBuilder.or(p1));
                Predicate predicate1 = criteriaBuilder.or(predicateList1.toArray(new Predicate[0]));
                return criteriaQuery.where(predicate, predicate1).getRestriction();
            }
            return predicate;
        };
        if (pageQuery != null) {
            return query(themeDetailRepository, pageQuery, sort, specification);
        } else {
            return query(themeDetailRepository, sort, specification);
        }
    }

    public ThemeDetail findById(String id) {
        return getEntityById(themeDetailRepository, id);
    }

    public List<ThemeDetailVo> findByThemeId(String themeId) {
        List<ThemeDetail> themeDetailList = themeDetailRepository.findAllByThemeIdAndStatus(themeId, StatusEnum.OK.getCode());
        Map<String, ThemeDetailVo> themeDetailVoMap = new HashMap<>();
        for (ThemeDetail themeDetail : themeDetailList) {
            // 参与人员
            ThemeEmpParam themeEmpParam = new ThemeEmpParam();
            themeEmpParam = (ThemeEmpParam) BeanCopyUtil.copyBean(themeDetail, themeEmpParam);
            if (themeDetailVoMap.get(themeDetail.getEventName()) == null) {
                ThemeDetailVo themeDetailVo = new ThemeDetailVo();
                themeDetailVo = (ThemeDetailVo) BeanCopyUtil.copyBean(themeDetail, themeDetailVo);
                // 查找事件
                if (themeDetail.getEventId() != null) {
                    Event event = eventService.findById(themeDetail.getEventId());
                    themeDetailVo.setAscoreMin(event.getAscoreMin());
                    themeDetailVo.setAscoreMax(event.getAscoreMax());
                    themeDetailVo.setBscoreMin(event.getBscoreMin());
                    themeDetailVo.setBscoreMax(event.getBscoreMax());
                }
                themeDetailVoMap.put(themeDetail.getEventName(), themeDetailVo);
            }
            themeDetailVoMap.get(themeDetail.getEventName()).getThemeEmpParamList().add(themeEmpParam);
        }
        return new ArrayList<>(themeDetailVoMap.values());
    }
}