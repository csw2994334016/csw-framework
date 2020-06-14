package com.three.points.service;

import com.three.common.auth.SysEmployee;
import com.three.common.auth.SysOrganization;
import com.three.points.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointsService {

    @Autowired
    private CustomGroupEmpRepository customGroupRepository;

    @Autowired
    private AwardPrivilegeEmpRepository awardPrivilegeEmpRepository;

    @Autowired
    private ManagerTaskEmpRepository managerTaskEmpRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ThemeDetailRepository themeDetailRepository;

    @Transactional
    public void changeCustomGroupEmpInfo(SysEmployee sysEmployee) {
        // 更新自定义分组人员
        customGroupRepository.updateEmpInfoByEmpId(sysEmployee.getEmpNum(), sysEmployee.getFullName(), sysEmployee.getPicture(), sysEmployee.getOrganizationId(), sysEmployee.getOrgName(), sysEmployee.getId());
    }

    @Transactional
    public void changeAwardPrivilegeEmpInfo(SysEmployee sysEmployee) {
        // 更新奖扣权限人员
        awardPrivilegeEmpRepository.updateEmpInfoByEmpId(sysEmployee.getEmpNum(), sysEmployee.getFullName(), sysEmployee.getId());
    }

    @Transactional
    public void changeManagerTaskEmpInfo(SysEmployee sysEmployee) {
        // 更新管理任务设置人员
        managerTaskEmpRepository.updateEmpInfoByEmpId(sysEmployee.getEmpNum(), sysEmployee.getFullName(), sysEmployee.getCellNum(), sysEmployee.getTitleLevel(), sysEmployee.getGender(), sysEmployee.getOrganizationId(), sysEmployee.getOrgName(), sysEmployee.getId());
    }

    @Transactional
    public void changeThemeEmpInfo(SysEmployee sysEmployee) {
        themeRepository.updateRecorderName(sysEmployee.getFullName(), sysEmployee.getId());
        themeRepository.updateAttnName(sysEmployee.getFullName(), sysEmployee.getId());
        themeRepository.updateAuditName(sysEmployee.getFullName(), sysEmployee.getId());
    }

    @Transactional
    public void changeThemeDetailEmpInfo(SysEmployee sysEmployee) {
        themeDetailRepository.updateEmpInfoByEmpId(sysEmployee.getEmpNum(), sysEmployee.getFullName(), sysEmployee.getOrganizationId(), sysEmployee.getOrgName(), sysEmployee.getId());
    }

    @Transactional
    public void changeCustomGroupOrgInfo(SysOrganization sysOrganization) {
        customGroupRepository.updateOrgInfoByEmpId(sysOrganization.getOrgName(), sysOrganization.getId());
    }

    @Transactional
    public void changeManagerTaskOrgInfo(SysOrganization sysOrganization) {
        managerTaskEmpRepository.updateOrgInfoByEmpId(sysOrganization.getOrgName(), sysOrganization.getId());
    }

    @Transactional
    public void changeThemeDetailOrgInfo(SysOrganization sysOrganization) {
        themeDetailRepository.updateOrgInfoByEmpId(sysOrganization.getOrgName(), sysOrganization.getId());
    }
}
