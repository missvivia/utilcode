/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.mainsite.facade.MainsiteFacade;
import com.xyl.mmall.mainsite.vo.OrderReplenishStoreVO;
import com.xyl.mmall.order.dto.OrderReplenishDTO;

/**
 * ReplenishController.java created by yydx811 at 2015年6月5日 上午11:24:59
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@Controller
@RequestMapping("/replenish")
public class ReplenishController {
	
	@Autowired
	private MainsiteFacade mainsiteFacade;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String replenish(Model model) {
		model.addAttribute("name", "replenish");
		return "/pages/replenish/list";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getReplenishList(BasePageParamVO<OrderReplenishStoreVO> pageParamVO) {
		BaseJsonVO ret = new BaseJsonVO();
		long uid = SecurityContextUtils.getUserId();
		OrderReplenishDTO replenishDTO = new OrderReplenishDTO();
		replenishDTO.setUserId(uid);
		pageParamVO.setList(mainsiteFacade.getReplenishList(pageParamVO, replenishDTO));
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(pageParamVO);
		return ret;
	}
}
