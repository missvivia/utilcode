package com.xyl.mmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author hzlihui2014
 *
 */
@Controller
@RequestMapping(value = "/3g")
public class TestWapController {

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String getIndex(Model model) {
		return "3g/pages/index";
	}
	@RequestMapping(value = "/schedule", method = RequestMethod.GET)
	public String getSchedule(Model model) {
		return "3g/pages/schedule/schedule";
	}
}
