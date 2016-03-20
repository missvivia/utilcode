package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.order.dao.SkuOrderStockDao;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.order.meta.SkuOrderStock;
import com.xyl.mmall.order.service.SkuOrderStockService;

/**
 * @author dingmingliang
 * 
 */
@Service("skuOrderStockService")
public class SkuOrderStockServiceImpl implements SkuOrderStockService {

	@Autowired
	SkuOrderStockDao skuOrderStockDao;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.SkuOrderStockService#getSkuOrderStockDTOBySkuId(long)
	 */
	//@Cacheable(value = "skuOrderStock")
	public SkuOrderStockDTO getSkuOrderStockDTOBySkuId(long skuId) {
		// 1.从DB中读取数据
		SkuOrderStock skuOrderStock = new SkuOrderStock();
		skuOrderStock.setSkuId(skuId);
		skuOrderStock = skuOrderStockDao.getObjectByPrimaryKeyAndPolicyKey(skuOrderStock);
		// 2.封装DTO对象
		SkuOrderStockDTO skuOrderStockDTO = SkuOrderStockDTO.genSkuOrderStockDTO(skuOrderStock);
		return skuOrderStockDTO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.SkuOrderStockService#getSkuOrderStockDTOListBySkuIds(java.util.Collection)
	 */
	//@Cacheable(value = "skuOrderStock")
	public List<SkuOrderStockDTO> getSkuOrderStockDTOListBySkuIds(Collection<Long> skuIdColl) {
		// 1.从DB中读取数据
		List<SkuOrderStock> skuOrderStockList = skuOrderStockDao.getListBySkuIds(skuIdColl);
		if (CollectionUtil.isEmptyOfCollection(skuOrderStockList))
			return null;
		// 2.封装DTO对象
		List<SkuOrderStockDTO> skuOrderStockDTOList = new ArrayList<>();
		for (SkuOrderStock skuOrderStock : skuOrderStockList) {
			SkuOrderStockDTO skuOrderStockDTO = SkuOrderStockDTO.genSkuOrderStockDTO(skuOrderStock);
			CollectionUtil.addOfListFilterNull(skuOrderStockDTOList, skuOrderStockDTO);
		}
		return skuOrderStockDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.SkuOrderStockService#addSkuOrderStockDTO(com.xyl.mmall.order.dto.SkuOrderStockDTO)
	 */
	@Transaction
	public boolean addSkuOrderStockDTO(SkuOrderStockDTO skuOrderStockDTO) {
		// 先删除旧数据,再添加新数据
		skuOrderStockDao.deleteObjectByKey(skuOrderStockDTO);
		return skuOrderStockDao.addObject(skuOrderStockDTO) != null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.SkuOrderStockService#addSkuOrderStockDTOColl(java.util.Collection)
	 */
	@Transaction
	public boolean saveSkuOrderStockDTOColl(Collection<SkuOrderStockDTO> skuOrderStockDTOColl) {
		// 先删除旧数据,再添加新数据
		skuOrderStockDao.deleteObjectByKeys(skuOrderStockDTOColl);
		return skuOrderStockDao.addObjects(skuOrderStockDTOColl);
	}

	@Override
	@Transaction
	public boolean updateSkuOrderStock(long skuId, int count) {
		SkuOrderStock skuOrderStock = new SkuOrderStock();
		skuOrderStock.setSkuId(skuId);
		skuOrderStock = skuOrderStockDao.getLockByKey(skuOrderStock);
		skuOrderStock.setStockCount(count);
		return skuOrderStockDao.updateObjectByKey(skuOrderStock);
	}

	@Override
	@Transaction
	public boolean deleteSkuOrderStock(long skuId) {
		return skuOrderStockDao.deleteById(skuId);
	}

	@Override
	@Transaction
	public boolean bacthDeleteSkuOrderStock(List<Long> skuIds) {
		List<SkuOrderStock>skuOrderStocks = new ArrayList<SkuOrderStock>();
		for(Long skuId:skuIds){
			SkuOrderStock skuOrderStock = new SkuOrderStock();
			skuOrderStock.setSkuId(skuId);
		}
		return skuOrderStockDao.deleteObjectByKeys(skuOrderStocks);
	}
}
