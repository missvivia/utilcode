package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.dto.SendDistrictDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.CategoryFacade;
import com.xyl.mmall.cms.facade.ItemModelFacade;
import com.xyl.mmall.cms.facade.ItemSPUFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.SiteCMSFacade;
import com.xyl.mmall.cms.facade.util.DistrictCodeUtil;
import com.xyl.mmall.cms.vo.CategoryContentVO;
import com.xyl.mmall.cms.vo.CategoryNormalVO;
import com.xyl.mmall.cms.vo.ItemModelVO;
import com.xyl.mmall.cms.vo.ItemSPUVO;
import com.xyl.mmall.cms.vo.SiteAreaVO;
import com.xyl.mmall.cms.vo.SiteCMSVO;
import com.xyl.mmall.common.enums.CategoryErrorMsgEnum;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.content.dto.SearchCategoryContentDTO;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.AreaCodeUtil;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.itemcenter.dto.CategoryNormalDTO;
import com.xyl.mmall.itemcenter.enums.CategoryNormalLevel;
import com.xyl.mmall.member.service.AgentService;

/**
 * CategoryController.java created by yydx811 at 2015年4月27日 上午9:38:18
 * 分类controller
 *
 * @author yydx811
 */
@Controller
@RequestMapping(value = "/category")
public class CategoryController {

	private static Logger logger = Logger.getLogger(CategoryController.class);
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	@Autowired
	private CategoryFacade categoryFacade;
	
	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private AgentService agentService;
	
	@Autowired
	private ItemModelFacade itemModelFacade;
	
	@Autowired
	private ItemSPUFacade itemSPUFacade;
	
	@Autowired
	private SiteCMSFacade siteCMSFacade;
	
	@Autowired
	private LocationService locationService;
	
	@RequestMapping(value = "/normal")
	@RequiresPermissions(value = { "category:normal" })
	public String normal(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/category/normal";
	}
	
	@RequestMapping(value = "/normal/firstList")
	@RequiresPermissions(value = { "category:normal" })
	public @ResponseBody BaseJsonVO normalFirstList(BasePageParamVO<CategoryNormalVO> basePageParamVO) {
		BaseJsonVO resJsonVO = new BaseJsonVO();
		resJsonVO.setCode(ResponseCode.RES_SUCCESS);
		basePageParamVO.setList(categoryFacade.getFirstCategoryNormalList(basePageParamVO));
		resJsonVO.setResult(basePageParamVO);
		return resJsonVO;
	}

	@RequestMapping(value = "/normal/subList")
	@RequiresPermissions(value = { "category:normal" })
	public @ResponseBody BaseJsonVO normalSubList(@RequestParam(required = true, value = "parentId") long superCategoryId) {
		BaseJsonVO resJsonVO = new BaseJsonVO();
		resJsonVO.setCode(ResponseCode.RES_SUCCESS);
		resJsonVO.setResult(categoryFacade.getSubCategoryNormalList(superCategoryId));
		return resJsonVO;
	}

	@RequestMapping(value = "/normal/list")
	@RequiresPermissions(value = { "category:normal", "category:content" }, logical = Logical.OR)
	public @ResponseBody BaseJsonVO normalList(BasePageParamVO<CategoryNormalVO> basePageParamVO) {
		BaseJsonVO resJsonVO = new BaseJsonVO();
		resJsonVO.setCode(ResponseCode.RES_SUCCESS);
		basePageParamVO.setList(categoryFacade.getCategoryNormalList(basePageParamVO));
		resJsonVO.setResult(basePageParamVO);
		return resJsonVO;
	}

