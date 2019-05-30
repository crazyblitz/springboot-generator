package com.ley.springboot.base.dao;

import com.ley.springboot.base.entity.BaseEntity;
import com.ley.springboot.base.page.BasePage;

import java.util.List;

/**
 * base dao<br/>
 * type <b>T</b> is entity type
 **/
public interface BaseDao<T extends BaseEntity> {

    /**
     * insert entity
     *
     * @param entity
     * @return
     **/
    int insert(T entity);

    /**
     * insert entity selective
     *
     * @param entity
     * @return
     **/
    int insertSelective(T entity);


    /**
     * update entity by primary key
     *
     * @param entity
     * @return
     **/
    int updateByPrimaryKey(T entity);


    /**
     * update entity by primary key selective
     *
     * @param entity
     * @return
     **/
    int updateByPrimaryKeySelective(T entity);

    /**
     * select entity by primary key
     *
     * @param primaryKey
     * @return
     **/
    T selectByPrimaryKey(Object primaryKey);

    /**
     * delete entity by primary key
     *
     * @param primaryKey
     * @return
     **/
    int deleteByPrimaryKey(Object primaryKey);

    /**
     * query entities by list
     *
     * @param basePage
     * @return entities list
     **/
    List<T> queryByList(BasePage basePage);

    /**
     * query entities count
     *
     * @param basePage
     * @return
     **/
    int queryByCount(BasePage basePage);

    /**
     * query entities by page
     *
     * @param basePage
     * @return return page list
     **/
    List<T> queryByPage(BasePage basePage);
}