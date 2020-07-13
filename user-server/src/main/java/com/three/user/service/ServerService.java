package com.three.user.service;

import com.three.user.entity.Server;
import com.three.user.repository.ServerRepository;
import com.three.user.param.ServerParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.common.utils.BeanValidator;
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
 * Created by csw on 2020-07-06.
 * Description:
 */

@Service
public class ServerService extends BaseService<Server, String> {

    @Autowired
    private ServerRepository serverRepository;

    @Transactional
    public void create(ServerParam serverParam) {
        BeanValidator.check(serverParam);

        Server server = new Server();
        server = (Server) BeanCopyUtil.copyBean(serverParam, server);

        serverRepository.save(server);
    }

    @Transactional
    public void update(ServerParam serverParam) {
        BeanValidator.check(serverParam);

        Server server = getEntityById(serverRepository, serverParam.getId());
        server = (Server) BeanCopyUtil.copyBean(serverParam, server);

        serverRepository.save(server);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<Server> serverList = new ArrayList<>();
        for (String id : idSet) {
            Server server = getEntityById(serverRepository, id);
            server.setStatus(code);
            serverList.add(server);
        }

        serverRepository.saveAll(serverList);
    }

    public PageResult<Server> query(Integer page, Integer limit, int code, String searchValue) {
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<Server> specification = (root, criteriaQuery, criteriaBuilder) -> {
            Specification<Server> codeAndOrganizationSpec = getCodeAndSearchKeySpec(code, "serverName", searchValue);
            List<Predicate> predicateList1 = new ArrayList<>();
            Predicate predicate = codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder);
            Predicate p2 = criteriaBuilder.equal(root.get("serverType"), 1);
            predicateList1.add(criteriaBuilder.and(p2));
            Predicate predicate1 = criteriaBuilder.and(predicateList1.toArray(new Predicate[0]));
            return criteriaQuery.where(predicate, predicate1).getRestriction();
        };
        if (page != null && limit != null) {
            return query(serverRepository, new PageQuery(page, limit), sort, specification);
        } else {
            return query(serverRepository, sort, specification);
        }
    }

    public Server findById(String id) {
        return getEntityById(serverRepository, id);
    }
}