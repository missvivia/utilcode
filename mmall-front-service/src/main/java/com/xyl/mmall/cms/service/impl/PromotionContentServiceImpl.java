package com.xyl.mmall.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.xyl.mmall.cms.dao.AreaDao;
import com.xyl.mmall.cms.dao.PromotionContentDao;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.cms.service.PromotionContentService;
import com.xyl.mmall.framework.annotation.Transaction;
/**
 * 
 * @author hzliujie
 * @create 2014年9月24日
 */
@Service
public class PromotionContentServiceImpl implements PromotionContentService {

	@Autowired
	public PromotionContentDao promotionContentDao;
	
	@Autowired
	public AreaDao areaDao;
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.service.PromotionContentService#addPromotionContent(com.xyl.mmall.cms.meta.PromotionContent)
	 */
	@Override
	//@CacheEvict(value = "bannerCache")
	public PromotionContent addPromotionContent(PromotionContent content) {
		int sequence = promotionContentDao.getTotalByProvince(content.getAreaId(),content.getDevice().getIntValue())+1;
		content.setSequence(sequence);
		PromotionContent result = promotionContentDao.addObject(content);
		return result;
	}


	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.service.PromotionContentService#getPromotionContentByProvinceAndTime(long, int)
	 */
	@Override
	public List<PromotionContent> getPromotionContentByProvinceAndTime(
			long searchTime, int provinceId) {
		return promotionContentDao.getPromotionContentByProvinceAndTime(searchTime, provinceId);
	}


	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.service.PromotionContentService#getPromotionContentByProvinceAndTime(long, int, int)
	 */
	@Override
	@Cacheable(value = "bannerCache")
	public List<PromotionContent> getPromotionContentByAreaAndTimeAndPosition(
			long searchTime, long areaId, int position) {
		return promotionContentDao.getPromotionContentByAreaAndTimeAndPosition(searchTime,areaId,position);
	}


	@Override
	public boolean deletePromotionContent(long id) {
		return promotionContentDao.deleteById(id);
	}


	@Override
	public boolean updatePromotionContent(PromotionContent content) {
		return promotionContentDao.updatePromotionContent(content);
	}


	@Override
	public List<PromotionContent> getPCByProvTimeDevice(long searchTime, long provinceId, int device) {
		return promotionContentDao.getPCByProvTimeDevice(searchTime, provinceId, device);
	}


	@Transaction
	@Override
	public boolean adjustSequenceOfId(long id, int move, long searchTime) {
		try{
			PromotionContent pc = getPCById(id);
			List<PromotionContent> list = getPCByProvTimeDevice(searchTime, pc.getAreaId(), pc.getDevice().getIntValue());
			int index = -1;
			for(int i=0;i<list.size();i++){
				if(list.get(i).getId()==pc.getId()){
					index = i;
					break;
				}
					
			}
			if(move<0){
				if(index<=0)
					return false;
				promotionContentDao.updateSequence(pc.getAreaId(), pc.getSequence(), 0);
				promotionContentDao.updateSequence(pc.getAreaId(), list.get(index-1).getSequence(),pc.getSequence());
				promotionContentDao.updateSequence(pc.getAreaId(), 0, list.get(index-1).getSequence());
			}else{
				if(index>=list.size()-1)
					return false;
				promotionContentDao.updateSequence(pc.getAreaId(), pc.getSequence(), 0);
				promotionContentDao.updateSequence(pc.getAreaId(), list.get(index+1).getSequence(),pc.getSequence());
				promotionContentDao.updateSequence(pc.getAreaId(), 0, list.get(index+1).getSequence());
			}
			return true;
		}catch(Exception e){
			throw e;
		}
	}


	@Override
	public PromotionContent getPCById(long id) {
		return promotionContentDao.getPCById(id);
	}


	@Override
	public boolean changeOnlineStatusPC(long id, int action) {
		boolean result = false;
		if(action==0)
			result = promotionContentDao.updateOnline(id, 0);
		else if(action==1)
			result = promotionContentDao.updateOnline(id, 1);
		return result;
	}

}
