/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.service.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ItemCenterException;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.util.NOSUtil;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dao.product.PoProductUserFavDao;
import com.xyl.mmall.itemcenter.dao.product.ProductPriceDao;
import com.xyl.mmall.itemcenter.dao.productsku.ProdParamDao;
import com.xyl.mmall.itemcenter.dao.productsku.ProdPicDao;
import com.xyl.mmall.itemcenter.dao.productsku.ProdSpeciDao;
import com.xyl.mmall.itemcenter.dao.productsku.ProductSKUDao;
import com.xyl.mmall.itemcenter.dao.productsku.ProductSKUDetailDao;
import com.xyl.mmall.itemcenter.dao.productsku.SkuRecommendationDao;
import com.xyl.mmall.itemcenter.dao.spu.ItemSPUDao;
import com.xyl.mmall.itemcenter.dto.ProdParamDTO;
import com.xyl.mmall.itemcenter.dto.ProdPicDTO;
import com.xyl.mmall.itemcenter.dto.ProdSpeciDTO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDetailDTO;
import com.xyl.mmall.itemcenter.dto.SkuRecommendationDTO;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.meta.ItemSPU;
import com.xyl.mmall.itemcenter.meta.ProdParam;
import com.xyl.mmall.itemcenter.meta.ProdPic;
import com.xyl.mmall.itemcenter.meta.ProdSpeci;
import com.xyl.mmall.itemcenter.meta.ProductPrice;
import com.xyl.mmall.itemcenter.meta.ProductSKU;
import com.xyl.mmall.itemcenter.meta.ProductSkuDetail;
import com.xyl.mmall.itemcenter.meta.SkuRecommendation;
import com.xyl.mmall.itemcenter.param.ProductSKUSearchParam;
import com.xyl.mmall.itemcenter.param.ProductSearchMainSiteParam;
import com.xyl.mmall.itemcenter.service.ItemProductService;

/**
 * ItemProductServiceImpl.java created by yydx811 at 2015年5月14日 下午8:08:01
 * 商品service实现
 *
 * @author lhp,yydx811
 */
@Service
public class ItemProductServiceImpl implements ItemProductService {

	private static Logger logger = Logger.getLogger(ItemProductServiceImpl.class);

	@Autowired
	private ProdSpeciDao prodSpeciDao;

	@Autowired
	private ProdParamDao prodParamDao;

	@Autowired
	private ProdPicDao prodPicDao;

	@Autowired
	private ProductSKUDao productSKUDao;

	@Autowired
	private ProductSKUDetailDao prodDetailDao;

	@Autowired
	private ProductPriceDao productPriceDao;

	@Autowired
	private ItemSPUDao itemSPUDao;

	@Autowired
	private PoProductUserFavDao productUserFavDao;

	@Autowired
	private SkuRecommendationDao skuRecommendationDao;

	@Override
	public BasePageParamVO<ProductSKUDTO> searchProductSKU(ProductSKUSearchParam param) {
		BasePageParamVO<ProductSKUDTO> basePageParamVO = new BasePageParamVO<ProductSKUDTO>();
		// 先搜索单品
		if (StringUtils.isNotEmpty(param.getBarCode())) {
			ItemSPU itemSPU = new ItemSPU();
			itemSPU.setBarCode(param.getBarCode());
			itemSPU = itemSPUDao.getItemSPU(itemSPU);
			if (itemSPU == null) {
				return basePageParamVO;
			} else {
				List<Long> spuIdList = new ArrayList<Long>();
				spuIdList.add(itemSPU.getId());
				param.setSpuIdList(spuIdList);
			}
		}
		List<ProductSKUDTO> productSKUDTOs = new ArrayList<ProductSKUDTO>();
		int count = productSKUDao.countProductSKUDTOBySearchParam(param);
		if (count == 0) {
			return basePageParamVO;
		}
		basePageParamVO.setTotal(count);
		productSKUDTOs = productSKUDao.searchProductSKU(param);
		if (CollectionUtil.isEmptyOfList(productSKUDTOs)) {
			return basePageParamVO;
		}
		List<Long> spuIdList = new ArrayList<Long>();
		for (ProductSKUDTO productSKU : productSKUDTOs) {
			spuIdList.add(productSKU.getSpuId());
		}
		// 取商品图片
		Map<Long, List<ProdPicDTO>> prodPicMap = getShowPicMap(productSKUDTOs);
		// 取商品价格
		Map<Long, List<ProductPriceDTO>> prodPriceMap = getProductPriceDTOByProductIds(new ArrayList<Long>(
				prodPicMap.keySet()));

		for (ProductSKUDTO prodSkudto : productSKUDTOs) {
			if (prodPicMap.get(prodSkudto.getId()) == null) {
				continue;
			}
			// 设置缩略图
			prodSkudto.setShowPicPath(prodPicMap.get(prodSkudto.getId()).get(0).getPath());
			// 设置价格
			prodSkudto.setPriceList(prodPriceMap.get(prodSkudto.getId()));
		}
		basePageParamVO.setList(productSKUDTOs);
		basePageParamVO.setPageSize(param.getLimit());
		return basePageParamVO;
	}

