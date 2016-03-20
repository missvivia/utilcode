/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.dto.SiteAreaDTO;
import com.xyl.mmall.cms.dto.SiteCMSDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.SiteCMSFacade;
import com.xyl.mmall.cms.vo.SiteAreaVO;
import com.xyl.mmall.cms.vo.SiteCMSVO;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.ip.enums.LocationLevel;
import com.xyl.mmall.ip.meta.LocationCode;

/**
 * SiteController.java created by yydx811 at 2015年7月16日 上午10:45:03
 * 站点controller
 *
 * @author yydx811
 */
@Controller
@RequestMapping("/site")
public class SiteController {

	private static Logger logger = Logger.getLogger(SiteController.class);
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	@Autowired
	private SiteCMSFacade siteCMSFacade;
	
	@Autowired
	private LocationFacade locationFacade;
	
	@Autowired
	private BusinessFacade businessFacade;
	
	@RequestMapping(value = "/site", method = RequestMethod.GET)
	@RequiresPermissions({ "site:list" })
	public String list(Model model) {
		model.addAttribute("pages", 
				leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/site/site";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@RequiresPermissions({ "site:list" })
	public @ResponseBody BaseJsonVO list(
			@RequestParam(value = "searchValue", required = false) String searchValue,
			BasePageParamVO<SiteCMSVO> basePageParamVO) {
		BaseJsonVO ret = new BaseJsonVO();
		basePageParamVO.setList(siteCMSFacade.getSiteCMSList(searchValue, basePageParamVO));
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(basePageParamVO);
		return ret;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	@RequiresPermissions({ "site:create" })
	public String createSite(Model model) {
		model.addAttribute("pages", 
				leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/site/create";
	}
	
	@RequestMapping(value = "/areaList", method = RequestMethod.GET)
	@RequiresPermissions(value = {"site:create", "site:edit"}, logical = Logical.OR)
	public @ResponseBody BaseJsonVO getSiteAreaList(
			@RequestParam(value = "siteId", required = false, defaultValue = "0") long siteId,
			@RequestParam(value = "areaId", required = false, defaultValue = "0") long areaId) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setResult(siteCMSFacade.getSiteAreaList(areaId, siteId));
		ret.setCode(ResponseCode.RES_SUCCESS);
		return ret;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@RequiresPermissions({ "site:create" })
	public @ResponseBody BaseJsonVO addSite(@RequestBody SiteCMSVO siteCMSVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (CollectionUtils.isEmpty(siteCMSVO.getAreaList())) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "覆盖区域不能为空！");
			return ret;
		}
		if (StringUtils.isBlank(siteCMSVO.getSiteName())) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "站点名不能为空！");
			return ret;
		}
		List<SiteAreaDTO> addList = new ArrayList<SiteAreaDTO>();
		List<Long> delList = new ArrayList<Long>();
		String result = checkAddSiteAreaList(addList, delList, siteCMSVO.getAreaList(), 0l);
		if (result != null) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, result);
			return ret;
		}
		if (CollectionUtils.isEmpty(addList)) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "覆盖区域不能为空！");
			return ret;
		}
		try {
			long uid = SecurityContextUtils.getUserId();
			SiteCMSDTO siteCMSDTO = new SiteCMSDTO();
			siteCMSDTO.setName(siteCMSVO.getSiteName());
			siteCMSDTO.setCreateOperator(uid);
			siteCMSDTO.setUpdateOperator(uid);
			siteCMSDTO.setAreaList(addList);
			if (siteCMSFacade.addSiteCMS(siteCMSDTO) > 0l) {
				ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "添加成功！");
			} else {
				ret.setCodeAndMessage(ResponseCode.RES_ERROR, "添加失败！");
			}
		} catch (Exception e) {
			logger.error("Add site error!", e);
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "添加失败！");
		}
		return ret;
	}

	@RequestMapping(value = "/edit/{siteId}", method = RequestMethod.GET)
	@RequiresPermissions({ "site:edit" })
	public String editSite(Model model, @PathVariable long siteId) {
		model.addAttribute("pages", 
				leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("siteCMS", siteCMSFacade.getSiteCMS(siteId, false));
		return "pages/site/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@RequiresPermissions({ "site:edit" })
	public @ResponseBody BaseJsonVO updateSite(@RequestBody SiteCMSVO siteCMSVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (siteCMSVO.getSiteId() < 1l) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "参数错误！");
			return ret;
		}
		// 获取原站点信息
		SiteCMSVO old = siteCMSFacade.getSiteCMS(siteCMSVO.getSiteId(), true);
		if (old == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "站点不存在！");
			return ret;
		}
		List<SiteAreaDTO> addList = null;
		List<Long> delList = null;
		if (CollectionUtils.isNotEmpty(siteCMSVO.getAreaList())) {
			addList = new ArrayList<SiteAreaDTO>();
			delList = new ArrayList<Long>();
			String result = checkUpdateSiteAreaList(addList, delList, siteCMSVO.getAreaList(), old);
			if (result != null) {
				ret.setCodeAndMessage(ResponseCode.RES_EPARAM, result);
				return ret;
			}
		}
		try {
			long uid = SecurityContextUtils.getUserId();
			SiteCMSDTO siteCMSDTO = new SiteCMSDTO();
			siteCMSDTO.setId(siteCMSVO.getSiteId());
			siteCMSDTO.setName(siteCMSVO.getSiteName());
			siteCMSDTO.setUpdateOperator(uid);
			siteCMSDTO.setAreaList(addList);
			int res = siteCMSFacade.updateSiteCMS(siteCMSDTO, delList);
			if (res > 0) {
				ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "更新成功！");
			} else {
				ret.setCodeAndMessage(ResponseCode.RES_ERROR, "更新失败！");
			}
		} catch (Exception e) {
			logger.error("Update site error! SiteId : " + siteCMSVO.getSiteId(), e);
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "更新失败！");
		}
		return ret;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@RequiresPermissions({ "site:edit" })
	public @ResponseBody BaseJsonVO deleteSite(@RequestBody JSONObject json) {
		BaseJsonVO ret = new BaseJsonVO();
		String siteIds = json.getString("siteIds");
		// id转换
		String[] str = siteIds.split(",");
		List<Long> idList = new ArrayList<Long>(str.length);
		for (String idStr : str) {
			if (RegexUtils.isAllNumber(idStr)) {
				// 站点下区域使用情况
				long id = Long.parseLong(idStr);
				// 获取原站点信息
				SiteCMSVO old = siteCMSFacade.getSiteCMS(id, true);
				if (old == null) {
					continue;
				}
				List<SiteAreaVO> areaList = old.getAreaList();
				if (CollectionUtils.isNotEmpty(areaList)) {
					for (SiteAreaVO siteAreaVO : areaList) {
						if (businessFacade.getBusinessCountByAreaId(siteAreaVO.getAreaId()) > 0) {
							ret.setCodeAndMessage(ResponseCode.RES_ERROR, "该站点下包含已激活的商家，无法删除！");
							return ret;
						}
					}
				}
				idList.add(id);
			} else {
				ret.setCodeAndMessage(ResponseCode.RES_ERROR, "站点id错误！");
				return ret;
			}
		}
		try {
			int res = siteCMSFacade.deleteBulkSiteCMS(idList);
			if (res <= 0) {
				ret.setCodeAndMessage(ResponseCode.RES_ERROR, "删除失败！");
			} else {
				ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "删除成功！");
			}
		} catch (Exception e) {
			logger.error("Delete sites error! SiteIds : [" + siteIds + "]", e);
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "删除失败！");
		}
		return ret;
	}
	
	/**
	 * 检测覆盖区域正确性
	 * @param addList
	 * @param delList
	 * @param siteAreaList
	 * @param siteId
	 * @return
	 */
	private String checkAddSiteAreaList(List<SiteAreaDTO> addList, List<Long> delList, 
			List<SiteAreaVO> siteAreaList, long siteId) {
		for (SiteAreaVO siteAreaVO : siteAreaList) {
			long areaId = siteAreaVO.getAreaId();
			if (siteAreaVO.getIsChecked() == 0) {
				delList.add(areaId);
			} else {
				SiteAreaDTO siteAreaDTO = new SiteAreaDTO();
				siteAreaDTO.setAreaId(areaId);
				siteAreaDTO.setSiteId(siteId);
				LocationCode locationCode = locationFacade.getLocationCodeByCode(areaId);
				if (locationCode == null || locationCode.getLevel() != LocationLevel.LEVEL_CITY) {
					return "所选区域错误！";
				}
				if (siteCMSFacade.getSiteAreaCount(areaId, 0l) > 0) {
					return "所选区域“" + locationCode.getLocationName() + "”已被其他站点覆盖，操作无法完成！";
				}
				addList.add(siteAreaDTO);
			}
		}
		return null;
	}
	
	/**
	 * 检测覆盖区域正确性
	 * @param addList
	 * @param delList
	 * @param siteAreaList
	 * @param old
	 * @return
	 */
	private String checkUpdateSiteAreaList(List<SiteAreaDTO> addList, List<Long> delList, 
			List<SiteAreaVO> siteAreaList, SiteCMSVO old) {
		Set<Long> areaIds = null;
		if (CollectionUtils.isNotEmpty(old.getAreaList())) {
			areaIds = new HashSet<Long>(old.getAreaList().size());
			for (SiteAreaVO oldArea : old.getAreaList()) {
				areaIds.add(oldArea.getAreaId());
			}
		}
		for (SiteAreaVO siteAreaVO : siteAreaList) {
			long areaId = siteAreaVO.getAreaId();
			if (siteAreaVO.getIsChecked() == 0) {
				// 在旧的区域中的加入删除列表
				if (areaIds != null && areaIds.contains(areaId)) {
					if (businessFacade.getBusinessCountByAreaId(areaId) > 0) {
						return "所选区域下包含已激活的商家，无法取消选择！";
					}
					delList.add(areaId);
				}
			} else {
				// 重复的
				if (areaIds != null && areaIds.contains(areaId)) {
					continue;
				}
				SiteAreaDTO siteAreaDTO = new SiteAreaDTO();
				siteAreaDTO.setAreaId(areaId);
				siteAreaDTO.setSiteId(old.getSiteId());
				LocationCode locationCode = locationFacade.getLocationCodeByCode(areaId);
				if (locationCode == null || locationCode.getLevel() != LocationLevel.LEVEL_CITY) {
					return "所选区域错误！";
				}
				if (siteCMSFacade.getSiteAreaCount(areaId, 0l) > 0) {
					return "所选区域“" + locationCode.getLocationName() + "”已被其他站点覆盖，操作无法完成！";
				}
				addList.add(siteAreaDTO);
			}
		}
		// 判断覆盖区域总数量
		int total = (areaIds == null ? 0 : areaIds.size()) - delList.size() + addList.size();
		if (total > 0) {
			return null;
		} else {
			return "覆盖区域不能为空！";
		}
	}
}
