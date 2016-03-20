package com.xyl.mmall.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xyl.mmall.timer.facade.ReturnTimerFacade;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年11月7日 下午1:00:54
 *
 */
@Controller
@RequestMapping("/return")
public class ReturnJobController {

	@Resource
	private ReturnTimerFacade retTimer;
	
	@RequestMapping("/cancel")
	public void cancel() {
		retTimer.setReturnStateToCanceled();
	}
	
	@RequestMapping("/jitPush")
	public void jitPush() {
		retTimer.pushReturnPackageToJIT();
	}
	
	@RequestMapping("/recycleCoupon")
	public void recycleCoupon() {
		retTimer.recycleCoupon();
	}
	
	@RequestMapping("/distributeCoupon")
	public void distributeCoupon() {
		retTimer.distributeReturnExpHb();
	}
}