	@Override
	@Transaction
	public boolean deleteProduct(long businessId, long productId) throws ItemCenterException {
		boolean result = true;
		result = productSKUDao.deleteProductBybusiIdAndProId(businessId, productId);
		if (!result) {
			throw new ItemCenterException("Delete ProductSKU failed!");
		}
		// 删商品图片
		prodPicDao.deleteProdPicByProductId(productId);
		// 删商品详情
		prodDetailDao.deleteDetailByProductId(productId);
		// 删区间价格
		productPriceDao.deleteProductPriceBySKUId(productId);
		// 删商品规格,商品规格表中不一定有
		prodSpeciDao.deleteSpeciByProductId(productId);
		// 删商品属性,商品属性表中不一定有
		prodParamDao.deleteParamByProductId(productId);
		// 删除被收藏的商品
		productUserFavDao.removeProductFromFavListByProId(productId);
		return result;
	}

	@Override
	@Transaction
	public boolean batchDeleteProducts(Long businessId, List<Long> prodIds) throws ItemCenterException {
		if (CollectionUtil.isEmptyOfList(prodIds)) {
			return false;
		}
		boolean result = true;
		result = productSKUDao.batchDeleteProductSKU(prodIds, businessId);
		if (!result) {
			throw new ItemCenterException("Batch Delete ProdProduct failed!");
		} else {
			// 批量删商品图片
			prodPicDao.batchDeleteProdPic(prodIds);
			// 批量删商品详情
			prodDetailDao.batchDeleteProductSKUDetail(prodIds);
			// 批量删区间价格
			productPriceDao.batchDeleteProductPriceBySKUIds(prodIds);
			// 批量删商品规格,商品规格表中不一定有
			prodSpeciDao.batchDeleteProdSpeci(prodIds);
			// 批量删商品属性，商品属性表中不一定有
			prodParamDao.batchDeleteProdParam(prodIds);
			// 批量删除被收藏的商品
			productUserFavDao.batchRemoveProductFromFavListByProIds(prodIds);
		}
		return result;
	}

	@Override
	@Transaction
	public List<ProductSKUDTO> addProductSKUs(List<ProductSKUDTO> productSKUDTOList) throws ItemCenterException {
		for (ProductSKUDTO productSKUDTO : productSKUDTOList) {
			long start = System.currentTimeMillis();
			// 添加商品返回skuid
			ProductSKU productSKU = new ProductSKU(productSKUDTO);
			long skuId = productSKUDao.addProductSKU(productSKU);
			if (skuId < 1l) {
				throw new ItemCenterException("Add productSKU failed! Wrong return Id!");
			}
			productSKUDTO.setId(skuId);
			// 添加商品详情
			ProductSkuDetail skuDetail = new ProductSkuDetail(productSKUDTO.getDetailDTO());
			skuDetail.setProductSKUId(skuId);
			if (StringUtils.isNotBlank(skuDetail.getCustomEditHTML())
					|| StringUtils.isNotBlank(skuDetail.getProdParam())) {
				Map<String, String> map = new HashMap<String, String>();
				if (StringUtils.isNotBlank(skuDetail.getProdParam())) {
					map.put("param", skuDetail.getProdParam());
				}
				if (StringUtils.isNotBlank(skuDetail.getCustomEditHTML())) {
					map.put("html", skuDetail.getCustomEditHTML());
				}
				String saveStr = JsonUtils.toJson(map);
				ByteArrayInputStream is = new ByteArrayInputStream(saveStr.getBytes());
				String fileName = productSKUDTO.getBusinessId() + "_" + skuId;
				try {
					String url = NOSUtil.uploadFile(is, fileName);
					skuDetail.setCustomEditHTML(url);
				} catch (Exception e) {
					logger.error("Upload file to NOS error!", e);
					throw new ItemCenterException("Add productSKUDetail failed! Upload file to NOS error!");
				}
			}
			if (prodDetailDao.addProductDetail(skuDetail) < 1) {
				throw new ItemCenterException("Add productSKUDetail failed!");
			}
			// 添加商品价格
			List<ProductPrice> priceList = new ArrayList<ProductPrice>(productSKUDTO.getPriceList().size());
			for (ProductPriceDTO priceDTO : productSKUDTO.getPriceList()) {
				ProductPrice price = new ProductPrice(priceDTO);
				price.setProductId(skuId);
				priceList.add(price);
			}
			if (!productPriceDao.addBulkProductPrice(priceList)) {
				throw new ItemCenterException("Add productPriceList failed!");
			}
			// 添加图片列表
			List<ProdPic> picList = new ArrayList<ProdPic>(productSKUDTO.getPicList().size());
			for (ProdPicDTO picDTO : productSKUDTO.getPicList()) {
				ProdPic pic = new ProdPic(picDTO);
				pic.setProductSKUId(skuId);
				pic.setCreateBy(productSKUDTO.getCreateBy());
				pic.setUpdateBy(productSKUDTO.getUpdateBy());
				picList.add(pic);
			}
			if (!prodPicDao.addBulkProdPics(picList)) {
				throw new ItemCenterException("Add prodPicList failed!");
			}
			// 添加属性列表
			if (!CollectionUtils.isEmpty(productSKUDTO.getParamList())) {
				List<ProdParam> paramList = new ArrayList<ProdParam>(productSKUDTO.getParamList().size());
				for (ProdParamDTO paramDTO : productSKUDTO.getParamList()) {
					ProdParam param = new ProdParam(paramDTO);
					param.setProductSKUId(skuId);
					param.setCreateBy(productSKUDTO.getCreateBy());
					param.setUpdateBy(productSKUDTO.getUpdateBy());
					paramList.add(param);
				}
				if (!prodParamDao.addBulkProdParams(paramList)) {
					throw new ItemCenterException("Add prodParamList failed!");

				}
			}
			// 添加规格列表
			if (!CollectionUtils.isEmpty(productSKUDTO.getSpeciList())) {
				List<ProdSpeci> speciList = new ArrayList<ProdSpeci>(productSKUDTO.getSpeciList().size());
				for (ProdSpeciDTO speciDTO : productSKUDTO.getSpeciList()) {
					ProdSpeci speci = new ProdSpeci(speciDTO);
					speci.setProductSKUId(skuId);
					speci.setCreateBy(productSKUDTO.getCreateBy());
					speci.setUpdateBy(productSKUDTO.getUpdateBy());
					speciList.add(speci);
				}
				if (!prodSpeciDao.addBulkProdSpeciList(speciList)) {
					throw new ItemCenterException("Add prodSpeciList failed!");
				}
			}
			long end = System.currentTimeMillis();
			logger.info("############## add product sku cos : " + (end - start) + "ms.");
		}
		return productSKUDTOList;
	}

