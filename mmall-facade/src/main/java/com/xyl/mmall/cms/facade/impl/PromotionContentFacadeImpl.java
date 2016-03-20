package com.xyl.mmall.cms.facade.impl;

import java.util.List;

import javax.annotation.Resource;

import com.xyl.mmall.cms.facade.PromotionContentFacade;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.cms.service.PromotionContentService;
import com.xyl.mmall.framework.annotation.Facade;

/**
 * 推广内容服务Facade
 * 
 * @author hzliujie 
 * @create 2014年9月24日
 * 
 */
@Facade
public class PromotionContentFacadeImpl implements PromotionContentFacade {

	@Resource
	private PromotionContentService promotionContentService;
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.PromotionContentFacade#addContent(com.xyl.mmall.cms.meta.PromotionContent)
	 */
	@Override
	public PromotionContent addContent(PromotionContent content) {
		return promotionContentService.addPromotionContent(content);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.PromotionContentFacade#getPromotionContentByProvinceAndTime(long, int)
	 */
	@Override
	public List<PromotionContent> getPromotionContentByProvinceAndTime(
			long searchTime, int provinceId) {
		return promotionContentService.getPromotionContentByProvinceAndTime(searchTime, provinceId);
	}

	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.PromotionContentFacade#getPromotionContentByAreaAndTimeAndPosition(long, long, int)
	 */
	@Override
	public List<PromotionContent> getPromotionContentByAreaAndTimeAndPosition(
			long searchTime, long areaId, int position) {
		return promotionContentService.getPromotionContentByAreaAndTimeAndPosition(searchTime, areaId, position);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.PromotionContentFacade#deletePromotionContent(long)
	 */
	@Override
	public boolean deletePromotionContent(long id) {
		return promotionContentService.deletePromotionContent(id);
	}

	@Override
	public boolean updatePromotionContent(PromotionContent content) {
		return promotionContentService.updatePromotionContent(content);
	}

	@Override
	public List<PromotionContent> getMobilePCByProvTimeDevice(long searchTime, long provinceId) {
		return promotionContentService.getPCByProvTimeDevice(searchTime, provinceId, 1);

	}

	@Override
	public boolean adjustSequenceOfId(long id, int move,long searchTime) {
		return promotionContentService.adjustSequenceOfId(id, move, searchTime);
	}

	@Override
	public PromotionContent getPCById(long id) {
		return promotionContentService.getPCById(id);
	}

	@Override
	public boolean changeOnlineStatusPC(long id, int action) {
		return promotionContentService.changeOnlineStatusPC(id, action);
	}

}
