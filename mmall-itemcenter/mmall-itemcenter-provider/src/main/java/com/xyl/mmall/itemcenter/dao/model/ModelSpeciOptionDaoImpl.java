/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.model;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.meta.ModelSpeciOption;

/**
 * ModelSpeciOptionDaoImpl.java created by yydx811 at 2015年5月5日 下午2:39:37
 * 商品模型规格选项dao实现
 *
 * @author yydx811
 */
@Repository
public class ModelSpeciOptionDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ModelSpeciOption> implements ModelSpeciOptionDao {

	private static Logger logger = Logger.getLogger(ModelParameterDaoImpl.class);
	
	@Override
	public List<ModelSpeciOption> getSpeciOptionList(long specificationId, int optionType) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		sql.append("AND SpecificationId = ").append(specificationId).append(" AND Type = ").append(optionType);
		try {
			orderByShowIndexASC(sql);
		} catch (SQLException e) {
			logger.error(e);
			return null;
		}
		return queryObjects(sql.toString());
	}

	@Override
	public boolean addBulkSpeciOptions(List<ModelSpeciOption> speciOptionList) {
		return addObjects(speciOptionList);
	}

	@Override
	public long addSpeciOption(ModelSpeciOption speciOption) {
		long id = 1l;
		speciOption.setShowIndex(getMaxIndex(speciOption) + 1);
		addObject(speciOption);
		return id;
	}

	@Override
	public int getMaxIndex(ModelSpeciOption speciOption) {
		StringBuilder sql = new StringBuilder("SELECT MAX(ShowIndex) FROM ");
		sql.append(this.getTableName()).append(" WHERE 1=1 ");
		SQLUtils.appendExtParamObject(sql, "SpecificationId", speciOption.getSpecificationId());
		return this.getSqlSupport().queryCount(sql.toString());
	}
	
	/**
	 * 按ShowIndex升序排序
	 * @param sql
	 * @throws SQLException
	 */
	private void orderByShowIndexASC(StringBuilder sql) throws SQLException {
		String[] columns = {"ShowIndex"};
		boolean[] isASC = {true};
		SQLUtils.appendExtOrderByColumns(sql, columns, isASC);
	}

	@Override
	public int updateModelSpeciOption(ModelSpeciOption option) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET AgentId = ").append(option.getAgentId());
		if (StringUtils.isNotBlank(option.getOptionValue())) {
			sql.append(", OptionValue = '").append(option.getOptionValue()).append("'");
		}
		sql.append(" WHERE Id = ").append(option.getId()).append(" AND SpecificationId = ").append(option.getSpecificationId());
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public ModelSpeciOption getModelSpeciOption(long id, long specificationId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "Id", id);
		SQLUtils.appendExtParamObject(sql, "SpecificationId", specificationId);
		return queryObject(sql.toString());
	}
	
	@Override
	public int deleteModelSpeciOption(long id, long specificationId) {
		ModelSpeciOption old = getModelSpeciOption(id, specificationId);
		if (old == null || old.getId() < 1l) {
			return 1;
		}
		StringBuilder sql = new StringBuilder("DELETE FROM ");
		sql.append(this.getTableName()).append(" WHERE Id = ").append(id).append(" AND SpecificationId = ").append(specificationId);
		int res = this.getSqlSupport().excuteUpdate(sql.toString());
		if (res > 0) {
			sql = new StringBuilder("UPDATE ");
			sql.append(this.getTableName()).append(" SET ShowIndex = ShowIndex - 1 WHERE SpecificationId = ")
				.append(specificationId).append(" AND ShowIndex > ").append(old.getShowIndex());
			this.getSqlSupport().excuteUpdate(sql.toString());
		}
		return res;
	}

	@Override
	public int deleteBulkModelSpeciOption(long specificationId) {
		StringBuilder sql = new StringBuilder("DELETE FROM ");
		sql.append(this.getTableName()).append(" WHERE SpecificationId = ").append(specificationId);
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}
}
