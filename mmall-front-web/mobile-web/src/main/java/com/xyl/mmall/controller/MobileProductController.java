/**
 * 
 */
package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ProdPicDTO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;
import com.xyl.mmall.logger.CommonLogger;
import com.xyl.mmall.mobile.facade.MobileBrandFacade;
import com.xyl.mmall.mobile.facade.MobilePoFacade;
import com.xyl.mmall.mobile.facade.MobileProductFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.param.MobileHeaderAO;
import com.xyl.mmall.mobile.facade.param.MobilePageCommonAO;
import com.xyl.mmall.mobile.facade.vo.MobileProductSearchVO;
import com.xyl.mmall.mobile.ios.facade.MobilePrdFacade;
import com.xyl.mmall.mobile.ios.facade.pageView.common.MobileSKUDetailVO;
import com.xyl.mmall.mobile.ios.facade.pageView.prdctlist.MobileSku;
import com.xyl.mmall.mobile.ios.facade.pageView.prodctDetail.SkuDetailVo;
import com.xyl.mmall.mobile.util.MobileHelper;
import com.xyl.mmall.mobile.web.facade.MobileItemProductFacade;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.SkuPriceDTO;
import com.xyl.mmall.order.dto.SkuSPDTO;
import com.xyl.mmall.service.MobileHeaderProcess;
import com.xyl.mmall.util.AreaUtils;

/**
 * @author dingjiang
 *
 */
