/**
 * 
 */
package com.xyl.mmall.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.common.constant.CalendarConst;
import com.netease.print.exceljar.ExcelExportUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.vo.PickSkuVo;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.UserInfoFacade;
import com.xyl.mmall.framework.util.FrameworkExcelUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.oms.dto.PickSkuDTO;
import com.xyl.mmall.oms.dto.ShipOrderDTO;
import com.xyl.mmall.oms.meta.ShipOrderForm;

/**
 * 用户信息相关。
 * 
 * @author lihui
 *
 */
@Controller
@RequestMapping(value = "/user")
public class UserInfoController {

	@Autowired
	private UserInfoFacade userFacade;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private JITSupplyManagerFacade facade;

	@Autowired
	private BusinessFacade businessFacade;

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@RequiresPermissions(value = { "user:query" })
	public String getUserList(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/user/user";
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	@RequiresPermissions(value = { "user:query" })
	public String getUserInfo(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/user/info";
	}

	/**
	 * 商家拣货单查询
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/shiporder", method = RequestMethod.GET)
	@RequiresPermissions(value = { "user:shiporder" })
	public String shiporder(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/user/shiporder";
	}

	@RequestMapping(value = "/shiporder/list", method = RequestMethod.GET)
	@RequiresPermissions(value = { "user:shiporder" })
	public String shiporderList(@RequestParam(value = "startTime", required = true) String startTime,
			@RequestParam(value = "endTime", required = true) String endTime, Model model) throws ParseException {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

		long startTime1 = sd.parse(startTime).getTime();
		long endTime1 = sd.parse(endTime).getTime() + CalendarConst.DAY_TIME - 1L;
		//
		List<ShipOrderForm> shipOrderFormList = this.facade.queryShipOrderFormByTime(startTime1, endTime1);
		List<ShipOrderDTO> shipDTOList = new ArrayList<ShipOrderDTO>();
		for (ShipOrderForm shipOrderForm : shipOrderFormList) {
			ShipOrderDTO dto = new ShipOrderDTO(shipOrderForm);
			BusinessDTO businessDTO = getBusinessDTO(shipOrderForm.getSupplierId());
			dto.setAccount(businessDTO.getBusinessAccount());
			dto.setBrandName(businessDTO.getActingBrandName());
			shipDTOList.add(dto);
		}

		Comparator<ShipOrderDTO> comp = new Comparator<ShipOrderDTO>() {
			@Override
			public int compare(ShipOrderDTO o1, ShipOrderDTO o2) {
				if (o1.getSupplierId() != o2.getSupplierId())
					return o1.getSupplierId() > o2.getSupplierId() ? 1 : -1;
				else
					return o1.getCreateTime() < o2.getCreateTime() ? 1 : -1;
			}
		};
		Collections.sort(shipDTOList, comp);

		model.addAttribute("ships", shipDTOList);
		return "pages/user/shiporderList";
	}

	/**
	 * 导出某个拣货单
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/shiporder/export/{supplierId}/{pkId}" })
	public void exportPK(HttpServletResponse response, HttpServletRequest request, @PathVariable String pkId,
			@PathVariable long supplierId, Model model) {

		File f = new File(pkId + ".xlsx");

		LinkedHashMap<String, String> pickOrderSkuExcel = new LinkedHashMap<String, String>();
		pickOrderSkuExcel.put("pickOrderId", "拣货单号");
		pickOrderSkuExcel.put("mode", "类别");
		pickOrderSkuExcel.put("brandName", "品牌名称");
		pickOrderSkuExcel.put("warehouse", "入库仓库");
		pickOrderSkuExcel.put("exportTime", "导出时间");
		pickOrderSkuExcel.put("codeNO", "条形码");
		pickOrderSkuExcel.put("productName", "商品名称");
		pickOrderSkuExcel.put("size", "尺码");
		pickOrderSkuExcel.put("color", "颜色");
		pickOrderSkuExcel.put("skuQuantity", "数量");
		pickOrderSkuExcel.put("poOrderId", "PO单编号");

		ExcelExportUtil util = new ExcelExportUtil();
		util.initExcelExportUtil("拣货单详情", pickOrderSkuExcel, PickSkuVo.class);
		List<PickSkuDTO> tradList = facade.getPickSkuInfo(pkId, supplierId);
		util.write("拣货单详情", tradList);
		util.close(f.getAbsolutePath());
		FrameworkExcelUtil.writeExcel("拣货单详情", pickOrderSkuExcel, PickSkuDTO.class, f.getName(), tradList, request,
				response);
	}

	private Map<Long, BusinessDTO> mapCache = new HashMap<Long, BusinessDTO>();

	private BusinessDTO getBusinessDTO(long supplierId) {
		if (!mapCache.containsKey(supplierId)) {
			BusinessDTO dto = businessFacade.getBusinessById(supplierId);
			if (dto != null) {
				mapCache.put(supplierId, dto);
			}
		}
		return mapCache.get(supplierId);
	}

	/**
	 * 根据查询条件分页获取用户列表。
	 * 
	 * @param type
	 *            查询条件类型
	 * @param search
	 *            查询条件值
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return
	 */
	@RequestMapping(value = "/userlist", method = RequestMethod.GET)
	@RequiresPermissions(value = { "user:query" })
	public @ResponseBody BaseJsonVO getUserList(@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "search", required = false) String search, @RequestParam("limit") int limit,
			@RequestParam("offset") int offset) {
		return userFacade.getUserList(type, search, limit, offset);
	}

