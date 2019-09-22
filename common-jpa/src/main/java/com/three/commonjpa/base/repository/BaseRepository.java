package com.three.commonjpa.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by csw on 2019/03/27.
 * Description:
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * 查找ID且一个状态
     *
     * @param id     主键ID
     * @param status 状态
     */
    public T findByIdAndStatus(ID id, Integer status);

    /**
     * 查找ID且多个状态
     *
     * @param id     主键ID
     * @param status 状态数组
     */
    public T findByIdAndStatusIn(ID id, Integer[] status);

    List<T> findAllByStatus(Integer code);
}
