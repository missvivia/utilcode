/**
 * 
 */
package com.xyl.mmall.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * @author lihui
 *
 */
@RestController
@RequestMapping("/m/web/")
public class MobileWebController {
	
	@RequestMapping(value = "/po", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO sharePO(
			@RequestParam(value = "poId", required = false)Long poId){
				return null;
	}

	@RequestMapping(value = "/prdt", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO sharePrdt(
			@RequestParam(value = "prdtId", required = false)Long poId){
				return null;
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO productDetail(
			@RequestParam(value = "prdtId", required = false)Long poId){
				return null;
	}
	
	@RequestMapping(value = "/logistics", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO logisticsDetail(
			@RequestParam(value = "deliverCode", required = false)Long deliverCode){
				return null;
	}
	
	@RequestMapping(value = "/gift", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO shareGift(
			@RequestParam(value = "id", required = false)Long id	){
				return null;
	}
	
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO helpCenter(){
				return null;
	}
}
