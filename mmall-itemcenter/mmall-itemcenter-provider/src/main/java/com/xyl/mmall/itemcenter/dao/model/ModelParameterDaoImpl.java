/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.meta.ModelParameter;

/**
 * ModelParameterDaoImpl.java created by yydx811 at 2015年5月5日 下午1:37:35
 * 商品模型属性dao实现
 *
 * @author yydx811
 */
@Repository
public class ModelParameterDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ModelParameter> implements ModelParameterDao {

	private static Logger logger = Logger.getLogger(ModelParameterDaoImpl.class);
	
	@Override
	public List<ModelParameter> getParameterList(long modelId, int isShow) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "ModelId", modelId);
		if (isShow > 0) {
			SQLUtils.appendExtParamObject(sql, "IsShow", isShow);
		}
		return queryObjects(sql.toString());
	}

	@Override
	public long addModelParameter(ModelParameter modelParameter) {
		long id = this.allocateRecordId();
		modelParameter.setId(id);
		return addObject(modelParameter) == null ? -1l : id;
	}

	@Override
	public ModelParameter getModelParameter(long id, long modelId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "Id", id);
		SQLUtils.appendExtParamObject(sql, "ModelId", modelId);
		return queryObject(sql.toString());
	}

	@Override
	public int updateModelParameter(ModelParameter parameter) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET AgentId = ").append(parameter.getAgentId());
		if (parameter.getIsShow() > 0) {
			sql.append(", IsShow = ").append(parameter.getIsShow());
		}
		if (parameter.getIsSingle() > 0) {
			sql.append(", IsSingle = ").append(parameter.getIsSingle());
		}
		if (StringUtils.isNotBlank(parameter.getName())) {
			sql.append(", Name = '").append(parameter.getName()).append("'");
		}
		sql.append(" WHERE Id = ").append(parameter.getId()).append(" AND ModelId = ").append(parameter.getModelId());
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public int deleteModelParameter(long id, long modelId) {
		StringBuilder sql = new StringBuilder("DELETE FROM ");
		sql.append(this.getTableName()).append(" WHERE Id = ").append(id).append(" AND ModelId = ").append(modelId);
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

}
