package com.xyl.mmall.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xyl.mmall.timer.facade.CODAuditTimerFacade;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年11月7日 下午1:00:54
 *
 */
@Controller
@RequestMapping("/codAudit")
public class CODAuditJobController {

	@Resource
	private CODAuditTimerFacade codAuditTimer;
	
	@RequestMapping("/timeDetain")
	public void timeDetain() {
		codAuditTimer.passCODAuditBeforeSomeTime();
	}
	
	@RequestMapping("/whitelist")
	public void whitelist() {
		codAuditTimer.passCODAuditInWhiteList();
	}
	
	@RequestMapping("/timeout")
	public void timeout() {
		codAuditTimer.cancelCODAuditOfTimeout();
	}
}
