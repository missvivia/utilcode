/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.model;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.meta.ItemModel;

/**
 * ItemModelDaoImpl.java created by yydx811 at 2015年5月5日 上午10:19:06
 * 商品模型dao实现
 *
 * @author yydx811
 */
@Repository
public class ItemModelDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ItemModel> implements ItemModelDao {

	private static Logger logger = Logger.getLogger(ItemModelDaoImpl.class);
	
	@Override
	public List<ItemModel> getItemModelList(String searchValue, String startTime, String endTime) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		createSearchSQL(sql, searchValue, startTime, endTime);
		try {
			orderByUpdateTimeDESC(sql);
		} catch (SQLException e) {
			logger.error(e);
			return null;
		}
		return queryObjects(sql.toString());
	}

	@Override
	public List<ItemModel> getItemModelList(BasePageParamVO<?> pageParamVO, String searchValue, String startTime,
			String endTime) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		createSearchSQL(sql, searchValue, startTime, endTime);
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
	public int getItemModelCount(String searchValue, String startTime, String endTime) {
		StringBuilder sql = new StringBuilder(genCountSql());
		createSearchSQL(sql, searchValue, startTime, endTime);
		return this.getSqlSupport().queryCount(sql.toString());
	}

	/**
	 * 根据查询参数创建sql
	 * @param sql
	 * @param searchValue
	 * @param startTime
	 * @param endTime
	 */
	private void createSearchSQL(StringBuilder sql, String searchValue, String startTime, String endTime) {
		if (StringUtils.isNotBlank(searchValue)) {
			String name = searchValue.replaceAll("'", "\\\\'");
			sql.append(" AND (Name like '%").append(name).append("%'");
			try {
				long id = Long.parseLong(searchValue.trim());
				if (id > 0l) {
					sql.append(" OR Id = ").append(id);
				}
			} catch (NumberFormatException e) {
				logger.info("SearchValue is not a number. searchValue : " + searchValue);
			}
			sql.append(")");
		}
		if (StringUtils.isNotBlank(startTime)) {
			sql.append(" AND UpdateTime >= '").append(
					DateUtil.dateToString(new Date(Long.parseLong(startTime)), DateUtil.LONG_PATTERN)).append("'");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" AND UpdateTime < '").append(
					DateUtil.dateToString(new Date(Long.parseLong(endTime)), DateUtil.LONG_PATTERN)).append("'");
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
	public ItemModel getItemModel(ItemModel itemModel) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		createNormalSQL(sql, itemModel);
		List<ItemModel> list = queryObjects(sql.toString());
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}
	
	/**
	 * 根据表字段创建sql
	 * @param sql
	 * @param itemModel
	 */
	private void createNormalSQL(StringBuilder sql, ItemModel itemModel) {
		if (itemModel == null) {
			return;
		}
		if (itemModel.getId() > 0l) {
			sql.append(" AND Id = ").append(itemModel.getId());
		}
		if (itemModel.getCategoryNormalId() > 0l) {
			sql.append(" AND CategoryNormalId = ").append(itemModel.getCategoryNormalId());
		}
	}

	@Override
	public long addItemModel(ItemModel model) {
		long id = this.allocateRecordId();
		model.setId(id);
		return addObject(model) == null ? -1l : id;
	}
	
	@Override
	public int updateItemModel(ItemModel model) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET AgentId = ").append(model.getAgentId());
		if (model.getCategoryNormalId() > 0l) {
			sql.append(", CategoryNormalId = ").append(model.getCategoryNormalId());
		}
		if (StringUtils.isNotBlank(model.getName())) {
			sql.append(", Name = '").append(model.getName()).append("'");
		}
		sql.append(" WHERE Id = ").append(model.getId());
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public int deleteItemModel(long id) {
		StringBuilder sql = new StringBuilder("DELETE FROM ");
		sql.append(this.getTableName()).append(" WHERE Id = ").append(id);
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}
}
