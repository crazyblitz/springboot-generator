package com.ley.springboot.base.dao;

import com.ley.springboot.base.entity.BaseEntity;
import com.ley.springboot.base.page.BasePage;

import java.util.List;

/**
 * base dao<br/>
 * type <b>T</b> is entity type
 **/
public interface BaseDao<T extends BaseEntity> {

    int insert(T arg0);

    int insertSelective(T arg0);

    int updateByPrimaryKey(T arg0);

    int updateByPrimaryKeySelective(T arg0);

    T selectByPrimaryKey(Object arg0);

    int deleteByPrimaryKey(Object arg0);

    List<T> queryByList(BasePage arg0);

    int queryByCount(BasePage arg0);

    List<T> queryByPage(BasePage arg0);
}