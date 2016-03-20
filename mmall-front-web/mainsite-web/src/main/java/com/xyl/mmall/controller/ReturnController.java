package com.xyl.mmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.ReturnPackageFacade;
import com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam;
import com.xyl.mmall.mainsite.facade.param._FrontReturnExpInfoParam;
import com.xyl.mmall.mainsite.util.MainsiteVOConvertUtil;
import com.xyl.mmall.mainsite.vo.order.OrderForm2VO;
import com.xyl.mmall.mainsite.vo.order.ReturnApplyVO;
import com.xyl.mmall.mainsite.vo.order.ReturnExpInfoVO;
import com.xyl.mmall.mainsite.vo.order.ReturnPriceVO;
import com.xyl.mmall.mainsite.vo.order.ReturnStatusVO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.service.OrderPackageSimpleService;

/**
 * 退货
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 上午11:05:05
 *
 */
@Controller
@RequestMapping("/_returnorder")
public class ReturnController extends BaseController {

	@Autowired
	private ReturnPackageFacade retFacade;
	
	@Autowired
	private OrderFacade orderFacade;
	
	@Autowired
	private OrderPackageSimpleService ordPkgSimpleService;
	
	/**
	 * 退货列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getRetOrdSkuList(
			Model model, 
			@RequestParam(value="ordPkgId") long ordPkgId
			) {
		appendStaticMethod(model);
		model.addAttribute("data", retFacade.getReturnOrderSkuVOList(ordPkgId));
		return "pages/return/return";
	}
	
	/**
	 * 退全部商品
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.PUT)
	@ResponseBody 
	public ReturnPriceVO returnAllOrdSku(
			Model model, 
			@RequestBody _FrontReturnApplyParam param
			) {
		return retFacade.returnAllOrdSku(param);
	}
	
	/**
	 * 添加退货商品：默认数量
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.PUT)
	@ResponseBody 
	public ReturnPriceVO addRetOrdSkuDefault(
			Model model, 
			@RequestBody _FrontReturnApplyParam param
			) {
		return retFacade.addRetOrdSkuDefault(param);
	}
	
	/**
	 * 添加退货商品：增加选中数量
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/increase", method = RequestMethod.PUT)
	@ResponseBody 
	public ReturnPriceVO increaseRetOrdSku(
			Model model, 
			@RequestBody _FrontReturnApplyParam param
			) {
		return retFacade.increaseRetOrdSku(param);
	}

	/**
	 * 取消单个退货
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody 
	public ReturnPriceVO removeRetOrdSku(
			Model model, 
			@RequestBody _FrontReturnApplyParam param
			) {
		return retFacade.removeRetOrdSku(param);
	}
	
	/**
	 * 添加退货商品：减少选中数量
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/decrease", method = RequestMethod.PUT)
	@ResponseBody 
	public ReturnPriceVO decreaseRetOrdSku(
			Model model, 
			@RequestBody _FrontReturnApplyParam param
			) {
		return retFacade.decreaseRetOrdSku(param);
	}
	
	/**
	 * 提交申请
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@BILog(action = "page", type = "returnGoodsApplyPage")
	@RequestMapping(value = "/applyReturn", method = RequestMethod.PUT)
	@ResponseBody 
	public ReturnApplyVO applyReturn(
			Model model, 
			@RequestBody _FrontReturnApplyParam param
			) {
/** for log: start */
		appendStaticMethod(model);
		// 退款方式：0->原路返回, 1->网易宝, 2->银行卡
		model.addAttribute("refundType", param.getRefundType());
/** for log: end */
		return retFacade.applyReturn(param);
	}
	
	/**
	 * 提交申请：填写地址信息
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@BILog(action = "page", type = "returnGoodsBackPage")
	@RequestMapping(value = "/applyStep2", method = RequestMethod.PUT)
	@ResponseBody 
	public ReturnExpInfoVO expInfoReturn(
			Model model, 
			@RequestBody _FrontReturnExpInfoParam param
			) {
/** for log: start */
		appendStaticMethod(model);
		// 退回类型：1->寄回商品, 2->仅退款, 0->未知错误
		model.addAttribute("orderId", param.getOrderId());
		model.addAttribute("userId", SecurityContextUtils.getUserId());
