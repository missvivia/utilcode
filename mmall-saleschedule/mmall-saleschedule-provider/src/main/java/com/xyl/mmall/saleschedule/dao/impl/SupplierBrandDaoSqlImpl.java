package com.xyl.mmall.saleschedule.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.saleschedule.dao.SupplierBrandDao;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.enums.BrandImgSize;
import com.xyl.mmall.saleschedule.enums.BrandProbability;
import com.xyl.mmall.saleschedule.enums.BrandStatus;
import com.xyl.mmall.saleschedule.meta.Brand;
import com.xyl.mmall.saleschedule.meta.SupplierBrand;

/**
 * BrandDao的SQL实现
 * @author chengximing
 *
 */
@Repository
public class SupplierBrandDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<SupplierBrand> 
	implements SupplierBrandDao {

	@Cacheable(value = "brandCache")
	@Override
	public RetArg/*List<SupplierBrand>*/ getSupplierBrandListBySupplierId(DDBParam param, long supplierId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		List<SupplierBrand> list = getListByDDBParam(sql.toString(), param);
		RetArg ret = new RetArg();
		RetArgUtil.put(ret, param);
		RetArgUtil.put(ret, list);
		return ret;
    }

	@Cacheable(value = "brandCache")
	@Override
	public SupplierBrand getOnlineSupplierBrandBySupplierId(long supplierId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		SqlGenUtil.appendExtParamObject(sql, "status", BrandStatus.BRAND_AUDITPASSED_USING.getIntValue());
		SqlGenUtil.appendExtParamObject(sql, "supplierIsFreeze", false);
		return queryObject(sql);
	}
	
	@Cacheable(value = "brandCache")
	@Override
	public List<SupplierBrand> getOnlineSupplierBrandList(long brandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "status", BrandStatus.BRAND_AUDITPASSED_USING.getIntValue());
		SqlGenUtil.appendExtParamObject(sql, "brandId", brandId);
		SqlGenUtil.appendExtParamObject(sql, "supplierIsFreeze", false);
		return queryObjects(sql);
	}
	
	@Override
	public boolean updateBrandStatus(SupplierBrand brand) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("supplierBrandId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("status");
		setOfUpdate.add("statusUpdateDate");
		if (brand.getStatus() == BrandStatus.BRAND_AUDITREFUSED ||
				brand.getStatus() == BrandStatus.BRAND_AUDITPASSED_UNUSED ||
				brand.getStatus() == BrandStatus.BRAND_AUDITING) {
			setOfUpdate.add("rejectReason");
		}
		return SqlGenUtil.update(getTableName(), setOfUpdate, setOfWhere, brand, getSqlSupport());
    }

	@Override
	public boolean updateBrandContent(SupplierBrand brand) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("supplierBrandId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("brandDesc1");
		setOfUpdate.add("brandDesc2");
		setOfUpdate.add("status");
		setOfUpdate.add("rejectReason");
		return SqlGenUtil.update(getTableName(), setOfUpdate, setOfWhere, brand, getSqlSupport());
	}
	
	@Cacheable(value = "brandCache")
	@Override
	public SupplierBrand getSupplierBrandById(long id) {
		return getObjectById(id);
	}

	@Cacheable(value = "brandCache")
	@Override
	public List<SupplierBrand> getAuditSupplierBrandListBySupplierId(
			long supplierId, int status, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		if (status == 0) {
			SqlGenUtil.appendExtParamObject(sql, "status", BrandStatus.BRAND_AUDITING.getIntValue());
		} else {
			sql.append(" AND ( ");
			SqlGenUtil.appendExtParamObject(sql, "status", BrandStatus.BRAND_AUDITPASSED_UNUSED.getIntValue());
			sql.append(" OR ");
			SqlGenUtil.appendExtParamObject(sql, "status", BrandStatus.BRAND_AUDITPASSED_USING.getIntValue());
			sql.append(" OR ");
			SqlGenUtil.appendExtParamObject(sql, "status", BrandStatus.BRAND_AUDITREFUSED.getIntValue());
			sql.append(" ) ");
		}
		//DDBParam param = new DDBParam("supplierBrandId", true, limit, offset);
		return getListByDDBParam(sql.toString(), param);
	}

	@Override
	public List<BrandDTO> getBrandsBySupplerIdList(List<Long> supplierIdList) {
		StringBuilder supplerListString = new StringBuilder(256);
		if (supplierIdList.size() > 0) {
			supplerListString.append("(");
			for (Long supplierId : supplierIdList) {
				supplerListString.append(supplierId).append(",");
			}
			supplerListString.deleteCharAt(supplerListString.lastIndexOf(","));
			supplerListString.append(") ");
		} else {
			List<BrandDTO> dtoList = new ArrayList<>();
			return dtoList;
		}
		// 优化SQL查询 拆分成两步 第一步从business中确定出对应的brand
		Map<Long, Long> brandIdsMap = new HashMap<>();
		StringBuilder sqlBusiness = new StringBuilder(512);
		sqlBusiness.append("SELECT id, actingBrandId FROM Mmall_CMS_Business WHERE id IN ").append(supplerListString);
		DBResource dbr = this.getSqlSupport().excuteQuery(sqlBusiness.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				long supplierId = rs.getLong("id");
				long brandId = rs.getLong("actingBrandId");
				brandIdsMap.put(brandId, supplierId);
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		
		StringBuffer brandIdString = new StringBuffer(256);
		if (brandIdsMap.size() > 0) {
			brandIdString.append("(");
			for (Long brandId : brandIdsMap.keySet()) {
				brandIdString.append(brandId).append(",");
			}
			brandIdString.deleteCharAt(brandIdString.lastIndexOf(","));
			brandIdString.append(") ");
		} else {
			return new ArrayList<BrandDTO>();
		}
		
		// 第二步 从品牌表中构建数据
		StringBuilder sql = new StringBuilder(512);
		sql.append("SELECT A.*, B.brandImgUrl FROM Mmall_ItemCenter_Brand A, Mmall_SaleSchedule_BrandLogoImg B ");
		sql.append(" WHERE A.brandId = B.brandId AND A.brandId IN ").append(brandIdString);
		sql.append(" AND B.brandImgSize = ").append(BrandImgSize.SIZE_BRAND_LOGO.getIntValue());
		dbr = this.getSqlSupport().excuteQuery(sql.toString());
		rs = dbr.getResultSet();
		List<BrandDTO> dtoList = new ArrayList<>();
		try {
			while (rs.next()) {
				String brandImgUrl = rs.getString("brandImgUrl");
				long brandId = rs.getLong("brandId");
				String brandNameEn = rs.getString("brandNameEn");
				String brandNameZh = rs.getString("brandNameZh");
				int brandProbability = rs.getInt("brandProbability");
				long brandUpdateDate = rs.getLong("brandUpdateDate");
				long createDate = rs.getLong("createDate");
				String createUser = rs.getString("createUser");
				Brand brand = new Brand();
				brand.setBrandId(brandId);
				brand.setBrandNameEn(brandNameEn);
				brand.setBrandNameZh(brandNameZh);
				brand.setBrandProbability(BrandProbability.CATEGORY_TYPE_A.genEnumByIntValue(brandProbability));
				brand.setBrandUpdateDate(brandUpdateDate);
				brand.setCreateDate(createDate);
				brand.setCreateUser(createUser);
				BrandDTO brandDTO = new BrandDTO();
				brandDTO.setBrand(brand);
				brandDTO.setSupplierId(brandIdsMap.get(brandId));
				brandDTO.setLogo(brandImgUrl);
				dtoList.add(brandDTO);
			}
		} catch (SQLException e) {
			// return dtoList;
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return dtoList;
	}

	@Override
	public RetArg searchAuditBrand(List<Long> businessIdList, int status,
			String key, DDBParam param) {
		StringBuilder supplerListString = new StringBuilder(256);
		if (businessIdList.size() > 0) {
			supplerListString.append("(");
			for (Long supplierId : businessIdList) {
				supplerListString.append(supplierId).append(",");
			}
			supplerListString.deleteCharAt(supplerListString.lastIndexOf(","));
			supplerListString.append(") ");
		} else {
			RetArg retArg = new RetArg();
			List<SupplierBrand> list = new ArrayList<>();
			RetArgUtil.put(retArg, list);
			RetArgUtil.put(retArg, param);
			return retArg;
		}
		
		RetArg retArg = new RetArg();
		StringBuilder sql = new StringBuilder(512);
		sql.append("SELECT distinct A.* FROM Mmall_SaleSchedule_SupplierBrand A, Mmall_ItemCenter_Brand B ")
			.append("WHERE A.supplierId IN ").append(supplerListString).append(" AND A.brandId = B.brandId");
		if (status == 0) {
			SqlGenUtil.appendExtParamObject(sql, "A.status", BrandStatus.BRAND_AUDITING.getIntValue());
		} else {
			sql.append(" AND ( ");
			SqlGenUtil.appendExtParamObject(sql, "A.status", BrandStatus.BRAND_AUDITPASSED_UNUSED.getIntValue());
			sql.append(" OR ");
			SqlGenUtil.appendExtParamObject(sql, "A.status", BrandStatus.BRAND_AUDITPASSED_USING.getIntValue());
			sql.append(" OR ");
			SqlGenUtil.appendExtParamObject(sql, "A.status", BrandStatus.BRAND_AUDITREFUSED.getIntValue());
			sql.append(" ) ");
		}
		if (key != null && !"".equals(key.trim())) {
			key = key.trim();
//			sql.append(" AND (B.brandNameZh LIKE '%" + key + "%'");
//			sql.append(" OR B.brandNameEn LIKE '%" + key + "%')");
			sql.append(" AND (B.brandNameZh = '").append(key).append("' OR ");
			sql.append(" B.brandNameEn = '").append(key).append("')");
		}
		List<SupplierBrand> list = getListByDDBParam(sql.toString(), param);
		RetArgUtil.put(retArg, list);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	// 这个接口暂时没有使用
	@Override
	public Map<Long, Integer> getAuditBrandCountsBySupplierList(List<Long> supplierIdList) {
		Map<Long, Integer> retMap = new HashMap<>();
		if (supplierIdList.size() == 0) {
			return retMap;
		} else {
			StringBuilder idsString = new StringBuilder(256);
			idsString.append("(");
			for (Long supplierId : supplierIdList) {
				idsString.append(supplierId).append(",");
			}
			idsString.deleteCharAt(idsString.lastIndexOf(","));
			idsString.append(") ");
			StringBuilder sql = new StringBuilder(512);
			sql.append("SELECT COUNT(*), supplierId FROM Mmall_SaleSchedule_SupplierBrand WHERE ");
			sql.append(" supplierId IN ").append(idsString).append(" AND status = ")
				.append(BrandStatus.BRAND_AUDITING.getIntValue()).append(" GROUP BY supplierId");
			DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
			ResultSet rs = dbr.getResultSet();
			try {
				while (rs.next()) {
					int count = rs.getInt("COUNT(*)");
					long supplierId = rs.getLong("supplierId");
					retMap.put(supplierId, count);
				}
			} catch (SQLException e) {
				// return retMap;
			} finally {
				if (dbr != null) 
					dbr.close();
			}
			
			for (Long supplierId : supplierIdList) {
				if (!retMap.containsKey(supplierId)) {
					retMap.put(supplierId, 0);
				}
			}
			return retMap;
		}
	}

	@Override
	public Map<Long, Integer> getAuditBrandCountsByAreaList(List<Long> areaIdList) {
		Map<Long, Integer> retMap = new HashMap<>();
		if (areaIdList.size() == 0) {
			return retMap;
		} else {
			try {
				long inputBitmap = ProvinceCodeMapUtil.getProvinceFmtByCodeList(areaIdList);
				StringBuilder sql = new StringBuilder(256);
				sql.append("select * from Mmall_SaleSchedule_SupplierBrand where status = ").append(BrandStatus.BRAND_AUDITING.getIntValue());
				sql.append(" AND areaFormatCode & ").append(inputBitmap).append(" = areaFormatCode");
				List<SupplierBrand> auditList = queryObjects(sql);
				for (SupplierBrand supplierBrand : auditList) {
					long bitmapCode = supplierBrand.getAreaFormatCode();
					List<Long> areaCodeList = ProvinceCodeMapUtil.getCodeListByProvinceFmt(bitmapCode);
					for (Long areaId : areaCodeList) {
						Integer count = retMap.get(areaId);
						if (count == null) {
							retMap.put(areaId, 1);
						} else {
							retMap.put(areaId, ++count);
						}
					}
				}
			} catch (Exception e) {
				return retMap;
			}
			return retMap;
//			StringBuilder idsString = new StringBuilder(256);
//			idsString.append("(");
//			for (Long areaId : areaIdList) {
//				idsString.append(areaId).append(",");
//			}
//			idsString.deleteCharAt(idsString.lastIndexOf(","));
//			idsString.append(") ");
//			StringBuilder sql = new StringBuilder(512);
//			sql.append("SELECT COUNT(*), areaId FROM Mmall_SaleSchedule_SupplierBrand WHERE ")
//				.append("areaId IN ").append(idsString).append(" AND status = ")
//				.append(BrandStatus.BRAND_AUDITING.getIntValue()).append(" GROUP BY areaId");
//			DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
//			ResultSet rs = dbr.getResultSet();
//			try {
//				while (rs.next()) {
//					int count = rs.getInt("COUNT(*)");
//					long areaId = rs.getLong("areaId");
//					retMap.put(areaId, count);
//				}
//			} catch (SQLException e) {
//				// return retMap;
//			} finally {
//				if (dbr != null)
//					dbr.close();
//			}
//			
//			for (Long areaId : areaIdList) {
//				if (!retMap.containsKey(areaId)) {
//					retMap.put(areaId, 0);
//				}
//			}
//			return retMap;
		}
	}

	@Override
	public boolean resetSupplierBrandBitmap(long supplierId, long newBitmap) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("update Mmall_SaleSchedule_SupplierBrand set AreaFormatCode = ").append(newBitmap)
		.append(" where supplierId = ").append(supplierId);
		return this.getSqlSupport().excuteUpdate(sql.toString()) >= 0;
	}

	@Override
	public void syncData() {
		String sql = "select id, SiteId, isActive from Mmall_CMS_Business";
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		Map<Long, Long> busMap = new HashMap<>();
		Map<Long, Integer> actMap = new HashMap<>();
		try {
			while (rs.next()) {
				long id = rs.getLong("id");
				long areaBitMap = rs.getLong("SiteId");
				/** 是否激活状态,0激活,1冻结 */
				int isActive = rs.getInt("isActive");
				busMap.put(id, areaBitMap);
				actMap.put(id, isActive);
			}
		} catch (SQLException e) {
			
		} finally {
			if (dbr != null)
				dbr.close();
		}

		for (Long id : busMap.keySet()) {
			long areaBitMap = busMap.get(id);
			int isActive = actMap.get(id);
			String sqlupdate = "update Mmall_SaleSchedule_SupplierBrand set areaFormatCode = " + 
			areaBitMap + ", supplierIsFreeze = " + isActive + " where supplierId = " + id;
			this.getSqlSupport().excuteUpdate(sqlupdate);
		}
//		sql = "select brandShopId, city from Mmall_SaleSchedule_BrandShop";
//		dbr = this.getSqlSupport().excuteQuery(sql.toString());
//		rs = dbr.getResultSet();
//		try {
//			while (rs.next()) {
//				long brandShopId = rs.getLong("brandShopId");
//				long city = rs.getLong("city");
//				String getParSql = "select parentCode from Mmall_IP_LocationCode where valid = 1 and code = " + city;
//				DBResource dbrInner = this.getSqlSupport().excuteQuery(getParSql.toString());
//				ResultSet rsInner = dbrInner.getResultSet();
//				try {
//					while (rsInner.next()) {
//						long parentCode = rsInner.getLong("parentCode");
//						String getNameSql = "select code, locationName from Mmall_IP_LocationCode where valid = 1 and code = " + parentCode;
//						DBResource dbrII = this.getSqlSupport().excuteQuery(getNameSql.toString());
//						ResultSet rsII = dbrII.getResultSet();
//						try {
//							while (rsII.next()) {
//								long code = rsII.getLong("code");
//								String locationName = rsII.getString("locationName");
//								String updateLoc = "update Mmall_SaleSchedule_BrandShop set province = " + code + ", provinceName = '" +
//										locationName + "' where brandShopId = " + brandShopId;
//								this.getSqlSupport().excuteUpdate(updateLoc);
//								break;
//							}
//						} catch (SQLException e) {
//							
//						} finally {
//							if (dbrII != null) {
//								dbrII.close();
//							}
//						}
//						break;
//					}
//				} catch (SQLException e) {
//					
//				} finally {
//					if (dbrInner != null)
//						dbrInner.close();
//				}
//			}
//		} catch (SQLException e) {
//			
//		} finally {
//			if (dbr != null)
//				dbr.close();
//		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.saleschedule.dao.SupplierBrandDao#freezeSupplierBrand(long, boolean)
	 */
	@Override
	public boolean freezeSupplierBrand(long supplierId, boolean bFreeze) {
		int freeze = (bFreeze ? 1 : 0);
		StringBuilder sql = new StringBuilder(256);
		sql.append("update Mmall_SaleSchedule_SupplierBrand set supplierIsFreeze = ").append(freeze)
		.append(" where supplierId = ").append(supplierId);
		return this.getSqlSupport().excuteUpdate(sql.toString()) >= 0;
	}

//	@Override
//	public Map<Long, String> getSupplierAreaListMap(long supplierId) {
//		Map<Long, String> retMap = new HashMap<>();
//		StringBuilder sql = new StringBuilder(256);
//		sql.append("SELECT AreaId, AreaName FROM Mmall_CMS_BusinessArea WHERE supplierId = ").append(supplierId);
//		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
//		ResultSet rs = dbr.getResultSet();
//		try {
//			while (rs.next()) {
//				long id = rs.getLong("AreaId");
//				String name = rs.getString("AreaName");
//				retMap.put(id, name);
//			}
//		} catch (SQLException e) {
//			// return retMap;
//		} finally {
//			if (dbr != null)
//				dbr.close();
//		}
//		return retMap;
//	}

}
