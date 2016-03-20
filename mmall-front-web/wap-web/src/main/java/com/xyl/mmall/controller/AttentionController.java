package com.xyl.mmall.controller;

import java.util.ArrayList;
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
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.param.ProductUserFavParam;
import com.xyl.mmall.mainsite.facade.MainsiteItemFacade;

/**
 * 收藏
 * @author author:lhp
 *
 * @version date:2015年8月25日上午10:57:10
 */
@Controller
@RequestMapping("/attention")
public class AttentionController {
	
	@Resource
	private MainsiteItemFacade itemFacade;

	/**
	 * 收藏商品
	 * 
	 */
	@BILog(action = "click", type = "followProduct", clientType="wap")
	@RequestMapping(value = "/product/follow", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addFavoritesOfUser(@RequestBody JSONObject param) {
		long userId = SecurityContextUtils.getUserId();
		JSONObject jsonObject = new JSONObject();
		if(userId<=0){
			 jsonObject.put("code", ResponseCode.RES_ERROR);
			 jsonObject.put("message", "商品已删除");
			 return jsonObject;
		}
		long productId = param.getLongValue("poId");
		int result = itemFacade.addPoProductIntoFavList(userId, productId);
		switch (result) {
			case -1:
				 jsonObject.put("code", ResponseCode.RES_ERROR);
				 jsonObject.put("message", "商品已删除");
			     break;
			case -2:
				 jsonObject.put("code", ResponseCode.RES_ERROR);
				 jsonObject.put("message", "商品已下架");
			     break;
			case -3:
				jsonObject.put("code", ResponseCode.RES_ERROR);
				jsonObject.put("message", "收藏商品失败");
			    break;
		    default:
			    jsonObject.put("code", ResponseCode.RES_SUCCESS);
			    jsonObject.put("message", "收藏商品成功");
			    break;
		}
		return jsonObject;
	}
	
	/**
	 * 取消收藏商品
	 * 
	 */
	@BILog(action = "click", type = "calfollowProduct", clientType="wap")
	@RequestMapping(value = "/product/unfollow", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject calFavoritesOfUser(@RequestBody JSONObject param) {
		long userId = SecurityContextUtils.getUserId();
		JSONObject jsonObject = new JSONObject();
		long productId = param.getLongValue("poId");
		if (userId > 0 && itemFacade.removePoProductFromFavList(userId, productId)) {
			jsonObject.put("code", ResponseCode.RES_SUCCESS);
			jsonObject.put("message", "ok");
		} else {
			jsonObject.put("code", ResponseCode.RES_ERROR);
			jsonObject.put("message", "follow skuId = " + productId + " userId = " + userId + " failed!");
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
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		productUserFavParam.setUserId(userId);
		baseJsonVO.setResult(itemFacade.getProductDTOListByProductUserFavParam(productUserFavParam));
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		return baseJsonVO;
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
		if(userId<=0){
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			return baseJsonVO;
		}
		List<Long> productIds = new ArrayList<Long>();
		productIds.add(prouductId);
		Map<String, String> map = itemFacade.getPoProductFavListByUserIdOrPoIds(userId,productIds);
		if(map.size()>0){
			baseJsonVO.setCode(ResponseCode.RES_SUCCESS);
		}else{
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
		}
		return baseJsonVO;
	}
}
