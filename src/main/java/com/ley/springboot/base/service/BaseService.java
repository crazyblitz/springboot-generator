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
 **/
public abstract class BaseService<T extends BaseEntity, K> {

    public abstract BaseDao<T> getDao();

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public int insert(T t) {
        return this.getDao().insert(t);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public int insertSelective(T t) {
        return this.getDao().insertSelective(t);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public int updateByPrimaryKey(T t) {
        return this.getDao().updateByPrimaryKey(t);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public int updateByPrimaryKeySelective(T t) {
        return this.getDao().updateByPrimaryKeySelective(t);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Throwable.class, readOnly = true)
    public T selectByPrimaryKey(K value) {
        return this.getDao().selectByPrimaryKey(value);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public int deleteByPrimaryKey(K value) {
        return this.getDao().deleteByPrimaryKey(value);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Throwable.class, readOnly = true)
    public List<T> queryByList(BasePage page) {
        return this.getDao().queryByList(page);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Throwable.class, readOnly = true)
    public int queryByCount(BasePage page) throws Exception {
        return this.getDao().queryByCount(page);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Throwable.class, readOnly = true)
    public List<T> queryByPage(BasePage page) throws Exception {
        Integer rowCount = Integer.valueOf(this.queryByCount(page));
        page.getPager().setRowCount(rowCount.intValue());
        return this.getDao().queryByPage(page);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Throwable.class, readOnly = true)
    public T queryBySingle(BasePage page) throws Exception {
        page.setPageSize(Integer.valueOf(1));
        List results = this.getDao().queryByList(page);
        return (T) (null != results && results.size() != 0 ? (BaseEntity) results.get(0) : null);
    }
}