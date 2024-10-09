package com.education.common.model;

import org.apache.ibatis.session.RowBounds;

/**
 *   
 *   

 */
public class PagingBounds extends RowBounds {

    private int offset;
    private int limit;

    public PagingBounds() {
        super();
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