	@RequestMapping(value = "/normal/create", method = RequestMethod.POST)
	@RequiresPermissions(value = { "category:normal" })
	public @ResponseBody BaseJsonVO normalCreate(@RequestBody CategoryNormalVO categoryNormalVO) {
		BaseJsonVO resJsonVO = new BaseJsonVO();
		if (StringUtils.isBlank(categoryNormalVO.getCategoryName())) {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "分类名不能为空！");
			return resJsonVO;
		}
		categoryNormalVO.setCategoryName(categoryNormalVO.getCategoryName().trim());
		CategoryNormalDTO categoryNormalDTO = categoryNormalVO.convertToDTO();
		categoryNormalDTO.setAgentId(SecurityContextUtils.getUserId());
		long id = categoryFacade.createCategoryNormal(categoryNormalDTO);
		if (id > 0) {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_SUCCESS, "创建成功！");
		} else if (id == -1) {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "上级商品分类未找到！");
		} else if (id == -2) {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, "所选上级商品分类为第三级不能添加子分类！");
		} else {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "未知错误！");
		}
		return resJsonVO;
	}

	@RequestMapping(value = "/normal/edit")
	@RequiresPermissions(value = { "category:normal" })
	public void normalEdit(@RequestParam(required = true, value = "categoryId") long id, Model model) {
		CategoryNormalVO categoryNormalVO = categoryFacade.getCategoryNormalById(id, false);
		if (categoryNormalVO == null || categoryNormalVO.getCategoryId() < 1l) {
			model.addAttribute("code", ResponseCode.RES_ENOTEXIST);
			model.addAttribute("result", null);
			model.addAttribute("message", "商品分类不存在！");
		} else {
			model.addAttribute("code", ResponseCode.RES_SUCCESS);
			model.addAttribute("result", categoryNormalVO);
		}
	}
	
	@RequestMapping(value = "/normal/update", method = RequestMethod.POST)
	@RequiresPermissions(value = { "category:normal" })
	public @ResponseBody BaseJsonVO normalUpdate(@RequestBody CategoryNormalVO categoryNormalVO) {
		BaseJsonVO resJsonVO = new BaseJsonVO();
		CategoryNormalVO old = categoryFacade.getCategoryNormalById(categoryNormalVO.getCategoryId(), true);
		if (old == null || old.getCategoryId() < 1l) {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "商品分类不存在！");
			return resJsonVO;
		}
		if (categoryNormalVO.getParentId() > 0l && categoryNormalVO.getParentId() != old.getParentId()) {
			if (old.getCategoryDepth() == CategoryNormalLevel.LEVEL_FIRST.getIntValue()) {
				resJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, "一级分类无法更换上级分类！");
				return resJsonVO;
			}
			if (!CollectionUtils.isEmpty(old.getSubCategoryList())) {
				resJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, "该分类下有子分类！");
				return resJsonVO;
			}
			// 单品 模型判断
			if (old.getCategoryDepth() == CategoryNormalLevel.LEVEL_SECOND.getIntValue()) {
				if (!CollectionUtils.isEmpty(old.getSubCategoryList())) {
					for (CategoryNormalVO c : old.getSubCategoryList()) {
						ItemModelVO modelVO = itemModelFacade.getItemModel(c.getCategoryId(), false);
						if (modelVO != null) {
							resJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, "当前分类子分类下包含商品模型，无法更换上级分类！");
							return resJsonVO;
						}
						ItemSPUVO spuVO = new ItemSPUVO();
						spuVO.setCategoryNormalId(c.getCategoryId());
						List<ItemSPUVO> list = itemSPUFacade.getItemSPUList(null, spuVO, null);
						if (!CollectionUtils.isEmpty(list)) {
							resJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, "当前分类子分类下包含单品，无法更换上级分类！");
							return resJsonVO;
						}
					}
				}
			}
			if (old.getCategoryDepth() == CategoryNormalLevel.LEVEL_THIRD.getIntValue()) {
				ItemModelVO modelVO = itemModelFacade.getItemModel(categoryNormalVO.getCategoryId(), false);
				if (modelVO != null) {
					resJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, "当前分类下包含商品模型，无法更换上级分类！");
					return resJsonVO;
				}
				ItemSPUVO spuVO = new ItemSPUVO();
				spuVO.setCategoryNormalId(categoryNormalVO.getCategoryId());
				List<ItemSPUVO> list = itemSPUFacade.getItemSPUList(null, spuVO, null);
				if (!CollectionUtils.isEmpty(list)) {
					resJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, "当前分类子分类下包含单品，无法更新！");
					return resJsonVO;
				}
			}
		}
		// 判断更新分类名称
		if (StringUtils.isNotBlank(categoryNormalVO.getCategoryName())) {
			categoryNormalVO.setCategoryName(categoryNormalVO.getCategoryName().trim());
			if (StringUtils.equals(categoryNormalVO.getCategoryName(), old.getCategoryName())) {
				categoryNormalVO.setCategoryName("");
			}
		}
		CategoryNormalDTO categoryNormalDTO = categoryNormalVO.convertToDTO();
		categoryNormalDTO.setAgentId(SecurityContextUtils.getUserId());
		if (categoryFacade.updateCategoryNormal(categoryNormalDTO) > 0) {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_SUCCESS, "更新成功！");
		} else {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "未知错误！");
		}
		return resJsonVO;
	}

	@RequestMapping(value = "/normal/sort")
	@RequiresPermissions(value = { "category:normal" })
	public @ResponseBody BaseJsonVO normalSort(@RequestParam(required = true, value = "categoryId") long id, 
			@RequestParam(required = true, value = "isUp") int isUp) {
		BaseJsonVO resJsonVO = new BaseJsonVO();
		CategoryNormalVO old = categoryFacade.getCategoryNormalById(id, false);
		if (old == null || old.getCategoryId() < 1l) {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "商品分类不存在！");
			return resJsonVO;
		}
		if (old.getCategoryIndex() == 1 && isUp == 1) {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_EBATCH_NO_CHANGE, "已经移到顶部！");
			return resJsonVO;
		}
		CategoryNormalDTO categoryNormalDTO = old.convertToDTO();
		if (categoryFacade.getMaxShowIndex(categoryNormalDTO) == old.getCategoryIndex() && isUp != 1) {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_EBATCH_NO_CHANGE, "已经移到底部！");
			return resJsonVO;
		}
		categoryNormalDTO.setAgentId(SecurityContextUtils.getUserId());

		if (categoryFacade.updateCategoryNormalSort(categoryNormalDTO, isUp) > 0) {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_SUCCESS, "更新成功！");
		} else {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "未知错误！");
		}
		return resJsonVO;
	}

	@RequestMapping(value = "/normal/delete")
	@RequiresPermissions(value = { "category:normal" })
	public @ResponseBody BaseJsonVO normalDelete(@RequestParam(required = true, value = "categoryId") long id) {
		BaseJsonVO resJsonVO = new BaseJsonVO();
		CategoryNormalVO old = categoryFacade.getCategoryNormalById(id, true);
		if (old == null || old.getCategoryId() < 1l) {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_SUCCESS, "删除成功!");
			return resJsonVO;
		}
		// 判断是否有子分类
		if (!CollectionUtils.isEmpty(old.getSubCategoryList())) {
			resJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, "“" + old.getCategoryName() + "”下包含子分类，无法删除\n请先删除子分类");
			return resJsonVO;
		}
		// 判断是否是第三级分类
		if (old.getCategoryDepth() == CategoryNormalLevel.LEVEL_THIRD.getIntValue()) {
			// 判断是否有 单品 模型等
			ItemModelVO modelVO = itemModelFacade.getItemModel(id, false);
			if (modelVO != null) {
				resJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, "“" + old.getCategoryName() + "”下包含商品模型，无法删除！");
				return resJsonVO;
			}
			ItemSPUVO spuVO = new ItemSPUVO();
			spuVO.setCategoryNormalId(id);
			List<ItemSPUVO> list = itemSPUFacade.getItemSPUList(null, spuVO, null);
			if (!CollectionUtils.isEmpty(list)) {
				resJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, "“" + old.getCategoryName() + "”下包含单品，无法删除！");
				return resJsonVO;
			}
			String result = categoryFacade.isContainNormalCategory(id);
			if (result != null) {
				resJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, "商品分类已绑定内容分类“" + result + "”，无法删除！");
				return resJsonVO;
			}
		}
		categoryFacade.deleteCategoryNormal(id);
		resJsonVO.setCodeAndMessage(ResponseCode.RES_SUCCESS, "删除成功!");
		return resJsonVO;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/content")
	public String content(Model model) {
		long userId = SecurityContextUtils.getUserId();
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(userId));
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(userId);
		if(retArg ==null){
			model.addAttribute("username", SecurityContextUtils.getUserName());//username 403页面显示有用到
			return "error/403";//没权限
		}
		Set<Long>areaList = RetArgUtil.get(retArg, HashSet.class);
		if(RetArgUtil.get(retArg, Boolean.class)){
			model.addAttribute("siteCMSVO",null);
		}else{
			SiteCMSVO siteCMSVO = siteCMSFacade.getSiteCMS(RetArgUtil.get(retArg, Long.class), true);
			for (SiteAreaVO siteAreaVO : siteCMSVO.getAreaList()) {
				if (areaList.contains(siteAreaVO.getAreaId())) {
					siteAreaVO.setIsChecked(1);
				}
			}
			model.addAttribute("siteCMSVO",siteCMSVO);
		}
		return "pages/category/content";
	}
	
	@RequestMapping(value = "/content/createPage")
	public String createContentPage(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/category/createContent";
	}
	
	@RequestMapping(value = "/content/edit")
	public String editContent(@RequestParam(required = true, value = "contentCategoryId") long id, Model model) {
		long userId = SecurityContextUtils.getUserId();
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(userId));
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(userId);
		if(retArg ==null){
			model.addAttribute("username", SecurityContextUtils.getUserName());//username 403页面显示有用到
			return "error/403";//没权限
		}
		CategoryContentVO categoryContentVO = categoryFacade.getCategoryContentById(id);
		if(categoryContentVO.getLevel()!=3){
			categoryContentVO.setCategoryNormalVOs(null);
		}
		model.addAttribute("result",categoryContentVO);
		return "pages/category/createContent";//创建和编辑同用一个页面
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/content/editContentTree")
	public String editContentTree(@RequestParam(required = true, value = "rootId") long rootId, Model model) {
		long userId = SecurityContextUtils.getUserId();
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(userId));
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(userId);
		if(retArg ==null){
			model.addAttribute("username", SecurityContextUtils.getUserName());//username 403页面显示有用到
			return "error/403";//没权限
		}
		CategoryContentVO categoryContentVO = categoryFacade.getCategoryContentById(rootId);
		boolean isRoot = RetArgUtil.get(retArg, Boolean.class);
		Set<Long>areaSet = RetArgUtil.get(retArg, HashSet.class);
		
		if(categoryContentVO!=null&&CollectionUtil.isNotEmptyOfList(categoryContentVO.getSendDistrictDTOs())){
			Set<Long>codeSet = new HashSet<Long>();
			//针对直辖市
			Set<Long>dirctcodeSet = new HashSet<Long>();
			for(SendDistrictDTO sendDistrictDTO:categoryContentVO.getSendDistrictDTOs()){
				boolean isEdit = false;
				//区域权限设置
				if(isRoot||sendDistrictDTO.getProvinceId()==0){
					sendDistrictDTO.setEdit(true);
					isEdit = true;
				}
				//区域是全市时判断用户有没保函全市区域
				if(!isEdit&&sendDistrictDTO.getCityId()==0){
					List<LocationCode> ret = locationService.getCityListByProvinceCode(sendDistrictDTO.getProvinceId());
					for (LocationCode locationCode : ret) {
						if(!areaSet.contains(locationCode.getCode())){
							sendDistrictDTO.setEdit(false);
							break;
						}
						sendDistrictDTO.setEdit(true);
					}
				}
				if (!isEdit&&(areaSet.contains(sendDistrictDTO.getCityId())||areaSet.contains(-sendDistrictDTO.getCityId()))){
					sendDistrictDTO.setEdit(true);
				}
				//取code list
				if(sendDistrictDTO.getProvinceId()!=0){
				    codeSet.add(sendDistrictDTO.getProvinceId());	
				}
				if(sendDistrictDTO.getCityId()>0){
				    codeSet.add(sendDistrictDTO.getCityId());	
				}else if(sendDistrictDTO.getCityId()<0){
					dirctcodeSet.add(sendDistrictDTO.getCityId());
				}
				if(sendDistrictDTO.getDistrictId()!=0){
				    codeSet.add(sendDistrictDTO.getDistrictId());	
				}
			}
			List<LocationCode> locationCodes = locationService.getLocationCodeListByCodeList(new ArrayList<Long>(codeSet));
			for (Long dirctcode : dirctcodeSet) {
				locationCodes.add(locationService.getLocationCode(dirctcode));
			}
			Map<Long, LocationCode> locationCodeMap = Maps.uniqueIndex(locationCodes, new Function<LocationCode, Long>() {
				@Override
				public Long apply(LocationCode arg0) {
					return arg0.getCode();
				}
			});
			for(SendDistrictDTO sendDistrictDTO:categoryContentVO.getSendDistrictDTOs()){
				String provinceName = locationCodeMap.get(sendDistrictDTO.getProvinceId())==null?"":locationCodeMap.get(sendDistrictDTO.getProvinceId()).getLocationName();
				sendDistrictDTO.setProvinceName(sendDistrictDTO.getProvinceId()==0?"全国":provinceName);
				String cityName = locationCodeMap.get(sendDistrictDTO.getCityId())==null?"":locationCodeMap.get(sendDistrictDTO.getCityId()).getLocationName();
				sendDistrictDTO.setCityName(sendDistrictDTO.getCityId()==0?"全部城市":cityName);
				String distName = locationCodeMap.get(sendDistrictDTO.getDistrictId())==null?"":locationCodeMap.get(sendDistrictDTO.getDistrictId()).getLocationName();
				sendDistrictDTO.setDistName(sendDistrictDTO.getDistrictId()==0?"全部区/县":distName);
			}
		}
		if(categoryContentVO!=null){
			categoryContentVO.setSubCategoryContentDTOList(categoryFacade.getCategoryContentListByRootId(rootId));
		}
		model.addAttribute("result",categoryContentVO);
		return "pages/category/editContentTree";
	}
	
	/**
	 * 根据类名关键字搜索内容类目。
	 * @param searchDto
	 * @return
	 */
	@RequestMapping(value = "/content/search")
	@RequiresPermissions(value = { "category:content" })
	public @ResponseBody BaseJsonVO searchCategoryContentList(SearchCategoryContentDTO searchDto) {
		BaseJsonVO resJsonVO = new BaseJsonVO();
		resJsonVO.setResult(categoryFacade.searchCategoryContentList(searchDto));
		resJsonVO.setCode(ResponseCode.RES_SUCCESS);
		return resJsonVO;
	}
	
	@RequestMapping(value = "/content/listBySuperCategoryId", method = RequestMethod.GET)
	@RequiresPermissions(value = { "category:content" })
	public @ResponseBody BaseJsonVO getSubCategoryContentListBySuperCategoryId(@RequestParam(value = "superCategoryId") long superCategoryId) {
		BaseJsonVO resJsonVO = new BaseJsonVO();
		resJsonVO.setResult(new BaseJsonListResultVO(categoryFacade.getSubCategoryContentList(superCategoryId)));
		resJsonVO.setCode(ResponseCode.RES_SUCCESS);
		return resJsonVO;
	}
	
	@RequestMapping(value = "/content/listCategoryContentByRootId", method = RequestMethod.GET)
	@RequiresPermissions(value = { "category:content" })
	public @ResponseBody BaseJsonVO getCategoryContentListByLevel(@RequestParam(value = "rootId") long rootId) {
		BaseJsonVO resJsonVO = new BaseJsonVO();
		resJsonVO.setResult(categoryFacade.getCategoryContentListByRootId(rootId));
		resJsonVO.setCode(ResponseCode.RES_SUCCESS);
		return resJsonVO;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/content/listCategoryContent", method = RequestMethod.GET)
	@RequiresPermissions(value = { "category:content" })
	public @ResponseBody BaseJsonVO getCategoryContents(@RequestParam(required = true, value = "areaId") long areaId) {
		
		BaseJsonVO resJsonVO = new BaseJsonVO();
		long userId = SecurityContextUtils.getUserId();
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(userId);
		List<Long>userIdList = RetArgUtil.get(retArg, ArrayList.class);
		Set<Long>areaList = RetArgUtil.get(retArg, HashSet.class);
		boolean isRoot = RetArgUtil.get(retArg, Boolean.class);
		List<CategoryContentVO> reslutCategoryContentVOs = new ArrayList<CategoryContentVO>();
		List<CategoryContentDTO> rootCategoryContentDTOs = categoryFacade.getCategoryContentListByLevelAndRootId(0, -1);
		for (CategoryContentDTO categoryContentDTO : rootCategoryContentDTOs) {
			if(!isRoot&&!userIdList.contains(categoryContentDTO.getCreateBy())){
				continue;
			}
			boolean isVisible = false;
			CategoryContentVO categoryContentVO = new CategoryContentVO(categoryContentDTO);
			//构建区域
			if(StringUtils.isNotEmpty(categoryContentDTO.getDistrictIds())){
				List<SendDistrictDTO>sendDistricts = new ArrayList<SendDistrictDTO>();
				String [] idsArray =  StringUtils.split(categoryContentDTO.getDistrictIds(),",");
				String [] namesArray =  StringUtils.split(categoryContentDTO.getDistrictNames(),",");
				for(int i = 0;i<idsArray.length;++i ){
					SendDistrictDTO sendDistrictDTO = new SendDistrictDTO();
					sendDistrictDTO.setProvinceId(DistrictCodeUtil.getProvinceCode(idsArray[i]));
					sendDistrictDTO.setCityId(idsArray[i].substring(2, 4).equals("00")?0:DistrictCodeUtil.getCityCode(idsArray[i]));
					sendDistrictDTO.setDistrictId(idsArray[i].substring(4, 6).equals("00")?0:Long.parseLong(idsArray[i]));
					sendDistrictDTO.setDistrictName(namesArray[i]);
					if(isRoot||RetArgUtil.get(retArg, HashSet.class).contains(sendDistrictDTO.getCityId())){
						sendDistrictDTO.setEdit(true);
					}
					if(areaId==0){
						if(isRoot){
							isVisible = true;
						}
						if(areaList!=null){
							for (Long area : areaList) {
								if(area<0&&sendDistrictDTO.getProvinceId()==Math.abs(area/100)){
									isVisible = true;
								}
								if(DistrictCodeUtil.isContainArea(area+"00", idsArray[0])||area==sendDistrictDTO.getCityId()){
									isVisible = true;
									break;
								}
							}
						}
					}else if(DistrictCodeUtil.isContainArea(Math.abs(areaId)+"00", idsArray[0])||areaId==sendDistrictDTO.getCityId()||DistrictCodeUtil.isContainArea(areaId+"00", idsArray[0])){
						
						isVisible = true;
					}
					sendDistricts.add(sendDistrictDTO);
				}
				categoryContentVO.setSendDistrictDTOs(sendDistricts);
			}
			if((StringUtils.isEmpty(categoryContentDTO.getDistrictIds())&&areaId==0)||isVisible){
				categoryContentVO.setSubCategoryContentDTOList(categoryFacade.getCategoryContentListByRootId(categoryContentDTO.getId()));
				reslutCategoryContentVOs.add(categoryContentVO);
			}
		}
		resJsonVO.setResult(reslutCategoryContentVOs);
		resJsonVO.setCode(ResponseCode.RES_SUCCESS);
		return resJsonVO;
	}
	
//	private List<CategoryContentVO> convertCategoryContentDTOToVO(List<CategoryContentDTO> categoryContentDTOs){
//		List<CategoryContentVO> categoryContentVOs = new ArrayList<CategoryContentVO>();
//		for (CategoryContentDTO categoryContentDTO : categoryContentDTOs) {
//			CategoryContentVO categoryContentVO = new CategoryContentVO(categoryContentDTO);
//			
//			categoryContentVOs.add(categoryContentVO);
//		}
//		return categoryContentVOs;
//		
//	}
	
	
	
	
	@RequestMapping(value = "/content/add", method = RequestMethod.POST)
	@RequiresPermissions(value = { "category:content" })
	public @ResponseBody BaseJsonVO addCategoryContent(@RequestBody CategoryContentDTO categoryContentDTO) {
		long userId = SecurityContextUtils.getUserId();
		categoryContentDTO.setCreateBy(userId);
		categoryContentDTO.setUpdateBy(userId);
		BaseJsonVO resJsonVO = new BaseJsonVO();
		String validResult = validArea(categoryContentDTO);
		if(StringUtils.isNotEmpty(validResult)){
			resJsonVO.setCode(ResponseCode.RES_ERROR);
			resJsonVO.setMessage(validResult);
			return resJsonVO;
		}
		int result = categoryFacade.saveCategoryContent(categoryContentDTO);
		if(result==CategoryErrorMsgEnum.SUCCESS.getIntValue()){
			resJsonVO.setCode(ResponseCode.RES_SUCCESS);
		}else {
			resJsonVO.setCode(ResponseCode.RES_ERROR);
			resJsonVO.setMessage(CategoryErrorMsgEnum.getDescByIntValue(result));
		}
		return resJsonVO;
	}
	

	@RequestMapping(value = "/content/update", method = RequestMethod.POST)
	@RequiresPermissions(value = { "category:content" })
	public @ResponseBody BaseJsonVO updateCategoryContent(@RequestBody CategoryContentDTO categoryContentDTO) {
		categoryContentDTO.setUpdateBy(SecurityContextUtils.getUserId());
		BaseJsonVO resJsonVO = new BaseJsonVO();
		String validResult = validArea(categoryContentDTO);
		if(StringUtils.isNotEmpty(validResult)){
			resJsonVO.setCode(ResponseCode.RES_ERROR);
			resJsonVO.setMessage(validResult);
			return resJsonVO;
		}
		int result = categoryFacade.updateCategoryContent(categoryContentDTO);
		if(result==CategoryErrorMsgEnum.SUCCESS.getIntValue()){
			resJsonVO.setCode(ResponseCode.RES_SUCCESS);
		}else {
			resJsonVO.setCode(ResponseCode.RES_ERROR);
			resJsonVO.setMessage(CategoryErrorMsgEnum.getDescByIntValue(result));
		}
		
		return resJsonVO;
	}
	
	/**
	 * 验证分配区域
	 * @param dto
	 * @return
	 */
	private String validArea(CategoryContentDTO dto){
		StringBuilder sb = new StringBuilder();
		if(dto.getLevel()==0){
			List<CategoryContentDTO> rootCategoryContentDTOs = categoryFacade.getCategoryContentListByLevelAndRootId(0, -1);
			if(StringUtils.isEmpty(dto.getDistrictIds())||CollectionUtil.isEmptyOfList(rootCategoryContentDTOs)){
				return null;
			}
			String[] distirctArray = StringUtils.split(dto.getDistrictIds(),',');
			String[] distirctNameArray = StringUtils.split(dto.getDistrictNames(),',');
			//判断已有的区域有没包含新增的
			for(int i=0;i< distirctArray.length;++i){
				for(CategoryContentDTO categoryContentDTO2:rootCategoryContentDTOs){
					if(categoryContentDTO2.getId()==dto.getId()){
						continue;
					}
					String districtIds = categoryContentDTO2.getDistrictIds();
					if(StringUtils.isNotEmpty(districtIds)&&AreaCodeUtil.isContainArea(distirctArray[i], districtIds)){
						sb.append(distirctNameArray[i]).append(" ");
					}
				}
			}
			if(sb.length()>0){
				sb.append(" 区域已存在,请先移除原先区域再分配");
				return sb.toString();
			}
			//判断新增的区域有没包含已有的
			for(CategoryContentDTO categoryContentDTO2:rootCategoryContentDTOs){
				if(categoryContentDTO2.getId()==dto.getId()){
					continue;
				}
				if(StringUtils.isEmpty(categoryContentDTO2.getDistrictIds())){
					continue;
				}
				String[] distirctArray2 = StringUtils.split(categoryContentDTO2.getDistrictIds(),',');
				String[] distirctNameArray2 = StringUtils.split(categoryContentDTO2.getDistrictNames(),',');
				for(int i=0;i< distirctArray2.length;++i){
					String districtIds = categoryContentDTO2.getDistrictIds();
					if(StringUtils.isNotEmpty(districtIds)&&AreaCodeUtil.isContainArea(distirctArray2[i], dto.getDistrictIds())){
						sb.append(distirctNameArray2[i]).append(" ");
					}
				}
			}
			
		}
		if(sb.length()>0)sb.append(" 区域已存在,请先移除原先区域再分配");
		return sb.length()>0?sb.toString():null;
	}
	
	
	@RequestMapping(value = "/content/delete", method = RequestMethod.GET)
	@RequiresPermissions(value = { "category:content" })
	public @ResponseBody BaseJsonVO deleteCategoryContent(@RequestParam(value = "categoryId") long categoryId) {
		List<CategoryContentVO> list = categoryFacade.getSubCategoryContentList(categoryId);
		BaseJsonVO resJsonVO = new BaseJsonVO();
		if(list.size()>0){
			resJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "包含子类目，无法删除!");
		}else{
			boolean result = categoryFacade.deleteCategoryContent(categoryId);
			if(result){
				resJsonVO.setCode(ResponseCode.RES_SUCCESS);
			}else {
				resJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "删除内容分类失败!");
			}
		}
		
		return resJsonVO;
	}
	
	@RequestMapping(value = "/content/deleteTree", method = RequestMethod.GET)
	@RequiresPermissions(value = { "category:content" })
	public @ResponseBody BaseJsonVO deleteCategoryContentTree(@RequestParam(value = "rootId") long rootId) {
	    BaseJsonVO resJsonVO = new BaseJsonVO();
		boolean result = categoryFacade.deleteCategoryContentTree(rootId);
		if(result){
			resJsonVO.setCode(ResponseCode.RES_SUCCESS);
		}else {
			resJsonVO.setCode(ResponseCode.RES_ERROR);
			resJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "删除内容分类树失败!");
		}
		return resJsonVO;
	}
	

}
