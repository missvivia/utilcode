package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
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
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.vo.IdNameBean;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.CmsBrandFacade;
import com.xyl.mmall.cms.facade.ItemSPUFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.vo.ItemSPUVO;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;

/**
 * BrandController.java created by yydx811 at 2015年4月28日 下午7:45:32
 * 商品品牌controller
 *
 * @author yydx811
 */
@Controller
public class BrandController extends BaseController {
	@Resource
	private CmsBrandFacade cmsBrandFacade;
	
	@Resource
	private BusinessFacade businessFacade;
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	@Autowired
	private LocationFacade locationFacade;
	
	@Autowired
	private ItemSPUFacade itemSPUFacade;
	
	private static List<String> indexsList = new ArrayList<String>();
	
	private static String[] charSet = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
										  "J", "K", "L", "M", "N", "O", "P", "Q", "R",
										  "S", "T", "U", "V", "W", "X", "Y", "Z" };
	
	static {
		indexsList.add("ALL");
		indexsList.addAll(Arrays.asList(charSet));
		indexsList.add("OTHER");
	}
	
	// 审核预览
	@RequestMapping(value = "/item/brand/preview", method = RequestMethod.GET)
	@RequiresPermissions(value = { "audit:brand" })
	public @ResponseBody SupplierBrandFullDTO previewBrandFullDTO(@RequestParam long id) {
		// 接受的id是 supplierBrandId
		return cmsBrandFacade.getSupplierBrandFullOnlyShowOnlineShops(id);
	}
	
	@RequestMapping(value = "/item/brand", method = RequestMethod.GET)
	@RequiresPermissions(value = { "item:brand" })
	public String showBrandListPage(Model model) {
		appendStaticMethod(model);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/item/brand";
	}
	
	@RequestMapping(value = "/item/brand/remove", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:brand" })
	public @ResponseBody BaseJsonVO deleteBrand(@RequestBody JSONObject json) {
		long delId = json.getLong("id");
		BaseJsonVO vo = new BaseJsonVO();
		// 判断是否有单品
		ItemSPUVO spuVO = new ItemSPUVO();
		spuVO.setBrandId(delId);
		List<ItemSPUVO> list = itemSPUFacade.getItemSPUList(null, spuVO, null);
		if (!CollectionUtils.isEmpty(list)) {
			vo.setCode(ResponseCode.RES_ERROR);
			vo.setMessage("品牌下存在单品不能删除！");
			return vo;
		}
//		if (businessFacade.existsActiveBusinessByBrand(delId)) {
//			vo.setCode(400);
//			vo.setMessage("品牌关联的供应商没有冻结，拒绝删除品牌!");
//			return vo;
//		}
		if (cmsBrandFacade.delBrand(delId)) {
			vo.setCode(200);
			vo.setMessage("ok");
		} else {
			vo.setCode(400);
			vo.setMessage("delete failed!");
		}
		return vo;
	}
	
	// 这个接口在二期优化中直接根据key来搜索品牌的功能被去掉了
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/business/brand/list", method = RequestMethod.GET)
//	@RequiresPermissions(value = { "business:brand" })
//	public @ResponseBody BaseJsonVO getBrandList(@RequestParam(required = false) String key, 
//			@RequestParam(required = false) int limit, @RequestParam(required = false) int offset) {
//		RetArg retArg = null;
//		DDBParam param = new DDBParam("createDate", false, limit, offset);
//		if (key == null || key.trim().equals("")) {
//			retArg = cmsBrandFacade.getAllBrandItemList(param);
//		} else {
//			retArg = cmsBrandFacade.getSearchedBrandItemList(key, param);
//		}
//		List<BrandItemDTO> brandDTOList = RetArgUtil.get(retArg, ArrayList.class);
//		param = RetArgUtil.get(retArg, DDBParam.class);
//		BaseJsonVO vo = new BaseJsonVO();
//		vo.setCode(200);
//		vo.setMessage("ok");
//		BaseJsonListResultVO resultListVO = new BaseJsonListResultVO(brandDTOList);
//		resultListVO.setTotal(param.getTotalCount());
//		vo.setResult(resultListVO);
//		return vo;
//	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/item/brand/list", method = RequestMethod.GET)
	@RequiresPermissions(value = { "item:brand" })
	public @ResponseBody BaseJsonVO getBrandList(@RequestParam int index, int limit, int offset) {
		BaseJsonVO vo = new BaseJsonVO();
		if (index >= 0 && index <= 27) {
			DDBParam param = new DDBParam("createDate", false, limit, offset);
			RetArg retArg = cmsBrandFacade.getBrandItemListByIndex(indexsList.get(index), param);
			List<BrandItemDTO> brandDTOList = RetArgUtil.get(retArg, ArrayList.class);
			param = RetArgUtil.get(retArg, DDBParam.class);
			vo.setCode(200);
			vo.setMessage("ok");
			BaseJsonListResultVO resultListVO = new BaseJsonListResultVO(brandDTOList);
			resultListVO.setTotal(param.getTotalCount());
			vo.setResult(resultListVO);
		} else {
			vo.setCode(400);
			vo.setMessage("index参数范围不正确");
		}
		return vo;
	}
	
	@RequestMapping(value = "/item/brand/update", method = RequestMethod.PUT)
	@RequiresPermissions(value = { "item:brand" })
	public @ResponseBody BaseJsonVO editBrand(@RequestBody BrandItemDTO dto) {
		String userName = SecurityContextUtils.getUserName();
		return cmsBrandFacade.editBrand(dto, userName);
	}
	
	@RequestMapping(value = "/audit/brand", method = RequestMethod.GET)
	@RequiresPermissions(value = { "audit:brand" })
	public String showAuditSearchPage(Model model) {
		appendStaticMethod(model);
		long userId = SecurityContextUtils.getUserId();
		List<AreaDTO> areaList = cmsBrandFacade.getAreaList(userId, "audit:brand");
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute("provinceList", list);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/audit/brand";
	}
	
	@RequestMapping(value = "/rest/audit/brand/pass", method = RequestMethod.POST)
	@RequiresPermissions(value = { "audit:brand" })
	public @ResponseBody BaseJsonVO passAuditBrand(@RequestBody List<Long> ids) {
		List<Long> failedIds = cmsBrandFacade.passAuditBrand(ids);
		BaseJsonVO vo = new BaseJsonVO();
		if (failedIds.size() > 0) {
			StringBuffer message = new StringBuffer();
			for (Long id : failedIds) {
				message.append(" " + id);
			}
			vo.setCode(400);
			vo.setMessage(message.toString());
			vo.setResult(false);
		} else {
			vo.setCode(200);
			vo.setMessage("ok");
			vo.setResult(true);
		}
		return vo;
	}
	
	@RequestMapping(value = "/rest/audit/brand/reject", method = RequestMethod.POST)
	@RequiresPermissions(value = { "audit:brand" })
	public @ResponseBody BaseJsonVO rejectAuditBrand(@RequestBody JSONObject paramJson) {
		String ids = paramJson.getString("ids");
		String reason = paramJson.getString("reason");
		String[] idsSplit = ids.substring(1, ids.lastIndexOf("]")).split(",");
		List<Long> dataList = new ArrayList<>();
		for (String string : idsSplit) {
			dataList.add(Long.parseLong(string));
		}
		List<Long> failedIds = cmsBrandFacade.rejectAuditBrand(dataList, reason);
		BaseJsonVO vo = new BaseJsonVO();
		if (failedIds.size() > 0) {
			StringBuffer message = new StringBuffer();
			for (Long id : failedIds) {
				message.append(" " + id);
			}
			vo.setCode(400);
			vo.setMessage(message.toString());
			vo.setResult(false);
		} else {
			vo.setCode(200);
			vo.setMessage("ok");
			vo.setResult(true);
		}
		return vo;
	}
	
	// 这个接口可以优化
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/audit/brand/search", method = RequestMethod.GET)
	@RequiresPermissions(value = { "audit:brand" })
	public @ResponseBody BaseJsonVO searchAuditBrand(@RequestParam long curSupplierAreaId, @RequestParam int status,
		@RequestParam(required = false) String brandName, @RequestParam(required = false) int limit, 
		@RequestParam(required = false) int offset, @RequestParam(required = false) boolean total) {
		List<BusinessDTO> businesses = new ArrayList<>();
		long userId = SecurityContextUtils.getUserId();
		List<AreaDTO> areaList = cmsBrandFacade.getAreaList(userId, "audit:brand");
		// areaIdList是cms人员的权限范围
		List<Long> areaIdList = new ArrayList<Long>(areaList.size());
		for (AreaDTO dto : areaList) {
			areaIdList.add(dto.getId());
		}
		//  需要支持多个区域
		if (curSupplierAreaId <= 0) {
			businesses = businessFacade.getBusinessDTOListByAreaIdListAndAreaId(areaIdList, 0);
			// businesses = businessFacade.getBusinessListByAreaIdList(areaIdList);
		} else {
			businesses = businessFacade.getBusinessDTOListByAreaIdListAndAreaId(areaIdList, curSupplierAreaId);
			// businesses = businessFacade.getBusinessListByAreaId(curSupplierAreaId);
		}
		// 获得供应商id列表（需要判断商家是否被冻结）
		Set<Long> idSet = new HashSet<>();
		for (BusinessDTO businessDTO : businesses) {
			if (businessDTO.getIsActive() == 0) {
				idSet.add(businessDTO.getId());
			}
		}
		List<Long> idList = new ArrayList<>(idSet);
//		List<AreaDTO> areaDTOs = businessFacade.getAreadByIdList(idList);
//		Map<Long, String> retMap = new HashMap<Long, String>();
//		for (AreaDTO dto : areaDTOs) {
//			retMap.put(dto.getId(), dto.getAreaName());
//		}
		DDBParam param = new DDBParam("statusUpdateDate", false, limit, offset);
		RetArg retArg = cmsBrandFacade.searchAuditBrand(idList, brandName, status, param);
		param = RetArgUtil.get(retArg, DDBParam.class);
		List<SupplierBrandDTO> ret = RetArgUtil.get(retArg, ArrayList.class);
		// 地区控制，以前是单个的，现在是多个
		List<Long> idResList = new ArrayList<>();
		for (SupplierBrandDTO dto : ret) {
			idResList.add(dto.getSupplierId());
		}
		List<BusinessDTO> bDTOs = new ArrayList<>();
		if (idResList.size() != 0) {
			bDTOs = businessFacade.getBusinessDTOByIdList(idResList);
		}
		Map<Long, BusinessDTO> dataMap = new HashMap<>();
		for (BusinessDTO dto : bDTOs) {
			dataMap.put(dto.getId(), dto);
		}
		for (SupplierBrandDTO dto : ret) {
			BusinessDTO bDto = dataMap.get(dto.getSupplierId());
			dto.setAreaNames(bDto != null ? bDto.getAreaNames() : null);
		}
		BaseJsonVO vo = new BaseJsonVO();
		vo.setCode(200);
		vo.setMessage("ok");
		BaseJsonListResultVO resultListVO = new BaseJsonListResultVO(ret);
		resultListVO.setTotal(param.getTotalCount());
		vo.setResult(resultListVO);
		return vo;
	}
}
