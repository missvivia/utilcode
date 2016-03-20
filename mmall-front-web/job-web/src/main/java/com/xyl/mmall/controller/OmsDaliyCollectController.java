package com.xyl.mmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xyl.mmall.cms.facade.OmsDaliyCollectFacade;

@Controller
@RequestMapping("/omsDaily")
public class OmsDaliyCollectController {
	@Autowired
	private OmsDaliyCollectFacade omsDaliyCollectFacade;

	@RequestMapping("/collectShipData")
	public void collectShipData() {
		omsDaliyCollectFacade.collectShipData();
	}

}
