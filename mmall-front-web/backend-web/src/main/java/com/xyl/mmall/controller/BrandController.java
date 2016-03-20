package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.facade.ItemCenterFacade;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;

@Controller
@RequestMapping("/brand")
public class BrandController extends BaseController {
	@Resource
	private BrandFacade brandFacade;
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	@Autowired
	private ItemCenterFacade itemCenterFacade;
	
	@Autowired
	private BusinessFacade businessFacade;
	
	@Autowired
	private LocationFacade locationFacade;
	
	@RequestMapping(value ={"/display", "/"}, method = RequestMethod.GET)
	@RequiresPermissions(value = { "brand:display" })
	 public String brandDisplay(Model model){
		model.addAttribute("name", "aaa");
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/brand/display";
	}

	@RequestMapping(value ="/create", method = RequestMethod.GET)
	@RequiresPermissions(value = { "brand:display" })
	public String brandCreate(Model model){
		appendStaticMethod(model);
		model.addAttribute("name", "aaa");
		long userId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(userId);
		BusinessDTO supplier = businessFacade.getBusinessById(supplierId);
		model.addAttribute("logo", brandFacade.getBrandUrlByBrandId(supplier.getActingBrandId()));
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/brand/create";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody BaseJsonVO displaySupplierBrandList(@RequestParam int limit, @RequestParam int offset) {
		long userId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(userId);
		return brandFacade.getSupplierBrandlist(supplierId, limit, offset);
	}
	
	@RequestMapping(value = "/copy", method = RequestMethod.POST)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody SupplierBrandDTO copySupplierBrand(HttpServletRequest request) {
		long id = Long.parseLong(request.getParameter("id"));
		return brandFacade.copySupplierBrand(id);
	}
	
	@RequestMapping(value = "/online", method = RequestMethod.POST)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody SupplierBrandDTO onlineSupplierBrand(HttpServletRequest request) {
		long id = Long.parseLong(request.getParameter("id"));
		return brandFacade.onlineSupplierBrand(id);
	}
	
	@RequestMapping(value = "/offline", method = RequestMethod.POST)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody SupplierBrandDTO offlineSupplierBrand(HttpServletRequest request) {
		long id = Long.parseLong(request.getParameter("id"));
		return brandFacade.offlineSupplierBrand(id);
	}
	
	@RequestMapping(value = "/audit", method = RequestMethod.POST)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody SupplierBrandDTO submitAuditSupplierBrand(HttpServletRequest request) {
		long id = Long.parseLong(request.getParameter("id"));
		return brandFacade.submitAuditSupplierBrand(id);
	}
	
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody boolean delSupplierBrand(HttpServletRequest request) {
		long id = Long.parseLong(request.getParameter("id"));
		return brandFacade.delSupplierBrand(id);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.PUT)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody SupplierBrandFullDTO addNewSupplierBrand(@RequestBody SupplierBrandFullDTO supplierBrandFullDTO) {
		String userName = SecurityContextUtils.getUserName();
		long userId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(userId);
		BusinessDTO supplierDTO = businessFacade.getBusinessById(supplierId);
		// long areaId = supplier.getAreaId();
		// 地区以前只有一个，现在有可能有多个
		List<Long> areaIds = supplierDTO.getAreaIds();
		long fmt;
		try {
			fmt = ProvinceCodeMapUtil.getProvinceFmtByCodeList(areaIds);
		} catch (Exception e) {
			return null;
		}
		return brandFacade.addNewSupplierBrand(supplierBrandFullDTO, supplierDTO.getActingBrandId(), 
				supplierId, userName, fmt);
	}
	
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody SupplierBrandFullDTO getSupplierBrandFull(@RequestParam long id) {
		return brandFacade.getSupplierBrandFull(id);
	}
	
	@RequestMapping(value ="/edit", method = RequestMethod.GET)
	@RequiresPermissions(value = { "brand:display" })
	public String brandEdit(Model model, @RequestParam long id){
		appendStaticMethod(model);
		model.addAttribute("name", "aaa");
		model.addAttribute("data", brandFacade.getSupplierBrandFull(id));
        model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/brand/edit";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.PUT)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody SupplierBrandDTO saveSupplierBrand(@RequestBody SupplierBrandDTO dto) {
		return brandFacade.saveSupplierBrand(dto);
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.PUT)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody SupplierBrandDTO submitSupplierBrand(@RequestBody SupplierBrandDTO dto) {
		return brandFacade.submitSupplierBrand(dto);
	}
	
	// 确定当前门店的所在的省份
	@RequestMapping(value = "/shop/province", method = RequestMethod.GET)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody JSONObject getCurrentBrandShopProvince() {
		// String userName = SecurityContextUtils.getUserName();
		long userId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(userId);
		// Business supplier = businessFacade.getBusinessById(supplierId);
		BusinessDTO businessDTO = businessFacade.getBusinessById(supplierId);
		List<Map<String, Object>> prolist = new ArrayList<Map<String, Object>>();
		List<Long> areaIds = businessDTO.getAreaIds();
		List<String> nameList = businessDTO.getAreaNames();
		for (int idx = 0; idx < areaIds.size(); idx++) {
			Map<String, Object> areaDetailsMap = new HashMap<>();
			areaDetailsMap.put("id", areaIds.get(idx));
			areaDetailsMap.put("name", nameList.get(idx));
			prolist.add(areaDetailsMap);	
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("shopProvinces", prolist);
		return jsonObject;
	}
	
	@RequestMapping(value = "/shop/add", method = RequestMethod.PUT)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody BrandShopDTO addNewBrandShop(@RequestBody BrandShopDTO brandShop) {
		return brandFacade.addNewBrandShop(brandShop);
	}
	
	@RequestMapping(value = "/shop/active", method = RequestMethod.POST)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody BrandShopDTO activeBrandShop(@RequestParam long id) {
		return brandFacade.activeBrandShop(id);
	}
	
	@RequestMapping(value = "/shop/stop", method = RequestMethod.POST)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody BrandShopDTO stopBrandShop(@RequestParam long id) {
		return brandFacade.stopBrandShop(id);
	}
	
	@RequestMapping(value = "/shop/edit", method = RequestMethod.POST)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody BrandShopDTO editBrandShop(@RequestBody BrandShopDTO brandShop) {
		return brandFacade.editBrandShop(brandShop);
	}
	
	@RequestMapping(value = "/shop/del", method = RequestMethod.POST)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody boolean delBrandShop(@RequestParam long id) {
		return brandFacade.delBrandShop(id);
	}
	
	@RequestMapping(value = "/preview", method = RequestMethod.GET)
	@RequiresPermissions(value = { "brand:display" })
	public @ResponseBody SupplierBrandFullDTO previewBrandFullDTO(@RequestParam long id) {
		// 接受的id是 supplierBrandId
		return brandFacade.getSupplierBrandFullOnlyShowOnlineShops(id);
	}

}
