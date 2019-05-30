package com.ley.springboot.base.web;


import com.ley.springboot.base.page.PageInfo;
import com.ley.springboot.base.page.Pager;

import java.util.List;

/**
 * base controller<br/>
 * type <b>T</b> is entity type
 *
 * @author liuenyuan
 **/
public class BaseController<T> {


    /**
     * get page info
     *
     * @param pager
     * @param rows
     * @return {@link PageInfo}
     * @see PageInfo
     **/
    protected PageInfo<T> getPageInfo(Pager pager, List<T> rows) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(rows);
        pageInfo.setCount(Long.valueOf((long) pager.getRowCount()));
        pageInfo.setPageSize(Integer.valueOf(pager.getPageSize()));
        pageInfo.setPageCount(Long.valueOf((long) pager.getPageCount()));
        pageInfo.setPageNo(Integer.valueOf(pager.getPageId()));
        return pageInfo;
    }

}