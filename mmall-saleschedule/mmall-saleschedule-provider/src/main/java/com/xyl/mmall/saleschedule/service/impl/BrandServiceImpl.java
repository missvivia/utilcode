package com.xyl.mmall.saleschedule.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.saleschedule.dao.BrandDao;
import com.xyl.mmall.saleschedule.dao.BrandLogoImgDao;
import com.xyl.mmall.saleschedule.dao.BrandShopDao;
import com.xyl.mmall.saleschedule.dao.BrandUserFavDao;
import com.xyl.mmall.saleschedule.dao.ScheduleDao;
import com.xyl.mmall.saleschedule.dao.SupplierBrandDao;
import com.xyl.mmall.saleschedule.dao.SupplierBrandImgDao;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;
import com.xyl.mmall.saleschedule.enums.BrandImgSize;
import com.xyl.mmall.saleschedule.enums.BrandProbability;
import com.xyl.mmall.saleschedule.enums.BrandShopStatus;
import com.xyl.mmall.saleschedule.enums.BrandStatus;
import com.xyl.mmall.saleschedule.meta.Brand;
import com.xyl.mmall.saleschedule.meta.BrandLogoImg;
import com.xyl.mmall.saleschedule.meta.BrandShop;
import com.xyl.mmall.saleschedule.meta.BrandUserFav;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.SupplierBrand;
import com.xyl.mmall.saleschedule.meta.SupplierBrandImg;
import com.xyl.mmall.saleschedule.service.BrandService;

/**
 * 提供品牌列表的相关服务
 * 
 * @author chengximing
 *
 */
@Service
public class BrandServiceImpl implements BrandService {

	private static final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private SupplierBrandDao supplierBrandDao;

	@Autowired
	private BrandLogoImgDao brandLogoImgDao;

	@Autowired
	private SupplierBrandImgDao supplierBrandImgDao;

	@Autowired
	private BrandShopDao brandShopDao;

	@Autowired
	private BrandUserFavDao brandUserFavDao;

	@Autowired
	private ScheduleDao scheduleDao;

