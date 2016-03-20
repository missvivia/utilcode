package com.xyl.mmall.oms.facade;

public interface PickSkuFacade {
	/**
	 * 通过poId获取未拣货数量
	 * @param poId
	 * @return
	 */
	public int unPickCountOfPoOrderId(String poId);
	
	
	/**
	 * 通过poId获取未发货数量
	 * @param poId
	 * @return
	 */
	public int unShipCountOfPoOrderId(String poId);
	
	
}
