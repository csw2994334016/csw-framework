package com.three.resource_jpa.jpa.script.repository;

import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import com.three.resource_jpa.jpa.script.entity.Script;
import org.springframework.stereotype.Repository;

/**
 * Created by csw on 2019/09/07.
 * Description:
 */
@Repository
public interface ScriptRepository extends BaseRepository<Script, String> {

    Script findByScriptNameAndStatus(String name, int code);

    int countByScriptNameAndStatus(String scriptName, int code);

    int countByScriptNameAndStatusAndIdNot(String scriptName, int code, String id);
}