	private SupplierBrandFullDTO addSupplierBrand(SupplierBrandFullDTO fullDto, BrandStatus brandStatus, long brandId,
			long supplierId, String userName, long areaFmt) {
		logger.debug("addSupplierBrand brandId = " + brandId + " supplierId = " + supplierId);
		SupplierBrandDTO dto = fullDto.getBasic();
		List<BrandShopDTO> brandShopList = fullDto.getShops();
		long current = System.currentTimeMillis();
		SupplierBrand brand = new SupplierBrand();
		brand.setStatus(brandStatus);
		brand.setBrandDesc1(dto.getIntro());
		brand.setBrandDesc2(dto.getIntro2());
		// brand.setAreaId(areaId);
		// 支持多个省份的二进制编码
		brand.setAreaFormatCode(areaFmt);
		brand.setEditor(userName);
		brand.setStatusUpdateDate(current);
		brand.setSupplierBrandId(-1);
		brand.setBrandId(brandId);
		brand.setSupplierId(supplierId);
		SupplierBrand brandOut = supplierBrandDao.addObject(brand);
		List<Map<String, String>> brandImgList = fullDto.getBasic().getMaxImages();
		List<Map<String, String>> brandShowCaseImgList = fullDto.getBasic().getFixImages();
		long index = -1;
		for (Map<String, String> data : brandImgList) {
			String url = data.get("src");
			supplierBrandImgDao.setBrandVisualImg(brandOut.getSupplierBrandId(), ++index, url);
		}
		index = -1;
		for (Map<String, String> data : brandShowCaseImgList) {
			String url = data.get("src");
			String desc = data.get("desc");
			supplierBrandImgDao.setBrandShowCaseImg(brandOut.getSupplierBrandId(), ++index, url, desc);
		}
		List<SupplierBrandImg> supplierImgList = supplierBrandImgDao.getBrandVisualImgList(brandOut
				.getSupplierBrandId());
		List<SupplierBrandImg> showCaseImgList = supplierBrandImgDao.getBrandShowCaseImgList(brandOut
				.getSupplierBrandId());
		String supplierImgUrl = null;
		if (supplierImgList.size() > 0) {
			supplierImgUrl = supplierImgList.get(0).getImageUrl();
		}
		SupplierBrandDTO newDto = getSupplierBrandDTO(brandOut, supplierImgUrl, supplierImgList, showCaseImgList);
		fullDto.setBasic(newDto);
		for (BrandShopDTO brandShopDTO : brandShopList) {
			BrandShop brandShop = brandShopDTO.changeDataIntoBrandShop();
			brandShop.setBrandShopId(-1);
			brandShop.setStatusUpdateDate(current);
			brandShop.setSupplierBrandId(brandOut.getSupplierBrandId());
			if (brandShopDTO.getStatus() != null) {
				brandShop.setStatus(brandShopDTO.getStatus());
			} else {
				brandShop.setStatus(BrandShopStatus.SHOP_NEW);
			}
			brandShopDao.addObject(brandShop);
		}
		return fullDto;
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public SupplierBrandDTO saveSupplierBrand(SupplierBrandDTO dto) {
		logger.debug("saveSupplierBrand supplierBrandId = " + dto.getId());
		SupplierBrand supplierBrand = supplierBrandDao.getObjectById(dto.getId());
		if (supplierBrand.getStatus() == BrandStatus.BRAND_NEW
				|| supplierBrand.getStatus() == BrandStatus.BRAND_AUDITREFUSED) {
			supplierBrand.setBrandDesc1(StringEscapeUtils.escapeSql(dto.getIntro()));
			supplierBrand.setBrandDesc2(StringEscapeUtils.escapeSql(dto.getIntro2()));
			supplierBrandDao.updateObjectByKey(supplierBrand);
			//supplierBrandDao.updateBrandContent(supplierBrand);
			List<Map<String, String>> brandImgList = dto.getMaxImages();
			List<Map<String, String>> brandShowCaseImgList = dto.getFixImages();
			long index = -1;
			for (Map<String, String> data : brandImgList) {
				String url = data.get("src");
				supplierBrandImgDao.setBrandVisualImg(supplierBrand.getSupplierBrandId(), ++index, url);
			}
			index = -1;
			for (Map<String, String> data : brandShowCaseImgList) {
				String url = data.get("src");
				String desc = data.get("desc");
				supplierBrandImgDao.setBrandShowCaseImg(supplierBrand.getSupplierBrandId(), ++index, url, desc);
			}
			List<SupplierBrandImg> supplierImgList = supplierBrandImgDao.getBrandVisualImgList(supplierBrand
					.getSupplierBrandId());
			List<SupplierBrandImg> showCaseImgList = supplierBrandImgDao.getBrandShowCaseImgList(supplierBrand
					.getSupplierBrandId());
			String supplierImgUrl = null;
			if (supplierImgList.size() > 0) {
				supplierImgUrl = supplierImgList.get(0).getImageUrl();
			}
			return getSupplierBrandDTO(supplierBrand, supplierImgUrl, supplierImgList, showCaseImgList);
		} else {
			logger.error("saveSupplierBrand supplierBrand.getStatus() != BrandStatus.BRAND_NEW failed!");
			return null;
		}
	}

	private SupplierBrandDTO getSupplierBrandDTO(SupplierBrand supplierBrand, String imgUrl,
			List<SupplierBrandImg> visualImgList, List<SupplierBrandImg> showCaseImgList) {
		SupplierBrandDTO dto = new SupplierBrandDTO();
		dto.setId(supplierBrand.getSupplierBrandId());
		dto.setSupplierId(supplierBrand.getSupplierId());
		dto.setImageUrl(imgUrl);
		dto.setIntro(supplierBrand.getBrandDesc1());
		dto.setIntro2(supplierBrand.getBrandDesc2());
		dto.setEditor(supplierBrand.getEditor());
		dto.setUpdateTime(supplierBrand.getStatusUpdateDate());
		Map<String, Object> status = new HashMap<String, Object>();
		status.put("type", supplierBrand.getStatus().getIntValue());
		status.put("desc", supplierBrand.getStatus().getDesc());
		dto.setStatus(status);
		dto.setReason(supplierBrand.getRejectReason());
		// dto.setSiteArea(supplierBrand.getAreaId());
		// dto.setSiteIdNameMap(supplierBrandDao.getSupplierAreaListMap(dto.getSupplierId()));
		Brand brand = brandDao.getObjectById(supplierBrand.getBrandId());
		BrandLogoImg brandImg = brandLogoImgDao.getBrandLogoByBrandId(supplierBrand.getBrandId(),
				BrandImgSize.SIZE_BRAND_LOGO);
		dto.setLogoUrl(brandImg.getBrandImgUrl());
		dto.setBrandName(getCombinedBrandName(brand.getBrandNameEn(), brand.getBrandNameZh()));
		if (visualImgList != null && showCaseImgList != null) {
			dto.setVisualImgs(visualImgList);
			dto.setShowCaseImgs(showCaseImgList);
		}
		return dto;
	}
	
	private String getCombinedBrandName(String brandNameEn, String brandNameZH) {
		brandNameEn = (brandNameEn==null) ? "" : brandNameEn;
		brandNameZH = (brandNameZH==null) ? "" : brandNameZH;
		if ("".equals(brandNameEn.trim())) {
			return brandNameZH;
		} 
		if ("".equals(brandNameZH.trim())) {
			return brandNameEn;
		}
		if (!"".equals(brandNameEn.trim()) && !"".equals(brandNameZH.trim())) {
			if (isChinese(brandNameZH)) {
				return brandNameEn + "/" + brandNameZH;
			} else {
				return brandNameEn;
			}
		}
		
		return brandNameZH;
	}
	
	private static boolean isChinese(String str) {
		if(str == null || "".equals(str.trim())) {
			return false;
		}
		
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
	                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
	                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
	            return true;
	        }
		}
		return false;
    }

	@Override
	public SupplierBrandDTO getSupplierBrandById(long id) {
		logger.debug("getSupplierBrandById supplierBrandId = " + id);
		SupplierBrand brand = supplierBrandDao.getSupplierBrandById(id);
		List<SupplierBrandImg> supplierImgList = supplierBrandImgDao.getBrandVisualImgList(brand.getSupplierBrandId());
		List<SupplierBrandImg> showCaseImgList = supplierBrandImgDao
				.getBrandShowCaseImgList(brand.getSupplierBrandId());
		String supplierImgUrl = null;
		if (supplierImgList.size() > 0) {
			supplierImgUrl = supplierImgList.get(0).getImageUrl();
		}
		return getSupplierBrandDTO(brand, supplierImgUrl, supplierImgList, showCaseImgList);
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public SupplierBrandDTO submitSupplierBrand(SupplierBrandDTO dto) {
		logger.debug("getSupplierBrandById supplierBrandId = " + dto.getId());
		SupplierBrand supplierBrand = supplierBrandDao.getObjectById(dto.getId());
		if (supplierBrand.getStatus() == BrandStatus.BRAND_NEW
				|| supplierBrand.getStatus() == BrandStatus.BRAND_AUDITREFUSED) {
			supplierBrand.setBrandDesc1(StringEscapeUtils.escapeSql(dto.getIntro()));
			supplierBrand.setBrandDesc2(StringEscapeUtils.escapeSql(dto.getIntro2()));
			supplierBrand.setRejectReason("");
			supplierBrand.setStatus(BrandStatus.BRAND_AUDITING);
			supplierBrandDao.updateObjectByKey(supplierBrand);
			List<SupplierBrandImg> supplierImgList = supplierBrandImgDao.getBrandVisualImgList(supplierBrand
					.getSupplierBrandId());
			String supplierImgUrl = null;
			if (supplierImgList.size() > 0) {
				supplierImgUrl = supplierImgList.get(0).getImageUrl();
			}
			return getSupplierBrandDTO(supplierBrand, supplierImgUrl, null, null);
		} else {
			logger.error("submitSupplierBrand supplierBrand.getStatus() != BrandStatus.BRAND_NEW failed!");
			return null;
		}
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public SupplierBrandFullDTO addNewSupplierBrand(SupplierBrandFullDTO fullDto, long brandId, 
			long supplierId, String userName, long areaFmt) {
		return addSupplierBrand(fullDto, BrandStatus.BRAND_NEW, brandId, 
				supplierId, userName, areaFmt);
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public SupplierBrandDTO copyFromSupplierBrand(long copyId) {
		logger.debug("copyFromSupplierBrand from supplierBrandId = " + copyId);
		SupplierBrand brand = supplierBrandDao.getSupplierBrandById(copyId);
		long current = System.currentTimeMillis();
		brand.setStatus(BrandStatus.BRAND_NEW);
		brand.setStatusUpdateDate(current);
		brand.setRejectReason(null);
		brand.setSupplierBrandId(-1);
		SupplierBrand brandOut = supplierBrandDao.addObject(brand);
		List<BrandShop> brandShopList = brandShopDao.getBrandShopListByBrandSupplierId(copyId, false);
		for (BrandShop brandShop : brandShopList) {
			brandShop.setBrandShopId(-1);
			brandShop.setStatusUpdateDate(current);
			brandShop.setSupplierBrandId(brandOut.getSupplierBrandId());
			brandShopDao.addObject(brandShop);
		}
		List<SupplierBrandImg> supplierImgList = supplierBrandImgDao.getBrandVisualImgList(copyId);
		List<SupplierBrandImg> showCaseImgList = supplierBrandImgDao.getBrandShowCaseImgList(copyId);
		String supplierImgUrl = null;
		if (supplierImgList.size() > 0) {
			supplierImgUrl = supplierImgList.get(0).getImageUrl();
		}
		SupplierBrandDTO dto = getSupplierBrandDTO(brandOut, supplierImgUrl, supplierImgList, showCaseImgList);
		List<Map<String, String>> brandImgList = dto.getMaxImages();
		List<Map<String, String>> brandShowCaseImgList = dto.getFixImages();
		long index = -1;
		for (Map<String, String> data : brandImgList) {
			String url = data.get("src");
			supplierBrandImgDao.setBrandVisualImg(brandOut.getSupplierBrandId(), ++index, url);
		}
		index = -1;
		for (Map<String, String> data : brandShowCaseImgList) {
			String url = data.get("src");
			String desc = data.get("desc");
			supplierBrandImgDao.setBrandShowCaseImg(brandOut.getSupplierBrandId(), ++index, url, desc);
		}
		return dto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RetArg/* List<SupplierBrandDTO> */getSupplierBrandListBySupplierId(DDBParam param, long supplierId) {
		logger.debug("getSupplierBrandListBySupplierId supplierId = " + supplierId);
		RetArg retArg = supplierBrandDao.getSupplierBrandListBySupplierId(param, supplierId);
		List<SupplierBrand> supplierBrandList = RetArgUtil.get(retArg, ArrayList.class);
		List<SupplierBrandDTO> out = new ArrayList<SupplierBrandDTO>(supplierBrandList.size());
		for (SupplierBrand supplierBrand : supplierBrandList) {
			List<SupplierBrandImg> supplierImgList = supplierBrandImgDao.getBrandVisualImgList(supplierBrand
					.getSupplierBrandId());
			String supplierImgUrl = null;
			if (supplierImgList.size() > 0) {
				supplierImgUrl = supplierImgList.get(0).getImageUrl();
			}
			out.add(getSupplierBrandDTO(supplierBrand, supplierImgUrl, null, null));
		}
		RetArg ret = new RetArg();
		RetArgUtil.put(ret, out);
		RetArgUtil.put(ret, param);
		return ret;
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public boolean setBrandNewStatus(long id, BrandStatus newStatus) {
		logger.debug("setBrandNewStatus SupplierBrand id = " + id + " newStatus = " + newStatus.toString());
		SupplierBrand sBrand = supplierBrandDao.getSupplierBrandById(id);
		sBrand.setStatus(newStatus);
		sBrand.setStatusUpdateDate(System.currentTimeMillis());
		return supplierBrandDao.updateBrandStatus(sBrand);
	}

	@Cacheable(value = "brandCache")
	@Override
	public RetArg getAllBrandList(DDBParam param) {
		logger.debug("getAllBrandList() called");
		List<Brand> brandList = brandDao.getAllBrand(param, 0);
		List<Long> idsList = new ArrayList<>(brandList.size());
		for (Brand brand : brandList) {
			idsList.add(brand.getBrandId());
		}
		Map<Long, BrandLogoImg> logoMap = brandLogoImgDao.getBrandLogoListByBrandIdList(idsList,
				BrandImgSize.SIZE_BRAND_LOGO);
		Map<Long, BrandLogoImg> visualWebMap = brandLogoImgDao.getBrandLogoListByBrandIdList(idsList,
				BrandImgSize.SIZE_BRAND_VISUAL_WEB);
		// Map<Long, BrandLogoImg> visualAppMap =
		// brandLogoImgDao.getBrandLogoListByBrandIdList(idsList,
		// BrandImgSize.SIZE_BRAND_VISUAL_APP);
		List<BrandDTO> outDtoList = new ArrayList<BrandDTO>(brandList.size());
		for (Brand brand : brandList) {
			BrandLogoImg logo = logoMap.get(brand.getBrandId());
			BrandLogoImg visualWeb = visualWebMap.get(brand.getBrandId());
			// BrandLogoImg visualApp = visualAppMap.get(brand.getBrandId());
			BrandDTO dto = new BrandDTO();
			dto.setBrand(brand);
			dto.setLogo(logo != null ? logo.getBrandImgUrl() : null);
			dto.setVisualImgWeb(visualWeb != null ? visualWeb.getBrandImgUrl() : null);
			// dto.setVisualImgApp(visualApp != null ?
			// visualApp.getBrandImgUrl() : null);
			outDtoList.add(dto);
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, outDtoList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	@Override
	@Cacheable(value = "brandCache")
	public BrandDTO getBrandByBrandId(long id) {
		logger.debug("getBrandByBrandId id = " + id);
		Brand brand = brandDao.getObjectById(id);
		BrandDTO brandDTO = new BrandDTO();
		if (brand != null) {
			BrandLogoImg logo = brandLogoImgDao.getBrandLogoByBrandId(brand.getBrandId(), BrandImgSize.SIZE_BRAND_LOGO);
			BrandLogoImg visualWeb = brandLogoImgDao.getBrandLogoByBrandId(brand.getBrandId(),
					BrandImgSize.SIZE_BRAND_VISUAL_WEB);
			BrandLogoImg visualApp = brandLogoImgDao.getBrandLogoByBrandId(brand.getBrandId(),
					BrandImgSize.SIZE_BRAND_VISUAL_APP);
			brandDTO.setBrand(brand);
			brandDTO.setLogo(logo != null ? logo.getBrandImgUrl() : null);
			brandDTO.setVisualImgWeb(visualWeb != null ? visualWeb.getBrandImgUrl() : null);
			brandDTO.setVisualImgApp(visualApp != null ? visualApp.getBrandImgUrl() : null);
		}
		return brandDTO;
	}
	
	@Override
	@Cacheable(value = "brandCache")
	public BrandDTO getBrandByBrandIdAsync(long id) {
		return getBrandByBrandId(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public SupplierBrandDTO onlineSupplierBrand(long onlineId) {
		logger.debug("onlineSupplierBrand onlineId = " + onlineId);
		SupplierBrand supplierBrandToOnline = supplierBrandDao.getObjectById(onlineId);
		long supplierId = supplierBrandToOnline.getSupplierId();
		DDBParam param = new DDBParam("supplierBrandId", true, 0, 0);
		RetArg retArg = supplierBrandDao.getSupplierBrandListBySupplierId(param, supplierId);
		List<SupplierBrand> supplierBrandList = RetArgUtil.get(retArg, ArrayList.class);
		if (supplierBrandToOnline.getStatus() == BrandStatus.BRAND_AUDITPASSED_UNUSED) {
			for (SupplierBrand brand : supplierBrandList) {
				if (brand.getStatus() == BrandStatus.BRAND_AUDITPASSED_USING) {
					SupplierBrandDTO dto = new SupplierBrandDTO();
					dto.setId(-1);
					return dto;
				}
			}
			supplierBrandToOnline.setStatus(BrandStatus.BRAND_AUDITPASSED_USING);
			supplierBrandToOnline.setStatusUpdateDate(System.currentTimeMillis());
			supplierBrandDao.updateBrandStatus(supplierBrandToOnline);
			List<SupplierBrandImg> supplierImgList = supplierBrandImgDao.getBrandVisualImgList(supplierBrandToOnline
					.getSupplierBrandId());
			String supplierImgUrl = null;
			if (supplierImgList.size() > 0) {
				supplierImgUrl = supplierImgList.get(0).getImageUrl();
			}
			return getSupplierBrandDTO(supplierBrandToOnline, supplierImgUrl, null, null);
		} else {
			logger.error("the online supplierBrandToOnline status != BRAND_AUDITPASSED_UNUSE");
			return null;
		}
	}

	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public SupplierBrandDTO offlineSupplierBrand(long offlineId) {
		logger.debug("offlineSupplierBrand offlineId = " + offlineId);
		SupplierBrand supplierBrandToOffline = supplierBrandDao.getObjectById(offlineId);
		if (supplierBrandToOffline.getStatus() == BrandStatus.BRAND_AUDITPASSED_USING) {
			supplierBrandToOffline.setStatus(BrandStatus.BRAND_AUDITPASSED_UNUSED);
			supplierBrandToOffline.setStatusUpdateDate(System.currentTimeMillis());
			supplierBrandDao.updateBrandStatus(supplierBrandToOffline);
			List<SupplierBrandImg> supplierImgList = supplierBrandImgDao.getBrandVisualImgList(supplierBrandToOffline
					.getSupplierBrandId());
			String supplierImgUrl = null;
			if (supplierImgList.size() > 0) {
				supplierImgUrl = supplierImgList.get(0).getImageUrl();
			}
			return getSupplierBrandDTO(supplierBrandToOffline, supplierImgUrl, null, null);
		} else {
			logger.error("the offline supplierBrandToOnline status != BRAND_AUDITPASSED_UNUSE");
			return null;
		}
	}

	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public SupplierBrandDTO auditSupplierBrand(long auditId) {
		logger.debug("auditSupplierBrand auditId = " + auditId);
		SupplierBrand supplierBrandToAudit = supplierBrandDao.getObjectById(auditId);
		if (supplierBrandToAudit.getStatus() == BrandStatus.BRAND_NEW
				|| supplierBrandToAudit.getStatus() == BrandStatus.BRAND_AUDITREFUSED) {
			long current = System.currentTimeMillis();
			supplierBrandToAudit.setStatus(BrandStatus.BRAND_AUDITING);
			supplierBrandToAudit.setStatusUpdateDate(current);
			supplierBrandToAudit.setRejectReason("");
			supplierBrandDao.updateBrandStatus(supplierBrandToAudit);
			List<SupplierBrandImg> supplierImgList = supplierBrandImgDao.getBrandVisualImgList(supplierBrandToAudit
					.getSupplierBrandId());
			String supplierImgUrl = null;
			if (supplierImgList.size() > 0) {
				supplierImgUrl = supplierImgList.get(0).getImageUrl();
			}
			return getSupplierBrandDTO(supplierBrandToAudit, supplierImgUrl, null, null);
		} else {
			logger.error("the audit supplierBrandToOnline status != BRAND_NEW");
			return null;
		}
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public boolean deleteSupplierBrand(long delId) {
		logger.debug("deleteSupplierBrand delId = " + delId);
		SupplierBrand supplierBrandToDel = supplierBrandDao.getObjectById(delId);
		if (supplierBrandToDel.getStatus() == BrandStatus.BRAND_AUDITREFUSED) {
			if (supplierBrandDao.deleteById(delId)) {
				// 如果有关联的品牌门店和品牌相关图片也要删掉
				List<BrandShop> brandShopList = brandShopDao.getBrandShopListByBrandSupplierId(delId, false);
				for (BrandShop brandShop : brandShopList) {
					brandShopDao.deleteById(brandShop.getBrandShopId());
				}
				List<SupplierBrandImg> supplierImgList = supplierBrandImgDao.getBrandVisualImgList(delId);
				List<SupplierBrandImg> showCaseImgList = supplierBrandImgDao.getBrandShowCaseImgList(delId);
				for (SupplierBrandImg supplierImg : supplierImgList) {
					supplierBrandImgDao.deleteById(supplierImg.getImgId());
				}
				for (SupplierBrandImg showCaseImg : showCaseImgList) {
					supplierBrandDao.deleteById(showCaseImg.getImgId());
				}
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public BrandShopDTO addNewBrandShop(BrandShopDTO brandShopDTO) {
		logger.debug("addNewBrandShop called");
		long current = System.currentTimeMillis();
		BrandShop brandShop = brandShopDTO.changeDataIntoBrandShop();
		brandShop.setBrandShopId(-1);
		brandShop.setShopUpdateDate(current);
		brandShop.setStatusUpdateDate(current);
		brandShop.setStatus(BrandShopStatus.SHOP_NEW);
		BrandShop brandShopOut = brandShopDao.addObject(brandShop);
		return brandShopOut.changeDataIntoBrandShopDTO();
	}

	@Override
	public List<BrandShopDTO> getBrandShopListBySupplierId(long supplierId) {
		List<BrandShop> brandShopList = new ArrayList<>();
		SupplierBrand supplierBrand = supplierBrandDao.getOnlineSupplierBrandBySupplierId(supplierId);
		if (supplierBrand != null) {
			brandShopList = brandShopDao.getBrandShopListByBrandSupplierId(supplierBrand.getSupplierBrandId(), true);
		}
		List<BrandShopDTO> out = new ArrayList<>(brandShopList.size());
		for (BrandShop brandShop : brandShopList) {
			out.add(brandShop.changeDataIntoBrandShopDTO());
		}
		return out;
	}

	@Override
	public SupplierBrandFullDTO getSupplierBrandFullDetails(long supplierBrandId, boolean showShops,
			boolean onlyShowUnderUsingBrandShop) {
		logger.debug("getSupplierBrandFullDetails supplierBrandId = " + supplierBrandId);
		SupplierBrandFullDTO fullDTO = new SupplierBrandFullDTO();
		SupplierBrand supplierBrand = supplierBrandDao.getObjectById(supplierBrandId);
		fullDTO.setBrandId(supplierBrand.getBrandId());
		List<BrandShop> brandShopList = null;
		if (showShops) {
			brandShopList = brandShopDao
					.getBrandShopListByBrandSupplierId(supplierBrandId, onlyShowUnderUsingBrandShop);
		}
		List<SupplierBrandImg> supplierImgList = supplierBrandImgDao.getBrandVisualImgList(supplierBrand
				.getSupplierBrandId());
		List<SupplierBrandImg> showCaseImgList = supplierBrandImgDao.getBrandShowCaseImgList(supplierBrand
				.getSupplierBrandId());
		String supplierImgUrl = null;
		if (supplierImgList.size() > 0) {
			supplierImgUrl = supplierImgList.get(0).getImageUrl();
		}
		fullDTO.setBasic(getSupplierBrandDTO(supplierBrand, supplierImgUrl, supplierImgList, showCaseImgList));
		int brandShopSize = 0;
		if (brandShopList != null) {
			brandShopSize = brandShopList.size();
			List<BrandShopDTO> brandShopDTOList = new ArrayList<>(brandShopSize);
			for (BrandShop brandShop : brandShopList) {
				brandShopDTOList.add(brandShop.changeDataIntoBrandShopDTO());
			}
			fullDTO.setShops(brandShopDTOList);
		}
		return fullDTO;
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public BrandShopDTO activeBrandShop(long id) {
		logger.debug("activeBrandShop id = " + id);
		BrandShop brandShop = brandShopDao.getObjectById(id);
		if (brandShop.getStatus() == BrandShopStatus.SHOP_NEW || brandShop.getStatus() == BrandShopStatus.SHOP_STOPED) {
			brandShop.setStatus(BrandShopStatus.SHOP_USING);
			brandShopDao.updateBrandShopStatus(brandShop);
			return brandShop.changeDataIntoBrandShopDTO();
		} else {
			logger.error("activeBrandShop id = " + id + " brandShop.getStatus() = " + brandShop.getStatus().toString());
			return null;
		}
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public BrandShopDTO stopBrandShop(long id) {
		logger.debug("stopBrandShop id = " + id);
		BrandShop brandShop = brandShopDao.getObjectById(id);
		if (brandShop.getStatus() == BrandShopStatus.SHOP_USING) {
			brandShop.setStatus(BrandShopStatus.SHOP_STOPED);
			brandShopDao.updateBrandShopStatus(brandShop);
			return brandShop.changeDataIntoBrandShopDTO();
		} else {
			logger.error("stopBrandShop id = " + id + " brandShop.getStatus() = " + brandShop.getStatus().toString());
			return null;
		}
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public boolean delBrandShop(long id) {
		logger.debug("delBrandShop id = " + id);
		BrandShop brandShop = brandShopDao.getObjectById(id);
		if (brandShop.getStatus() != BrandShopStatus.SHOP_USING) {
			if (brandShopDao.deleteById(id)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public BrandItemDTO submitBrandItem(BrandItemDTO dto, String userName) {
		logger.debug("submitBrandItem called");
		if (StringUtils.isBlank(dto.getBrandNameZh()) && StringUtils.isBlank(dto.getBrandNameEn())) {
			return null;
		}
		if (dto.getBrandId() <= 0 && brandDao.isBrandExist(dto.getBrandNameZh(), dto.getBrandNameEn())) {
			return null;
		}
		Brand brand = new Brand();
		long current = System.currentTimeMillis();
		String zhName = dto.getBrandNameZh();
		brand.setBrandNameZh(zhName);
		String enName = dto.getBrandNameEn();
		brand.setBrandNameEn(enName);
		String head = null;
		if (enName != null && enName.length() > 0) {
			char c = enName.toUpperCase().charAt(0);
			if (c >= 'A' && c <= 'Z') {
				head = String.valueOf(c);
			}
		}
		brand.setBrandNameHead(head);
		head = null;
		// 中文名字如果是英文开头，英文也会算入拼音里面
		if (zhName != null && zhName.length() > 0) {
			char c = zhName.toUpperCase().charAt(0);
			boolean bEnChar = (c >= 'A' && c <= 'Z');
			boolean bNumber = (c >= '0' && c <= '9');
			if (!bNumber && !bEnChar) {
				String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c);
				if (pinyin != null && pinyin.length > 0 && pinyin[0].length() > 0) {
					head = String.valueOf(pinyin[0].charAt(0)).toUpperCase();
				}
			} else if (bEnChar) {
				head = String.valueOf(c);
			}
		}
		brand.setBrandNameHeadPinYin(head);
		brand.setCreateUser(userName);
		brand.setBrandUpdateDate(current);
		BrandProbability probability = BrandProbability.CATEGORY_TYPE_A.genEnumByIntValue(dto.getBrandProbability());
		brand.setBrandProbability(probability);
		if (dto.getBrandId() <= 0) { // id为空说明是新建
			if ((dto.getLogo() == null || dto.getLogo().trim().length() == 0)
					|| (dto.getBrandVisualImgWeb() == null || dto.getBrandVisualImgWeb().trim().length() == 0)
					|| (dto.getBrandVisualImgApp() == null || dto.getBrandVisualImgApp().trim().length() == 0)) {
				return null;
			}
			logger.debug("new BrandItem");
//			brand.setBrandId(-1);
			brand.setCreateDate(current);
			long brandId = brandDao.allocateRecordId();
			if (brandId < 1l) {
				throw new DBSupportRuntimeException("Get generateId failed!");
			}
			brand.setBrandId(brandId);
			brandDao.addObject(brand);
			brandLogoImgDao.addNewBrandLogo(brandId, dto.getLogo(), dto.getBrandVisualImgWeb(),
					dto.getBrandVisualImgApp());
		} else {
			logger.debug("update BrandItem brandId = " + dto.getBrandId());
			brand.setBrandId(dto.getBrandId());
			Brand oldBrand = brandDao.getObjectById(brand.getBrandId());
			brand.setCreateDate(oldBrand.getCreateDate());
			brandDao.updateObjectByKey(brand);
			brandDao.updateBrandNameforSale(brand.getBrandId(), brand.getBrandNameZh(), brand.getBrandNameEn());
			brandDao.updateBrandName(brand.getBrandNameHead(), brand.getBrandNameHeadPinYin(), brand.getBrandId());
			brandLogoImgDao.setNewBrandLogo(brand.getBrandId(), dto.getLogo(), dto.getBrandVisualImgWeb(),
					dto.getBrandVisualImgApp());
		}
		return getBrandItemDTOByBrandId(dto.getBrandId(), 0);
	}

	@Cacheable(value = "brandCache")
	@Override
	public RetArg getAllBrandItemList(DDBParam param, long time) {
		logger.debug("getAllBrandItemList called");
		List<Brand> brandList = brandDao.getAllBrand(param, time);
		List<BrandItemDTO> dtoList = new ArrayList<BrandItemDTO>(brandList.size());
		List<Long> idsList = new ArrayList<>(brandList.size());
		for (Brand brand : brandList) {
			idsList.add(brand.getBrandId());
		}
		Map<Long, BrandLogoImg> logoMap = brandLogoImgDao.getBrandLogoListByBrandIdList(idsList,
				BrandImgSize.SIZE_BRAND_LOGO);
		Map<Long, BrandLogoImg> visualWebMap = brandLogoImgDao.getBrandLogoListByBrandIdList(idsList,
				BrandImgSize.SIZE_BRAND_VISUAL_WEB);
		Map<Long, BrandLogoImg> visualAppMap = brandLogoImgDao.getBrandLogoListByBrandIdList(idsList,
				BrandImgSize.SIZE_BRAND_VISUAL_APP);
		for (Brand brand : brandList) {
			BrandLogoImg logo = logoMap.get(brand.getBrandId());
			BrandLogoImg visualWeb = visualWebMap.get(brand.getBrandId());
			BrandLogoImg visualApp = visualAppMap.get(brand.getBrandId());
			BrandItemDTO dto = new BrandItemDTO(brand);
			dto.setBrandNameZh(getCombinedBrandName(brand.getBrandNameEn(), brand.getBrandNameZh()));
			dto.setBrandProbability(brand.getBrandProbability().getIntValue());
			dto.setLogo(logo != null ? logo.getBrandImgUrl() : null);
			dto.setBrandVisualImgWeb(visualWeb != null ? visualWeb.getBrandImgUrl() : null);
			dto.setBrandVisualImgApp(visualApp != null ? visualApp.getBrandImgUrl() : null);
			dtoList.add(dto);
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, param);
		RetArgUtil.put(retArg, dtoList);
		return retArg;
	}

	@Cacheable(value = "brandCache")
	@Override
	public List<BrandItemDTO> getRecommendBrandItemList(long areaId, long userId, int recCount, boolean isApp) {
		logger.debug("getRecommendBrandItemList called");
		List<Brand> recbrandList = brandDao.getRecommendBrands(areaId, recCount);
		List<Long> brandIdList = new ArrayList<>(recbrandList.size());
		for (Brand brand : recbrandList) {
			brandIdList.add(brand.getBrandId());
		}
		Map<Long, BrandLogoImg> logoMap = brandLogoImgDao.getBrandLogoListByBrandIdList(brandIdList,
				BrandImgSize.SIZE_BRAND_LOGO);
		Map<Long, Boolean> state = null;
		if (userId > 0) {
			state = brandUserFavDao.getBrandFavStateByIds(brandIdList, userId);
		}
		Map<Long, Integer> brandFavCountMap = brandUserFavDao.getBrandFavCountByIds(brandIdList);
		Map<Long, BrandLogoImg> visualWebMap = null;
		Map<Long, BrandLogoImg> visualAppMap = null;
		if (isApp) {
			visualAppMap = brandLogoImgDao.getBrandLogoListByBrandIdList(brandIdList,
					BrandImgSize.SIZE_BRAND_VISUAL_APP);
		} else {
			visualWebMap = brandLogoImgDao.getBrandLogoListByBrandIdList(brandIdList,
					BrandImgSize.SIZE_BRAND_VISUAL_WEB);
		}
		List<BrandItemDTO> dtoList = new ArrayList<BrandItemDTO>(recbrandList.size());
		for (Brand brand : recbrandList) {
			BrandLogoImg logo = logoMap.get(brand.getBrandId());
			BrandItemDTO dto = new BrandItemDTO(brand);
			if (isApp) {
				BrandLogoImg visualApp = visualAppMap.get(brand.getBrandId());
				dto.setBrandVisualImgApp(visualApp != null ? visualApp.getBrandImgUrl() : null);
			} else {
				BrandLogoImg visualWeb = visualWebMap.get(brand.getBrandId());
				dto.setBrandVisualImgWeb(visualWeb != null ? visualWeb.getBrandImgUrl() : null);
			}
			if (brandFavCountMap.containsKey(brand.getBrandId())) {
				dto.setFavCount(brandFavCountMap.get(brand.getBrandId()));
			} else {
				dto.setFavCount(0);
			}
			dto.setFavorited(state != null ? state.get(brand.getBrandId()) : false);
			dto.setBrandProbability(brand.getBrandProbability().getIntValue());
			dto.setLogo(logo != null ? logo.getBrandImgUrl() : null);
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public RetArg searchBrand(String name, DDBParam param) {
		logger.debug("searchBrand called key = " + name);
		List<Brand> brandList = brandDao.searchBrand(name, param);
		if (brandList == null) {
			return null;
		}
		List<Long> idsList = new ArrayList<>(brandList.size());
		for (Brand brand : brandList) {
			idsList.add(brand.getBrandId());
		}
		Map<Long, BrandLogoImg> logoMap = brandLogoImgDao.getBrandLogoListByBrandIdList(idsList,
				BrandImgSize.SIZE_BRAND_LOGO);
		Map<Long, BrandLogoImg> visualWebMap = brandLogoImgDao.getBrandLogoListByBrandIdList(idsList,
				BrandImgSize.SIZE_BRAND_VISUAL_WEB);
		Map<Long, BrandLogoImg> visualAppMap = brandLogoImgDao.getBrandLogoListByBrandIdList(idsList,
				BrandImgSize.SIZE_BRAND_VISUAL_APP);
		List<BrandDTO> outDtoList = new ArrayList<BrandDTO>(brandList.size());
		for (Brand brand : brandList) {
			BrandLogoImg logo = logoMap.get(brand.getBrandId());
			BrandLogoImg visualWeb = visualWebMap.get(brand.getBrandId());
			BrandLogoImg visualApp = visualAppMap.get(brand.getBrandId());
			BrandDTO dto = new BrandDTO();
			dto.setLogo(logo != null ? logo.getBrandImgUrl() : null);
			dto.setVisualImgWeb(visualWeb != null ? visualWeb.getBrandImgUrl() : null);
			dto.setVisualImgApp(visualApp != null ? visualApp.getBrandImgUrl() : null);
			outDtoList.add(dto);
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, param);
		RetArgUtil.put(retArg, outDtoList);
		return retArg;
	}

	@Override
	public RetArg searchBrandItem(String name, DDBParam param) {
		logger.debug("searchBrandItem name = " + name);
		List<Brand> brandList = brandDao.searchBrand(name, param);
		if (brandList == null) {
			return null;
		}
		List<Long> idsList = new ArrayList<>(brandList.size());
		for (Brand brand : brandList) {
			idsList.add(brand.getBrandId());
		}
		Map<Long, BrandLogoImg> logoMap = brandLogoImgDao.getBrandLogoListByBrandIdList(idsList,
				BrandImgSize.SIZE_BRAND_LOGO);
		Map<Long, BrandLogoImg> visualWebMap = brandLogoImgDao.getBrandLogoListByBrandIdList(idsList,
				BrandImgSize.SIZE_BRAND_VISUAL_WEB);
		Map<Long, BrandLogoImg> visualAppMap = brandLogoImgDao.getBrandLogoListByBrandIdList(idsList,
				BrandImgSize.SIZE_BRAND_VISUAL_APP);
		List<BrandItemDTO> dtoList = new ArrayList<BrandItemDTO>(brandList.size());
		for (Brand brand : brandList) {
			BrandLogoImg logo = logoMap.get(brand.getBrandId());
			BrandLogoImg visualWeb = visualWebMap.get(brand.getBrandId());
			BrandLogoImg visualApp = visualAppMap.get(brand.getBrandId());
			BrandItemDTO dto = new BrandItemDTO(brand);
			dto.setBrandProbability(brand.getBrandProbability().getIntValue());
			dto.setLogo(logo != null ? logo.getBrandImgUrl() : null);
			dto.setBrandVisualImgWeb(visualWeb != null ? visualWeb.getBrandImgUrl() : null);
			dto.setBrandVisualImgApp(visualApp != null ? visualApp.getBrandImgUrl() : null);
			dtoList.add(dto);
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, param);
		RetArgUtil.put(retArg, dtoList);
		return retArg;
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public boolean delBrand(long brandId) {
		logger.debug("delBrand brandId = " + brandId);
		Brand brand = brandDao.getObjectById(brandId);
		if (brand != null) {
			if (brandLogoImgDao.delBrandLogoListByBrandId(brandId)) {
				return brandDao.deleteById(brand.getBrandId());
			}
		}
		return false;
	}

	@Override
	public List<BrandLogoImg> getBrandLogoImgs(long brandId) {
		logger.debug("getBrandLogoImgs brandId = " + brandId);
		List<BrandLogoImg> ret = new ArrayList<BrandLogoImg>(3);
		ret.add(brandLogoImgDao.getBrandLogoByBrandId(brandId, BrandImgSize.SIZE_BRAND_LOGO));
		ret.add(brandLogoImgDao.getBrandLogoByBrandId(brandId, BrandImgSize.SIZE_BRAND_VISUAL_WEB));
		ret.add(brandLogoImgDao.getBrandLogoByBrandId(brandId, BrandImgSize.SIZE_BRAND_VISUAL_APP));
		return ret;
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public boolean deleteBrandVisualImg(long id, long index) {
		logger.debug("deleteBrandVisualImg id = " + id + " index = " + index);
		return supplierBrandImgDao.delSupplierBrandVisualImg(id, index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RetArg searchAuditBrand(List<Long> businessIds, String key, int status, DDBParam param) {
		logger.debug("searchAuditBrand key = " + key);
		// businessIds传递供应商的id列表 status决定是审核过的
		RetArg retArg = supplierBrandDao.searchAuditBrand(businessIds, status, key, param);
		List<SupplierBrand> supplierBrandList = RetArgUtil.get(retArg, ArrayList.class);
		param = RetArgUtil.get(retArg, DDBParam.class);
		List<SupplierBrandDTO> out = new ArrayList<SupplierBrandDTO>(supplierBrandList.size());

		for (SupplierBrand supplierBrand : supplierBrandList) {
			List<SupplierBrandImg> supplierImgList = supplierBrandImgDao.getBrandVisualImgList(supplierBrand
					.getSupplierBrandId());
			String supplierImgUrl = null;
			if (supplierImgList.size() > 0) {
				supplierImgUrl = supplierImgList.get(0).getImageUrl();
			}
			out.add(getSupplierBrandDTO(supplierBrand, supplierImgUrl, null, null));
		}
		RetArg ret = new RetArg();
		RetArgUtil.put(ret, param);
		RetArgUtil.put(ret, out);
		return ret;
	}

	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public List<Long> passAuditBrand(List<Long> ids) {
		logger.debug("passAuditBrand ids " + ids.toString());
		List<Long> failed = new ArrayList<>();
		for (Long id : ids) {
			SupplierBrand supplierBrand = supplierBrandDao.getObjectById(id);
			if (supplierBrand.getStatus() == BrandStatus.BRAND_AUDITING) {
				supplierBrand.setStatus(BrandStatus.BRAND_AUDITPASSED_UNUSED);
				supplierBrand.setStatusUpdateDate(System.currentTimeMillis());
				supplierBrand.setRejectReason("");
				supplierBrandDao.updateBrandStatus(supplierBrand);
			} else {
				failed.add(id);
			}
		}
		return failed;
	}

	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public List<Long> rejectAuditBrand(List<Long> ids, String reason) {
		logger.debug("rejectAuditBrand ids " + ids.toString() + " reason = " + reason);
		List<Long> failed = new ArrayList<>();
		for (Long id : ids) {
			SupplierBrand supplierBrand = supplierBrandDao.getObjectById(id);
			if (supplierBrand.getStatus() == BrandStatus.BRAND_AUDITING
					|| supplierBrand.getStatus() == BrandStatus.BRAND_AUDITPASSED_UNUSED
					|| supplierBrand.getStatus() == BrandStatus.BRAND_AUDITPASSED_USING) {
				supplierBrand.setStatus(BrandStatus.BRAND_AUDITREFUSED);
				supplierBrand.setRejectReason(reason);
				supplierBrand.setStatusUpdateDate(System.currentTimeMillis());
				supplierBrandDao.updateBrandStatus(supplierBrand);
			} else {
				failed.add(id);
			}
		}
		return failed;
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public BrandDTO getBrandBySupplierId(long id) {
	// DDBParam param = new DDBParam("supplierBrandId", true, 0, 0);
	// RetArg retArg = supplierBrandDao.getSupplierBrandListBySupplierId(param,
	// id);
	// List<SupplierBrand> supplierBrandList = RetArgUtil.get(retArg,
	// ArrayList.class);
	// if (supplierBrandList.size() > 0) {
	// SupplierBrand supplierBrand = supplierBrandList.get(0);
	// return getBrandByBrandId(supplierBrand.getBrandId());
	// }
	// return null;
	// }

	@Override
	public List<BrandDTO> getBrandListBySupplierIdList(List<Long> supplierIdList) {
		return supplierBrandDao.getBrandsBySupplerIdList(supplierIdList);
	}

	@Cacheable(value = "brandCache")
	@Override
	public List<BrandShopDTO> getBrandShops(long supplierId) {
		List<BrandShop> ret = brandShopDao.getBrandShopListByByBrandSupplierId(supplierId);
		List<BrandShopDTO> out = new ArrayList<>(ret.size());
		for (BrandShop brandShop : ret) {
			out.add(brandShop.changeDataIntoBrandShopDTO());
		}
		return out;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.saleschedule.service.BrandService#getSupplierBrandFullDTOBySupplierId(long)
	 */
	@Cacheable(value = "brandCache")
	@Override
	public SupplierBrandFullDTO getSupplierBrandFullDTOBySupplierId(long supplierId) {
		SupplierBrand supplierBrand = supplierBrandDao.getOnlineSupplierBrandBySupplierId(supplierId);
		if (supplierBrand != null) {
			long supplierBrandId = supplierBrand.getSupplierBrandId();
			boolean showShops = true;
			SupplierBrandFullDTO fullDTO = getSupplierBrandFullDetails(supplierBrandId, showShops, true);
			return fullDTO;
		}
		return null;
	}

	@Override
	public SupplierBrandFullDTO getSupplierBrandFullDTOBySupplierId(long supplierId, long userId, boolean showShops,
			boolean bMobile) {
		SupplierBrand supplierBrand = supplierBrandDao.getOnlineSupplierBrandBySupplierId(supplierId);
		if (supplierBrand != null) {
			long supplierBrandId = supplierBrand.getSupplierBrandId();
			SupplierBrandFullDTO fullDTO = getSupplierBrandFullDetails(supplierBrandId, showShops, true);
			fullDTO.setFavByUser(brandUserFavDao.isBrandInFavList(userId, supplierBrand.getBrandId()));
			if (bMobile) {
				long brandId = fullDTO.getBrandId();
				BrandLogoImg brandVisualImgApp = brandLogoImgDao.getBrandLogoByBrandId(brandId,
						BrandImgSize.SIZE_BRAND_VISUAL_APP);
				fullDTO.setBrandVisualImgApp(brandVisualImgApp.getBrandImgUrl());
			}
			return fullDTO;
		}
		return null;
	}

	@Override
	public boolean getBrandCollectionState(long userId, long brandId) {
		return brandUserFavDao.isBrandInFavList(userId, brandId);
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public boolean addBrandCollection(long userId, long brandId) {
		return brandUserFavDao.addBrandIntoFavList(userId, brandId);
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public boolean removeBrandCollection(long userId, long brandId) {
		return brandUserFavDao.removeBrandFromFavList(userId, brandId);
	}

	// 接口由于千人千面的关系，这里作废，处理的逻辑移动到controller里面
	private RetArg getOnlineScheduleSizeAndNextPoTime(List<ScheduleDTO> dtoList) {
		int onlineSize = 0;
		long nextPoTime = 0;
		long nextPoEndTime = 0;
		RetArg retArg = new RetArg();
		long current = System.currentTimeMillis();
		for (ScheduleDTO dto : dtoList) {
			long startTime = dto.getSchedule().getStartTime();
			long endTime = dto.getSchedule().getEndTime();
			if (startTime <= current && endTime >= current) {
				onlineSize++;
			} else if (startTime > current) {
				if (nextPoTime == 0) {
					nextPoTime = startTime;
					nextPoEndTime = endTime;
				} else if (nextPoTime > startTime) {
					nextPoTime = startTime;
					nextPoEndTime = endTime;
				}
			}
		}
		RetArgUtil.put(retArg, onlineSize);
		if (onlineSize == 0 && nextPoTime - current <= 4 * 24 * 3600 * 1000L) {
			RetArgUtil.put(retArg, nextPoTime, 0);
			RetArgUtil.put(retArg, nextPoEndTime, 1);
		} else {
			RetArgUtil.put(retArg, 0L, 0);
			RetArgUtil.put(retArg, 0L, 1);
		}
		return retArg;
	}

	@Cacheable(value = "brandCache")
	@Override
	public RetArg getUserFavBrandList(DDBParam param, long time, long userId, long areaId, boolean showDetail,
			boolean bApp) {
		logger.debug("getUserFavBrandList called");
		RetArg retArg = new RetArg();
		List<BrandUserFav> list = brandUserFavDao.getBrandUserFavListByUserId(param, userId, time);
		if (list == null || list.size() == 0) {
			List<BrandItemDTO> dtoList = new ArrayList<BrandItemDTO>();
			RetArgUtil.put(retArg, param);
			RetArgUtil.put(retArg, dtoList);
			return retArg;
		}
		List<Long> ids = new ArrayList<>(list.size());
		for (BrandUserFav fav : list) {
			ids.add(fav.getBrandId());
		}
		List<Brand> brandList = brandDao.getBrandListByBrandIdList(ids);
		List<BrandItemDTO> dtoList = new ArrayList<BrandItemDTO>(brandList.size());
		Map<Long, BrandLogoImg> logoMap = brandLogoImgDao.getBrandLogoListByBrandIdList(ids,
				BrandImgSize.SIZE_BRAND_LOGO);
		Map<Long, BrandLogoImg> visualWebMap = new HashMap<>();
		Map<Long, BrandLogoImg> visualAppMap = new HashMap<>();
		Map<Long, Long> favTimeMap = new HashMap<>();
		if (!bApp) {
			visualWebMap = brandLogoImgDao.getBrandLogoListByBrandIdList(ids, BrandImgSize.SIZE_BRAND_VISUAL_WEB);
		} else {
			visualAppMap = brandLogoImgDao.getBrandLogoListByBrandIdList(ids, BrandImgSize.SIZE_BRAND_VISUAL_APP);
			for (BrandUserFav fav : list) {
				favTimeMap.put(fav.getBrandId(), fav.getCreateDate());
			}
		}
		Map<Long, Boolean> state = null;
		Map<Long, Integer> brandFavCountMap = new HashMap<>();
		Map<Long, List<ScheduleDTO>> poResultMap = new HashMap<Long, List<ScheduleDTO>>();
		if (areaId > 0) {
			ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
			paramDTO.brandIdList = ids;
			paramDTO.curSupplierAreaId = areaId;
			try {
				paramDTO.saleSiteFlag = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
			} catch (Exception e) {
				paramDTO.saleSiteFlag = 0;
			}
			POListDTO poList = scheduleDao.getScheduleListByBrandIdList(paramDTO);
			for (PODTO po : poList.getPoList()) {
				Schedule s = po.getScheduleDTO().getSchedule();
				long brandId = s.getBrandId();
				if (poResultMap.get(brandId) == null) {
					poResultMap.put(brandId, new ArrayList<ScheduleDTO>());
				}
				ScheduleDTO dto = new ScheduleDTO();
				dto.setSchedule(s);
				poResultMap.get(brandId).add(dto);
			}
		}
		if (showDetail) {
			if (userId > 0) {
				state = brandUserFavDao.getBrandFavStateByIds(ids, userId);
			}
			brandFavCountMap = brandUserFavDao.getBrandFavCountByIds(ids);
		}
		for (Brand brand : brandList) {
			BrandItemDTO dto = new BrandItemDTO(brand);
			BrandLogoImg logo = logoMap.get(brand.getBrandId());
			if (!bApp) {
				BrandLogoImg visualWeb = visualWebMap.get(brand.getBrandId());
				dto.setBrandVisualImgWeb(visualWeb != null ? visualWeb.getBrandImgUrl() : null);
			} else {
				BrandLogoImg visualApp = visualAppMap.get(brand.getBrandId());
				Long favTime = favTimeMap.get(brand.getBrandId());
				dto.setFavTime(favTime != null ? favTime : 0);
				dto.setBrandVisualImgApp(visualApp != null ? visualApp.getBrandImgUrl() : null);
			}
			dto.setLogo(logo != null ? logo.getBrandImgUrl() : null);
			if (poResultMap.get(brand.getBrandId()) == null) {
				dto.setScheduleId(0);
				dto.setPoCount(0);
				dto.setNextPoTime(0);
				dto.setNextPoEndTime(0);
			} else {
				List<ScheduleDTO> resDtos = poResultMap.get(brand.getBrandId());
				dto.setScheduleId(resDtos.get(0).getSchedule().getId());
				RetArg retArgPO = getOnlineScheduleSizeAndNextPoTime(resDtos);
				dto.setPoCount(RetArgUtil.get(retArgPO, Integer.class));
				dto.setNextPoTime(RetArgUtil.get(retArgPO, Long.class, 0));
				dto.setNextPoEndTime(RetArgUtil.get(retArgPO, Long.class, 1));
			}
			if (showDetail) {
				dto.setFavorited(state != null ? state.get(brand.getBrandId()) : false);
				if (brandFavCountMap.containsKey(brand.getBrandId())) {
					dto.setFavCount(brandFavCountMap.get(brand.getBrandId()));
				} else {
					dto.setFavCount(0);
				}
			}
			dtoList.add(dto);
		}
		RetArgUtil.put(retArg, param);
		RetArgUtil.put(retArg, dtoList);
		return retArg;
	}

	@Transaction
	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public BrandShopDTO editBrandShop(BrandShopDTO brandShop) {
		logger.debug("Update BrandShopid = " + brandShop.getBrandShopId());
		BrandShop shop = brandShop.changeDataIntoBrandShop();
		if (brandShopDao.updateObjectByKey(shop)) {
			BrandShop newShop = brandShopDao.getObjectById(shop.getBrandShopId());
			return newShop.changeDataIntoBrandShopDTO();
		} else {
			logger.debug("update BrandShopid = " + brandShop.getBrandShopId() + " failed");
			return null;
		}
	}

	@Cacheable(value = "brandCache")
	@Override
	public RetArg getAllBrandItemListWithDetails(DDBParam param, long time, long userId, String begin, boolean bApp,
			long areaId) {
		List<Brand> brandList = brandDao.getBrandListByIndex(param, time, begin, areaId);
		List<BrandItemDTO> dtoList = new ArrayList<>(brandList.size());
		if (brandList.size() > 0) {
			List<Long> ids = new ArrayList<>(brandList.size());
			for (Brand brand : brandList) {
				ids.add(brand.getBrandId());
			}
			Map<Long, BrandLogoImg> logoMap = brandLogoImgDao.getBrandLogoListByBrandIdList(ids,
					BrandImgSize.SIZE_BRAND_LOGO);
			Map<Long, BrandLogoImg> visualWebMap = new HashMap<Long, BrandLogoImg>();
			Map<Long, BrandLogoImg> visualAppMap = new HashMap<Long, BrandLogoImg>();
			Map<Long, Integer> brandFavCountMap = new HashMap<>();
			if (!bApp) {
				visualWebMap = brandLogoImgDao.getBrandLogoListByBrandIdList(ids, BrandImgSize.SIZE_BRAND_VISUAL_WEB);
			} else {
				visualAppMap = brandLogoImgDao.getBrandLogoListByBrandIdList(ids, BrandImgSize.SIZE_BRAND_VISUAL_APP);
				brandFavCountMap = brandUserFavDao.getBrandFavCountByIds(ids);
			}
			Map<Long, Boolean> state = null;
			if (userId > 0) {
				state = brandUserFavDao.getBrandFavStateByIds(ids, userId);
			}

			for (Brand brand : brandList) {
				BrandItemDTO dto = new BrandItemDTO(brand);
				dto.setFavorited(state != null ? state.get(brand.getBrandId()) : false);
				BrandLogoImg logo = logoMap.get(brand.getBrandId());
				if (!bApp) {
					BrandLogoImg visualWeb = visualWebMap.get(brand.getBrandId());
					dto.setBrandVisualImgWeb(visualWeb != null ? visualWeb.getBrandImgUrl() : null);
				} else {
					BrandLogoImg visualApp = visualAppMap.get(brand.getBrandId());
					dto.setBrandVisualImgApp(visualApp != null ? visualApp.getBrandImgUrl() : null);
					if (brandFavCountMap.containsKey(brand.getBrandId())) {
						dto.setFavCount(brandFavCountMap.get(brand.getBrandId()));
					} else {
						dto.setFavCount(0);
					}
				}
				dto.setLogo(logo != null ? logo.getBrandImgUrl() : null);
				dtoList.add(dto);
			}
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, param);
		RetArgUtil.put(retArg, dtoList);
		return retArg;
	}

	@Cacheable(value = "brandCache")
	@Override
	public boolean hasActiveForBrandLike(long userId, long areaId, long startTime, long endTime) {
		return brandUserFavDao.hasActiveForBrandLike(userId, areaId, startTime, endTime);
	}

	@Override
	public BrandItemDTO getBrandItemDTOByBrandId(long brandId, long userId) {
		Brand brand = brandDao.getObjectById(brandId);
		if (brand == null) {
			return new BrandItemDTO();
		}
		BrandItemDTO dto = new BrandItemDTO(brand);
		BrandLogoImg logo = brandLogoImgDao.getBrandLogoByBrandId(brandId, BrandImgSize.SIZE_BRAND_LOGO);
		BrandLogoImg visualWeb = brandLogoImgDao.getBrandLogoByBrandId(brandId, BrandImgSize.SIZE_BRAND_VISUAL_WEB);
		BrandLogoImg visualApp = brandLogoImgDao.getBrandLogoByBrandId(brandId, BrandImgSize.SIZE_BRAND_VISUAL_APP);
		if (userId > 0) {
			dto.setFavorited(brandUserFavDao.isBrandInFavList(userId, brandId));
			dto.setFavCount(brandUserFavDao.getBrandFavCount(brandId));
		}
		dto.setBrandProbability(brand.getBrandProbability().getIntValue());
		dto.setLogo(logo != null ? logo.getBrandImgUrl() : null);
		dto.setBrandVisualImgWeb(visualWeb != null ? visualWeb.getBrandImgUrl() : null);
		dto.setBrandVisualImgApp(visualApp != null ? visualApp.getBrandImgUrl() : null);
		return dto;
	}

	@Override
	public Map<Long, Integer> getAuditBrandCountsBySupplierList(List<Long> supplierIdList) {
		return supplierBrandDao.getAuditBrandCountsBySupplierList(supplierIdList);
	}

	@Override
	public Map<Long, Integer> getAuditBrandCountsByAreaList(List<Long> areaIdList) {
		return supplierBrandDao.getAuditBrandCountsByAreaList(areaIdList);
	}

	@Cacheable(value = "brandCache")
	@Override
	public boolean isBrandInFavList(long userId, long brandId) {
		return brandUserFavDao.isBrandInFavList(userId, brandId);
	}

	@Override
	public Map<Long, Boolean> getBrandFavStateByIds(List<Long> brandIdList, long userId) {
		return brandUserFavDao.getBrandFavStateByIds(brandIdList, userId);
	}

	@Cacheable(value = "brandCache")
	@Override
	public RetArg getAllBrandForCMS(DDBParam param, String index) {
		List<Brand> brandList = brandDao.getBrandListByIndexCMS(param, index);
		List<BrandItemDTO> dtoList = new ArrayList<>(brandList.size());
		if (brandList.size() > 0) {
			List<Long> ids = new ArrayList<>(brandList.size());
			for (Brand brand : brandList) {
				ids.add(brand.getBrandId());
			}
			Map<Long, BrandLogoImg> logoMap = brandLogoImgDao.getBrandLogoListByBrandIdList(ids,
					BrandImgSize.SIZE_BRAND_LOGO);
			Map<Long, BrandLogoImg> visualWebMap = new HashMap<Long, BrandLogoImg>();
			Map<Long, BrandLogoImg> visualAppMap = new HashMap<Long, BrandLogoImg>();
			visualWebMap = brandLogoImgDao.getBrandLogoListByBrandIdList(ids, BrandImgSize.SIZE_BRAND_VISUAL_WEB);
			visualAppMap = brandLogoImgDao.getBrandLogoListByBrandIdList(ids, BrandImgSize.SIZE_BRAND_VISUAL_APP);

			for (Brand brand : brandList) {
				BrandItemDTO dto = new BrandItemDTO(brand);
				dto.setBrandProbability(brand.getBrandProbability().getIntValue());
				BrandLogoImg logo = logoMap.get(brand.getBrandId());
				BrandLogoImg visualWeb = visualWebMap.get(brand.getBrandId());
				dto.setBrandVisualImgWeb(visualWeb != null ? visualWeb.getBrandImgUrl() : null);
				BrandLogoImg visualApp = visualAppMap.get(brand.getBrandId());
				dto.setBrandVisualImgApp(visualApp != null ? visualApp.getBrandImgUrl() : null);
				dto.setLogo(logo != null ? logo.getBrandImgUrl() : null);
				dtoList.add(dto);
			}
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, param);
		RetArgUtil.put(retArg, dtoList);
		return retArg;
	}

	/**
	 * 这个函数返回按照字母索引顺序的品牌列表 排序规则依据2014/12/20号所定的规则
	 * 
	 * @param userId
	 * @param areaId
	 * @return
	 */
	@Cacheable(value = "brandCache")
	@Override
	public List<BrandItemDTO> getAllBrandForApp2(long userId, long areaId) {
		DDBParam param = new DDBParam("createDate", false, 0, 0);
		List<Brand> brandList = brandDao.getBrandListByIndex(param, 0, "ALL", areaId);
		List<BrandItemDTO> dtoList = new ArrayList<>();
		if (brandList.size() == 0) {
			return dtoList;
		}
		List<Long> ids = new ArrayList<>(brandList.size());
		for (Brand brand : brandList) {
			ids.add(brand.getBrandId());
		}
		Map<Long, BrandLogoImg> logoMap = brandLogoImgDao.getBrandLogoListByBrandIdList(ids,
				BrandImgSize.SIZE_BRAND_LOGO);
		Map<Long, BrandLogoImg> visualAppMap = brandLogoImgDao.getBrandLogoListByBrandIdList(ids,
				BrandImgSize.SIZE_BRAND_VISUAL_APP);
		// Map<Long, Integer> brandFavCountMap =
		// brandUserFavDao.getBrandFavCountByIds(ids);
		Map<Long, Boolean> state = null;
		if (userId > 0) {
			state = brandUserFavDao.getBrandFavStateByIds(ids, userId);
		}

		for (Brand brand : brandList) {
			BrandLogoImg logo = logoMap.get(brand.getBrandId());
			BrandLogoImg visualApp = visualAppMap.get(brand.getBrandId());
			Boolean bFav = (state != null ? state.get(brand.getBrandId()) : false);
			String logoUrl = (logo != null ? logo.getBrandImgUrl() : null);
			String visualAppUrl = (visualApp != null ? visualApp.getBrandImgUrl() : null);
			// int favCount = 0;
			// if (brandFavCountMap.containsKey(brand.getBrandId())) {
			// favCount = brandFavCountMap.get(brand.getBrandId());
			// }
			if (brand.getBrandNameHead() != null) {
				BrandItemDTO dto = new BrandItemDTO(brand);
				dto.setBrandVisualImgApp(visualAppUrl);
				dto.setLogo(logoUrl);
				// dto.setFavCount(favCount);
				dto.setFavorited(bFav);
				dtoList.add(dto);
			}
			if ((brand.getBrandNameHeadPinYin() != null && brand.getBrandNameHead() != null && !brand
					.getBrandNameHeadPinYin().toUpperCase().equals(brand.getBrandNameHead().toUpperCase()))
					|| (brand.getBrandNameHeadPinYin() != null && brand.getBrandNameHead() == null)) {
				BrandItemDTO dto = new BrandItemDTO(brand);
				dto.setBrandVisualImgApp(visualAppUrl);
				dto.setLogo(logoUrl);
				// dto.setFavCount(favCount);
				dto.setFavorited(bFav);
				dtoList.add(dto);
			}

			if (brand.getBrandNameHead() == null || brand.getBrandNameHeadPinYin() == null) {
				BrandItemDTO dto = new BrandItemDTO(brand);
				dto.setBrandVisualImgApp(visualAppUrl);
				dto.setLogo(logoUrl);
				// dto.setFavCount(favCount);
				dto.setFavorited(bFav);
				dtoList.add(dto);
			}
		}

		Collections.sort(dtoList, new Comparator<BrandItemDTO>() {
			@Override
			public int compare(BrandItemDTO o1, BrandItemDTO o2) {
				if (o1.getBrandHead() != null && o2.getBrandHead() != null) {
					return (o2.getBrandHead().charAt(0) < o1.getBrandHead().charAt(0) ? 1 : -1);
				} else if ((o1.getBrandHead() != null && o2.getBrandHead() == null)
						|| (o1.getBrandHead() == null && o2.getBrandHead() != null)) {
					return (o1.getBrandHead() != null ? -1 : 1);
				} else {
					return (o2.getCreateDate() < o1.getCreateDate() ? -1 : 1);
				}
			}
		});
		return dtoList;
	}

	@Cacheable(value = "brandCache")
	@Override
	public List<BrandItemDTO> getAllBrandForApp(DDBParam param, long brandIdAfter, long userId, long areaId) {
		DDBParam paramNew = new DDBParam(param.getOrderColumn(), param.isAsc(), 0, 0);
		List<Brand> brandList = brandDao.getBrandListByIndex(paramNew, 0, "ALL", areaId);
		List<Long> retIdList = new ArrayList<>();
		List<Brand> selectedbrandList = new ArrayList<>();
		boolean bFind = false;
		if (brandIdAfter != 0) {
			for (Brand brand : brandList) {
				if (bFind) {
					if (retIdList.size() < param.getLimit()) {
						selectedbrandList.add(brand);
						retIdList.add(brand.getBrandId());
						continue;
					} else {
						break;
					}
				} else if (brand.getBrandId() == brandIdAfter) {
					bFind = true;
				}
			}
		} else {
			for (Brand brand : brandList) {
				if (retIdList.size() < param.getLimit()) {
					selectedbrandList.add(brand);
					retIdList.add(brand.getBrandId());
				} else {
					break;
				}
			}
		}

		List<BrandItemDTO> dtoList = new ArrayList<>();
		if (retIdList.size() == 0) {
			return dtoList;
		}
		Map<Long, BrandLogoImg> logoMap = brandLogoImgDao.getBrandLogoListByBrandIdList(retIdList,
				BrandImgSize.SIZE_BRAND_LOGO);
		Map<Long, BrandLogoImg> visualAppMap = brandLogoImgDao.getBrandLogoListByBrandIdList(retIdList,
				BrandImgSize.SIZE_BRAND_VISUAL_APP);
		Map<Long, Integer> brandFavCountMap = brandUserFavDao.getBrandFavCountByIds(retIdList);
		Map<Long, Boolean> state = null;
		if (userId > 0) {
			state = brandUserFavDao.getBrandFavStateByIds(retIdList, userId);
		}

		for (Brand brand : selectedbrandList) {
			BrandItemDTO dto = new BrandItemDTO(brand);
			dto.setFavorited(state != null ? state.get(brand.getBrandId()) : false);
			BrandLogoImg logo = logoMap.get(brand.getBrandId());
			BrandLogoImg visualApp = visualAppMap.get(brand.getBrandId());
			dto.setBrandVisualImgApp(visualApp != null ? visualApp.getBrandImgUrl() : null);
			if (brandFavCountMap.containsKey(brand.getBrandId())) {
				dto.setFavCount(brandFavCountMap.get(brand.getBrandId()));
			} else {
				dto.setFavCount(0);
			}
			dto.setLogo(logo != null ? logo.getBrandImgUrl() : null);
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public boolean resetSupplierBrandBitmap(long supplierId, long newBitmap) {
		return supplierBrandDao.resetSupplierBrandBitmap(supplierId, newBitmap);
	}

	@Override
	public List<Brand> getBrandByName(String name) {
		return brandDao.getBrandByName(name);
	}

	@Override
	public void syncData() {
		supplierBrandDao.syncData();
	}

	@Override
	@CacheEvict(value = "brandCache", allEntries = true)
	public boolean freezeSupplierBrand(long supplierId, boolean bFreeze) {
		return supplierBrandDao.freezeSupplierBrand(supplierId, bFreeze);
	}

	@Override
	public RetArg getUserFavListByBrandIdList(List<Long> brandIdList,
			long timeAfter, int limit, int offset) {
		return brandUserFavDao.getUserFavListByBrandIdList(brandIdList, timeAfter, limit, offset);
	}

	@Cacheable(value = "brandCache")
	@Override
	public List<JSONObject> getBrandListInOrderByCategory(long areaId, List<Long> brandValue) {
		return brandDao.getBrandListInOrderByCategory(areaId, brandValue);
	}

	@Override
	public List<JSONObject> getBrandListInOrderByIds(List<Long> ids) {
		return brandDao.getBrandListInOrderByIds(ids);
	}

	@Override
	public List<JSONObject> getBrandListOrderBySKUSaleNum(List<Long> brandIds) {
		// 按序取出
		Brand[] brandArray = brandDao.getBrandBreifInfoListInListOrder(brandIds);
		if (brandArray == null || brandArray.length < 1) {
			return new ArrayList<JSONObject>(0);
		}
		List<JSONObject> retList = new ArrayList<JSONObject>();
		// 去空位，按首字母归类
		Map<String, List<Brand>> indexMap = new TreeMap<String, List<Brand>>(new Comparator<String>() {

			@Override
			public int compare(String key1, String key2) {
				// 按字母序排列 other最后
				return key1.compareTo(key2);
			}
			
		});
		List<Brand> allList = new ArrayList<Brand>();
		for (Brand brand : brandArray) {
			if (brand == null) {
				continue;
			}
			allList.add(brand);
			String index = brand.getBrandNameHeadAuto();
			List<Brand> list = null;
			if (StringUtils.isBlank(index)) {
				index = "other";
			}
			list = indexMap.get(index);
			if (list == null) {
				list = new ArrayList<Brand>();
			}
			list.add(brand);
			indexMap.put(index, list);
		}
		// 所有品牌
		JSONObject indexALL = new JSONObject();
		indexALL.put("index", "all");
		indexALL.put("list", allList);
		retList.add(indexALL);
		// 转换list
		for (Map.Entry<String, List<Brand>> entry : indexMap.entrySet()) {
			JSONObject josn = new JSONObject();
			josn.put("index", entry.getKey());
			josn.put("list", entry.getValue());
			retList.add(josn);
		}
		
		return retList;
	}
}
