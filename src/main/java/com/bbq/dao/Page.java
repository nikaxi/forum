package com.bbq.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page  implements Serializable{

    private static int DEFAULT_PAGE_SIZE = 20;

    private int pageSize = DEFAULT_PAGE_SIZE;

    private long start;

    private List data;

    private long totalCount;

    public Page() {
        this(0, 0, DEFAULT_PAGE_SIZE, new ArrayList());
    }

    public Page(long start, long totalSize, int pageSize, List data) {
        this.pageSize = pageSize;
        this.start = start;
        this.totalCount = totalSize;
        this.data = data;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List getResult(){
        return this.data;
    }

    public long getCurrentPageNo() {
        return start / pageSize + 1;
    }

    public boolean isHasNextPage(){
        return this.getCurrentPageNo() < this.getTotalPageCount();
    }

    public long getTotalPageCount() {
        return this.totalCount;
    }

    public boolean isHasPreviousPage(){
        return  this.getCurrentPageNo() > 1;
    }

    protected static int getStartOfPage(int pageNo) {
        return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
    }

    public static int getStartOfPage(int pageNo, int pageSize){
        return (pageNo -1 ) * pageSize;
    }



}