	@Override
	@Transaction
	public ProductSKUDTO addProductSKU(ProductSKUDTO productSKUDTO) throws ItemCenterException {
		long start = System.currentTimeMillis();
		// 添加商品返回skuid
		ProductSKU productSKU = new ProductSKU(productSKUDTO);
		long skuId = productSKUDao.addProductSKU(productSKU);
		if (skuId < 1l) {
			throw new ItemCenterException("Add productSKU failed! Wrong return Id!");
		}
		productSKUDTO.setId(skuId);
		// 添加商品详情
		ProductSkuDetail skuDetail = new ProductSkuDetail(productSKUDTO.getDetailDTO());
		skuDetail.setProductSKUId(skuId);
		if (StringUtils.isNotBlank(skuDetail.getCustomEditHTML()) || StringUtils.isNotBlank(skuDetail.getProdParam())) {
			Map<String, String> map = new HashMap<String, String>();
			if (StringUtils.isNotBlank(skuDetail.getProdParam())) {
				map.put("param", skuDetail.getProdParam());
			}
			if (StringUtils.isNotBlank(skuDetail.getCustomEditHTML())) {
				map.put("html", skuDetail.getCustomEditHTML());
			}
			String saveStr = JsonUtils.toJson(map);
			ByteArrayInputStream is = new ByteArrayInputStream(saveStr.getBytes());
			String fileName = productSKUDTO.getBusinessId() + "_" + skuId;
			try {
				String url = NOSUtil.uploadFile(is, fileName);
				skuDetail.setCustomEditHTML(url);
			} catch (Exception e) {
				logger.error("Upload file to NOS error!", e);
				throw new ItemCenterException("Add productSKUDetail failed! Upload file to NOS error!");
			}
		}
		if (prodDetailDao.addProductDetail(skuDetail) < 1) {
			throw new ItemCenterException("Add productSKUDetail failed!");
		}
		// 添加商品价格
		List<ProductPrice> priceList = new ArrayList<ProductPrice>(productSKUDTO.getPriceList().size());
		for (ProductPriceDTO priceDTO : productSKUDTO.getPriceList()) {
			ProductPrice price = new ProductPrice(priceDTO);
			price.setProductId(skuId);
			priceList.add(price);
		}
		if (!productPriceDao.addBulkProductPrice(priceList)) {
			throw new ItemCenterException("Add productPriceList failed!");
		}
		// 添加图片列表
		List<ProdPic> picList = new ArrayList<ProdPic>(productSKUDTO.getPicList().size());
		for (ProdPicDTO picDTO : productSKUDTO.getPicList()) {
			ProdPic pic = new ProdPic(picDTO);
			pic.setProductSKUId(skuId);
			pic.setCreateBy(productSKUDTO.getCreateBy());
			pic.setUpdateBy(productSKUDTO.getUpdateBy());
			picList.add(pic);
		}
		if (!prodPicDao.addBulkProdPics(picList)) {
			throw new ItemCenterException("Add prodPicList failed!");
		}
		// 添加属性列表
		if (!CollectionUtils.isEmpty(productSKUDTO.getParamList())) {
			List<ProdParam> paramList = new ArrayList<ProdParam>(productSKUDTO.getParamList().size());
			for (ProdParamDTO paramDTO : productSKUDTO.getParamList()) {
				ProdParam param = new ProdParam(paramDTO);
				param.setProductSKUId(skuId);
				param.setCreateBy(productSKUDTO.getCreateBy());
				param.setUpdateBy(productSKUDTO.getUpdateBy());
				paramList.add(param);
			}
			if (!prodParamDao.addBulkProdParams(paramList)) {
				throw new ItemCenterException("Add prodParamList failed!");

			}
		}
		// 添加规格列表
		if (!CollectionUtils.isEmpty(productSKUDTO.getSpeciList())) {
			List<ProdSpeci> speciList = new ArrayList<ProdSpeci>(productSKUDTO.getSpeciList().size());
			for (ProdSpeciDTO speciDTO : productSKUDTO.getSpeciList()) {
				ProdSpeci speci = new ProdSpeci(speciDTO);
				speci.setProductSKUId(skuId);
				speci.setCreateBy(productSKUDTO.getCreateBy());
				speci.setUpdateBy(productSKUDTO.getUpdateBy());
				speciList.add(speci);
			}
			if (!prodSpeciDao.addBulkProdSpeciList(speciList)) {
				throw new ItemCenterException("Add prodSpeciList failed!");
			}
		}
		long end = System.currentTimeMillis();
		logger.info("############## add product sku cos : " + (end - start) + "ms.");
		return productSKUDTO;
	}

