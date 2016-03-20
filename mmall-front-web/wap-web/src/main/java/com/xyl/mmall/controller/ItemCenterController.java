/**
 * 
 */
package com.xyl.mmall.controller;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.NotFoundException;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.enums.SupplierType;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.CategoryFacade;
import com.xyl.mmall.cms.facade.ItemModelFacade;
import com.xyl.mmall.cms.facade.ItemSPUFacade;
import com.xyl.mmall.cms.vo.CategoryContentVO;
import com.xyl.mmall.cms.vo.CategoryNormalVO;
import com.xyl.mmall.cms.vo.ItemModelVO;
import com.xyl.mmall.cms.vo.ItemSPUVO;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.content.constants.CategoryContentLevel;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.util.ZXingUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;
import com.xyl.mmall.mainsite.facade.ItemProductFacade;
import com.xyl.mmall.mainsite.facade.MainsiteItemFacade;
import com.xyl.mmall.mainsite.util.MainsiteHelper;
import com.xyl.mmall.mainsite.vo.ProductSKUMainSiteVO;
import com.xyl.mmall.mainsite.vo.ProductSearchMainSiteVO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.SkuSPDTO;
import com.xyl.mmall.util.AreaUtils;

/**
 * @author hzlihui2014
 *
 */
@Controller
public class ItemCenterController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ItemCenterController.class);

	@Resource
	private MainsiteItemFacade itemFacade;

	@Autowired
	private MainsiteHelper mainsiteHelper;
	
	@Autowired
	private CategoryFacade categoryFacade;
	
	@Autowired
	private ItemModelFacade itemModelFacade;
	
	@Autowired
	private ItemSPUFacade itemSPUFacade;
	
	@Autowired
	private ItemProductFacade itemProductFacade;
	
	@Autowired
	private BusinessFacade businessFacade;
	
	@Autowired
	private OrderFacade orderFacade;

	@RequestMapping(value = "/search")
	public String search(Model model, @RequestParam String keyword) {
		try {
			keyword = URLDecoder.decode(keyword, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("SearchValue decode error!", e);
		}
		// 品牌筛选项
		List<JSONObject> brandList = 
				itemProductFacade.getBrands(null, keyword, AreaUtils.getAreaCode());
		model.addAttribute("brandList", brandList);
		return "pages/product/search";
	}
	
	/**
	 * 商品列表页面请求
	 * @return
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	public String list(Model model, @PathVariable String id) {
		List<Long> categoryNormalIds = new ArrayList<Long>();
		List<CategoryContentVO> retList = new ArrayList<CategoryContentVO>();
		if (StringUtils.isBlank(id)) {
			List<CategoryContentDTO> categoryContentDTOs = mainsiteHelper.getCategoryList(AreaUtils.getAreaCode());
			for(CategoryContentDTO categoryContentDTO: categoryContentDTOs) {
				retList.add(new CategoryContentVO(categoryContentDTO));
			}
		} else {
			String[] str = id.split("-");
			if (RegexUtils.isAllNumber(str[0])) {
				long categoryContentId = Long.parseLong(str[0]);
				CategoryContentVO contentVO = categoryFacade.getCategoryContentById(categoryContentId);
				if (contentVO != null && contentVO.getId() > 0l) {
					// 一级内容分类
					if (contentVO.getLevel() == CategoryContentLevel.LEVEL_FIRST.getIntValue()) {
						// 获取二级内容分类
						List<CategoryContentVO> secondList = categoryFacade.getSubCategoryContentList(categoryContentId);
						if (!CollectionUtils.isEmpty(secondList)) {
							for (CategoryContentVO c : secondList) {
								// 获取三级内容分类
								List<CategoryContentVO> thirdList = categoryFacade.getSubCategoryContentList(c.getId());
								if (!CollectionUtils.isEmpty(thirdList)) {
									c.setSubCategoryContentList(thirdList);
									retList.add(c);
								}
							}
						}
					} else if (contentVO.getLevel() == CategoryContentLevel.LEVEL_SECOND.getIntValue()) {
						// 二级内容分类
						List<CategoryContentVO> thirdList = categoryFacade.getSubCategoryContentList(categoryContentId);
						if (!CollectionUtils.isEmpty(thirdList)) {
							contentVO.setSubCategoryContentList(thirdList);
							contentVO.setSendDistrictDTOs(null);
							retList.add(contentVO);
						}
					} else {
						// 三级内容分类
						JSONObject categoryNormalNav = new JSONObject(2);
						if (!CollectionUtils.isEmpty(contentVO.getCategoryNormalVOs())) {
							if (str.length > 1) {
								if (RegexUtils.isAllNumber(str[1])) {
									long categoryNormalId = Long.parseLong(str[1]);
									boolean isFound = false;
									for (CategoryNormalVO c : contentVO.getCategoryNormalVOs()) {
										// 获取属性规格筛选项
										if (categoryNormalId == c.getCategoryId()) {
											ItemModelVO modelVO = itemModelFacade.getItemModel(categoryNormalId, 1);
											if (modelVO != null) {
												model.addAttribute("parameterList", modelVO.getParameterList());
												model.addAttribute("specificationList", modelVO.getSpecificationList());
											}
											categoryNormalIds.add(categoryNormalId);
											List<JSONObject> brandList = 
													itemProductFacade.getBrands(categoryNormalIds, null, AreaUtils.getAreaCode());
											if (!CollectionUtils.isEmpty(brandList)) {
												model.addAttribute("brandList", brandList);
											}
											categoryNormalNav.put("id", categoryNormalId);
											categoryNormalNav.put("name", c.getCategoryName());
											isFound = true;
											break;
										}
									}
									if (!isFound) {
										model.addAttribute("contentVO", contentVO);
									}
								}
							} else {
								// 商品分类id获取品牌列表
								if (!CollectionUtils.isEmpty(contentVO.getCategoryNormalVOs())) {
									for (CategoryNormalVO c : contentVO.getCategoryNormalVOs()) {
										categoryNormalIds.add(c.getCategoryId());
									}
									List<JSONObject> brandList = 
											itemProductFacade.getBrands(categoryNormalIds, null, AreaUtils.getAreaCode());
									model.addAttribute("brandList", brandList);
								}
								model.addAttribute("contentVO", contentVO);
							}
						}
						// 设置导航
						model.addAttribute("categoryNav", initCategoryNav(contentVO, categoryNormalNav));
						return "pages/product/list";
					}
					// 商品分类id获取品牌列表
					if (!CollectionUtils.isEmpty(contentVO.getCategoryNormalVOs())) {
						for (CategoryNormalVO c : contentVO.getCategoryNormalVOs()) {
							categoryNormalIds.add(c.getCategoryId());
						}
						List<JSONObject> brandList = 
								itemProductFacade.getBrands(categoryNormalIds, null, AreaUtils.getAreaCode());
						model.addAttribute("brandList", brandList);
					}
				}
				// 设置导航
				model.addAttribute("categoryNav", initCategoryNav(contentVO, null));
			}
		}
		model.addAttribute("categoryContentList", retList);
		return "/pages/product/list";
	}
	
	@RequestMapping(value = "/rest/product/list")
	public @ResponseBody BaseJsonVO listProductByCategroy(BasePageParamVO<ProductSKUMainSiteVO> pageParamVO, 
			ProductSearchMainSiteVO productSearchVO, Model model) {
		BaseJsonVO ret = new BaseJsonVO();
		productSearchVO.setAreaCode(AreaUtils.getAreaCode());
		if (productSearchVO.getSearchValue() != null) {
			try {
				productSearchVO.setSearchValue(URLDecoder.decode(productSearchVO.getSearchValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		pageParamVO.setList(itemProductFacade.getProudctByParameters(pageParamVO, productSearchVO));
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(pageParamVO);
		return ret;
	}
	
	/**
	 * 根据扫描的条形码/二维码查询当前区域的商品。
	 * 
	 * @param imageOfBarcode
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@RequestMapping(value = "/rest/product/barcode", method=RequestMethod.POST)
	public @ResponseBody BaseJsonVO listProductByBarCode(@RequestBody BufferedImage imgBarcode) {
//		//测试用
//		File file = new File(System.getenv("HOME")+File.separator+"testbarcode.png");
//		try {
//			OutputStream os = new FileOutputStream(file);
//			int len = 0;
//			for(byte[] bs = new byte[1024]; (len = image.read(bs)) != -1;) {
//				os.write(bs, 0, len);
//			}
//			os.flush();
//			os.close();
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally{
//		}//以上测试用
		
		BaseJsonVO ret = new BaseJsonVO();
		ItemSPUVO spuVO = null;
		try {			
			String barCode = ZXingUtil.decode(imgBarcode);
			ItemSPUDTO spuDTO = new ItemSPUDTO();
			spuDTO.setBarCode(barCode);
			spuVO = itemSPUFacade.getItemSPU(spuDTO);
		} catch (NotFoundException e) {
			ret.setCode(400);
			ret.setMessage("扫码无效");
			return ret;
		}
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(spuVO);
//		if (spuVO != null) {
//			BasePageParamVO<ProductSKUMainSiteVO> pageParamVO = new BasePageParamVO<>();
//			ProductSearchMainSiteVO productSearchVO = new ProductSearchMainSiteVO();
//			productSearchVO.setAreaCode(AreaUtils.getAreaCode());
//			productSearchVO.setSpuId(spuVO.getSpuId());
//			pageParamVO.setList(itemProductFacade.getProudctByParameters(pageParamVO, productSearchVO));
//			ret.setResult(pageParamVO);
//		}
		return ret;
	}
	
	@BILog(action = "page", type = "goodsPage", clientType="wap")
	@RequestMapping(value = "/product/detail", method = RequestMethod.GET)
	public String detailPage(Model model, HttpServletRequest request) {
		long uid = SecurityContextUtils.getUserId();
		String sku = request.getParameter("skuId");
		if (!RegexUtils.isAllNumber(sku)) {
			return "/pages/404";
		}
		long skuId = Long.parseLong(sku);
		
		String snapShot = request.getParameter("isSnapShot");
		if (!StringUtils.isBlank(snapShot) && "true".equals(snapShot)) {
			model.addAttribute("isSnapshot", true);
		}
		ProductSKUDTO skuDTO = new ProductSKUDTO();
		skuDTO.setId(skuId);
		skuDTO.setStatus(ProductStatusType.ONLINE.getIntValue());
		ProductSKUMainSiteVO productVO = itemProductFacade.getProductSKUVO(skuDTO);
		if (productVO != null) {
			// 获取店铺信息
			long businessId = productVO.getSupplierId();
			BusinessDTO store = businessFacade.getBusinessById(businessId);
			// 是否是特许经营
			if (SupplierType.SPECIALMANAGE.getIntValue() == store.getType()) {
				boolean allowed = false;
				// 用户是否登录
				if (uid > 0l) {
					allowed = isBusinessAllowed(businessId, uid);
				}
				if (!allowed) {
					model.addAttribute("isAllowed", allowed);
					return "/pages/product/detail";
				}
			}
			// 获取商家区域是否允许
			model.addAttribute("isAllowed", isIPAllowed(businessId));
			model.addAttribute("product", productVO);
			model.addAttribute("storeInfo", store);
			appendStaticMethod(model);
		}
		return "/pages/product/detail";
	}

	@BILog(action = "page", type = "skuSnapShotPage", clientType="wap")
	@RequestMapping(value = "/product/snapShot", method = RequestMethod.GET)
	public String snapShot(Model model, @RequestParam(value = "skuId") long skuId,
			@RequestParam(value = "orderId") long orderId, @RequestParam(value = "userId") long userId) {
		// 取商品快照
		OrderSkuDTO orderSkuDTO = orderFacade.getSkuSnapShot(userId, orderId, skuId);
		if (orderSkuDTO == null) {
			return "/pages/404";
		}
		String snapShot = orderSkuDTO.getSkuSnapshot();
		if (StringUtils.isBlank(snapShot)) {
			return "/pages/404";
		}
		SkuSPDTO skuSnapShot = JsonUtils.fromJson(snapShot, SkuSPDTO.class);
		if (skuSnapShot == null) {
			return "/pages/404";
		}
		skuSnapShot.setSkuId(skuId);
		
		// 判断商品是否有更新
		ProductSKUDTO skuDTO = new ProductSKUDTO();
		skuDTO.setId(skuId);
		skuDTO.setStatus(ProductStatusType.ONLINE.getIntValue());
		skuDTO = itemProductFacade.getProductSKUDTO(skuDTO);
		if (skuDTO != null 
				&& skuDTO.getUpdateTime() != null && orderSkuDTO.getCreateTime() != null
				&& skuDTO.getUpdateTime().getTime() > orderSkuDTO.getCreateTime().getTime()) {
			skuSnapShot.setUpdateTime(DateUtil.dateToString(skuDTO.getUpdateTime(), DateUtil.LONG_PATTERN));
		}
		model.addAttribute("snapshot", skuSnapShot);
		// 获取店铺信息
		long businessId = orderSkuDTO.getSupplierId();
		BusinessDTO store = businessFacade.getBusinessById(businessId);
		model.addAttribute("storeInfo", store);
		if (StringUtils.isNotBlank(skuSnapShot.getCustomEditHTML())) {
			try {
				Map<String, String> map = 
						ItemCenterUtil.getEditHTML_Param(skuSnapShot.getCustomEditHTML(), null);
				if (!CollectionUtils.isEmpty(map)) {
					skuSnapShot.setCustomEditHTML(map.get("html"));
				}
			} catch (Exception e) {
				logger.error("SKU snapshot, get customEditHTML error! customEditHTML : " 
						+ skuSnapShot.getCustomEditHTML(), e);
			}
		}
		
		return "/pages/product/snapShot";
	}
	
	private boolean isIPAllowed(long businessId) {
		int areaCode = AreaUtils.getProvinceCode();
		return itemProductFacade.isIPAllowedByBusinessId(areaCode, businessId);
	}
	
	private boolean isBusinessAllowed(long businessId, long uid) {
		return itemProductFacade.isBusinessAllowed(businessId, uid);
	}
	
	/**
	 * 分类导航栏
	 * @param contentVO
	 * @param categoryNormalNav
	 * @return JSONObject 导航栏
	 */
	private JSONObject initCategoryNav(CategoryContentVO contentVO, JSONObject categoryNormalNav) {
		JSONObject firstNav = new JSONObject(3);
		try {
			// 一级内容分类，直接设置
			if (contentVO.getLevel() == CategoryContentLevel.LEVEL_FIRST.getIntValue()) {
				firstNav.put("id", contentVO.getId());
				firstNav.put("name", contentVO.getName());
			} else if (contentVO.getLevel() == CategoryContentLevel.LEVEL_SECOND.getIntValue()) {
				// 设置二级内容分类
				JSONObject secondNav = new JSONObject(3);
				secondNav.put("id", contentVO.getId());
				secondNav.put("name", contentVO.getName());
				// 获取父内容分类
				CategoryContentVO superContenVO = 
						categoryFacade.getBriefCategoryContentById(contentVO.getParentId());
				// 设置一级内容分类
				firstNav.put("id", superContenVO.getId());
				firstNav.put("name", superContenVO.getName());
				firstNav.put("sub", secondNav);
			} else if (contentVO.getLevel() == CategoryContentLevel.LEVEL_THIRD.getIntValue()) {
				// 设置三级内容分类
				JSONObject thirdNav = new JSONObject(3);
				thirdNav.put("id", contentVO.getId());
				thirdNav.put("name", contentVO.getName());
				thirdNav.put("sub", categoryNormalNav);
				// 获取二级内容分类
				CategoryContentVO superContenVO = 
						categoryFacade.getBriefCategoryContentById(contentVO.getParentId());
				// 设置二级内容分类
				JSONObject secondNav = new JSONObject(3);
				secondNav.put("id", superContenVO.getId());
				secondNav.put("name", superContenVO.getName());
				secondNav.put("sub", thirdNav);
				// 获取一级内容分类
				superContenVO = 
						categoryFacade.getBriefCategoryContentById(superContenVO.getParentId());
				// 设置一级内容分类
				firstNav.put("id", superContenVO.getId());
				firstNav.put("name", superContenVO.getName());
				firstNav.put("sub", secondNav);
			}
		} catch (Exception e) {
			logger.error("Init categoryNav error! CategoryContentId : " + contentVO.getId());
		}
		return firstNav;
	}
}
