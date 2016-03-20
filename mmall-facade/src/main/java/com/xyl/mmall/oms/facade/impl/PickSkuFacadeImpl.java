package com.xyl.mmall.oms.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.oms.enums.PickStateType;
import com.xyl.mmall.oms.facade.PickSkuFacade;
import com.xyl.mmall.oms.meta.PickSkuItemForm;
import com.xyl.mmall.oms.service.PickSkuService;

@Facade
public class PickSkuFacadeImpl implements PickSkuFacade {

	@Autowired
	private PickSkuService pickSkuService; 
	
	
	@Override
	public int unPickCountOfPoOrderId(String poId) {
		int unPick = 0;
		List<PickSkuItemForm> list = pickSkuService.getUnPickSkuByPoOrderId(poId);
		if(list!= null && list.size()>0){
			for(PickSkuItemForm pickSku:list){
				if("0".equals(pickSku.getPickOrderId()))
						unPick+=pickSku.getSkuQuantity();
			}
		}
		return unPick;
	}


	@Override
	public int unShipCountOfPoOrderId(String poId) {
		int unShip = 0;
		List<PickSkuItemForm> list = pickSkuService.getUnPickSkuByPoOrderId(poId);
		if(list!= null && list.size()>0){
			for(PickSkuItemForm pickSku:list){
				if(!"0".equals(pickSku.getPickOrderId()) && pickSku.getPickStates()==PickStateType.PICKING)
					unShip += pickSku.getSkuQuantity();
			}
		}
		return unShip;
	}

}