	@Override
	public ProductSKUDTO getProductSKUDTO(ProductSKUDTO skuDTO, boolean isGetAll) {
		// 获取sku
		ProductSKU sku = new ProductSKU(skuDTO);
		sku = productSKUDao.getProductSKU(sku);
		if (sku == null || sku.getId() < 1l) {
			return null;
		}
		long skuId = sku.getId();
		skuDTO = new ProductSKUDTO(sku);
		if (!isGetAll) {
			return skuDTO;
		}
		// 获取商品详情
		ProductSkuDetail skuDetail = new ProductSkuDetail();
		skuDetail.setProductSKUId(skuId);
		skuDetail = prodDetailDao.getProductDetail(skuDetail);
		if (skuDetail != null && skuDetail.getId() > 0l) {
			skuDTO.setDetailDTO(new ProductSKUDetailDTO(skuDetail));
		}
		// 获取商品价格列表
		List<ProductPriceDTO> priceList = productPriceDao.getProductPriceDTOByProductId(skuId);
		skuDTO.setPriceList(priceList);
		// 获取商品图片列表
		List<ProdPic> picList = prodPicDao.getPicListBySKUId(skuId);
		skuDTO.setPicList(prodPicListConvertToDTO(picList));
		// 获取商品属性列表
		List<ProdParam> paramList = prodParamDao.getParamListBySKUId(skuId);
		skuDTO.setParamList(prodParamListConvertToDTO(paramList));
		// 获取商品规格列表
		List<ProdSpeci> speciList = prodSpeciDao.getSpeciListBySKUId(skuId);
		skuDTO.setSpeciList(prodSpeciListConvertToDTO(speciList));
		return skuDTO;
	}

	private List<ProdPicDTO> prodPicListConvertToDTO(List<ProdPic> picList) {
		if (CollectionUtils.isEmpty(picList)) {
			return null;
		} else {
			List<ProdPicDTO> retList = new ArrayList<ProdPicDTO>(picList.size());
			for (ProdPic prodPic : picList) {
				retList.add(new ProdPicDTO(prodPic));
			}
			return retList;
		}
	}

	private List<ProdParamDTO> prodParamListConvertToDTO(List<ProdParam> paramList) {
		if (CollectionUtils.isEmpty(paramList)) {
			return null;
		} else {
			List<ProdParamDTO> retList = new ArrayList<ProdParamDTO>(paramList.size());
			for (ProdParam prodParam : paramList) {
				retList.add(new ProdParamDTO(prodParam));
			}
			return retList;
		}
	}

	private List<ProdSpeciDTO> prodSpeciListConvertToDTO(List<ProdSpeci> speciList) {
		if (CollectionUtils.isEmpty(speciList)) {
			return null;
		} else {
			List<ProdSpeciDTO> retList = new ArrayList<ProdSpeciDTO>(speciList.size());
			for (ProdSpeci prodSpeci : speciList) {
				retList.add(new ProdSpeciDTO(prodSpeci));
			}
			return retList;
		}
	}

	@Override
	public boolean updateProductSKUStatus(long prodId, ProductStatusType statusType, long modifyUserId) {
		// 商品只有处于审核中，才能修改为上架 ---等以后有审核再加
		// if(ProductStatusType.ONLINE.equals(statusType)){
		// ProductSKU productSKU = productSKUDao.getObjectById(prodId);
		// if(productSKU.getStatus()!=ProductStatusType.PENDING.getIntValue()){
		// return false;
		// }
		// }
		return productSKUDao.updateProductSKUStatus(prodId, statusType, modifyUserId);
	}

