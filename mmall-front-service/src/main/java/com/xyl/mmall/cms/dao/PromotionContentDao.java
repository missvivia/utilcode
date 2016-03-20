package com.xyl.mmall.cms.dao;


import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.cms.meta.Area;
import com.xyl.mmall.cms.meta.PromotionContent;

/**
 * 
 * @author hzliujie
 * @create 2014年9月24日
 *
 */
public interface PromotionContentDao extends AbstractDao<PromotionContent> {
	
	/**
	 * 根据时间和省份id查找推广内容
	 * @param searchTime
	 * @param areaId
	 * @return
	 */
	public List<PromotionContent> getPromotionContentByProvinceAndTime(long searchTime, int areaId);

	
	/**
	 * 根据时间和省份id和position查找推广内容
	 * @param searchTime
	 * @param areaId
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
	 * 修改推广内容
	 * @param id
	 * @return
	 */
	public boolean updatePromotionContent(PromotionContent content);
	
	/**
	 * 
	 * @param searchTime
	 * @param areaId
	 * @param device
	 * @return
	 */
	public List<PromotionContent> getPCByProvTimeDevice(long searchTime, long areaId, int device);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public PromotionContent getPCById(long id);
	
	/**
	 * 修改序号
	 * @param sequence
	 * @return
	 */
	public boolean updateSequence(long areaId,int sequence,int toSequence);	
 
	/**
	 * 
	 * @param id
	 * @param online
	 * @return
	 */
	public boolean updateOnline(long id,int online);
	
	/**
	 * 获取某个省的web及移动端推广内容总数
	 * @param areaId
	 * @return
	 */
	public int getTotalByProvince(long areaId,int device);
}
