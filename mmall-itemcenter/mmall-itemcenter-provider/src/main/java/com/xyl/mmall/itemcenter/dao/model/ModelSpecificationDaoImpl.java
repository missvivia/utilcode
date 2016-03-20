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
import com.xyl.mmall.itemcenter.meta.ModelSpecification;

/**
 * ModelSpecificationDaoImpl.java created by yydx811 at 2015年5月5日 下午2:13:12
 * 商品模型规格dao实现
 *
 * @author yydx811
 */
@Repository
public class ModelSpecificationDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ModelSpecification> implements ModelSpecificationDao {

	private static Logger logger = Logger.getLogger(ModelParameterDaoImpl.class);
	
	@Override
	public List<ModelSpecification> getSpecificationList(long modelId, int isShow) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "ModelId", modelId);
		if (isShow > 0) {
			SQLUtils.appendExtParamObject(sql, "IsShow", isShow);
		}
		return queryObjects(sql.toString());
	}

	@Override
	public long addModelSpecification(ModelSpecification specification) {
		long id = allocateRecordId();
		specification.setId(id);
		return addObject(specification) == null ? -1 : id;
	}

	@Override
	public ModelSpecification getModelSpecification(long id, long modelId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "Id", id);
		SQLUtils.appendExtParamObject(sql, "ModelId", modelId);
		return queryObject(sql.toString());
	}

	@Override
	public int updateModelSpecification(ModelSpecification specification) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET AgentId = ").append(specification.getAgentId());
		if (specification.getIsShow() > 0) {
			sql.append(", IsShow = ").append(specification.getIsShow());
		}
		if (specification.getType() > 0) {
			sql.append(", Type = ").append(specification.getType());
		}
		if (StringUtils.isNotBlank(specification.getName())) {
			sql.append(", Name = '").append(specification.getName()).append("'");
		}
		if (StringUtils.isNotBlank(specification.getRemark())) {
			sql.append(", Remark = '").append(specification.getRemark()).append("'");
		}
		sql.append(" WHERE Id = ").append(specification.getId()).append(" AND ModelId = ").append(specification.getModelId());
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public int deleteModelSpecification(long id, long modelId) {
		StringBuilder sql = new StringBuilder("DELETE FROM ");
		sql.append(this.getTableName()).append(" WHERE Id = ").append(id).append(" AND ModelId = ").append(modelId);
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public ModelSpecification getModelSpecification(long id) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "Id", id);
		return queryObject(sql.toString());
	}

}
