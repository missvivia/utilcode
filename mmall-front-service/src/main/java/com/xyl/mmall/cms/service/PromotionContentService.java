package com.xyl.mmall.cms.service;

import java.util.List;

import com.xyl.mmall.cms.meta.PromotionContent;

/**
 * 推广内容服务service
 * 
 * @author hzliujie 
 * @create 2014年9月24日
 * 
 */
public interface PromotionContentService {

	/**
	 * 添加推广内容
	 * @param content
	 * @return
	 */
	public PromotionContent addPromotionContent(PromotionContent content);
	
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
	 * @param position
	 * @return
	 */
	public List<PromotionContent> getPromotionContentByAreaAndTimeAndPosition(long searchTime, long areaId, int position);

	
	/**
	 * 删除推广内容
	 * @param id
	 * @return
	 */
	public boolean deletePromotionContent(long id);
	
	
	/**
	 * 根据id更新推广内容
	 * @param content
	 * @return
	 */
	public boolean updatePromotionContent(PromotionContent content);
	
	/**
	 * 
	 * @param searchTime
	 * @param provinceId
	 * @param device
	 * @return
	 */
	public List<PromotionContent> getPCByProvTimeDevice(long searchTime, long provinceId, int device);
	
	/**
	 * 
	 * @param provinceId
	 * @param sequence
	 * @return
	 */
	public boolean adjustSequenceOfId(long id,int move,long searchTime);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public PromotionContent getPCById(long id);
	
	/**
	 * 调整上下线状态
	 * @param id
	 * @param action
	 * @return
	 */
	public boolean changeOnlineStatusPC(long id,int action);
	
}
