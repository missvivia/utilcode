package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.param.ProductUserFavParam;
import com.xyl.mmall.mobile.web.facade.MobileItemFacade;

/**
 * 收藏
 * @author author:lhp
 *
 * @version date:2015年8月25日上午10:57:10
 */
@Controller
@RequestMapping("/m/attention")
public class MobileAttentionController {
	
	@Resource
	private MobileItemFacade mobileItemFacade;

	/**
	 * 收藏商品
	 * 
	 * @param model
	 * @param scheduleId
	 */
	@BILog(action = "click", type = "followProduct",clientType="app")
	@RequestMapping(value = "/product/follow", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addFavoritesOfUser(@RequestBody JSONObject param) {
		long userId = SecurityContextUtils.getUserId();
		JSONObject jsonObject = new JSONObject();
		long poId = param.getLongValue("poId");
		
		if (userId > 0 && mobileItemFacade.addPoProductIntoFavList(userId, poId)) {
			jsonObject.put("code", 200);
			jsonObject.put("message", "ok");
		} else {
			jsonObject.put("code", 400);
			jsonObject.put("message", "follow skuId = " + poId + " userId = " + userId + " failed!");
		}
		return jsonObject;
	}
	
	/**
	 * 取消收藏商品
	 * 
	 * @param model
	 * @param brandId
	 */
	@BILog(action = "click", type = "calfollowProduct",clientType="app")
	@RequestMapping(value = "/product/unfollow", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject calFavoritesOfUser(@RequestBody JSONObject param) {
		long userId = SecurityContextUtils.getUserId();
		JSONObject jsonObject = new JSONObject();
		long poId = param.getLongValue("poId");
		if (userId > 0 && mobileItemFacade.removePoProductFromFavList(userId, poId)) {
			jsonObject.put("code", 200);
			jsonObject.put("message", "ok");
		} else {
			jsonObject.put("code", 400);
			jsonObject.put("message", "follow skuId = " + poId + " userId = " + userId + " failed!");
		}
		return jsonObject;
	}
	
	
	/**
	 * 收藏商品list
	 * @param searchParam
	 * @return
	 */
	@RequestMapping(value = "/product/collectList")
	public @ResponseBody BaseJsonVO collectProductList(ProductUserFavParam productUserFavParam) {
		long userId = SecurityContextUtils.getUserId();
		productUserFavParam.setUserId(userId);
		return mobileItemFacade.getProductDTOListByProductUserFavParam(productUserFavParam);
	}

	/**
	 * 用户是否收藏商品
	 * @param searchParam
	 * @return
	 */
	@RequestMapping(value = "/product/isCollect")
	public @ResponseBody BaseJsonVO iscollectProduct(@RequestParam(value="productId") long prouductId) {
		long userId = SecurityContextUtils.getUserId();
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		Map<String, Boolean> hashMap = new HashMap<>();
		if(userId<=0){
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			baseJsonVO.setMessage("用户未登录");
			return baseJsonVO;
		}
		List<Long> productIds = new ArrayList<Long>();
		productIds.add(prouductId);
		Map<String, String> map = mobileItemFacade.getPoProductFavListByUserIdOrPoIds(userId,productIds);
		baseJsonVO.setCode(ResponseCode.RES_SUCCESS);
		if(map.size()>0){
			hashMap.put("isCollect", true);
			baseJsonVO.setMessage("该商品已被收藏！");
			baseJsonVO.setResult(hashMap);
		}else{
			hashMap.put("isCollect", false);
			baseJsonVO.setMessage("该商品未被收藏！");
			baseJsonVO.setResult(hashMap);
		}
		return baseJsonVO;
	}
}
