package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xyl.mmall.constant.NkvConstant;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.ios.facade.IosCartFacade;
import com.xyl.mmall.mobile.ios.facade.pageView.cartList.IosCart;
import com.xyl.mmall.mobile.web.facade.MobileCartFacade;
import com.xyl.mmall.mobile.web.vo.CartVO;
import com.xyl.mmall.mobile.web.vo.InputParam;
import com.xyl.mmall.promotion.enums.PlatformType;
import com.xyl.mmall.util.AreaUtils;



/**
 * Hello world!
 *
 */
@Controller
@RequestMapping("/m")
public class MobileCartController {
	@Resource
	private MobileCartFacade mobileCartFacade;
	@Resource
	private IosCartFacade iosCartFacade;

	private static final Logger logger = LoggerFactory
			.getLogger(MobileCartController.class);

	@BILog(action = "click", type = "cartPage",clientType="app")
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	@Deprecated
	public String getcart(Model model) {
		long userId = SecurityContextUtils.getUserId();
		CartVO cartVO = mobileCartFacade.getCart(userId,
				getProviceId(), null,PlatformType.PC);
	//	CartVO cartVO = cartFacade.getCartInfo(userId, getProviceId(), null, PlatformType.PC, true);
		model.addAttribute("cartVO",cartVO);
		model.addAttribute("cartEndTime",mobileCartFacade.getCartLeftTime(userId, getProviceId()));
		if((cartVO.getPoList()!=null&&cartVO.getPoList().size()>0)||(cartVO.getActivations()!=null&&cartVO.getActivations().size()>0)){
			return "pages/cart/cartlist";
		}else{
			model.addAttribute("cityName","杭州");
			return "pages/cart/nocart";
		}
		
	}
	
	
	@BILog(action = "click", type = "cartPage",clientType="app")
	@RequestMapping(value = "/cartlist", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getCartPage(Model model) {
		long userId = SecurityContextUtils.getUserId();
		IosCart cartVO = iosCartFacade.getCartInfo(userId, getProviceId());
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		baseJsonVO.setMessage(ErrorCode.SUCCESS.getDesc());
		baseJsonVO.setResult(cartVO);
		return baseJsonVO;
	}

	@RequestMapping(value = "/cart/list", method = RequestMethod.GET)
	@Deprecated
	public @ResponseBody BaseJsonVO list(Model model) {
		long userid = SecurityContextUtils.getUserId();
		BaseJsonVO retObj = new BaseJsonVO(mobileCartFacade.getCart(userid,
				getProviceId(), null,PlatformType.PC));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}
	
	@RequestMapping(value = "/cart/count", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO count(Model model) {
		long userid = SecurityContextUtils.getUserId();
		BaseJsonVO retObj = new BaseJsonVO(mobileCartFacade.getCart(userid,
				getProviceId(), null,PlatformType.PC));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}

	@RequestMapping(value = "/cart/listmini", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO listmini(Model model,@RequestParam(value = "cartIds",required = false) String cartIds ) {
		long userid = SecurityContextUtils.getUserId();
		List<Long>selectIds = new ArrayList<Long>();
		if(StringUtils.isNotEmpty(cartIds)){
			String[] cartIdArray = cartIds.split(",");
			for(String cartId:cartIdArray){
				selectIds.add(Long.parseLong(cartId));
			}
		}
		BaseJsonVO retObj = new BaseJsonVO(mobileCartFacade.getCartInfo(userid, getProviceId(), selectIds, PlatformType.PC, true));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}
	
	@RequestMapping(value = "/cart/rebuy", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO rebuy(Model model,
			@RequestBody InputParam obj) {
		long userid = SecurityContextUtils.getUserId();
		int ret = mobileCartFacade.rebuyAndReturnCode(userid, getProviceId(),
				obj.skuId, 1);

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

		obj.selectedIds.add(obj.skuId);
		CartVO cartvo = mobileCartFacade.getCart(userid, getProviceId(),
				obj.selectedIds,PlatformType.PC);
		BaseJsonVO retObj = new BaseJsonVO(cartvo);
		if (cartvo.getRetCode() == ErrorCode.CART_PO_INVALID.getIntValue())
			retObj.setCode(ErrorCode.CART_PO_INVALID);
		else
			retObj.setCode(retval);
		return retObj;

	}

	@RequestMapping(value = "/cart/select", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO select(@RequestBody InputParam obj) {
		long userid = SecurityContextUtils.getUserId();
		BaseJsonVO retObj = new BaseJsonVO(mobileCartFacade.getCart(userid,
				getProviceId(), obj.selectedIds,PlatformType.MOBILE));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	@RequestMapping(value = "/cart/deleteCart")
	public @ResponseBody BaseJsonVO deleteCart(Model model) {
		long userid = SecurityContextUtils.getUserId();
		BaseJsonVO retObj = new BaseJsonVO(mobileCartFacade.deleteCart(userid,
				getProviceId()));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	@RequestMapping(value = "/cart/delete", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO delete(Model model,
			@RequestBody InputParam obj) {
		long userId = SecurityContextUtils.getUserId();

		mobileCartFacade.deleteCartItem(userId, getProviceId(), obj.ids);
		BaseJsonVO retObj = new BaseJsonVO();
//		BaseJsonVO retObj = new BaseJsonVO(cartFacade.getCartAfterDelete(
//				userId, getProviceId(), obj.selectedIds,PlatformType.PC));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	@RequestMapping(value = "/cart/deletemini", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO deletemini(Model model,
			@RequestBody InputParam obj) {
		long userId = SecurityContextUtils.getUserId();

		mobileCartFacade.deleteCartItem(userId, getProviceId(), obj.ids);
		BaseJsonVO retObj = new BaseJsonVO(mobileCartFacade.getCartMini(userId,
				getProviceId(), null));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	@RequestMapping(value = "/cart/recover", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO recover(Model model,
			@RequestBody InputParam obj) {
		long userid = SecurityContextUtils.getUserId();
		InputParam retInputParam =  mobileCartFacade.recoverCart(userid, getProviceId());
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
		CartVO cartvo = mobileCartFacade.getCart(userid, getProviceId(),
				obj.selectedIds,PlatformType.PC);
		BaseJsonVO retObj = new BaseJsonVO(cartvo);
		if (cartvo.getRetCode() == ErrorCode.CART_PO_INVALID.getIntValue())
			retObj.setCode(ErrorCode.CART_PO_INVALID);
		else
			retObj.setCode(retval);
		return retObj;

	}

	@RequestMapping(value = "/cart/updateamount")
	public @ResponseBody BaseJsonVO updateamount(Model model,
			@RequestBody InputParam obj) {
		long userid = SecurityContextUtils.getUserId();
	
		
		int ret = mobileCartFacade.addItemToCart(userid,
				getProviceId(), obj.skuId, obj.diff, obj.selectedIds);

		

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

		CartVO cartvo = mobileCartFacade.getCart(userid, getProviceId(),
				obj.selectedIds,PlatformType.PC);
		BaseJsonVO retObj = new BaseJsonVO(cartvo);
		
		if (cartvo.getRetCode() == ErrorCode.CART_PO_INVALID.getIntValue())
			retObj.setCode(ErrorCode.CART_PO_INVALID);
		else
			retObj.setCode(retval);
		return retObj;

	}

	/**
	 * 二级页面 放入购物车
	 * 
	 * @param model
	 * @param skuId
	 * @param diff
	 * @return
	 */
	@BILog(action = "click", type = "addToCart",clientType="app")
	@RequestMapping(value = "/cart/addToCart",method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO addToCart(Model model,
			@RequestBody InputParam obj) {
		long userid = SecurityContextUtils.getUserId();
		int ret = mobileCartFacade.addItemToCart(userid, getProviceId(),
				obj.skuId, obj.diff);
		BaseJsonVO retObj = new BaseJsonVO();
		ErrorCode retval = ErrorCode.SUCCESS;
		if (ret >= 0)
			retval = ErrorCode.SUCCESS;
		else if (ret == -5){
			retval = ErrorCode.PROD_OFFLINE;
		}else if (ret == CartService.CART_OVER_ITMESCOUNT)
			retval = ErrorCode.CART_ITEMOVERFLOW;
		else
			retval = ErrorCode.CART_INTERNAL_ERROR;

		Map<String,Object> map = new HashMap<>();
		retObj.setCode(retval);
		retObj.setMessage(retval.getDesc());
		model.addAttribute("skuid", obj.skuId);
		map.put("skuid", obj.skuId);
		retObj.setResult(map);
		return retObj;

	}
	
	@RequestMapping(value = "/cart/addInventoryForTest",method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO setInventoryForTest(Model model,
			@RequestBody InputParam obj) {
		 mobileCartFacade.setInventoryCount(obj.skuId, obj.diff);
		BaseJsonVO retObj = new BaseJsonVO();
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	@RequestMapping(value = "/cart/resettime")
	public @ResponseBody BaseJsonVO resettime(Model model) {
		long userid = SecurityContextUtils.getUserId();
		BaseJsonVO retObj = new BaseJsonVO();
		retObj.setResult(mobileCartFacade.resetTime(userid, this.getProviceId()) + CartService.CART_TIME);
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	@RequestMapping(value = "/inventory/setcount")
	public @ResponseBody BaseJsonVO setInventoryCount(Model model,
			@RequestParam long skuId, @RequestParam int count) {

		BaseJsonVO retObj = new BaseJsonVO(mobileCartFacade.setInventoryCount(skuId,
				count));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	@RequestMapping(value = "/inventory/getcount")
	public @ResponseBody BaseJsonVO getInventoryCount(Model model,
			@RequestBody InputParam obj) {

		BaseJsonVO retObj = new BaseJsonVO(mobileCartFacade.getInventoryCount(obj.ids));
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}

	/**
	 * 购物车数量
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cart/getcount")
	public @ResponseBody BaseJsonVO getCartCount(Model model) {
		long userid = SecurityContextUtils.getUserId();
		BaseJsonVO retObj = new BaseJsonVO(mobileCartFacade.getCartValidCount(userid,getProviceId()));
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
	 * 前台调用此方法将某个sku加入到库存关注列表或从sku关注列表中移除
	 * 
	 * @param model
	 * @param skuId
	 * @param subscribed
	 *            操作类型,0:添加,1:移除
	 * @return
	 */
	@RequestMapping(value = "/cart/updateremindstorage", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO addUserToRemindWhenStorage(Model model,
			@RequestParam long skuId, @RequestParam int subscribed) {
		long userid = SecurityContextUtils.getUserId();
		BaseJsonVO retObj = new BaseJsonVO();
		boolean flag = false;
		if (subscribed == 1) {
			flag = mobileCartFacade.addUserToRemindWhenStorage(userid,
					this.getProviceId(), skuId);
		} else if (subscribed == 0) {
			flag = mobileCartFacade.removeUserRemind(userid, this.getProviceId(),
					skuId);
		} else {
			logger.error("not support subscribed:" + subscribed);
			retObj.setCode(ErrorCode.CART_INTERNAL_ERROR);
			return retObj;
		}
		retObj.setCode(flag ? ErrorCode.SUCCESS : ErrorCode.CART_INTERNAL_ERROR);
		return retObj;
	}

	@RequestMapping(value = "/cart/userexistinremind", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO userExistInRemind(Model model,
			@RequestParam long skuId) {
		long userid = SecurityContextUtils.getUserId();
		boolean flag = mobileCartFacade.userExistInRemindStorage(userid,
				this.getProviceId(), skuId);
		BaseJsonVO retObj = new BaseJsonVO(flag);
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}
	
	@RequestMapping(value = "/cart/deleteProduct", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO deleteProduct(Model model,
			@RequestBody InputParam obj) {
		long userId = SecurityContextUtils.getUserId();
		mobileCartFacade.deleteCartItems(userId, getProviceId(), obj.ids);
		BaseJsonVO retObj = new BaseJsonVO();
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}
	
	/**
	 * 清除无效的购物车信息
	 * @param model
	 * @param obj
	 * @return
	 */
	@RequestMapping(value = "/cart/deleteInvalidCartItems", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO deleteInvalidCartItems(Model model,
			@RequestBody InputParam obj) {
		long userId = SecurityContextUtils.getUserId();
		BaseJsonVO retObj = new BaseJsonVO();
		mobileCartFacade.deleteCartItemsExceptBySkuIds(userId, getProviceId(), obj.ids);
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;

	}
	
	
	/**
	 * 场景:进货单购物数量改变更新
	 * @param model
	 * @param obj
	 * @return
	 */
	@RequestMapping(value = "/cart/updateCartAmount")
	public @ResponseBody BaseJsonVO updateCartamount(Model model,
			@RequestBody InputParam obj) {
		long userid = SecurityContextUtils.getUserId();
		int result = mobileCartFacade.updateCartAmount(NkvConstant.NKV_CART_VALID,userid, getProviceId(), obj.getCartItemDTOs());
		BaseJsonVO retObj = new BaseJsonVO();
        if(result>0){
        	retObj.setCode(ErrorCode.SUCCESS);	
        }else if(result==-4){
        	retObj.setCode(201);	
        	retObj.setMessage("参数无效");
        }else if(result==-5){
        	retObj.setCode(201);
        	retObj.setMessage("购买商品已下架商品");
        }else if(result == -6){
        	retObj.setCode(201);
        	retObj.setMessage("购物车数量超上限");
        }else{
        	retObj.setCode(201);
        	retObj.setMessage("购物数量更新失败");
        }
		return retObj;

	}
	
	/**
	 * 场景:订单列表 再次购买
	 * @param model
	 * @param obj
	 * @return
	 */
	@RequestMapping(value = "/cart/buyAgain")
	public @ResponseBody BaseJsonVO buyAgain(Model model,
			@RequestBody InputParam obj) {
		long userid = SecurityContextUtils.getUserId();
		return mobileCartFacade.buyAgain(userid, getProviceId(), obj);
	}

}