	@Override
	public boolean batchUpdateProductSKUStatus(List<Long> prodIds, ProductStatusType statusType, long modifyUserId) {
		return productSKUDao.batchUpdateProductSKUStatus(prodIds, statusType, modifyUserId);
	}

	@Override
	public Map<ProductStatusType, Integer> countProductSKUByBusinessId(long businessId) {
		return productSKUDao.countProductSKUByBusinessId(businessId);
	}

	@Override
	public List<ProductSKUDTO> getProductSKUDTOByProdIds(List<Long> prodIds) {

		List<ProductSKU> productSKUs = productSKUDao.getProductSKUList(prodIds);

		if (CollectionUtil.isEmptyOfList(productSKUs)) {
			return null;
		}

		List<ProductSKUDTO> productSKUDTOs = new ArrayList<ProductSKUDTO>();
		List<Long> spuIdList = new ArrayList<Long>();
		for (ProductSKU productSKU : productSKUs) {
			spuIdList.add(productSKU.getSpuId());
			productSKUDTOs.add(new ProductSKUDTO(productSKU));
		}

		// 取单品信息
		List<ItemSPU> itemSPUs = itemSPUDao.getItemSPUList(spuIdList);
		Map<Long, ItemSPU> itemSPUMap = Maps.uniqueIndex(itemSPUs, new Function<ItemSPU, Long>() {
			public Long apply(ItemSPU itemSPU) {
				return itemSPU.getId();
			}
		});

		// 取商品图片
		Map<Long, List<ProdPicDTO>> prodPicMap = getShowPicMap(productSKUDTOs);

		for (ProductSKUDTO prodSkudto : productSKUDTOs) {
			if (prodPicMap.get(prodSkudto.getId()) == null) {
				continue;
			}
			// 设置缩略图
			prodSkudto.setShowPicPath(prodPicMap.get(prodSkudto.getId()).get(0).getPath());
			// 设置商品图片
			prodSkudto.setPicList(prodPicMap.get(prodSkudto.getId()));
			// 获取商品规格列表
			List<ProdSpeci> speciList = prodSpeciDao.getSpeciListBySKUId(prodSkudto.getId());
			prodSkudto.setSpeciList(prodSpeciListConvertToDTO(speciList));
			prodSkudto.setBarCode(itemSPUMap.get(prodSkudto.getSpuId()).getBarCode());
			// 获取商品详情
			ProductSkuDetail skuDetail = new ProductSkuDetail();
			skuDetail.setProductSKUId(prodSkudto.getId());
			skuDetail = prodDetailDao.getProductDetail(skuDetail);
			if (skuDetail != null && skuDetail.getId() > 0l) {
				prodSkudto.setDetailDTO(new ProductSKUDetailDTO(skuDetail));
			}
		}

		return productSKUDTOs;
	}

	@Override
	public Map<Long, StringBuilder> getProductBusinessIdsByIds(List<String> ids) {
		List<ProductSKU> productSKUList = productSKUDao.getProductBusinessIdsByIds(ids);
		Map<Long, StringBuilder> result = new HashMap<Long, StringBuilder>();
		for (ProductSKU productSKU : productSKUList) {
			if (result.containsKey(productSKU.getBusinessId())) {
				result.put(productSKU.getBusinessId(),
						result.get(productSKU.getBusinessId()).append("," + productSKU.getId()));
			} else {
				result.put(productSKU.getBusinessId(), new StringBuilder("" + productSKU.getId()));
			}
		}

		return result;
	}

	// 获取商品图片 返回skuId为key的map
	private Map<Long, List<ProdPicDTO>> getShowPicMap(List<ProductSKUDTO> productSKUDTOs) {
		Map<Long, List<ProdPicDTO>> prodPicMap = new HashMap<Long, List<ProdPicDTO>>();
		Function<ProductSKUDTO, Long> function = new Function<ProductSKUDTO, Long>() {
			@Override
			public Long apply(ProductSKUDTO arg0) {
				return arg0.getId();
			}
		};
		List<Long> prodList = Lists.transform(productSKUDTOs, function);
		// 取图片
		List<ProdPic> prodPics = prodPicDao.getPicListBySKUIds(prodList);
		if (CollectionUtil.isNotEmptyOfList(prodPics)) {
			List<ProdPicDTO> prodPicList = null;
			for (ProdPic prodPic : prodPics) {
				if (prodPicMap.get(prodPic.getProductSKUId()) == null) {
					prodPicList = new ArrayList<ProdPicDTO>();
					prodPicList.add(new ProdPicDTO(prodPic));
					prodPicMap.put(prodPic.getProductSKUId(), prodPicList);
				} else {
					prodPicMap.get(prodPic.getProductSKUId()).add(new ProdPicDTO(prodPic));
				}
			}
		}
		return prodPicMap;

	}

