package com.three.resource_jpa.jpa.script.repository;

import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import com.three.resource_jpa.jpa.script.entity.Script;
import org.springframework.stereotype.Repository;

/**
 * Created by csw on 2019/09/07.
 * Description:
 */
@Repository
public interface ScriptRepository extends BaseRepository<Script, Long> {

    Script findByName(String name);
}
