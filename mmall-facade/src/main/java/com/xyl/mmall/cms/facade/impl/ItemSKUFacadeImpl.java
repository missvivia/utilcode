/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.backend.vo.ProductSKULimitConfigVO;
import com.xyl.mmall.backend.vo.ProductSKULimitRecordVO;
import com.xyl.mmall.cms.facade.ItemSKUFacade;
import com.xyl.mmall.cms.vo.ItemSKUBriefVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitRecordDTO;
import com.xyl.mmall.saleschedule.service.ProductSKULimitService;

/**
 * ItemSKUFacadeImpl.java created by yydx811 at 2015年11月19日 下午11:55:46
 * cms商品facade接口实现
 *
 * @author yydx811
 */
@Facade
public class ItemSKUFacadeImpl implements ItemSKUFacade {
	
	@Autowired
	private ItemProductService itemProductService;
	
	@Autowired
	private ProductSKULimitService productSKULimitService;
	
	@Override
	public ItemSKUBriefVO getItemBriefInfo(long skuId) {
		// 获取商品简略信息
		ProductSKUDTO skuDTO = itemProductService.getProductSKUBreifInfo(skuId);
		if (skuDTO == null) {
			return null;
		}
		ItemSKUBriefVO briefInfo = new ItemSKUBriefVO(skuDTO);
		return briefInfo;
	}

	@Override
	public ProductSKULimitRecordVO getItemLimitConfigFromCacheAndDB(
			ProductSKULimitConfigVO limitConfigVO, long skuId, long userId) {
		long now = System.currentTimeMillis();
		// 配置为空或者限购时间未到或超过
		if (limitConfigVO == null 
				|| limitConfigVO.getLimitStartTime() > now 
				|| limitConfigVO.getLimitEndTime() < now) {
			return null;
		}
		ProductSKULimitRecordDTO limitRecordDTO = 
				productSKULimitService.getProductSKULimitRecordNoCache(userId, skuId);
		if (limitRecordDTO.getId() > 0l) {
			int temp = (int) limitConfigVO.getLimitPeriod();
			long time = limitRecordDTO.getCreateTime() + temp * DateUtil.ONE_DAY_MILLISECONDS;
			if (time <= now) {
				limitRecordDTO = new ProductSKULimitRecordDTO();
			}
		}
		ProductSKULimitRecordVO limitRecordVO = new ProductSKULimitRecordVO(limitRecordDTO);
		if (limitRecordVO.getRecordId() > 0l) {
			int cacheNum = productSKULimitService.getTotalBuyNumFromCache(skuId, userId);
			limitRecordVO.setBuyCacheNum(cacheNum);
		}
		return limitRecordVO;
	}

	@Override
	public int syncCache(long skuId, long userId) {
		return productSKULimitService.syncCache(skuId, userId);
	}

	@Override
	public int updateLimitRecord(long skuId, long userId, int canBuyNum) {
		return productSKULimitService.updateProductSKULimitRecord(userId, skuId, canBuyNum);
	}

}
