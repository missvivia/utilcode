package com.xyl.mmall.cms.vo;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class POStatusGetVO extends PageVO implements java.io.Serializable {

	private static final long serialVersionUID = 1851375180151189743L;

	private long startDate;

	private long endDate;

	private long curSupplierAreaId;

	/**
	 * 0 - not filter 1 - filter by prd list audit status 2 - filter by prd info
	 * audit status 3 - filter by page audit status 4 - filter by banner audit
	 * status  5 - filter for status 'ready for online'
	 */
	private int flag;

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public long getCurSupplierAreaId() {
		return curSupplierAreaId;
	}

	public void setCurSupplierAreaId(long curSupplierAreaId) {
		this.curSupplierAreaId = curSupplierAreaId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "POStatusGetVO [startDate=" + startDate + ", endDate=" + endDate + ", curSupplierAreaId="
				+ curSupplierAreaId + ", flag=" + flag + "]";
	}

}
