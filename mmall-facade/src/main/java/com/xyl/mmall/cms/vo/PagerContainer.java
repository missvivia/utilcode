package com.xyl.mmall.cms.vo;

import java.util.Collections;
import java.util.List;

/**
 * 
 * @author hzchaizhf
 * @version 2014-09-17
 */
public class PagerContainer {
    private Pagination pagination;

    private List<?> list = Collections.emptyList();

    public PagerContainer(Pagination pagination) {
        super();
        this.pagination = pagination;
    }

    public void setTotalCount(int count) {
        pagination.setTotalRecord(count);
    }

    public void setDataList(List<?> dataList) {
        this.list = dataList;
    }

    public List<?> getList() {
        return list;
    }

    public int getRecords() {
        return pagination.getTotalRecord();
    }

    public int getTotal() {
        return pagination.getTotalRecord();
    }

    public int getPage() {
        return pagination.getCurrentPage();
    }

    public Pagination getPagination() {
        return pagination;
    }

}
