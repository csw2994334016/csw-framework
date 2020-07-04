package com.three.resource_jpa.jpa.script.service;

import com.three.common.enums.StatusEnum;
import com.three.common.exception.ParameterException;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.BeanValidator;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.jpa.script.entity.Script;
import com.three.resource_jpa.jpa.script.param.ScriptParam;
import com.three.resource_jpa.jpa.script.repository.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019/09/07.
 * Description:
 */
@Service
public class ScriptService extends BaseService<Script, String> {

    @Autowired
    private ScriptRepository scriptRepository;

    @Transactional
    public void create(ScriptParam param) {
        BeanValidator.check(param);

        // 脚本名称不可以重复
        if (scriptRepository.countByScriptNameAndStatus(param.getScriptName(), StatusEnum.OK.getCode()) > 0) {
            throw new ParameterException("脚本名称已经存在");
        }

        Script script = new Script();
        script = (Script) BeanCopyUtil.copyBean(param, script);

        script.setVersion(1);

        scriptRepository.save(script);
    }

    @Transactional
    public void update(ScriptParam param) {
        BeanValidator.check(param);

        Script script = getEntityById(scriptRepository, param.getId());
        // 脚本名称不可以重复
        if (scriptRepository.countByScriptNameAndStatusAndIdNot(param.getScriptName(), StatusEnum.OK.getCode(), param.getId()) > 0) {
            throw new ParameterException("脚本名称已经存在");
        }
        script = (Script) BeanCopyUtil.copyBean(param, script, Arrays.asList("scriptCode"));

        script.setVersion(script.getVersion() + 1);

        scriptRepository.save(script);
    }

    @Transactional
    public void saveCode(ScriptParam param) {

        Script script = getEntityById(scriptRepository, param.getId());

        script.setScriptCode(param.getScriptCode());
        script.setVersion(script.getVersion() + 1);

        scriptRepository.save(script);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<Script> scriptList = new ArrayList<>();
        for (String id : idSet) {
            Script script = getEntityById(scriptRepository, id);
            script.setStatus(code);
            scriptList.add(script);
        }

        scriptRepository.saveAll(scriptList);
    }

    public PageResult<Script> query(PageQuery pageQuery, int code, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        PageResult<Script> pageResult;
        if (pageQuery != null) {
            pageResult = query(scriptRepository, pageQuery, sort, code, "scriptName", searchValue);
        } else {
            pageResult = query(scriptRepository, sort, code, "scriptName", searchValue);
        }
        for (Script script : pageResult.getData()) {
            script.setScriptCode(null);
        }
        return pageResult;
    }

    public Script findCode(String id) {
        return getEntityById(scriptRepository, id);
    }
}
