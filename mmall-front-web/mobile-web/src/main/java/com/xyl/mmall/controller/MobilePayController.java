/*
 * @(#) 2014-10-27
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.xyl.mmall.mobile.facade.MobileOrderFacade;

/**
 * MobilePayController.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-10-27
 * @since 1.0
 */
@Controller
@RequestMapping("/m")
public class MobilePayController {
	@Autowired
	private MobileOrderFacade mobileOrderFacade;

	@RequestMapping("notify")
	public ModelAndView notifyResult(@RequestParam(value = "tradeSerialId") long tradeId,
			@RequestParam(value = "state") boolean state) {
		if (state) {
			state = mobileOrderFacade.paySuccess(tradeId);
		}
		return new ModelAndView(new RedirectView("/m/result?netease:" + state));
	}
}