	// 取商品价格
	private Map<Long, List<ProductPriceDTO>> getProductPriceDTOByProductIds(List<Long> productIds) {
		if (productIds == null || productIds.size() == 0) {
			return null;
		}
		List<ProductPrice> productPrices = productPriceDao.getProductPriceByProductIds(productIds);
		Map<Long, List<ProductPriceDTO>> productMap = new HashMap<Long, List<ProductPriceDTO>>();
		List<ProductPriceDTO> productPriceList = null;
		if (productPrices != null && productPrices.size() > 0) {
			for (ProductPrice productPrice : productPrices) {
				productPriceList = productMap.get(String.valueOf(productPrice.getProductId()));
				if (productPriceList == null) {
					productPriceList = new ArrayList<ProductPriceDTO>();
					productMap.put(productPrice.getProductId(), productPriceList);
				}
				productPriceList.add(new ProductPriceDTO(productPrice));
			}
		}
		return productMap;
	}

	@Override
	public int deleteProdPic(long productSKUId, long id) {
		List<ProdPic> picList = prodPicDao.getPicListBySKUId(productSKUId);
		if (CollectionUtils.isEmpty(picList)) {
			return 0;
		}
		if (picList.size() == 1) {
			return -1;
		}
		return prodPicDao.deleteProdPic(productSKUId, id);
	}

	@Override
	@Transaction
	public int updateProductSKU(ProductSKUDTO productSKUDTO) throws ItemCenterException {
		ProductSKU sku = new ProductSKU(productSKUDTO);
		int res = productSKUDao.updateProductSKU(sku);
		if (res > 0) {
			long skuId = productSKUDTO.getId();
			if (productSKUDTO.getDetailDTO() != null) {
				if (StringUtils.isNotBlank(productSKUDTO.getDetailDTO().getCustomEditHTML())) {
					ProductSkuDetail skuDetail = new ProductSkuDetail(productSKUDTO.getDetailDTO());
					skuDetail.setProductSKUId(skuId);
					Map<String, String> map = new HashMap<String, String>();
					if (StringUtils.isNotBlank(skuDetail.getCustomEditHTML())) {
						map.put("html", skuDetail.getCustomEditHTML());
					}
					String saveStr = JsonUtils.toJson(map);
					ByteArrayInputStream is = new ByteArrayInputStream(saveStr.getBytes());
					String fileName = productSKUDTO.getBusinessId() + "_" + skuId;
					try {
						String url = NOSUtil.uploadFile(is, fileName);
						skuDetail.setCustomEditHTML(url);
					} catch (Exception e) {
						logger.error("Upload file to NOS error!", e);
						throw new ItemCenterException("Add productSKUDetail failed! Upload file to NOS error!");
					}
					if (prodDetailDao.updateProductDetail(skuDetail) < 1) {
						if (prodDetailDao.addProductDetail(skuDetail) < 1) {
							throw new ItemCenterException("Update productSKUDetail failed!");
						}
					}
				}
			}
			if (!CollectionUtils.isEmpty(productSKUDTO.getPriceList())) {
				productPriceDao.deleteProductPriceBySKUId(skuId);
				// 添加商品价格
				List<ProductPrice> priceList = new ArrayList<ProductPrice>(productSKUDTO.getPriceList().size());
				for (ProductPriceDTO priceDTO : productSKUDTO.getPriceList()) {
					ProductPrice price = new ProductPrice(priceDTO);
					price.setProductId(productSKUDTO.getId());
					price.setId(0l);
					priceList.add(price);
				}
				if (!productPriceDao.addBulkProductPrice(priceList)) {
					throw new ItemCenterException("Add productPriceList failed!");
				}
			}
			if (!CollectionUtils.isEmpty(productSKUDTO.getPicList())) {
				// 添加图片列表
				List<ProdPic> picList = new ArrayList<ProdPic>(productSKUDTO.getPicList().size());
				for (ProdPicDTO picDTO : productSKUDTO.getPicList()) {
					ProdPic pic = new ProdPic(picDTO);
					pic.setProductSKUId(skuId);
					pic.setCreateBy(productSKUDTO.getCreateBy());
					pic.setUpdateBy(productSKUDTO.getUpdateBy());
					pic.setId(0l);
					picList.add(pic);
				}
				if (!prodPicDao.addBulkProdPics(picList)) {
					throw new ItemCenterException("Add prodPicList failed!");
				}
			}
		}
		return res;
	}

	@Override
	public int updateProdParam(List<ProdParamDTO> addList, List<ProdParamDTO> delList) {
		if (!CollectionUtils.isEmpty(delList)) {
			for (ProdParamDTO prodParamDTO : delList) {
				prodParamDao.deleteParamByIndex(new ProdParam(prodParamDTO));
			}
		}

		List<ProdParam> tempList = new ArrayList<ProdParam>(addList.size());
		if (!CollectionUtils.isEmpty(addList)) {
			for (ProdParamDTO prodParamDTO : addList) {
				ProdParam prodParam = new ProdParam(prodParamDTO);
				if (prodParamDao.getParamByIndex(prodParam) == null) {
					tempList.add(prodParam);
				}
			}
		}

		return prodParamDao.addBulkProdParams(tempList) ? 1 : 0;
	}

