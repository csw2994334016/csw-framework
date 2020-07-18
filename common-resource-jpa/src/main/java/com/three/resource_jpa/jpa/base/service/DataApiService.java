package com.three.resource_jpa.jpa.base.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface DataApiService {

    public Object execute(Map<Object, Object> map);
}
