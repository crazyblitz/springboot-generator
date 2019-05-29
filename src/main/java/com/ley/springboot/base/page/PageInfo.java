package com.ley.springboot.base.page;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PaegInfo
 **/
public class PageInfo<T> {

    /**
     * current page number
     **/
    private Integer pageNo;

    /**
     * page size
     **/
    private Integer pageSize;


    /**
     * record count
     **/
    private Long count;

    /**
     * total page count
     **/
    private Long pageCount;

    /**
     * record list
     **/
    private List<T> list;

    /**
     * ext data(扩展数据)
     **/
    private Map<String, String> ext;

    /**
     * order by
     **/
    private String orderBy;

    /**
     * default page size
     **/
    protected static final int DEFAULT_PAGE_SIZE = 10;


    public PageInfo() {
        this.pageNo = Integer.valueOf(1);
        this.pageSize = Integer.valueOf(10);
        this.list = new ArrayList();
        this.ext = new HashMap();
        this.orderBy = "";
        this.pageSize = Integer.valueOf(-1);
    }


    public PageInfo(HttpServletRequest request) {
        this.pageNo = Integer.valueOf(1);
        this.pageSize = Integer.valueOf(10);
        this.list = new ArrayList();
        this.ext = new HashMap();
        this.orderBy = "";
        String no = request.getParameter("pageNo");
        if (StringUtils.isEmpty(no)) {
            this.setPageNo(Integer.valueOf(1));
        } else if (StringUtils.isNumeric(no)) {
            this.setPageNo(Integer.valueOf(Integer.parseInt(no)));
        }

        String size = request.getParameter("pageSize");
        if (StringUtils.isEmpty(size)) {
            this.setPageSize(Integer.valueOf(DEFAULT_PAGE_SIZE));
        }

        if (StringUtils.isNumeric(size)) {
            this.setPageSize(Integer.valueOf(Integer.parseInt(size)));
        }

        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isNotBlank(orderBy)) {
            this.setOrderBy(orderBy);
        }

    }

    public PageInfo(Integer pageNo, Integer pageSize) {
        this(pageNo, pageSize, Long.valueOf(0L));
    }


    public PageInfo(Integer pageNo, Integer pageSize, Long count) {
        this(pageNo, pageSize, count, new ArrayList());
    }

    public PageInfo(Integer pageNo, Integer pageSize, Long count, List<T> list) {
        this.pageNo = Integer.valueOf(1);
        this.pageSize = Integer.valueOf(10);
        this.list = new ArrayList();
        this.ext = new HashMap();
        this.orderBy = "";
        if (pageNo == null) {
            pageNo = Integer.valueOf(1);
        }

        if (pageSize == null) {
            pageSize = Integer.valueOf(10);
        }

        this.setCount(count);
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
        this.setList(list);
    }

    public Long getCount() {
        return this.count;
    }

    public void setCount(Long count) {
        this.count = count;
        if ((long) this.pageSize.intValue() >= count.longValue()) {
            this.pageNo = Integer.valueOf(1);
        }

    }

    public Integer getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = Integer.valueOf(pageSize.intValue() <= 0 ? 10 : pageSize.intValue());
    }

    public List<T> getList() {
        return this.list;
    }

    public PageInfo<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Map<String, String> getExt() {
        return this.ext;
    }

    public void setExt(Map<String, String> ext) {
        this.ext = ext;
    }

    public Long getPageCount() {
        if (this.count.longValue() % (long) this.pageSize.intValue() != 0L) {
            this.pageCount = Long.valueOf(this.count.longValue() / (long) this.pageSize.intValue() + 1L);
        } else {
            this.pageCount = Long.valueOf(this.count.longValue() / (long) this.pageSize.intValue());
        }

        if (this.pageCount.longValue() < 1L) {
            this.pageCount = Long.valueOf(1L);
        }

        return this.pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }
}