	@Override
	public int countProdParamOptionInUse(long paramId, long paramOptionId) {
		return prodParamDao.countProdParamOptionInUse(paramId, paramOptionId);
	}

	@Override
	public int countProdSpeciOptionInUse(long speciId, long speciOptionId) {
		return prodSpeciDao.countProdSpeciOptionInUse(speciId, speciOptionId);
	}

	@Override
	public int countProductSKUBySPUId(long spuId) {
		return productSKUDao.countProductSKUBySPUId(spuId);
	}

	@Override
	public BasePageParamVO<ProductSKUDTO> getProductSKUList(BasePageParamVO<ProductSKUDTO> basePageParamVO,
			ProductSearchMainSiteParam searchParam) {
		List<Long> skuIds = null;
		if (!CollectionUtils.isEmpty(searchParam.getParamMap())) {
			skuIds = prodParamDao.getProductSKUIds(searchParam.getParamMap());
		}
		if (!CollectionUtils.isEmpty(searchParam.getSpeciMap())) {
			skuIds = prodSpeciDao.getProductSKUIds(searchParam.getSpeciMap(), skuIds);
		}
		basePageParamVO = productSKUDao.getProductSKUList(basePageParamVO, searchParam, skuIds);
		List<ProductSKUDTO> skuDTOList = basePageParamVO.getList();
		if (CollectionUtils.isEmpty(skuDTOList)) {
			return null;
		}
		for (ProductSKUDTO p : skuDTOList) {
			long skuId = p.getId();
			// 获取商品价格列表
			List<ProductPriceDTO> priceList = productPriceDao.getProductPriceDTOByProductId(skuId);
			p.setPriceList(priceList);
			// 获取商品图片列表
			List<ProdPic> picList = prodPicDao.getPicListBySKUId(skuId);
			p.setPicList(prodPicListConvertToDTO(picList));
		}
		basePageParamVO.setList(skuDTOList);
		return basePageParamVO;
	}

	@Override
	public ProductPriceDTO getProductPriceByBuyNum(long skuId, int buyNum) {
		List<ProductPriceDTO> priceList = productPriceDao.getProductPriceDTOByProductId(skuId);
		if (CollectionUtils.isEmpty(priceList)) {
			return null;
		}
		int min = 0;
		for (int i = 0; i < priceList.size(); ++i) {
			ProductPriceDTO priceDTO = priceList.get(i);
			if (i == 0) {
				if (priceDTO.getMinNumber() > buyNum) {
					return null;
				} else {
					min = i;
					continue;
				}
			}
			if (priceDTO.getMinNumber() > buyNum) {
				break;
			} else {
				min = i;
			}
		}
		return priceList.get(min);
	}

	@Override
	public List<Long> getCategoryNormalIdsByBusinessId(long businessId) {
		return productSKUDao.getCategoryNormalIdsByBusinessId(businessId);
	}

	@Override
	@Transaction
	public void updateProductSKUStatusByBusinessId(long businessId, ProductStatusType statusType, long modifyUserId)
			throws ItemCenterException {
		try {
			productSKUDao.updateProductSKUStatusByBusinessId(businessId, statusType, modifyUserId);
		} catch (Exception e) {
			throw new ItemCenterException("Update productSKUStatus failed!");
		}
	}

	@Override
	public boolean isProductStatusOnline(long productId) {
		return productSKUDao.isProductStatusOnline(productId);
	}

	@Override
	public Map<Long, Boolean> getProductStatusIsOnline(List<Long> productIds) {
		return productSKUDao.getProductStatusIsOnline(productIds);
	}

	@Override
	@Transaction
	public boolean updateProductsSaleNum(Map<Long, Integer> skuCartNumMap) {
		if (CollectionUtil.isEmptyOfMap(skuCartNumMap)) {
			return false;
		}
		ProductSKU productSKU = null;
		for (Map.Entry<Long, Integer> entry : skuCartNumMap.entrySet()) {
			// productSKU = new ProductSKU();
			// productSKU.setId(entry.getKey());
			// productSKU = productSKUDao.getLockByKey(productSKU);
			productSKU = productSKUDao.getObjectById(entry.getKey().longValue());
			if (productSKU == null) {
				logger.error("update product saleNum,sku is not exist,skuId:" + entry.getKey());
				continue;
			}
			productSKU.setSaleNum(entry.getValue().intValue());
			productSKUDao.updateObjectByKey(productSKU);
		}
		return true;
	}

	@Override
	public int updateProductSKUSaleNum(long skuId, int increment) {
		return productSKUDao.updateProductSKUSaleNum(skuId, increment);
	}

	@Override
	public List<Long> getBrandIdsByCategoryIds(List<Long> categoryNormalIds, String searchValue, Set<Long> businessIds) {
		return productSKUDao.getBrandIdsByCategoryIds(categoryNormalIds, searchValue, businessIds);
	}

	@Override
	public int countProductSKUDTOBySearchParam(ProductSKUSearchParam param) {
		return productSKUDao.countProductSKUDTOBySearchParam(param);
	}

