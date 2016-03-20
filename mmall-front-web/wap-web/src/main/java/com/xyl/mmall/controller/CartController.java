package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.framework.annotation.CheckFormToken;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.CartFacade;
import com.xyl.mmall.mainsite.vo.CartVO;
import com.xyl.mmall.mainsite.vo.InputParam;
import com.xyl.mmall.promotion.enums.PlatformType;
import com.xyl.mmall.util.AreaUtils;

/**
 */
@Controller
public class CartController {

	@Resource
	private CartFacade cartFacade;

	private static final Logger logger = LoggerFactory.getLogger(CartController.class);

	@CheckFormToken(isCheckRepeat = false)
	@BILog(action = "page", type = "cartPage", clientType = "wap")
	@RequestMapping(value = "/cartlist", method = RequestMethod.GET)
	public String getCartPage(Model model) {
		// long userId = SecurityContextUtils.getUserId();
		// CartVO cartVO = cartFacade.getCartInfo(userId, getProviceId(), null,
		// PlatformType.WAP, false);
		// model.addAttribute("cartVO",cartVO);
		// if((cartVO.getCartStoreList()!=null&&cartVO.getCartStoreList().size()>0)){
		return "pages/cart/cartlist";
		// }else{
		// return "pages/cart/nocart";
		// }

	}

