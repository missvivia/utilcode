package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileBasePagerVO<E> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5330989441196521713L;
	protected  boolean hasNext;
	protected  ArrayList<E> object;
	
	public boolean isHasNext() {
		return hasNext;
	}
	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	public ArrayList<E> getObject() {
		return object;
	}
	public void setObject(ArrayList<E> object) {
		this.object = object;
	}
	
	
	
}
