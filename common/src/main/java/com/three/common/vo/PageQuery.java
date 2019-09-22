package com.three.common.vo;

import javax.validation.constraints.Min;

public class PageQuery {

    @Min(value = 1, message = "当前页码不合法")
    private int pageNo = 1;

    @Min(value = 1, message = "每页展示数量不合法")
    private int pageSize = 10;

    public PageQuery(@Min(value = 1, message = "当前页码不合法") int pageNo, @Min(value = 1, message = "每页展示数量不合法") int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo - 1;
    }

    public int getPageSize() {
        return pageSize;
    }
}
