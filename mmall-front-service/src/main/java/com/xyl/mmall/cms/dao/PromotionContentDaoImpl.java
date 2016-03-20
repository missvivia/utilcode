package com.xyl.mmall.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.AreaCodeUtil;

/**
 * 
 * @author hzliujie
 * @create 2014年9月24日
 * 
 */
@Repository
public class PromotionContentDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<PromotionContent> implements
		PromotionContentDao {

	private String tableName = this.getTableName();

	private String get_promotion_by_area_and_time = "SELECT * FROM " + tableName
			+ " where AreaId=? and StartTime<=? and EndTime>=? and Device=0";

	private String get_promotion_by_area_and_time_position = "SELECT * FROM " + tableName
			+ " where (AreaId = 0 or AreaId = ? or AreaId = ? or AreaId = ?) and StartTime <= ? and EndTime >= ?"
			+ " and PositionId = ? and Device = 0 order by Sequence";

	private String delete_promotion_by_id = "DELETE FROM " + tableName + " where Id=?";

	private String update_promotion_by_id = "UPDATE "
			+ tableName
			+ " SET PromotionType=? ,StartTime=?, EndTime=?, ScheduleId=? ,AreaId=?, positionId=? ,Sequence=? ,ActivityUrl=? ,ImgUrl=? , ImgUrl2=? , UpdateTime=? ,PlatformType=? where Id=?";

	private String get_promotion_by_area_and_time_and_device = "SELECT * FROM " + tableName
			+ " where AreaId=? and StartTime<=? and EndTime>=? and device=? order by sequence";

	private String update_sequence = "UPDATE " + tableName
			+ " SET Sequence=? where AreaId=? and Sequence=? and Device=1";

	private String update_online = "UPDATE " + tableName + " SET Online=? Where id=?";

	private String get_total_pc_by_area = "SELECT max(Sequence) FROM " + tableName + " WHERE AreaId=? and Device=?";

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.dao.PromotionContentDao#getPromotionContentByProvinceAndTime(long,
	 *      int)
	 */
	@Override
	public List<PromotionContent> getPromotionContentByProvinceAndTime(long searchTime, int areaId) {
		List<PromotionContent> promotionContentList = queryObjects(get_promotion_by_area_and_time, areaId, searchTime,
				searchTime);
		return promotionContentList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.dao.PromotionContentDao#getPromotionContentByProvinceAndTime(long,
	 *      int, int)
	 */
	@Override
	public List<PromotionContent> getPromotionContentByAreaAndTimeAndPosition(long searchTime, long areaId,
			int positionId) {
		long cityCode = AreaCodeUtil.getCityCode(areaId);
		long provinceCode = AreaCodeUtil.getProvinceCode(areaId);
		List<PromotionContent> promotionContentList = queryObjects(get_promotion_by_area_and_time_position,
				provinceCode, cityCode, areaId, searchTime, searchTime, positionId);
		return promotionContentList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.dao.PromotionContentDao#deletePromotionContent(long)
	 */
	@Override
	public boolean deletePromotionContent(long id) {
		return this.getSqlSupport().excuteUpdate(delete_promotion_by_id, id) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.dao.PromotionContentDao#updatePromotionContent(long)
	 */
	@Override
	public boolean updatePromotionContent(PromotionContent content) {
		return this.getSqlSupport().excuteUpdate(update_promotion_by_id, content.getPromotionType(),
				content.getStartTime(), content.getEndTime(), content.getBusinessId(), content.getAreaId(),
				content.getPositionId(), content.getSequence(), content.getActivityUrl(), content.getImgUrl(),
				content.getImgUrl2(), content.getUpdateTime(), content.getPlatformType(), content.getId()) > 0;
	}

	@Override
	public List<PromotionContent> getPCByProvTimeDevice(long searchTime, long areaId, int device) {
		List<PromotionContent> promotionContentList = queryObjects(get_promotion_by_area_and_time_and_device, areaId,
				searchTime, searchTime, device);
		return promotionContentList;
	}

	@Override
	public PromotionContent getPCById(long id) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "id", id);
		return queryObject(sql);
	}

	@Override
	public boolean updateSequence(long areaId, int sequence, int toSequence) {
		return this.getSqlSupport().excuteUpdate(update_sequence, toSequence, areaId, sequence) > 0;
	}

	@Override
	public boolean updateOnline(long id, int online) {
		return this.getSqlSupport().excuteUpdate(update_online, online, id) > 0;
	}

	@Override
	public int getTotalByProvince(long areaId, int device) {
		return this.getSqlSupport().queryCount(get_total_pc_by_area, areaId, device);
	}

}
