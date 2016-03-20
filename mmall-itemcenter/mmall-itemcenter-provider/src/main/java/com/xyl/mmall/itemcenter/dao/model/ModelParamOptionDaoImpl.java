/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.meta.ModelParamOption;

/**
 * ModelParamOptionDaoImpl.java created by yydx811 at 2015年5月5日 下午1:54:23
 * 商品模型属性选项dao实现
 *
 * @author yydx811
 */
@Repository
public class ModelParamOptionDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ModelParamOption> implements ModelParamOptionDao {

	private static Logger logger = Logger.getLogger(ModelParameterDaoImpl.class);
	
	@Override
	public List<ModelParamOption> getParamOptionList(long parameterId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		sql.append("AND ParameterId = ").append(parameterId);
		return queryObjects(sql.toString());
	}

	@Override
	public boolean addBulkParamOptions(List<ModelParamOption> optionList) {
		return addObjects(optionList);
	}

	@Override
	public long addParamOption(ModelParamOption paramOption) {
		long id = 1l;
		addObject(paramOption);
		return id;
	}

	@Override
	public int updateModelParamOption(ModelParamOption option) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET AgentId = ").append(option.getAgentId());
		if (StringUtils.isNotBlank(option.getOptionValue())) {
			sql.append(", OptionValue = '").append(option.getOptionValue()).append("'");
		}
		sql.append(" WHERE Id = ").append(option.getId()).append(" AND ParameterId = ").append(option.getParameterId());
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public int deleteModelParamOption(long id, long parameterId) {
		StringBuilder sql = new StringBuilder("DELETE FROM ");
		sql.append(this.getTableName()).append(" WHERE Id = ").append(id).append(" AND ParameterId = ").append(parameterId);
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public int deleteBulkParamOption(long parameterId) {
		StringBuilder sql = new StringBuilder("DELETE FROM ");
		sql.append(this.getTableName()).append(" WHERE ParameterId = ").append(parameterId);
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

}