	/**
	 * 获取指定用户的详细信息。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户的详细信息
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@RequiresPermissions(value = { "user:query" })
	public @ResponseBody BaseJsonVO getUserInfo(@RequestParam("userId") long userId) {
		return userFacade.getUserInfo(userId);
	}

	/**
	 * 获取指定用户的详细信息。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户的详细信息
	 */
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@RequiresPermissions(value = { "user:query" })
	public @ResponseBody BaseJsonVO getUserInfo(@RequestParam("userId") long userId, @RequestParam("type") String type,
			@RequestParam("value") String value) {
		return userFacade.updateUserBindMobileEmail(userId, type, value);
	}

	/**
	 * 获取指定用户的收获地址。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户的收获地址信息
	 */
	@RequestMapping(value = "/consigneeAddress", method = RequestMethod.GET)
	@RequiresPermissions(value = { "user:query" })
	public @ResponseBody BaseJsonVO getUserConsigneeAddress(@RequestParam("userId") long userId) {
		return userFacade.getUserConsigneeAddress(userId);
	}

	/**
	 * 获取指定用户的优惠券。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户的优惠券信息
	 */
	@RequestMapping(value = "/coupon", method = RequestMethod.GET)
	@RequiresPermissions(value = { "user:query" })
	public @ResponseBody BaseJsonVO getUserCoupon(@RequestParam("userId") long userId,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset) {
		return userFacade.getUserCoupon(userId, limit == null ? 500 : limit, offset == null ? 0 : offset);
	}

	/**
	 * 获取指定用户的订单信息。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户的订单信息
	 */
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	@RequiresPermissions(value = { "user:query" })
	public @ResponseBody BaseJsonVO getUserOrder(@RequestParam("userId") long userId,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset) {
		return userFacade.getUserOrder(userId, limit == null ? 500 : limit, offset == null ? 0 : offset);
	}

	/**
	 * 获取指定用户的红包。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户的红包信息
	 */
	@RequestMapping(value = "/red", method = RequestMethod.GET)
	@RequiresPermissions(value = { "user:query" })
	public @ResponseBody BaseJsonVO getUserRedPacket(@RequestParam("userId") long userId,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset) {
		return userFacade.getUserRedPacket(userId, limit == null ? 500 : limit, offset == null ? 0 : offset);
	}

}
