/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.spu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;
import com.xyl.mmall.itemcenter.meta.ItemSPU;

/**
 * ItemSPUDaoImpl.java created by yydx811 at 2015年5月6日 下午8:01:38
 * 单品dao实现
 *
 * @author yydx811
 */
@Repository
public class ItemSPUDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ItemSPU> implements ItemSPUDao {

	private static Logger logger = Logger.getLogger(ItemSPUDaoImpl.class);
	
	@Override
	public List<ItemSPU> getItemSPUList(BasePageParamVO<?> pageParamVO, ItemSPUDTO spu, String searchValue) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		createSearchSQL(sql, spu, searchValue);
		try {
			orderByUpdateTimeDESC(sql);
			SQLUtils.getPageSql(sql, pageParamVO);
		} catch (SQLException e) {
			logger.error(e);
			return null;
		}
		return queryObjects(sql.toString());
	}

	@Override
	public int getItemSPUCount(ItemSPUDTO spu, String searchValue) {
		StringBuilder sql = new StringBuilder(genCountSql());
		createSearchSQL(sql, spu, searchValue);
		return this.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public List<ItemSPU> getItemSPUList(ItemSPUDTO spu, String searchValue) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		createSearchSQL(sql, spu, searchValue);
		try {
			orderByUpdateTimeDESC(sql);
		} catch (SQLException e) {
			logger.error(e);
			return null;
		}
		return queryObjects(sql.toString());
	}
	
	
	/**
	 * 根据查询参数创建sql
	 * @param sql
	 * @param spu
	 * @param searchValue
	 */
	private void createSearchSQL(StringBuilder sql, ItemSPUDTO spu, String searchValue) {
		if (StringUtils.isNotBlank(searchValue)) {
			String name = searchValue.replaceAll("'", "\\\\'");
			sql.append(" AND (Name like '%").append(name).append("%'");
			sql.append(" OR BarCode = '").append(searchValue).append("')");
		}
		if (spu != null) {
			if (spu.getCategoryNormalId() > 0l) {
				sql.append(" AND CategoryNormalId = ").append(spu.getCategoryNormalId());
			}
			if (spu.getBrandId() > 0l) {
				sql.append(" AND BrandId = ").append(spu.getBrandId());
			}
			if (StringUtils.isNotEmpty(spu.getCategoryNormalIds())) {
				sql.append(" AND CategoryNormalId in (").append(spu.getCategoryNormalIds()).append(")");
			}
		}
	}

	/**
	 * 按UpdateTime升序排序
	 * @param sql
	 * @throws SQLException
	 */
	private void orderByUpdateTimeDESC(StringBuilder sql) throws SQLException {
		String[] columns = {"UpdateTime"};
		boolean[] isASC = {false};
		SQLUtils.appendExtOrderByColumns(sql, columns, isASC);
	}

	@Override
	public int addItemSPU(ItemSPU spu) {
		addObject(spu);
		return 1;
	}

	@Override
	public ItemSPU getItemSPU(ItemSPU itemSPU) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		if (itemSPU.getId() > 0l) {
			SQLUtils.appendExtParamObject(sql, "Id", itemSPU.getId());
		} else if (StringUtils.isNotBlank(itemSPU.getBarCode())) {
			SQLUtils.appendExtParamObject(sql, "BarCode", itemSPU.getBarCode());
		} else {
			return null;
		}
		return queryObject(sql.toString());
	}

	@Override
	public int updateItemSPU(ItemSPU itemSPU) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET AgentId = ").append(itemSPU.getAgentId());
		if (StringUtils.isNotBlank(itemSPU.getBarCode())) {
			String barCode = itemSPU.getBarCode().replaceAll("'", "\\\\'");
			sql.append(", BarCode = '").append(barCode).append("'");
		}
		if (StringUtils.isNotBlank(itemSPU.getName())) {
			String name = itemSPU.getName().replaceAll("'", "\\\\'");
			sql.append(", Name = '").append(name).append("'");
		}
		if (itemSPU.getBrandId() > 0l) {
			sql.append(", BrandId = ").append(itemSPU.getBrandId());
		}
		if (itemSPU.getCategoryNormalId() > 0l) {
			sql.append(", CategoryNormalId = ").append(itemSPU.getCategoryNormalId());
			
		}
		sql.append(" WHERE Id = ").append(itemSPU.getId());
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public void deleteBulkItemSPU(Set<Long> idSet) {
		for (Long id : idSet) {
			try {
				this.deleteById(id);
			} catch (Exception e) {
				logger.error("Delete itemSPU error! id : " + id);
			}
		}
	}

	@Override
	public List<Long> getBrandIds(long categoryId) {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT(BrandId) FROM ");
		sql.append(this.getTableName()).append(" WHERE ").append("CategoryNormalId = ").append(categoryId);
		DBResource dbResource = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = (null == dbResource) ? null : dbResource.getResultSet();
		if (null == rs) {
			return null;
		}
		try {
			List<Long> idList = new ArrayList<Long>();
			while (rs.next()) {
				long id = rs.getLong(1);
				idList.add(id);
			}
			rs.close();
			return idList;
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			dbResource.close();
		}
		return null;
	}

	@Override
	public List<Long> getSPUIds(List<Long> categoryIds, Set<Long> brandIds, String searchValue) {
		StringBuilder sql = new StringBuilder("SELECT Id FROM ");
		sql.append(this.getTableName()).append(" WHERE 1 = 1");
		if (!CollectionUtils.isEmpty(categoryIds)) {
			SqlGenUtil.appendExtParamColl(sql, "CategoryNormalId", categoryIds);
		}
		if (!CollectionUtils.isEmpty(brandIds)) {
			SqlGenUtil.appendExtParamColl(sql, "BrandId", brandIds);
		}
		if (StringUtils.isNotBlank(searchValue)) {
			searchValue = searchValue.replaceAll("'", "\\\\'");
			sql.append(" AND Name LIKE '%").append(searchValue).append("%'");
		}
		DBResource dbResource = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = (null == dbResource) ? null : dbResource.getResultSet();
		if(null == rs) {
			return null;
		}
		try {
			List<Long> idList = new ArrayList<Long>();
			while (rs.next()) {
				long id = rs.getLong(1);
				idList.add(id);
			}
			rs.close();
			return idList;
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			dbResource.close();
		}
		return null;
	}

	@Override
	public List<ItemSPU> getItemSPUList(List<Long> spuIdList) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "Id", spuIdList);
		return queryObjects(sql.toString());
	}
	
	@Override
	public List<ItemSPU> getItemSPUListBySearchParam(ItemSPUDTO spu) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		if (spu != null) {
			if (StringUtils.isNotEmpty(spu.getName())) {
				sql.append(" AND Name like '%").append(spu.getName().replaceAll("'", "\\\\'")).append("%'");
			}
			if (StringUtils.isNotEmpty(spu.getBarCode())) {
//				sql.append(" And BarCode like '%").append(spu.getBarCode().replaceAll("'", "\\\\'")).append("%'");
				sql.append(" And BarCode = '").append(spu.getBarCode()).append("'");
			}
			if (spu.getCategoryNormalId() > 0l) {
				sql.append(" AND CategoryNormalId = ").append(spu.getCategoryNormalId());
			}
			if (spu.getBrandId() > 0l) {
				sql.append(" AND BrandId = ").append(spu.getBrandId());
			}
			if (StringUtils.isNotEmpty(spu.getCategoryNormalIds())) {
				sql.append(" AND CategoryNormalId in (").append(spu.getCategoryNormalIds()).append(")");
			}
		}
		return queryObjects(sql.toString());
	}

	@Override
	public List<Long> getBrandIds(List<Long> categoryIds) {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT(BrandId) FROM ");
		sql.append(this.getTableName()).append(" WHERE 1 = 1");
		SqlGenUtil.appendExtParamColl(sql, "CategoryNormalId", categoryIds);
		DBResource dbResource = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = (null == dbResource) ? null : dbResource.getResultSet();
		if (null == rs) {
			return null;
		}
		try {
			List<Long> idList = new ArrayList<Long>();
			while (rs.next()) {
				long id = rs.getLong(1);
				idList.add(id);
			}
			rs.close();
			return idList;
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			dbResource.close();
		}
		return null;
	}
}
