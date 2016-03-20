package com.xyl.mmall.cms.facade.param;

import java.io.Serializable;

/**
 * 查询、搜索的接口参数
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月11日 下午3:50:31
 *
 */
public class FrontTimeRangeSearchTypeParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5290025517164011364L;
	
	private int tag;
	private int status;
	private TimeRange timeRange = new TimeRange();
	private SearchPair search = new SearchPair();
	private int limit;
	private int offset;
	
	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int type) {
		this.status = type;
	}

	public TimeRange getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(TimeRange timeRange) {
		this.timeRange = timeRange;
	}

	public SearchPair getSearch() {
		return search;
	}

	public void setSearch(SearchPair search) {
		this.search = search;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public static enum TimeSearchTag {
		TIME(0), SEARCH(1);
		private int tag;
		private TimeSearchTag(int tag) {
			this.tag = tag;
		}
		public int getTag() {
			return tag;
		}
	}
	
	public static class TimeRange implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5675264666720483523L;
		
		private long startTime;
		private long endTime;
		public long getStartTime() {
			return startTime;
		}
		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}
		public long getEndTime() {
			return endTime;
		}
		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}
	}
	
	public static class SearchPair implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8457213633999246635L;
		
		private int searchType;
		private String searchKey = "";
		public int getSearchType() {
			return searchType;
		}
		public void setSearchType(int searchType) {
			this.searchType = searchType;
		}
		public String getSearchKey() {
			return searchKey;
		}
		public void setSearchKey(String searchKey) {
			this.searchKey = searchKey;
		}
	}
}
