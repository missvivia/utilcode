/*
 * @(#) 2014-12-2
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.controller.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xyl.mmall.cms.facade.ExpressFinanceFacade;
import com.xyl.mmall.controller.BaseController;

/**
 * TradeFinanceController.java
 *
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-12-2
 * @since 1.0
 */
@Controller
@RequestMapping("/finance/express")
public class ExpressFinanceController extends BaseController {

	@Autowired
	private ExpressFinanceFacade expressFinanceFacade;

	public String queryCODCash() {
		return "";
	}

	public String express() {
		return "";
	}
}
