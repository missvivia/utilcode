package com.xyl.mmall.saleschedule.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.saleschedule.dao.BrandDao;
import com.xyl.mmall.saleschedule.enums.BrandProbability;
import com.xyl.mmall.saleschedule.enums.BrandStatus;
import com.xyl.mmall.saleschedule.meta.Brand;

@Repository
public class BrandDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<Brand> implements BrandDao {

	private static Logger logger = Logger.getLogger(BrandDaoSqlImpl.class);

	private final double PROBABILITY_A = 0.6;

	private final double PROBABILITY_B = 0.3;

	// private final double PROBABILITY_C = 0.1;

	private String brandExistSQL = genSelectSql() + "AND (brandNameZh = ? or brandNameEn = ?)";

	private String brandZhExistSQL = genSelectSql() + "AND (brandNameZh = ?)";

	private String brandEnExistSQL = genSelectSql() + "AND (brandNameEn = ?)";

	private String SELECT_BRAND_BY_NAME = "select * from " + super.getTableName()
			+ " where brandNameZh = ? or brandNameEn = ? ";

	@Override
	public String getBrandName(long brandId) {
		Brand brand = getObjectById(brandId);
		if (brand != null) {
			return brand.getBrandNameAuto();
		} else {
			return null;
		}
	}

	@Override
	public List<Brand> getAllBrand(DDBParam param, long time) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (time > 0) {
			sql.append(" AND createTime <= ").append(time);
		}
		return getListByDDBParam(sql.toString(), param);
	}

	@Override
	public boolean isBrandExist(String brandNameZh, String brandNameEn) {
		// StringBuilder sql = new StringBuilder(256);
		// sql.append(genSelectSql());
		// sql.append(" AND (brandNameZh = '").append(brandNameZh).append("' OR ");
		// sql.append(" brandNameEn = '").append(brandNameEn).append("')");
		if (!StringUtils.isBlank(brandNameZh) && !StringUtils.isBlank(brandNameEn)) {
			if (queryObjects(brandExistSQL, brandNameZh, brandNameEn).size() > 0) {
				return true;
			} else {
				return false;
			}
		} else if (!StringUtils.isBlank(brandNameZh)) {
			if (queryObjects(brandZhExistSQL, brandNameZh).size() > 0) {
				return true;
			} else {
				return false;
			}
		} else if (!StringUtils.isBlank(brandNameEn)) {
			if (queryObjects(brandEnExistSQL, brandNameEn).size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// 由于品牌搜索逻辑被取消了，这个接口被作废
	@Deprecated
	@Override
	public List<Brand> searchBrand(String name, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (name != null && !"".equals(name.trim())) {
			name = name.trim();
			// sql.append(" AND (brandNameZh LIKE '%" + name + "%'");
			// sql.append(" OR brandNameEn LIKE '%" + name + "%')");
			// sql取消使用like，直接字段匹配
			sql.append(" AND (brandNameZh = '").append(name).append("' OR ");
			sql.append(" brandNameEn = '").append(name).append("')");
		}
		return getListByDDBParam(sql.toString(), param);
	}

	@Override
	public List<Brand> getBrandListByBrandIdList(List<Long> ids) {
		if (ids.size() > 0) {
			StringBuilder idsString = new StringBuilder(256);
			StringBuilder idsStringSet = new StringBuilder(256);
			idsString.append("(");
			for (Long brandId : ids) {
				idsString.append(brandId).append(",");
				idsStringSet.append(brandId).append(",");
			}
			idsString.deleteCharAt(idsString.lastIndexOf(","));
			idsString.append(")");
			idsStringSet.deleteCharAt(idsStringSet.lastIndexOf(","));
			StringBuilder sql = new StringBuilder(256);
			sql.append(genSelectSql());
			sql.append(" AND brandId IN ").append(idsString);
			sql.append(" ORDER BY FIND_IN_SET(brandId, '").append(idsStringSet).append("')");
			return queryObjects(sql);
		} else {
			List<Brand> retBrands = new ArrayList<>();
			return retBrands;
		}
	}

	@Override
	public List<Brand> getBrandListByIndexCMS(DDBParam param, String index) {
		List<Brand> ret = new ArrayList<>();
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT * FROM Mmall_ItemCenter_Brand WHERE 1=1");
		if (StringUtils.isNotBlank(index)) {
			boolean bAll = index.trim().toUpperCase().equals("ALL");
			if (!bAll) {
				boolean bOther = false;
				StringBuilder indexListString = new StringBuilder(25);
				if (index.trim().toUpperCase().equals("OTHER")) {
					bOther = true;
				} else {
					indexListString.append("'").append(index).append("'");
					sql.append(" AND (brandNameHead = ").append(indexListString);
					sql.append(" OR brandNameHeadPinYin = ").append(indexListString);
				}
				if (!bOther) {
					sql.append(")");
				} else {
					sql.append(" AND (brandNameHead is null or brandNameHeadPinYin is null)");
				}
			}
			ret = getListByDDBParam(sql.toString(), param);
		}
		return ret;
	}

	@Override
	public List<Brand> getBrandListByIndex(DDBParam param, long time, String index, long areaId) {
		if (areaId <= 0) {
			return new ArrayList<Brand>();
		}
		long bitmapCode = 0L;
		try {
			bitmapCode = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
		} catch (Exception e) {
			return new ArrayList<Brand>();
		}
		StringBuilder sql = new StringBuilder(256);
		StringBuilder sqlSuff = new StringBuilder(256);
		// 之前的逻辑是寻找在某个地区分配供应商的品牌
		// 现在修改成了寻找在某个地区里面分配了供应商品牌，并且上线的品牌
		sql.append("SELECT distinct A.* ");
		sqlSuff.append(" FROM Mmall_ItemCenter_Brand A, Mmall_SaleSchedule_SupplierBrand B WHERE B.areaFormatCode & ")
				.append(bitmapCode).append(" = ").append(bitmapCode).append(" AND B.supplierIsFreeze = 0 ")
				.append(" AND B.brandId = A.brandId AND B.status = ")
				.append(BrandStatus.BRAND_AUDITPASSED_USING.getIntValue());
		// 供应商的多个区域的限制，下面注释的代码以及无效
		// sql.append(" AND B.areaId = ").append(areaId);
		if (index != null && index.trim().length() > 0) {
			index = index.toUpperCase();
			boolean bAll = index.trim().equals("ALL");
			boolean bOther = index.trim().equals("OTHER");
			if (!bAll && !bOther) {
				sqlSuff.append(" AND (A.brandNameHead = '").append(index).append("'");
				sqlSuff.append(" OR A.brandNameHeadPinYin = '").append(index).append("')");
			} else if (bOther) {
				// 只要有一个为null就可以是其他里面的品牌
				sqlSuff.append(" AND (A.brandNameHead is null OR A.brandNameHeadPinYin is null)");
			}
		}
		if (time > 0) {
			sqlSuff.append(" AND A.createTime <= ").append(time);
		}
		sql.append(sqlSuff);
		List<Brand> ret = getListByDDBParam(sql.toString(), param);
		param.setTotalCount(getBrandListByIndexTotalCount(sqlSuff));
		return ret;
	}

	private int getBrandListByIndexTotalCount(StringBuilder sqlSuff) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("select COUNT(DISTINCT A.brandId) as count");
		sql.append(sqlSuff);
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		int total = 0;
		try {
			while (rs.next()) {
				total = rs.getInt("count");
				break;
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return total;
	}

	private Map<Integer, Integer> getRecommendBrandsCount(long areaId) {
		Map<Integer, Integer> ret = new HashMap<>();
		long bitmapCode = 0;
		try {
			bitmapCode = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
		} catch (Exception e) {
			return ret;
		}
		StringBuilder sql = new StringBuilder(256);
		// DDB不支持COUNT(distinct A.brandId)
		sql.append(
				"SELECT A.brandProbability, COUNT(distinct A.brandId) as count FROM Mmall_ItemCenter_Brand A, Mmall_SaleSchedule_SupplierBrand B WHERE B.areaFormatCode & ")
				.append(bitmapCode).append(" = ").append(bitmapCode)
				.append(" AND B.supplierIsFreeze = 0 AND B.brandId = A.brandId AND B.status = ")
				.append(BrandStatus.BRAND_AUDITPASSED_USING.getIntValue()).append(" group by A.brandProbability");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				int brandPro = rs.getInt("brandProbability");
				int count = rs.getInt("count");
				ret.put(brandPro, count);
				// int brandId = rs.getInt("A.brandId");
				// Integer data = ret.get(brandPro);
				// if (data != null) {
				// ret.put(brandPro, ++data);
				// } else {
				// ret.put(brandPro, 1);
				// }
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return ret;
	}

	private List<Brand> getRecommendBrandsByProbability(BrandProbability brandProbability, int count, long areaId) {
		if (areaId < 0) {
			return new ArrayList<>();
		}
		long bitmapCode = 0;
		try {
			bitmapCode = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
		} catch (Exception e) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(256);
		sql.append(
				"SELECT distinct A.* FROM Mmall_ItemCenter_Brand A, Mmall_SaleSchedule_SupplierBrand B WHERE B.areaFormatCode & ")
				.append(bitmapCode).append(" = ").append(bitmapCode).append(" AND B.supplierIsFreeze = 0 ")
				.append(" AND B.brandId = A.brandId AND A.brandProbability = ").append(brandProbability.getIntValue())
				.append(" AND B.status = ").append(BrandStatus.BRAND_AUDITPASSED_USING.getIntValue())
				.append(" LIMIT 1000");
		List<Brand> data = queryObjects(sql);
		int querySize = data.size();
		if (count >= querySize) {
			return data;
		} else {
			Random random = new Random(System.currentTimeMillis());
			List<Brand> ret = new ArrayList<>(count);
			Set<Integer> randSet = new HashSet<>();
			while (randSet.size() < count) {
				int value = random.nextInt(querySize);
				if (!randSet.contains(value)) {
					randSet.add(value);
					ret.add(data.get(value));
				}
			}
			return ret;
		}
	}

	@Cacheable(value = "brandCache")
	@Override
	public List<Brand> getRecommendBrands(long areaId, int count) {
		Map<Integer, Integer> countMap = getRecommendBrandsCount(areaId);
		int countAReal = 0;
		int countBReal = 0;
		int countCReal = 0;
		for (Integer pro : countMap.keySet()) {
			if (pro == BrandProbability.CATEGORY_TYPE_A.getIntValue()) {
				countAReal = countMap.get(pro);
			} else if (pro == BrandProbability.CATEGORY_TYPE_B.getIntValue()) {
				countBReal = countMap.get(pro);
			} else if (pro == BrandProbability.CATEGORY_TYPE_C.getIntValue()) {
				countCReal = countMap.get(pro);
			}
		}
		int countALeft = countAReal;
		int countBLeft = countBReal;
		// int countCLeft = countCReal;
		int countAPro = (int) (count * PROBABILITY_A);
		int countBPro = (int) (count * PROBABILITY_B);
		int countCPro = count - countAPro - countBPro;
		int countA = 0;
		int countB = 0;
		int countC = 0;
		// A
		if (countAPro > countAReal) {
			countALeft = 0;
			countA = countAReal;
		} else {
			countALeft = countAReal - countAPro;
			countA = countAPro;
		}
		// B
		if (countAPro + countBPro > countA + countBReal) {
			countBLeft = 0;
			countB = countBReal;
			// 剩余不够的由A补充
			if (countALeft > 0) {
				int countNeed = countAPro + countBPro - countA - countB;
				if (countALeft > countNeed) {
					countA += countNeed;
					countALeft -= countNeed;
				} else {
					countA += countALeft;
					countALeft = 0;
				}
			}
		} else {
			countBLeft = countA + countBReal - countAPro - countBPro;
			countB = countAPro + countBPro - countA;
		}
		// C
		if (countAPro + countBPro + countCPro > countA + countB + countCReal) {
			// countCLeft = 0;
			countC = countCReal;
			// 剩余不够的由A B补充
			if (countALeft + countBLeft > 0) {
				int countNeed = countAPro + countBPro + countCPro - countA - countB - countC;
				if (countALeft > countNeed) {
					countA += countNeed;
					countALeft -= countNeed;
				} else if (countALeft + countBLeft > countNeed) {
					countA += countALeft;
					countB += (countNeed - countALeft);
					countALeft = 0;
					countBLeft = countBReal - countB;
				} else if (countALeft + countBLeft <= countNeed) {
					countA += countALeft;
					countB += countBLeft;
					countALeft = 0;
					countBLeft = 0;
				}
			}
		} else {
			// countCLeft = countA + countB + countCReal - countAPro - countBPro
			// - countCPro;
			countC = countAPro + countBPro + countCPro - countA - countB;
		}

		List<Brand> ret = new ArrayList<>();
		ret.addAll(getRecommendBrandsByProbability(BrandProbability.CATEGORY_TYPE_A, countA, areaId));
		ret.addAll(getRecommendBrandsByProbability(BrandProbability.CATEGORY_TYPE_B, countB, areaId));
		ret.addAll(getRecommendBrandsByProbability(BrandProbability.CATEGORY_TYPE_C, countC, areaId));
		Collections.shuffle(ret);
		return ret;
	}

	@Override
	public List<Brand> getBrandByName(String name) {
		return super.queryObjects(SELECT_BRAND_BY_NAME, name, name);
	}

	@Override
	public void updateBrandNameforSale(long brandId, String brandNameZh, String brandNameEn) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("update Mmall_SaleSchedule_Schedule set ");
		sql.append("brandName = '").append(brandNameZh).append("', ");
		sql.append("brandNameEn = '").append(brandNameEn).append("'");
		sql.append(" where brandId = ").append(brandId);
		this.getSqlSupport().excuteUpdate(sql.toString());

		sql.setLength(0);
		sql.append("update Mmall_SaleSchedule_ScheduleBanner set ");
		sql.append("brandName = '").append(brandNameZh).append("', ");
		sql.append("brandNameEn = '").append(brandNameEn).append("'");
		sql.append(" where brandId = ").append(brandId);
		this.getSqlSupport().excuteUpdate(sql.toString());

		sql.setLength(0);
		sql.append("update Mmall_SaleSchedule_SchedulePage set ");
		sql.append("brandName = '").append(brandNameZh).append("', ");
		sql.append("brandNameEn = '").append(brandNameEn).append("'");
		sql.append(" where brandId = ").append(brandId);
		this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public void updateBrandName(String headEn, String headZh, long brandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("update Mmall_ItemCenter_Brand set ");
		if (headEn != null) {
			sql.append("brandNameHead = '").append(headEn).append("'");
		} else {
			sql.append("brandNameHead = null");
		}
		if (headZh != null) {
			sql.append(",brandNameHeadPinYin = '").append(headZh).append("'");
		} else {
			sql.append(",brandNameHeadPinYin = null");
		}
		sql.append(" where brandId = ").append(brandId);
		this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public List<JSONObject> getBrandListInOrderByIds(List<Long> ids) {
		List<JSONObject> ret = new ArrayList<JSONObject>();
		StringBuilder sql = new StringBuilder("SELECT DISTINCT * FROM ");
		sql.append(this.getTableName()).append(" WHERE BrandNameHead = ? OR BrandNameHeadPinYin = ?");
		List<Brand> allList = new ArrayList<Brand>();
		// A-Z
		for (int i = 65; i < 91; i++) {
			String index = String.valueOf((char) i);
			List<Brand> list = new ArrayList<Brand>();
			DBResource resource = this.getSqlSupport().excuteQuery(sql.toString(), index, index);
			ResultSet rs = resource.getResultSet();
			try {
				while (rs != null && rs.next()) {
					long brandId = rs.getLong("BrandId");
					if (ids.contains(brandId)) {
						Brand brand = new Brand();
						brand.setBrandId(brandId);
						brand.setBrandNameEn(rs.getString("BrandNameEn"));
						brand.setBrandNameHead(rs.getString("BrandNameHead"));
						brand.setBrandNameZh(rs.getString("BrandNameZh"));
						brand.setBrandNameHeadPinYin(rs.getString("BrandNameHeadPinYin"));
						list.add(brand);
						allList.add(brand);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				resource.close();
			}
			if (!CollectionUtils.isEmpty(list)) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("index", index);
				jsonObject.put("list", list);
				ret.add(jsonObject);
			}
		}
		// other
		sql = new StringBuilder("SELECT DISTINCT * FROM ");
		sql.append(this.getTableName()).append(" WHERE BrandNameHead IS NULL OR BrandNameHeadPinYin IS NULL");
		List<Brand> otherList = new ArrayList<Brand>();
		DBResource resource = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = resource.getResultSet();
		try {
			while (rs != null && rs.next()) {
				long brandId = rs.getLong("BrandId");
				if (ids.contains(brandId)) {
					Brand brand = new Brand();
					brand.setBrandId(brandId);
					brand.setBrandNameEn(rs.getString("BrandNameEn"));
					brand.setBrandNameHead(rs.getString("BrandNameHead"));
					brand.setBrandNameZh(rs.getString("BrandNameZh"));
					brand.setBrandNameHeadPinYin(rs.getString("BrandNameHeadPinYin"));
					otherList.add(brand);
					allList.add(brand);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resource.close();
		}
		if (!CollectionUtils.isEmpty(otherList)) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("index", "other");
			jsonObject.put("list", otherList);
			ret.add(jsonObject);
		}
		if (!CollectionUtils.isEmpty(allList)) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("index", "all");
			jsonObject.put("list", allList);
			ret.add(jsonObject);
		}
		return ret;
	}

	@Override
	public List<JSONObject> getBrandListInOrderByCategory(long areaId, List<Long> brandValue) {
		List<JSONObject> ret = new ArrayList<JSONObject>();
		if (areaId <= 0) {
			return ret;
		}
		long bitmapCode = 0L;
		try {
			bitmapCode = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
		} catch (Exception e) {
			return ret;
		}
		StringBuilder sql = new StringBuilder(256);
		// 之前的逻辑是寻找在某个地区分配供应商的品牌
		// 现在修改成了寻找在某个地区里面分配了供应商品牌，并且上线的品牌
		sql.append("SELECT distinct A.* ")
				.append(" FROM Mmall_ItemCenter_Brand A, Mmall_SaleSchedule_SupplierBrand B WHERE B.areaFormatCode & ")
				.append(bitmapCode).append(" = ").append(bitmapCode).append(" AND B.supplierIsFreeze = 0 ")
				.append(" AND B.brandId = A.brandId AND B.status = ")
				.append(BrandStatus.BRAND_AUDITPASSED_USING.getIntValue())
				.append(" AND (A.brandNameHead = ? OR A.brandNameZh = ?)");
		// 供应商的多个区域的限制，下面注释的代码以及无效
		// sql.append(" AND B.areaId = ").append(areaId);
		for (int i = 65; i < 91; i++) {
			String index = String.valueOf((char) i);
			List<Brand> list = new ArrayList<Brand>();
			DBResource resource = this.getSqlSupport().excuteQuery(sql.toString(), index, index);
			ResultSet rs = resource.getResultSet();
			try {
				while (rs != null && rs.next()) {
					long brandId = rs.getLong("BrandId");
					if (brandValue.contains(brandId)) {
						Brand brand = new Brand();
						brand.setBrandId(brandId);
						brand.setBrandNameEn(rs.getString("BrandNameEn"));
						brand.setBrandNameHead(rs.getString("BrandNameHead"));
						brand.setBrandNameZh(rs.getString("BrandNameZh"));
						brand.setBrandNameHeadPinYin(rs.getString("BrandNameHeadPinYin"));
						list.add(brand);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				resource.close();
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("key", index);
			jsonObject.put("list", list);
			ret.add(jsonObject);
		}
		return ret;
	}

	@Override
	public Brand[] getBrandBreifInfoListInListOrder(List<Long> brandIds) {
		if (CollectionUtils.isEmpty(brandIds)) {
			return new Brand[0];
		} else {
			StringBuilder sql = new StringBuilder(256);
			StringBuilder idsStr = new StringBuilder(256);
			for (Long brandId : brandIds) {
				idsStr.append(brandId).append(",");
			}
			idsStr.deleteCharAt(idsStr.lastIndexOf(","));
			// find_in_set 标量函数只能在查询字段中使用
			sql.append("SELECT FIND_IN_SET(BrandId, '").append(idsStr.toString()).append("'), ");
			sql.append("BrandId, BrandNameEn, BrandNameHead, BrandNameZh, BrandNameHeadPinYin FROM ");
			sql.append(this.getTableName()).append(" WHERE BrandId IN (").append(idsStr.toString()).append(")");
			DBResource dbResource = this.getSqlSupport().excuteQuery(sql.toString());
			ResultSet rs = dbResource.getResultSet();
			if (null == rs) {
				return new Brand[0];
			}
			Brand[] brandArray = new Brand[brandIds.size()];
			try {
				// 按序放入list，不存在的id可能会产生空位
				while (rs.next()) {
					int index = rs.getInt(1);
					Brand brand = new Brand();
					brand.setBrandId(rs.getLong(2));
					brand.setBrandNameEn(rs.getString(3));
					brand.setBrandNameHead(rs.getString(4));
					brand.setBrandNameZh(rs.getString(5));
					brand.setBrandNameHeadPinYin(rs.getString(6));
					brandArray[index - 1] = brand;
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				dbResource.close();
			}
			return brandArray;
		}
	}
}
