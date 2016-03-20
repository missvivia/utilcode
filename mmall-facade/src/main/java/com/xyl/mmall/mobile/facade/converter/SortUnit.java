package com.xyl.mmall.mobile.facade.converter;


public class SortUnit<T>  implements Comparable<SortUnit>{
	private long sortKey;
	private T t;
	public SortUnit(long key,T t){
		this.t = t;
		this.sortKey = key;
	}

	@Override
	public int compareTo(SortUnit o) {
		if(this.sortKey < o.getSortKey() )
			return -1;
		else if(this.sortKey > o.getSortKey())
			return 1;
		return 0;
	}

	public long getSortKey() {
		return sortKey;
	}

	public void setSortKey(long sortKey) {
		this.sortKey = sortKey;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

}