	@Override
	public ProductSKUDTO getProductSKUBreifInfo(long skuId) {
		ProductSKU sku = new ProductSKU();
		sku.setId(skuId);
		sku = productSKUDao.getProductSKU(sku);
		if (sku == null || sku.getId() < 1l) {
			return null;
		}
		ProductSKUDTO skuDTO = new ProductSKUDTO(sku);
		// 获取商品价格列表
		List<ProductPriceDTO> priceList = productPriceDao.getProductPriceDTOByProductId(skuId);
		skuDTO.setPriceList(priceList);
		// 获取商品图片列表
		List<ProdPic> picList = prodPicDao.getPicListBySKUId(skuId);
		if (!CollectionUtils.isEmpty(picList)) {
			skuDTO.setShowPicPath(picList.get(0).getPath());
		}

		return skuDTO;
	}

	@Override
	public List<ProductSKUDTO> getProductSKUDTOBySpuIds(List<Long> spuIds) {
		List<ProductSKU> productSKUs = productSKUDao.getProductSKUListBySpuIds(spuIds);

		List<ProductSKUDTO> productSKUDTOs = new ArrayList<ProductSKUDTO>();
		for (ProductSKU productSKU : productSKUs) {
			productSKUDTOs.add(new ProductSKUDTO(productSKU));
		}
		return productSKUDTOs;
	}

	@Override
	public List<SkuRecommendationDTO> getSKuRecommendationListByBusinessId(long businessId) {
		List<SkuRecommendation> skuRecommendations = skuRecommendationDao
				.getSKuRecommendationListByBusinessId(businessId);
		if (CollectionUtil.isEmptyOfList(skuRecommendations)) {
			return null;
		}
		Function<SkuRecommendation, Long> skuIdFunc = new Function<SkuRecommendation, Long>() {
			@Override
			public Long apply(SkuRecommendation arg0) {
				return arg0.getProductSKUId();
			}
		};
		List<Long> skuIds = Lists.transform(skuRecommendations, skuIdFunc);
		Map<Long, Boolean> productStatusMap = productSKUDao.getProductStatusIsOnline(skuIds);
		return convertSkuRecommendationTODTOList(skuRecommendations, productStatusMap);
	}

	private List<SkuRecommendationDTO> convertSkuRecommendationTODTOList(List<SkuRecommendation> skuRecommendations,
			Map<Long, Boolean> productStatusMap) {
		List<SkuRecommendationDTO> skuRecommendationDTOs = new ArrayList<SkuRecommendationDTO>();
		if (CollectionUtil.isEmptyOfList(skuRecommendations)) {
			return skuRecommendationDTOs;
		}
		SkuRecommendationDTO skuRecommendationDTO = null;
		for (SkuRecommendation skuRecommendation : skuRecommendations) {
			skuRecommendationDTO = new SkuRecommendationDTO(skuRecommendation);
			if (productStatusMap.get(skuRecommendation.getProductSKUId()) == null) {
				skuRecommendationDTO.setProductStatusMsg("商品已删除");
			} else if (!productStatusMap.get(skuRecommendation.getProductSKUId())) {
				skuRecommendationDTO.setProductStatusMsg("商品已下架");
			}
			skuRecommendationDTOs.add(skuRecommendationDTO);
		}
		return skuRecommendationDTOs;

	}

	@Override
	@Transaction
	public boolean addOrUpdateSkuRecommendationDTOs(List<SkuRecommendationDTO> skuRecommendationDTOs)
			throws ItemCenterException {
		if (CollectionUtil.isEmptyOfList(skuRecommendationDTOs)) {
			return false;
		}
		boolean result = true;
		List<SkuRecommendationDTO> addList = new ArrayList<SkuRecommendationDTO>();
		for (SkuRecommendationDTO skuRecommendationDTO : skuRecommendationDTOs) {
			if (skuRecommendationDTO.getId() > 0) {
				if (skuRecommendationDTO.getProductSKUId() > 0) {
					result = skuRecommendationDao.updateObjectByKey(skuRecommendationDTO);
				} else {
					result = skuRecommendationDao.deleteObjectByKey(skuRecommendationDTO);
				}
			} else {
				addList.add(skuRecommendationDTO);
			}
			if (!result) {
				throw new ItemCenterException("add Or Update SkuRecommendationDTOs failed");
			}
		}
		if (addList.size() > 0) {
			result = skuRecommendationDao.addObjects(addList);
		}
		if (!result) {
			throw new ItemCenterException("add Or Update SkuRecommendationDTOs failed");
		}
		return result;
	}

	@Override
	public int countSyncSKUBySPU(ItemSPU spu) {
		return productSKUDao.countSyncSKUBySPU(spu);
	}

	@Override
	public List<Long> getSyncSKUIdBySPU(ItemSPU spu) {
		return productSKUDao.getSyncSKUIdBySPU(spu);
	}

	@Override
	public int syncSKUByIds(String ids, ItemSPU spu) {
		return productSKUDao.syncSKUByIds(ids, spu);
	}
}
