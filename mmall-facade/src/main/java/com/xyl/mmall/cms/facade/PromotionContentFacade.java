package com.xyl.mmall.cms.facade;


import java.util.List;

import com.xyl.mmall.cms.meta.PromotionContent;


/**
 * 推广内容服务Facade
 * 
 * @author hzliujie 
 * @create 2014年9月24日
 * 
 */
public interface PromotionContentFacade {
	
	/**
	 * 添加推广内容
	 * @param content
	 * @return
	 */
	public PromotionContent addContent(PromotionContent content);
	
	/**
	 * 根据省份和查询时间获取推广内容
	 * @param searchTime
	 * @param provinceId
	 * @return
	 */
	public List<PromotionContent> getPromotionContentByProvinceAndTime(long searchTime, int provinceId);
	
	
	/**
	 * 根据省份和查询时间position获取推广内容
	 * @param searchTime
	 * @param areaId
	 * @return
	 */
	public List<PromotionContent> getPromotionContentByAreaAndTimeAndPosition(long searchTime, long areaId,int position);
	
	/**
	 * 根据id删除推广内容
	 * @param id
	 * @return
	 */
	public boolean deletePromotionContent(long id);
	
	/**
	 * 根据id修改推广内容
	 * @param content
	 * @return
	 */
	public boolean updatePromotionContent(PromotionContent content);
	
	/**
	 * 根据省份和查询设备时间获取推广内容
	 * @return
	 */
	public List<PromotionContent> getMobilePCByProvTimeDevice(long searchTime, long provinceId);
	
	/**
	 * 
	 * @param provinceId
	 * @param sequence
	 * @param move
	 * @return
	 */
	public boolean adjustSequenceOfId(long id, int move ,long searchTime);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public PromotionContent getPCById(long id);
	
	/**
	 * 
	 * @param id
	 * @param action
	 * @return
	 */
	public boolean changeOnlineStatusPC(long id, int action);
}
