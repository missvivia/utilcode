package com.xyl.mmall.cart.clean;

import java.io.Serializable;
import java.util.Map;

public class RDBResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Map<byte[], byte[]> result;
	
	private byte[] byteRes;
	
	private boolean searchSuccess;
	
	private Object resObj;
	
	public Map<byte[], byte[]> getResult() {
		return result;
	}

	public void setResult(Map<byte[], byte[]> result) {
		this.result = result;
	}

	public boolean isSearchSuccess() {
		return searchSuccess;
	}

	public void setSearchSuccess(boolean searchSuccess) {
		this.searchSuccess = searchSuccess;
	}

	public byte[] getByteRes() {
		return byteRes;
	}

	public void setByteRes(byte[] byteRes) {
		this.byteRes = byteRes;
	}

	public Object getResObj() {
		return resObj;
	}

	public void setResObj(Object resObj) {
		this.resObj = resObj;
	}

}
