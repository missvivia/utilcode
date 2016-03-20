/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xyl.mmall.backend.facade.ProductFacade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.utils.ERPAccountUtils;

/**
 * ItemController.java created by yydx811 at 2015年7月30日 下午1:47:39
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@RestController
public class ItemController {

	private static Logger logger = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ProductFacade productFacade;
	
	@RequestMapping(value = "/item/productDetail", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getProductDetail(
			@RequestParam(value = "skuId", required = true) long skuId,
			@RequestParam(value = "supplierId", required = true) long businessId,
    		@RequestParam(value = "appid") String appid) {
		BaseJsonVO retJson = new BaseJsonVO();
		retJson.setCode(ResponseCode.RES_SUCCESS);
		ProductSKUDTO skuDTO = new ProductSKUDTO();
		skuDTO.setId(skuId);
		skuDTO.setBusinessId(businessId);
		retJson.setResult(productFacade.getProductSKUVO(skuDTO, true));
		if (logger.isDebugEnabled()) {
			logger.debug("Get productDetail from erp! Appid : {}, SKUId : {}, BusinessId : {}.",
					appid, skuId, businessId);
		}
		return retJson;
	}

	@RequestMapping(value = "/item/skuStock/update", method = RequestMethod.POST)
	public BaseJsonVO updateSKUStock(
			@RequestParam(value = "skuId", required = true) long skuId,
			@RequestParam(value = "supplierId", required = true) long businessId,
			@RequestParam(value = "stockNum", required = true) int stockNum,
			@RequestParam(value = "appid") String appid) {
		BaseJsonVO retJson = new BaseJsonVO();
		String businessIds = ERPAccountUtils.getERPAccountBusinessIdsByAppId(appid);
		if (StringUtils.isBlank(businessIds)) {
			logger.info("No businessId bind to appid! AppId : " + appid + ".");
			retJson.setCodeAndMessage(ResponseCode.RES_ERROR, "No businessId bind to appid!");
			return retJson;
		}
		List<String> bindIds = Arrays.asList(businessIds.split(","));
		if (bindIds.contains(String.valueOf(businessId))) {
			int res = productFacade.updateProductSKUStock(skuId, stockNum, businessId);
			if (res > 0) {
				retJson.setCodeAndMessage(ResponseCode.RES_SUCCESS, "Update skuStock successful!");
				logger.info("Update skuStock Failure : Unexpected AppId! AppId : {}, businessId : {}.", 
						appid, businessId);
			} else if (res == -3) {
				retJson.setCodeAndMessage(ResponseCode.RES_SUCCESS, "Update skuStock successful, but cache failure!");
				logger.info("Update skuStock Failure : Unexpected AppId! AppId : {}, businessId : {}.", 
						appid, businessId);
			} else if (res == -4) {
				retJson.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "Update skuStock Failure : Unexpected BusinessId!");
				logger.info("Update skuStock Failure : Unexpected BusinessId! AppId : {}, businessId : {}.",
						appid, businessId);
			} else {
				retJson.setCodeAndMessage(ResponseCode.RES_ERROR, "更新失败！");
				logger.info("Update skuStock Failure! AppId : {}, businessId : {}.", appid, businessId);
			}
		} else {
			retJson.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "Update skuStock failure : Unexpected AppId!");
			logger.info("Update skuStock Failure : Unexpected AppId! AppId : {}, businessId : {}.", 
					appid, businessId);
		}
		return retJson;
	}
	
}