	/**
	 * 二级页面 放入购物车
	 * 
	 * @param model
	 * @param skuId
	 * @param diff
	 * @return
	 */
	@BILog(action = "click", type = "addToCart", clientType = "wap")
	@RequestMapping(value = "/cart/addToCart", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO addToCart(Model model, @RequestBody InputParam obj) {
		long userid = SecurityContextUtils.getUserId();
		int ret = cartFacade.addItemToCart(userid, getProviceId(), obj.skuId, obj.diff);
		BaseJsonVO retObj = new BaseJsonVO();
		ErrorCode retval = ErrorCode.SUCCESS;
		if (ret >= 0)
			retval = ErrorCode.SUCCESS;
		else if (ret == -5) {
			retval = ErrorCode.PROD_OFFLINE;
		} else if (ret == CartService.CART_OVER_ITMESCOUNT)
			retval = ErrorCode.CART_ITEMOVERFLOW;
		else
			retval = ErrorCode.CART_INTERNAL_ERROR;

		retObj.setCode(retval);
		retObj.setMessage(retval.getDesc());
		model.addAttribute("skuid", obj.skuId);
		return retObj;

	}

	@RequestMapping(value = "/cart/listmini", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO listmini(Model model,
			@RequestParam(value = "cartIds", required = false) String cartIds) {
		long userid = SecurityContextUtils.getUserId();
		List<Long> selectIds = new ArrayList<Long>();
		if (StringUtils.isNotEmpty(cartIds)) {
			String[] cartIdArray = cartIds.split(",");
			for (String cartId : cartIdArray) {
				selectIds.add(Long.parseLong(cartId));
			}
		}
		BaseJsonVO retObj = new BaseJsonVO(cartFacade.getCartInfo(userid, getProviceId(), selectIds, PlatformType.WAP,
				false));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}

	/**
	 * 删除全部购物车信息
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cart/deleteCart")
	public @ResponseBody BaseJsonVO deleteCart(Model model) {
		long userid = SecurityContextUtils.getUserId();
		BaseJsonVO retObj = new BaseJsonVO(cartFacade.deleteCart(userid, getProviceId()));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	/**
	 * 根据skuId删除购物车商品
	 * 
	 * @param model
	 * @param obj
	 * @return
	 */
	@RequestMapping(value = "/cart/deleteProduct", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO deleteProduct(Model model, @RequestBody InputParam obj) {
		long userId = SecurityContextUtils.getUserId();
		cartFacade.deleteCartItem(userId, getProviceId(), obj.ids);// 不同于web版本，这里调用可以恢复的删除方法
		BaseJsonVO retObj = new BaseJsonVO();
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	/**
	 * 清除无效的购物车信息
	 * 
	 * @param model
	 * @param obj
	 * @return
	 */
	@RequestMapping(value = "/cart/deleteInvalidCartItems", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO deleteInvalidCartItems(Model model, @RequestBody InputParam obj) {
		long userId = SecurityContextUtils.getUserId();
		BaseJsonVO retObj = new BaseJsonVO();
		cartFacade.deleteCartItemsExceptBySkuIds(userId, getProviceId(), obj.ids);
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	@RequestMapping(value = "/cart/recover", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO recover(Model model, @RequestBody InputParam obj) {
		long userid = SecurityContextUtils.getUserId();
		InputParam retInputParam = cartFacade.recoverCart(userid, getProviceId());
		int ret = retInputParam.diff;
		ErrorCode retval = ErrorCode.SUCCESS;
		if (ret >= 0)
			retval = ErrorCode.SUCCESS;
		else if (ret == CartService.CART_NO_INVENTORY)
			retval = ErrorCode.CART_COUNT_NOT_ENOUGH;
		else if (ret == CartService.CART_OVER_ITMESCOUNT)
			retval = ErrorCode.CART_OVERFLOW;
		else if (ret == CartService.CART_OVER_ITMESCOUNT)
			retval = ErrorCode.CART_ITEMOVERFLOW;
		else
			retval = ErrorCode.CART_INTERNAL_ERROR;

		obj.selectedIds.addAll(retInputParam.selectedIds);
		CartVO cartvo = cartFacade.getCart(userid, getProviceId(), obj.selectedIds, PlatformType.WAP);
		BaseJsonVO retObj = new BaseJsonVO(cartvo);
		if (cartvo.getRetCode() == ErrorCode.CART_PO_INVALID.getIntValue())
			retObj.setCode(ErrorCode.CART_PO_INVALID);
		else
			retObj.setCode(retval);
		return retObj;

	}

	@RequestMapping(value = "/cart/updateamount")
	public @ResponseBody BaseJsonVO updateamount(Model model, @RequestBody InputParam obj) {
		long userid = SecurityContextUtils.getUserId();

		int ret = cartFacade.addItemToCart(userid, getProviceId(), obj.skuId, obj.diff, obj.selectedIds);
		ErrorCode retval = ErrorCode.SUCCESS;
		if (ret >= 0)
			retval = ErrorCode.SUCCESS;
		else if (ret == CartService.CART_NO_INVENTORY)
			retval = ErrorCode.CART_COUNT_NOT_ENOUGH;
		else if (ret == CartService.CART_OVER_ITMESCOUNT)
			retval = ErrorCode.CART_OVERFLOW;
		else if (ret == CartService.CART_OVER_ITMESCOUNT)
			retval = ErrorCode.CART_ITEMOVERFLOW;
		else
			retval = ErrorCode.CART_INTERNAL_ERROR;

		CartVO cartvo = cartFacade.getCart(userid, getProviceId(), obj.selectedIds, PlatformType.WAP);
		BaseJsonVO retObj = new BaseJsonVO(cartvo);

		if (cartvo.getRetCode() == ErrorCode.CART_PO_INVALID.getIntValue())
			retObj.setCode(ErrorCode.CART_PO_INVALID);
		else
			retObj.setCode(retval);
		return retObj;

	}

	/**
	 * 购物车数量
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cart/getcount")
	public @ResponseBody BaseJsonVO getCartCount(Model model) {
		long userid = SecurityContextUtils.getUserId();
		BaseJsonVO retObj = new BaseJsonVO(cartFacade.getCartValidCount(userid, getProviceId()));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	/**
	 * 从环境变量，得到用户的省信息
	 * 
	 * @return
	 */
	private int getProviceId() {
		return AreaUtils.getProvinceCode();
	}

	/**
	 * 场景:进货单购物数量改变更新
	 * 
	 * @param model
	 * @param obj
	 * @return
	 */
	@RequestMapping(value = "/cart/updateCartAmount")
	public @ResponseBody BaseJsonVO updateCartamount(Model model, @RequestBody InputParam obj) {
		long userid = SecurityContextUtils.getUserId();
		int result = cartFacade.updateCartAmount("NKV_CART_VALID", userid, getProviceId(), obj.getCartItemDTOs());
		BaseJsonVO retObj = new BaseJsonVO();
		if (result > 0) {
			retObj.setCode(ErrorCode.SUCCESS);
		} else if (result == -4) {
			retObj.setCode(201);
			retObj.setMessage("参数无效");
		} else if (result == -5) {
			retObj.setCode(201);
			retObj.setMessage("购买商品已下架商品");
		} else {
			retObj.setCode(201);
			retObj.setMessage("购物数量更新失败");
		}
		return retObj;

	}

	@RequestMapping(value = "/cart/addInventoryForTest", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO setInventoryForTest(Model model, @RequestBody InputParam obj) {
		cartFacade.setInventoryCount(obj.skuId, obj.diff);
		BaseJsonVO retObj = new BaseJsonVO();
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	@RequestMapping(value = "/inventory/setcount")
	public @ResponseBody BaseJsonVO setInventoryCount(Model model, @RequestParam long skuId, @RequestParam int count) {

		BaseJsonVO retObj = new BaseJsonVO(cartFacade.setInventoryCount(skuId, count));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	@RequestMapping(value = "/inventory/getcount")
	public @ResponseBody BaseJsonVO getInventoryCount(Model model, @RequestBody InputParam obj) {

		BaseJsonVO retObj = new BaseJsonVO(cartFacade.getInventoryCount(obj.ids));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	/**
	 * 场景:订单列表 再次购买
	 * 
	 * @param model
	 * @param obj
	 *            - {"cartItemDTOs":[{"skuid":1010464,"count":11},{"skuid":
	 *            1015608,"count":13}],"orderId":1375632}
	 * @return
	 */
	@RequestMapping(value = "/cart/buyAgain")
	public @ResponseBody BaseJsonVO buyAgain(Model model, @RequestBody InputParam obj) {
		long userid = SecurityContextUtils.getUserId();
		return cartFacade.buyAgain(userid, getProviceId(), obj);
	}

	// /**
	// * 场景:获取进货单中选中的商品
	// * @param model
	// * @param skuIds - [1010464,1015608]
	// * @return 当前选中的商品
	// */
	// @RequestMapping(value = "/cart/selection")
	// public @ResponseBody BaseJsonVO select(Model model) {
	// long userId = SecurityContextUtils.getUserId();
	// BaseJsonVO result = new BaseJsonVO();
	// result.setCode(ErrorCode.SUCCESS);
	// result.setResult(cartFacade.selectCartItems(userId, getProviceId(), new
	// Long[0]));
	// return result;
	// }

	/**
	 * 场景:选中进货单中商品
	 * 
	 * @param model
	 * @param skuIds
	 *            - 当前选中的所有skuIds[1010464,1015608]
	 * @return 选中后的当前选中的商品
	 */
	@RequestMapping(value = "/cart/select")
	public @ResponseBody BaseJsonVO select(Model model, @RequestBody Long[] skuIds) {
		long userId = SecurityContextUtils.getUserId();
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		cartFacade.selectCartItems(userId, getProviceId(), skuIds);
		// result.setResult();
		return result;
	}

	// /**
	// * 场景:取消选中进货单中商品
	// * @param model
	// * @param skuIds - [1010464,1015608]
	// * @return 取消选中后的当前选中的商品
	// */
	// @RequestMapping(value = "/cart/unselect")
	// public @ResponseBody BaseJsonVO unselect(Model model,
	// @RequestBody Long[] skuIds) {
	// long userId = SecurityContextUtils.getUserId();
	// BaseJsonVO result = new BaseJsonVO();
	// result.setCode(ErrorCode.SUCCESS);
	// cartFacade.unselectCartItems(userId, getProviceId(), skuIds);
	// // result.setResult();
	// return result;
	// }

}
