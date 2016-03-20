package com.xyl.mmall.saleschedule.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.saleschedule.dao.BrandUserFavDao;
import com.xyl.mmall.saleschedule.dto.UserFavListDTO;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.meta.BrandUserFav;

/**
 * 用户收藏表的SQL实现
 * @author chengximing
 *
 */
@Repository
public class BrandUserFavDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<BrandUserFav>
	implements BrandUserFavDao {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	private String HAS_ACTIVE_FOR_BRAND_LIKE="select * from Mmall_SaleSchedule_BrandUserFav t,Mmall_SaleSchedule_Schedule a where t.BrandId=a.BrandId and t.UserId=? and a.CurSupplierAreaId=? "
//			+ " and a.StartTime<=? and a.StartTime>? and a.Status=?";
	
	@Override
	public boolean addBrandIntoFavList(long userId, long brandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "brandId", brandId);
		List<BrandUserFav> ret = queryObjects(sql);
		if (ret.size() == 0) {
			BrandUserFav fav = new BrandUserFav();
			fav.setBrandId(brandId);
			fav.setCreateDate(System.currentTimeMillis());
			fav.setFavId(-1);
			fav.setUserId(userId);
			addObject(fav);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeBrandFromFavList(long userId, long brandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "brandId", brandId);
		List<BrandUserFav> ret = queryObjects(sql);
		if (ret.size() == 0) {
			return false;
		} else {
			for (BrandUserFav fav : ret) {
				deleteObjectByKey(fav);
			}
			return true;
		}
	}

	@Cacheable(value = "brandCache")
	@Override
	public int getBrandFavCount(long brandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT COUNT(*) FROM Mmall_SaleSchedule_BrandUserFav ");
		//SqlGenUtil.appendExtParamObject(sql, "brandId", brandId);
		sql.append(" WHERE brandId = " + brandId);
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				int size = rs.getInt("COUNT(*)");
				return size;
			}
		} catch (SQLException e) {
			return 0;
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return 0;
	}

	@Override
	public boolean isBrandInFavList(long userId, long brandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "brandId", brandId);
		List<BrandUserFav> ret = queryObjects(sql);
		if (ret.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Cacheable(value = "brandCache")
	@Override
	public List<BrandUserFav> getBrandUserFavListByUserId(DDBParam param, long userId, long time) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (time > 0) {
			sql.append(" AND createDate <= ").append(time);
		}
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		return getListByDDBParam(sql.toString(), param);
	}

	@Cacheable(value = "brandCache")
	@Override
	public Map<Long, Boolean> getBrandFavStateByIds(List<Long> brandIdList, long userId) {
		Map<Long, Boolean> stateMap = new HashMap<Long, Boolean>();
		if (userId <= 0) {
			for (Long brandId : brandIdList) {
				stateMap.put(brandId, false);
			}
			return stateMap;
		}
		if (brandIdList.size() > 0) {
			StringBuilder brandIdListString = new StringBuilder(256);
			brandIdListString.append("(");
			for (Long id : brandIdList) {
				brandIdListString.append(id).append(",");
			}
			brandIdListString.deleteCharAt(brandIdListString.lastIndexOf(","));
			brandIdListString.append(")");
			StringBuilder sql = new StringBuilder(512);
			sql.append(genSelectSql());
			SqlGenUtil.appendExtParamObject(sql, "userId", userId);
			sql.append(" AND brandId IN ").append(brandIdListString);
			List<BrandUserFav> favList = queryObjects(sql);
			for (BrandUserFav fav : favList) {
				stateMap.put(fav.getBrandId(), true);
			}
			for (Long brandId : brandIdList) {
				if (!stateMap.containsKey(brandId)) {
					stateMap.put(brandId, false);
				}
			}
		}
		return stateMap;
	}

	@Cacheable(value = "brandCache")
	@Override
	public Map<Long, Integer> getBrandFavCountByIds(List<Long> brandIdList) {
		Map<Long, Integer> countMap = new HashMap<Long, Integer>();
		if (brandIdList.size() > 0) {
			StringBuilder brandIdListString = new StringBuilder(256);
			brandIdListString.append("(");
			for (Long id : brandIdList) {
				brandIdListString.append(id).append(",");
			}
			brandIdListString.deleteCharAt(brandIdListString.lastIndexOf(","));
			brandIdListString.append(")");
			StringBuilder sql = new StringBuilder(512);
			sql.append("SELECT brandId, COUNT(brandId) FROM Mmall_SaleSchedule_BrandUserFav");
			sql.append(" WHERE brandId IN ").append(brandIdListString).append(" GROUP BY brandId");
			DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
			ResultSet rs = dbr.getResultSet();
			try {
				while (rs.next()) {
					long brandId = rs.getLong("brandId");
					int count = rs.getInt("COUNT(brandId)");
					countMap.put(brandId, count);
				}
			} catch (SQLException e) {
				return countMap;
			} finally {
				if (dbr != null)
					dbr.close();
			}
		}
		return countMap;
	}
	
	@Override
	public boolean hasActiveForBrandLike(long userId, long areaId, long startTime, long endTime) {
		DDBParam param = new DDBParam("createDate", false, 0, 0);
		List<BrandUserFav> list = getBrandUserFavListByUserId(param, userId, 0);
		if (list.size() == 0) {
			return false;
		}
		StringBuilder brandIdListString = new StringBuilder(256);
		brandIdListString.append("(");
		for (BrandUserFav fav : list) {
			brandIdListString.append(fav.getBrandId()).append(",");
		}
		brandIdListString.deleteCharAt(brandIdListString.lastIndexOf(","));
		brandIdListString.append(")");
		ResultSet rs = null;
		boolean hasActiveFlag = false;
		DBResource dbResource = null;
		try {
			StringBuilder sql = new StringBuilder(256);
			sql.append("SELECT * FROM Mmall_SaleSchedule_Schedule WHERE brandId IN ").append(brandIdListString);
			sql.append(" AND curSupplierAreaId = ").append(areaId);
			sql.append(" AND startTime <= ").append(startTime).append(" AND endTime > ").append(endTime);
			sql.append(" AND status = ").append(ScheduleState.BACKEND_PASSED.getIntValue());
			dbResource = this.getSqlSupport().excuteQuery(sql.toString());
			rs = dbResource.getResultSet();
			hasActiveFlag = rs.next();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally{
			try {
				if(dbResource != null){
					dbResource.close();
				}
				if(rs != null){
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return hasActiveFlag;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.saleschedule.dao.BrandUserFavDao#getUserFavListByBrandIdList(java.util.List, long, int)
	 */
	@Override
	public RetArg getUserFavListByBrandIdList(List<Long> brandIdList, long timeAfter, 
			int limit, int offset) {
		DDBParam param = new DDBParam("createDate", true, limit, offset);
		List<UserFavListDTO> list = new ArrayList<>();
		int total = 0;
		if (brandIdList.size() == 0) {
			RetArg retArg = new RetArg();
			RetArgUtil.put(retArg, list);
			RetArgUtil.put(retArg, total);
			return retArg;
		}
		StringBuilder brandIdListString = new StringBuilder(256);
		brandIdListString.append("(");
		for (Long brandId : brandIdList) {
			brandIdListString.append(brandId).append(",");
		}
		brandIdListString.deleteCharAt(brandIdListString.lastIndexOf(","));
		brandIdListString.append(")");
		
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT * FROM Mmall_SaleSchedule_BrandUserFav where brandId in ").append(brandIdListString).
			append(" and createDate > ").append(timeAfter);
		List<BrandUserFav> retList = getListByDDBParam(sql.toString(), param);
		total = param.getTotalCount();
		for (BrandUserFav fav : retList) {
			UserFavListDTO dto = new UserFavListDTO();
			dto.setUserId(fav.getUserId());
			dto.setFavIdList(getBrandFavListByUser(fav.getUserId(), brandIdListString.toString()));
			list.add(dto);
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, list);
		RetArgUtil.put(retArg, total);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.saleschedule.dao.BrandUserFavDao#getBrandFavListByUser(long, java.util.List)
	 */
	@Override
	public List<Long> getBrandFavListByUser(long userId, String brandIdListString) {
		List<Long> list = new ArrayList<>();
		ResultSet rs = null;
		DBResource dbResource = null;
		
		try {
			StringBuilder sql = new StringBuilder(256);
			sql.append("select brandId from Mmall_SaleSchedule_BrandUserFav where brandId in ").append(brandIdListString);
			sql.append(" and userId = ").append(userId);
			dbResource = this.getSqlSupport().excuteQuery(sql.toString());
			rs = dbResource.getResultSet();
			while (rs.next()) {
				long brandId = rs.getLong("brandId");
				list.add(brandId);
			}
		} catch (Exception e) {
			return list;
		} finally {
			try {
				if(dbResource != null){
					dbResource.close();
				}
				if(rs != null){
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
