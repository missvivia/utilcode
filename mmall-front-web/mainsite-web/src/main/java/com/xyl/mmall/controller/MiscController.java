package com.xyl.mmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Hello world!
 * 
 */
@Controller
public class MiscController {

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello(Model model) {
		model.addAttribute("name", "aaa");
		return "pages/index";
	}

	

	@RequestMapping(value = "/profile/wallet", method = RequestMethod.GET)
	public String myWallet(Model model) {
		return "pages/profile/wallet";
	}

	@RequestMapping(value = "/profile/template", method = RequestMethod.GET)
	public String my(Model model) {
		return "pages/my.template";
	}

	@RequestMapping(value = "/return", method = RequestMethod.GET)
	public String returnProduct(Model model) {
		model.addAttribute("name", "order");
		return "pages/return/return";
	}

	@RequestMapping(value = "/404", method = RequestMethod.GET)
	public String error1(Model model) {
		return "pages/404";
	}

	@RequestMapping(value = "/500", method = RequestMethod.GET)
	public String error2(Model model) {
		return "pages/500";
	}
	
	@RequestMapping(value = "/error/404", method = RequestMethod.GET)
	public String error404(Model model) {
		return "pages/404";
	}

	@RequestMapping(value = "/error/500", method = RequestMethod.GET)
	public String error500(Model model) {
		return "pages/500";
	}

	@RequestMapping(value = "/overseas", method = RequestMethod.GET)
	public String error3(Model model) {
		return "pages/overseas";
	}

    @RequestMapping(value = "/nogoods", method = RequestMethod.GET)
    public String error4(Model model) {
        return "pages/nogoods";
    }
  @RequestMapping(value = "/aboutus", method = RequestMethod.GET)
  public String aboutus(Model model) {
      //return "pages/aboutus";
  	  return "pages/abouts-new";
  }
  @RequestMapping(value = "/aboutnew", method = RequestMethod.GET)
  public String aboutusnew(Model model) {
      return "pages/abouts-new";
  }
  @RequestMapping(value = "/service", method = RequestMethod.GET)
  public String service(Model model) {
      return "pages/service";
  }
}
