/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.ProductFacade;
import com.xyl.mmall.backend.vo.ProductSKULimitConfigVO;
import com.xyl.mmall.backend.vo.ProductSKULimitRecordVO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ItemSKUFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.OrderQueryFacade;
import com.xyl.mmall.cms.facade.SiteCMSFacade;
import com.xyl.mmall.cms.facade.impl.UserInfoFacadeImpl;
import com.xyl.mmall.cms.vo.ItemSKUBriefVO;
import com.xyl.mmall.cms.vo.order.OrderBriefInfoVO;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.param.OrderSearchParam;

/**
 * ItemLimitController.java created by yydx811 at 2015年11月18日 下午4:20:30
 * 商品限购管理controller
 *
 * @author yydx811
 */
@Controller
public class ItemLimitController {
	
	private static Logger logger = LoggerFactory.getLogger(ItemLimitController.class);

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	@Autowired
	private UserInfoFacadeImpl userInfoFacade;

	@Autowired
	private ItemSKUFacade itemSKUFacade;

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private SiteCMSFacade siteCMSFacade;
	
	@Autowired
	private ProductFacade productFacade;
	
	@Autowired
	private OrderQueryFacade cmsOrderQueryFacade;
	
	/**
	 * 商品限购管理页面请求
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/item/limit", method = RequestMethod.GET)
	@RequiresPermissions(value = { "item:limit" })
	public String itemLimit(Model model) {
		model.addAttribute("pages", 
				leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/item/limit";
	}
	
	/**
	 * 判断用户是否存在
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/item/limit/userExist", method = RequestMethod.GET)
	@RequiresPermissions(value = { "item:limit" })
	public @ResponseBody BaseJsonVO userExist(@RequestParam(required = true, value = "userName") String userName) {
		BaseJsonVO ret = new BaseJsonVO();
		if (StringUtils.isBlank(userName)) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "用户名不能为空！");
			return ret;
		}
		if (!StringUtils.endsWith(userName, MmallConstant.MAINSITE_ACCOUNT_SUFFIX)) {
			userName = userName + MmallConstant.MAINSITE_ACCOUNT_SUFFIX;
		}
		long userId = userInfoFacade.getUserIdByUserName(userName);
		if (userId > 0l) {
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "");
			ret.setResult(userId);
		} else {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "该用户不存在");
		}
		return ret;
 	}
	
	/**
	 * 获取商品
	 * @param userId
	 * @param skuId
	 * @return
	 */
	@RequestMapping(value = "/item/limit/getInfo", method = RequestMethod.GET)
	@RequiresPermissions(value = { "item:limit" })
	public @ResponseBody BaseJsonVO getProductInfoAndLimitInfo(@RequestParam(required = true, value = "userId") long userId,
			@RequestParam(required = true, value = "skuId") long skuId) {
		BaseJsonVO ret = new BaseJsonVO();
		// 获取用户信息
		UserProfileDTO userProfileDTO = userInfoFacade.getUserBaseInfo(userId);
		if (userProfileDTO.getUserId() < 1l) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "该用户不存在！");
			return ret;
		}
		// 获取商品信息
		ItemSKUBriefVO briefVO = itemSKUFacade.getItemBriefInfo(skuId);
		if (briefVO == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "未找到 " + skuId + " 相关的商品信息");
			return ret;
		}
		long businessId = briefVO.getStoreId();
		long agentId = SecurityContextUtils.getUserId();
		RetArg retArgArea = siteCMSFacade.getAgentAreaInfoByUserId(agentId);
		boolean isRoot = RetArgUtil.get(retArgArea, Boolean.class);
		BusinessDTO businessDTO = businessFacade.getBreifBusinessById(businessId, 0);
		if (businessDTO == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "商品所属商家不存在！");
		} else {
			if (retArgArea == null 
					|| (!isRoot && !RetArgUtil.get(retArgArea, ArrayList.class).contains(agentId))) {
				ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "没有权限查看该商品！");
			} else {
				ret.setCode(ResponseCode.RES_SUCCESS);
				JSONObject json = new JSONObject(3);
				briefVO.setStoreName(businessDTO.getStoreName());
				json.put("skuInfo", briefVO);
				// 限购
				if (briefVO.getIsLimited() == 1) {
					// 限购配置
					ProductSKULimitConfigVO limitConfigVO = productFacade.getProductSKULimitConfig(skuId);
					// 限购记录
					ProductSKULimitRecordVO limitRecordVO = 
							itemSKUFacade.getItemLimitConfigFromCacheAndDB(limitConfigVO, skuId, userId);
					if (limitRecordVO != null) {
						json.put("skuLimitConfig", limitConfigVO);
						json.put("skuLimitRecord", limitRecordVO);
					}
				}
				ret.setResult(json);
			}
		}
		return ret;
	}

	/**
	 * 同步缓存
	 * @param limitRecordVO
	 * @return
	 */
	@RequestMapping(value = "/item/limit/syncCache", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:limit" })
	public @ResponseBody BaseJsonVO syncCache(@RequestBody ProductSKULimitRecordVO limitRecordVO) {
		BaseJsonVO ret = new BaseJsonVO();
		long userId = limitRecordVO.getUserId();
		long skuId = limitRecordVO.getSkuId();
		// 获取商品信息
		ItemSKUBriefVO briefVO = itemSKUFacade.getItemBriefInfo(skuId);
		if (briefVO == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "未找到 " + skuId + " 相关的商品信息");
			return ret;
		}
		long businessId = briefVO.getStoreId();
		long agentId = SecurityContextUtils.getUserId();
		RetArg retArgArea = siteCMSFacade.getAgentAreaInfoByUserId(agentId);
		boolean isRoot = RetArgUtil.get(retArgArea, Boolean.class);
		BusinessDTO businessDTO = businessFacade.getBreifBusinessById(businessId, 0);
		if (businessDTO == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "商品所属商家不存在！");
		} else {
			if (retArgArea == null 
					|| (!isRoot && !RetArgUtil.get(retArgArea, ArrayList.class).contains(agentId))) {
				ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "没有权限查看该商品！");
			} else {
				int res = itemSKUFacade.syncCache(skuId, userId);
				String message = null;
				if (res >= 0) {
					message = "同步成功！";
					ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, message);
					ret.setResult(res);
				} else if (res == -1) {
					message = "限购配置不存在！";
					ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, message);
				} else if (res == -2) {
					message = "不在限购时间内！";
					ret.setCodeAndMessage(ResponseCode.RES_ERROR, message);
				} else {
					message = "同步失败！";
					ret.setCodeAndMessage(ResponseCode.RES_ERROR, message);
				}
				logger.info("Sync cache, userId : {}, skuId : {}, res : {}, message : {}.",
						userId, skuId, res, message);
			}
		}
		return ret;
	}

	/**
	 * 更改可购买数量
	 * @param limitRecordVO
	 * @return
	 */
	@RequestMapping(value = "/item/limit/update", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:limit" })
	public @ResponseBody BaseJsonVO updateLimitRecord(@RequestBody ProductSKULimitRecordVO limitRecordVO) {
		BaseJsonVO ret = new BaseJsonVO();
		int canBuyNum = limitRecordVO.getBuyNum();
		if (canBuyNum < 0) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "数量必须大于或等于0");
			return ret;
		}
		long userId = limitRecordVO.getUserId();
		long skuId = limitRecordVO.getSkuId();
		// 获取商品信息
		ItemSKUBriefVO briefVO = itemSKUFacade.getItemBriefInfo(skuId);
		if (briefVO == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "未找到 " + skuId + " 相关的商品信息");
			return ret;
		}
		long businessId = briefVO.getStoreId();
		long agentId = SecurityContextUtils.getUserId();
		RetArg retArgArea = siteCMSFacade.getAgentAreaInfoByUserId(agentId);
		boolean isRoot = RetArgUtil.get(retArgArea, Boolean.class);
		BusinessDTO businessDTO = businessFacade.getBreifBusinessById(businessId, 0);
		if (businessDTO == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "商品所属商家不存在！");
		} else {
			if (retArgArea == null 
					|| (!isRoot && !RetArgUtil.get(retArgArea, ArrayList.class).contains(agentId))) {
				ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "没有权限查看改商品！");
			} else {
				int res = itemSKUFacade.updateLimitRecord(skuId, userId, canBuyNum);
				String message = null;
				if (res > 0) {
					message = "修改成功";
					ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, message);
				} else if (res == -1) {
					message = "限购配置不存在！";
					ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, message);
				} else if (res == -2) {
					message = "不在限购时间内！";
					ret.setCodeAndMessage(ResponseCode.RES_ERROR, message);
				} else if (res == -3) {
					message = "超过限购量！";
					ret.setCodeAndMessage(ResponseCode.RES_ERROR, message);
				}else {
					message = "修改失败";
					ret.setCodeAndMessage(ResponseCode.RES_ERROR, message);
				}
				logger.info("Update limit record, userId : {}, skuId : {}, canBuyNum : {}, res : {}, message : {}.",
						userId, skuId, canBuyNum, res, message);
			}
		}
		return ret;
	}
	
	/**
	 * 获取限购订单列表
	 * @param userId
	 * @param skuId
	 * @param limit
	 * @param offset
	 * @return
	 */
	@RequestMapping(value = "/item/limit/order", method = RequestMethod.GET)
	@RequiresPermissions(value = { "item:limit", "item:order" }, logical = Logical.AND)
	public @ResponseBody BaseJsonVO getOrderByPeriod(@RequestParam(required = true, value = "userId") long userId,
			@RequestParam(required = true, value = "skuId") long skuId,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
		BaseJsonVO ret = new BaseJsonVO();
		// 限购配置
		ProductSKULimitConfigVO limitConfigVO = productFacade.getProductSKULimitConfig(skuId);
		// 限购记录
		ProductSKULimitRecordVO limitRecordVO = 
				itemSKUFacade.getItemLimitConfigFromCacheAndDB(limitConfigVO, skuId, userId);
		// 配置为空或者限购时间未到或超过
		if (limitRecordVO == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "限购配置为空或限购时间未到或已过期！");
			return ret;
		}
		if (limitRecordVO.getRecordId() < 1l) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "无购买记录！");
			return ret;
		}
		// 区域权限
		long agentId = SecurityContextUtils.getUserId();
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(agentId);
		if (retArg == null) {
			ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "没有区域或区域查询权限！");
			return ret;
		}
		OrderSearchParam param = new OrderSearchParam();
		if (!RetArgUtil.get(retArg, Boolean.class)) {
			param.setSiteAreaList(new ArrayList<Long>(RetArgUtil.get(retArg, HashSet.class)));
		}
		if (StringUtils.isEmpty(param.getOrderColumn())) {
			param.setOrderColumn("CreateTime");
			param.setAsc(false);
		}
		param.setUserId(userId);
		param.setStime(limitRecordVO.getCreateTime());
		param.setEtime(limitConfigVO.getLimitEndTime());
		JSONObject json = new JSONObject(2);
		retArg = cmsOrderQueryFacade.querySKULimitOrderList(param, skuId);
		param = RetArgUtil.get(retArg, OrderSearchParam.class);
		if (null == param || null == param.getTotalCount()) {
			json.put("total", 0);
		} else {
			List<OrderForm> orderList = RetArgUtil.get(retArg, ArrayList.class);
			if (orderList == null) {
				json.put("list", new ArrayList<OrderBriefInfoVO>(0));
				json.put("total", 0);
			} else {
				List<OrderBriefInfoVO> briefLimitList = new ArrayList<OrderBriefInfoVO>(limit);
				int count = 0;
				for (int i = 0; i < orderList.size(); ++i) {
					OrderForm order = orderList.get(i);
					long orderId = order.getOrderId();
					// 获取ordersku
					OrderSkuDTO orderSkuDTO = cmsOrderQueryFacade.getOrderSKU(skuId, orderId, userId);
					if (orderSkuDTO == null) {
						continue;
					}
					// 到达offset开始插入list
					if (count >= offset) {
						// 到达limit停止插入list
						if (count != limit) {
							OrderBriefInfoVO briefInfoVO = new OrderBriefInfoVO();
							briefInfoVO.setOrderId(orderId);
							briefInfoVO.setNum(orderSkuDTO.getTotalCount());
							briefInfoVO.setOrderDateStr(
									DateUtil.dateToString(new Date(order.getOrderTime()), DateUtil.LONG_PATTERN_SPLIT_POINT));
							briefLimitList.add(briefInfoVO);
						}
					}
					// 计数
					++count;
				}
				json.put("list", briefLimitList);
				json.put("total", count);
			}
		}
		ret.setResult(json);
		ret.setCode(ResponseCode.RES_SUCCESS);
		return ret;
	}
}
