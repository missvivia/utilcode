package com.xyl.mmall.oms.warehouse.test;

/**
 * @author hzzengchengyuan
 *
 */
public interface SimulatorPage {
	
	SimulatorPage initialize(Object data);
	
	String getTitle();
	
	String serviceName();
	
	String toHTML();
}
