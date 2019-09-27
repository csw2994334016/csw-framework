package com.three.user.service;

import com.three.user.entity.Company;
import com.three.user.repository.CompanyRepository;
import com.three.user.param.CompanyParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
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
 * Created by csw on 2019-09-22.
 * Description:
 */

@Service
public class CompanyService extends BaseService<Company,  String> {

    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    public void create(CompanyParam companyParam) {
        BeanValidator.check(companyParam);

        Company company = new Company();
        company = (Company) BeanCopyUtil.copyBean(companyParam, company);

        companyRepository.save(company);
    }

    @Transactional
    public void update(CompanyParam companyParam) {
        BeanValidator.check(companyParam);

        Company company = getEntityById(companyRepository, companyParam.getId());
        company = (Company) BeanCopyUtil.copyBean(companyParam, company);

        companyRepository.save(company);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<Company> companyList = new ArrayList<>();
        for (String id : idSet) {
            Company company = getEntityById(companyRepository, String.valueOf(id));
            company.setStatus(code);
            companyList.add(company);
        }

        companyRepository.saveAll(companyList);
    }

    public PageResult<Company> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return query(companyRepository, pageQuery, sort, code, searchKey, searchValue);
    }

}