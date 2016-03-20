package com.xyl.mmall.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.OrderBlacklistManageFacade;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.BlacklistQueryCategoryListVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.BlacklistQueryCategoryListVO.BlacklistSearchType;
import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * 黑名单管理：黑名单查询
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 上午11:05:05
 *
 */
@Controller
@RequestMapping("/blacklist")
public class OrderManageBlacklistController extends BaseController {

	@Autowired
	private OrderBlacklistManageFacade blacklistManageFacade;
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	/**
	 * 黑名单查询：类型列表
	 * @return
	 */
	@RequestMapping(value = {"/query"}, method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:query" })
	public String getTypeList(Model model) {
		appendStaticMethod(model);
		BlacklistQueryCategoryListVO vo = blacklistManageFacade.getBlacklistQueryTypeList();
		model.addAttribute("data", vo);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/order/query";
	}
	
	/**
	 * 用户黑名单查询：按用户信息查询
	 * @return
	 */
	@RequestMapping(value = "/query/user", method = RequestMethod.GET)
	@ResponseBody 
	public BaseJsonVO queryUserBlacklist(
			Model model, 
			@RequestParam(value="type") int typeId, 
			@RequestParam(value="key") String value, 
			@RequestParam(value="limit") int limit, 
			@RequestParam(value="offset") int offset
			) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(200);
		ret.setMessage("successful");
		BlacklistSearchType bt = BlacklistSearchType.USER_ID.genEnumByIntValue(typeId);
		if(null == bt) {
			ret.setCode(201);
			ret.setMessage("illegal type " + typeId);
			return ret;
		}
		ret.setResult(blacklistManageFacade.queryUserBlacklist(bt, value, limit, offset));
		return ret;
	}
	
	/**
	 * 用户黑名单查询：按用户信息查询
	 * @return
	 */
	@RequestMapping(value = "/query/address", method = RequestMethod.GET)
	@ResponseBody 
	public BaseJsonVO queryAddressBlacklist(
			Model model, 
			@RequestParam(value="type") int typeId, 
			@RequestParam(value="key") String value, 
			@RequestParam(value="limit") int limit, 
			@RequestParam(value="offset") int offset
			) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(200);
		ret.setMessage("successful");
		BlacklistSearchType bt = BlacklistSearchType.USER_ID.genEnumByIntValue(typeId);
		if(null == bt) {
			ret.setCode(201);
			ret.setMessage("illegal type " + typeId);
			return ret;
		}
		ret.setResult(blacklistManageFacade.queryAddressBlackList(bt, value, limit, offset));
		return ret;
	}
	
	/**
	 * 移出用户黑名单
	 * 
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/remove/user", method = RequestMethod.GET)
	@ResponseBody 
	public BaseJsonVO removeBlackUser(
			Model model, 
			@RequestParam(value="userId") long userId
			) {
		BaseJsonVO ret = new BaseJsonVO();
		boolean result = blacklistManageFacade.removeFromBlacklistUser(userId);
		if(result) {
			ret.setCode(200);
			ret.setMessage("successful");
		} else {
			ret.setCode(201);
			ret.setMessage("failed");
		}
		ret.setResult(result);
		return ret;
	}
	
	/**
	 * 移出地址黑名单
	 * 
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/remove/address", method = RequestMethod.GET)
	@ResponseBody 
	public BaseJsonVO removeBlackAddress(
			Model model, 
			@RequestParam(value="id") long id, 
			@RequestParam(value="userId") long userId
			) {
		BaseJsonVO ret = new BaseJsonVO();
		boolean result = blacklistManageFacade.removeFromBlacklistAddress(id, userId);
		if(result) {
			ret.setCode(200);
			ret.setMessage("successful");
		} else {
			ret.setCode(201);
			ret.setMessage("failed");
		}
		ret.setResult(result);
		return ret;
	}
	
}
