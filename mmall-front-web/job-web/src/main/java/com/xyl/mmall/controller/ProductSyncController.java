package com.xyl.mmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.timer.facade.OrderTimerFacade;

@Controller
public class ProductSyncController {
	
	@Autowired
	private OrderTimerFacade orderTimerFacade;
	
	@RequestMapping(value = "/product/saleNum/sync")
	@ResponseBody
	public BaseJsonVO saleNumSync(@RequestParam(value = "dealNum") int dealNum) {
		orderTimerFacade.syncProductSaleNum(dealNum);
		BaseJsonVO retObj = new BaseJsonVO();
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}

}
