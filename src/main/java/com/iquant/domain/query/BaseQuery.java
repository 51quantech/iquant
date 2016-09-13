package com.iquant.domain.query;

/**
 * Created by yonggangli on 2016/9/1.
 */
public class BaseQuery {

    private int startRow = 0;
    private int endRow = Integer.MAX_VALUE;

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }
}