/** for log: end */		
		return retFacade.completeApplyWithExpInfo(param);
	}
	
	/**
	 * 退货状态
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public String returnClientStatus(
			Model model, 
			@RequestParam(value="ordPkgId") long ordPkgId
			) {
		appendStaticMethod(model);
		ReturnStatusVO vo = retFacade.returnStatus(ordPkgId);
		if(null != vo) {
			vo.setFromMyOrder(true);
		}
		model.addAttribute("data", vo);
		return "pages/return/return";
	}
	
	/**
	 * 我的订单 - 取消退货
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/cancelreturn", method = RequestMethod.GET)
	@ResponseBody 
	public BaseJsonVO cancelReturn(
			Model model, 
			@RequestParam(value="ordPkgId") long ordPkgId
			) {
		BaseJsonVO ret = new BaseJsonVO();
		boolean result = retFacade.cancelReturn(ordPkgId);
		if(result) {
			ret.setCode(200);
			ret.setMessage("successful");
		} else {
			ret.setCode(201);
			ret.setMessage("failed");
		}
		long userId = SecurityContextUtils.getUserId();
		OrderPackageSimpleDTO ordPkg = ordPkgSimpleService.queryOrderPackageSimple(userId, ordPkgId);
		if(null != ordPkg) {
			OrderFormDTO ordForm = orderFacade.queryOrderForm(userId, ordPkg.getOrderId(), null);
			OrderForm2VO order2VO = MainsiteVOConvertUtil.convertToOrderForm2VO(ordForm, retFacade);
			ret.setResult(order2VO);
		}
		return ret;
	}
	
	/**
	 * 我的订单 - 请退货
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@BILog(action = "page", type = "returnGoodsInfoPage")
	@RequestMapping(value = "/completeapply", method = RequestMethod.GET)
	public String completeApply(
			Model model, 
			@RequestParam(value="ordPkgId") long ordPkgId
			) {
		appendStaticMethod(model);
		ReturnApplyVO vo = retFacade.getApply(ordPkgId);
		vo.setFromMyOrder(true);
		model.addAttribute("data", vo);
		return "pages/return/return";
	}
	
	/**
	 * 我的订单 - 退货中
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/query/returning", method = RequestMethod.GET)
	public String queryReturning(
			Model model, 
			@RequestParam(value="ordPkgId") long ordPkgId
			) {
		appendStaticMethod(model);
		ReturnExpInfoVO vo = retFacade.getReturning(ordPkgId);
		vo.setFromMyOrder(true);
		model.addAttribute("data", vo);
		return "pages/return/return";
	}
	
	/**
	 * 我的订单 - 已退货
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@BILog(action = "page", type = "returnGoodsServicePage")
	@RequestMapping(value = "/query/successful", method = RequestMethod.GET)
	public String querySuccessfulReturn(
			Model model, 
			@RequestParam(value="ordPkgId") long ordPkgId
			) {
		appendStaticMethod(model);
		ReturnStatusVO vo = retFacade.returnStatus(ordPkgId);
		vo.setFromMyOrder(true);
		model.addAttribute("data", vo);
/** for log: start */
		// 返回结果：1->成功
		model.addAttribute("status", 1);
/** for log: end */			
		return "pages/return/return";
	}
	
	/**
	 * 我的订单 - 退款失败
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@BILog(action = "page", type = "returnGoodsServicePage")
	@RequestMapping(value = "/query/failed", method = RequestMethod.GET)
	public String queryFailedReturn(
			Model model, 
			@RequestParam(value="ordPkgId") long ordPkgId
			) {
		appendStaticMethod(model);
		ReturnStatusVO vo = retFacade.returnStatus(ordPkgId);
		vo.setFromMyOrder(true);
		model.addAttribute("data", vo);
/** for log: start */
		// 返回结果：2->失败
		model.addAttribute("status", 1);
/** for log: end */	
		return "pages/return/return";
	}
	
}