@RestController
@RequestMapping("/m")
public class MobileProductController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(MobileProductController.class);
	@Autowired
	private CommonLogger logger;
	@Autowired
	private MobilePoFacade mobilePoFacade;
	@Autowired
	private MobileBrandFacade mobileBrandFacade;
	@Autowired
	private MobileProductFacade mobileProductFacade;
	@Autowired
	private MobileItemProductFacade itemProductFacade;
	@Autowired
	private BusinessFacade businessFacade;
	@Autowired
	private MobilePrdFacade mobilePrdFacade;
	@Autowired
	private OrderFacade orderFacade;
	@Autowired
	private MobileHelper mobileHelper;

	@RequestMapping(value = "/getPOList", method = RequestMethod.GET)
	public BaseJsonVO getPOList(HttpServletRequest request,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "online", required = false) Integer online, MobilePageCommonAO pager) {
		long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);

		if (type == -1) {
			logger.logger(ao, "page", "newPage", userId);
		} else if (online == 0) {
			logger.logger(ao, "page", "findPage", userId);
		} else {
			logger.logger(ao, "page", String.valueOf(type), userId);
		}
		return mobilePoFacade.getPOList(userId, areaCode, type, online, pager);
	}

	@RequestMapping(value = "/productDetail", method = RequestMethod.GET)
	public BaseJsonVO detailPage(@RequestParam(value = "skuId", required = true) Long skuId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
//		long uid = SecurityContextUtils.getUserId();
		baseJsonVO = mobileHelper.verifyView(skuId, null,AreaUtils.getProvinceCode());
		if(baseJsonVO.getCode() != ErrorCode.SUCCESS.getIntValue()){
			return baseJsonVO;
		}
		
		ProductSKUDTO skuDTO = new ProductSKUDTO();
		skuDTO.setId(skuId);
		SkuDetailVo sku = mobilePrdFacade.getProductSKUVO(skuDTO);
		if (sku != null) {
			// 获取店铺信息
			long businessId = sku.getStoreId();
			BusinessDTO store = businessFacade.getBusinessById(businessId);
			baseJsonVO = new BaseJsonVO(ErrorCode.SUCCESS);
			sku.setStoreName(store.getStoreName());
			sku.setBatchCash(store.getBatchCash());
			sku.setCOD(isIPAllowed(businessId));
			baseJsonVO.setResult(sku);
		} else {
			baseJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "商品信息不存在");
		}

		return baseJsonVO;
	}
	
	

	@RequestMapping(value = "/getBrandList", method = RequestMethod.GET)
	public BaseJsonVO getBrandList(@RequestParam(value = "type", required = false) Integer type,
			MobilePageCommonAO pager, HttpServletRequest request) {
		long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		logger.logger(ao, "page", "brandListPage", userId);
		BaseJsonVO vo = mobileBrandFacade.getBrandList(userId, areaCode, type, pager);
		return vo;

	}

	@RequestMapping(value = "/getBrandDetail", method = RequestMethod.GET)
	public BaseJsonVO getBrandDetail(@RequestParam(value = "brandId", required = false) Long brandId,
			HttpServletRequest request) {
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		long userId = MobileHeaderProcess.getUserId();
		int os = MobileHeaderProcess.getOS(ao);
		BaseJsonVO vo = mobileBrandFacade.getBrandDetail(userId, brandId, areaCode, os);
		logger.insertResult(vo, new String[] { "brandId", "brandName" }, new String[] { "branId", "branName" }, ao,
				"page", "brandDetailPage", userId);
		return vo;
	}

	@RequestMapping(value = "/getShopList", method = RequestMethod.GET)
	public BaseJsonVO getShopList(@RequestParam(value = "brandId", required = false) Long brandId,
			@RequestParam(value = "areacode", required = false) Integer areaCode2, HttpServletRequest request) {
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		int os = MobileHeaderProcess.getOS(ao);
		return mobileBrandFacade.getShopByCity(brandId, areaCode, areaCode2, os);
	}

	@RequestMapping(value = "/getPrdtDetail", method = RequestMethod.GET)
	public BaseJsonVO getPrdtDetail(@RequestParam(value = "prdtId", required = false) Long prdtId,
			HttpServletRequest request) {
		long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		BaseJsonVO vo = mobileProductFacade.getProductDetail(userId, areaCode, prdtId,
				Converter.protocolVersion(ao.getProtocolVersion()));
		logger.insertResult(vo, new String[] { "prdtId", "poId", "brandId", "brandName" },
				new String[] { "pid", "poId", "brandId", "brandName" }, ao, "page", "goodsPage", userId, true);
		return vo;
	}

	@RequestMapping(value = "/getPrdtListByParams", method = RequestMethod.GET)
	public BaseJsonVO getPrdtList(BasePageParamVO<MobileSku> pageParamVO, MobileProductSearchVO productSearchVO) {
		BaseJsonVO vo = new BaseJsonVO();
		productSearchVO.setAreaCode(AreaUtils.getAreaCode());
		pageParamVO.setList(mobileProductFacade.getSkuByParameters(pageParamVO, productSearchVO));
		vo.setCodeAndMessage(ErrorCode.SUCCESS.getIntValue(), ErrorCode.SUCCESS.getDesc());
		vo.setResult(pageParamVO);
		return vo;
	}

	@RequestMapping(value = "/findPrdtListByName", method = RequestMethod.GET)
	public BaseJsonVO getPrdtListBySearchValue(BasePageParamVO<MobileSku> pageParamVO, String searchValue) {
		MobileProductSearchVO productSearchVO = new MobileProductSearchVO();
		productSearchVO.setSearchValue(searchValue);
		return getPrdtList(pageParamVO, productSearchVO);
	}

	@RequestMapping(value = "/findPrdtListByBarCode", method = RequestMethod.GET)
	public BaseJsonVO getPrdtListByBarCode(BasePageParamVO<MobileSku> pageParamVO, String barCode) {
		MobileProductSearchVO productSearchVO = new MobileProductSearchVO();
		productSearchVO.setBarCode(barCode);
		return getPrdtList(pageParamVO, productSearchVO);
	}


	private boolean isIPAllowed(long businessId) {
		int areaCode = AreaUtils.getProvinceCode();
		return itemProductFacade.isIPAllowedByBusinessId(areaCode, businessId);
	}

	@BILog(action = "click", type = "skuSnapShotPage", clientType = "app")
	@RequestMapping(value = "/product/snapShot", method = RequestMethod.GET)
	public BaseJsonVO snapShot(Model model, @RequestParam(value = "skuId") long skuId,
			@RequestParam(value = "orderId") long orderId) {
		long userId = SecurityContextUtils.getUserId();
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		baseJsonVO.setCode(ErrorCode.SUCCESS.getIntValue());
		baseJsonVO.setMessage(ErrorCode.SUCCESS.getDesc());
		if (userId <= 0) {
			baseJsonVO.setCode(1006);
			baseJsonVO.setMessage("用户未登录");
			return baseJsonVO;
		}
		// 取商品快照
		OrderSkuDTO orderSkuDTO = orderFacade.getSkuSnapShot(userId, orderId, skuId);

		if (orderSkuDTO == null) {
			baseJsonVO.setCode(1006);
			baseJsonVO.setMessage("商品订购信息不存在");
			return baseJsonVO;
		}
		String snapShot = orderSkuDTO.getSkuSnapshot();
		if (StringUtils.isBlank(snapShot)) {
			baseJsonVO.setCode(1006);
			baseJsonVO.setMessage("商品订单快照信息不存在");
			return baseJsonVO;
		}
		SkuSPDTO skuSnapShot = JsonUtils.fromJson(snapShot, SkuSPDTO.class);
		if (skuSnapShot == null) {
			baseJsonVO.setCode(1006);
			baseJsonVO.setMessage("商品订单快照信息不存在");
			return baseJsonVO;
		}
		skuSnapShot.setSkuId(skuId);

		if (StringUtils.isNotBlank(skuSnapShot.getCustomEditHTML())) {
			try {
				Map<String, String> map = ItemCenterUtil.getEditHTML_Param(skuSnapShot.getCustomEditHTML(), null);
				if (!CollectionUtils.isEmpty(map)) {
					skuSnapShot.setCustomEditHTML(map.get("html"));
				}
			} catch (Exception e) {
				log.error("SKU snapshot, get customEditHTML error! customEditHTML : " + skuSnapShot.getCustomEditHTML(),
						e);
				baseJsonVO.setCode(1006);
				baseJsonVO.setMessage("SKU snapshot, get customEditHTML error");
				return baseJsonVO;
			}
		}

		SkuDetailVo skuDetailVo = coverToSkuDetailVo(skuSnapShot, orderSkuDTO);
		if(skuDetailVo == null){
			baseJsonVO.setCode(1006);
			baseJsonVO.setMessage("该商品所在的店铺不存在");
		}
		baseJsonVO.setResult(skuDetailVo);
		return baseJsonVO;
	}

	private SkuDetailVo coverToSkuDetailVo(SkuSPDTO skuSnapShot, OrderSkuDTO orderSkuDTO) {
		// TODO Auto-generated method stub
		// 判断商品是否有更新
		ProductSKUDTO skuDTO = new ProductSKUDTO();
		SkuDetailVo skuDetailVo = new SkuDetailVo();
		skuDTO.setId(skuSnapShot.getSkuId());
		// skuDTO.setStatus(ProductStatusType.ONLINE.getIntValue());
		skuDTO = itemProductFacade.getProductSKUDTO(skuDTO);
		if (skuDTO == null) {
			skuDetailVo.setStatus(-1);
		} else if (skuDTO.getUpdateTime() != null && orderSkuDTO.getCreateTime() != null
				&& skuDTO.getUpdateTime().getTime() > orderSkuDTO.getCreateTime().getTime()) {
//			skuSnapShot.setUpdateTime(DateUtil.dateToString(skuDTO.getUpdateTime(), DateUtil.LONG_PATTERN));
			skuDetailVo.setUpdateTime(String.valueOf(skuDTO.getUpdateTime().getTime()));
			skuDetailVo.setStatus(skuDTO.getStatus());
		}else{
			skuDetailVo.setStatus(skuDTO.getStatus());
		}
		// 获取店铺信息
		long businessId = orderSkuDTO.getSupplierId();
		BusinessDTO store = businessFacade.getBusinessById(businessId);
		if(store == null){
			return null;
		}
		
		skuDetailVo.setBatchCash(store.getBatchCash());
		skuDetailVo.setBatchNum(skuSnapShot.getBatchNum());
		skuDetailVo.setBrandName(skuSnapShot.getBrandName());
		skuDetailVo.setCanReturn(skuSnapShot.getCanReturn());
		skuDetailVo.setExpireDate(skuSnapShot.getExpireDate());
		skuDetailVo.setName(skuSnapShot.getProductName());
		
		
		List<ProdPicDTO> picList = new ArrayList<>();
		ProdPicDTO picDTO = null;
		for (String path : skuSnapShot.getPicPath()) {
			picDTO = new ProdPicDTO();
			picDTO.setPath(path);
			picList.add(picDTO);
		}
		skuDetailVo.setPicList(picList);
		skuDetailVo.setPriceList(converToProdcutPriceDTO(skuSnapShot.getPriceList()));
		MobileSKUDetailVO prodDetail = new MobileSKUDetailVO();
		prodDetail.setCustomEditHTML(skuSnapShot.getCustomEditHTML());
		skuDetailVo.setProdDetail(prodDetail);
		skuDetailVo.setProdProduceDate(skuSnapShot.getProdProduceDate());
		skuDetailVo.setSalePrice(skuSnapShot.getSalePrice());
		skuDetailVo.setSkuId(skuSnapShot.getSkuId());
		// skuDetailVo.setSpeciList(skuSnapShot.getSkuSpeciDTOs());
		skuDetailVo.setStoreId(store.getId());
		skuDetailVo.setStoreName(store.getStoreName());
		skuDetailVo.setTitle(skuSnapShot.getProductTitle());
		skuDetailVo.setUnit(skuSnapShot.getUnit());

		return skuDetailVo;
	}

	private List<ProductPriceDTO> converToProdcutPriceDTO(List<SkuPriceDTO> priceList) {
		if (CollectionUtils.isEmpty(priceList)) {
			return null;
		}
		List<ProductPriceDTO> list = new ArrayList<>();
		ProductPriceDTO priceDTO = null;
		for (SkuPriceDTO skuPriceDTO : priceList) {
			priceDTO = new ProductPriceDTO();
			priceDTO.setMaxNumber(skuPriceDTO.getProdMaxNumber());
			priceDTO.setMinNumber(skuPriceDTO.getProdMinNumber());
			priceDTO.setPrice(skuPriceDTO.getProdPrice());
			list.add(priceDTO);
		}

		// TODO Auto-generated method stub
		return list;
	}

}
