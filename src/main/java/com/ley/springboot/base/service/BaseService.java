package com.ley.springboot.base.service;


import com.ley.springboot.base.dao.BaseDao;
import com.ley.springboot.base.entity.BaseEntity;
import com.ley.springboot.base.page.BasePage;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * base service and this abstract class must add transaction annotation.<br/>
 * 由于该service类是抽象类,当其他类继承该类时,它是通过this.insert()的方式调用的,spring在进行事务管理切面
 * 拦截时,只会生成加上事务注解的类的AOP代理类,所以抽象类必须加上事务注解,否则spring的事务管理将对继承类继承过来
 * 的方法不起作用.
 *
 * @param <T> base entity派生类
 * @param <K> 主键类型
 * @author liuenyuan
 **/
public abstract class BaseService<T extends BaseEntity, K> {

    /**
     * get dao
     **/
    public abstract BaseDao<T> getDao();

    /**
     * @see BaseDao#insert(BaseEntity)
     **/
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public int insert(T entity) {
        return this.getDao().insert(entity);
    }


    /**
     * @see BaseDao#insertSelective(BaseEntity)
     **/
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public int insertSelective(T entity) {
        return this.getDao().insertSelective(entity);
    }

    /**
     * @see BaseDao#updateByPrimaryKey(BaseEntity)
     **/
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public int updateByPrimaryKey(T entity) {
        return this.getDao().updateByPrimaryKey(entity);
    }


    /**
     * @see BaseDao#updateByPrimaryKeySelective(BaseEntity)
     **/
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public int updateByPrimaryKeySelective(T entity) {
        return this.getDao().updateByPrimaryKeySelective(entity);
    }


    /**
     * @see BaseDao#selectByPrimaryKey(Object)
     **/
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Throwable.class, readOnly = true)
    public T selectByPrimaryKey(K primaryKey) {
        return this.getDao().selectByPrimaryKey(primaryKey);
    }

    /**
     * @see BaseDao#deleteByPrimaryKey(Object)
     **/
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public int deleteByPrimaryKey(K primaryKey) {
        return this.getDao().deleteByPrimaryKey(primaryKey);
    }


    /**
     * @see BaseDao#queryByList(BasePage)
     **/
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Throwable.class, readOnly = true)
    public List<T> queryByList(BasePage page) {
        return this.getDao().queryByList(page);
    }


    /**
     * @see BaseDao#queryByCount(BasePage)
     **/
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Throwable.class, readOnly = true)
    public int queryByCount(BasePage page) throws Exception {
        return this.getDao().queryByCount(page);
    }

    /**
     * @see BaseDao#queryByPage(BasePage)
     **/
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Throwable.class, readOnly = true)
    public List<T> queryByPage(BasePage page) throws Exception {
        Integer rowCount = Integer.valueOf(this.queryByCount(page));
        page.getPager().setRowCount(rowCount.intValue());
        return this.getDao().queryByPage(page);
    }

    /**
     * query by single
     **/
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Throwable.class, readOnly = true)
    public T queryBySingle(BasePage page) throws Exception {
        page.setPageSize(Integer.valueOf(1));
        List results = this.getDao().queryByList(page);
        return (T) (null != results && results.size() != 0 ? (BaseEntity) results.get(0) : null);
    }
}