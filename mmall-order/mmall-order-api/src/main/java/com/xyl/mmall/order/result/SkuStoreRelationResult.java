package com.xyl.mmall.order.result;

import java.io.Serializable;
import java.util.Map;

public class SkuStoreRelationResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8165990048917455752L;

    private long skuid;
    
    private String storeName;
    
    private long SupplierId;
    
    private Map<Long, String> skuStoremap;

	public long getSkuid() {
		return skuid;
	}

	public void setSkuid(long skuid) {
		this.skuid = skuid;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public long getSupplierId() {
		return SupplierId;
	}

	public void setSupplierId(long supplierId) {
		SupplierId = supplierId;
	}

	public Map<Long, String> getSkuStoremap() {
		return skuStoremap;
	}

	public void setSkuStoremap(Map<Long, String> skuStoremap) {
		this.skuStoremap = skuStoremap;
	}
}
