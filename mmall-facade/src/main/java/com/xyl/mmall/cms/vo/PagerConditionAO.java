package com.xyl.mmall.cms.vo;


public abstract class PagerConditionAO extends ConditionAO {

 
    public int limit = 10;

    public int offset = 0;

    public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        if (offset < 0) {
            offset = 0;
        }
        this.offset = offset;
    }


    public PagerContainer getPagerContainer() {
        return new PagerContainer(getPagination());
    }

    private Pagination getPagination() {
        return new Pagination(offset, limit);
    }

}
