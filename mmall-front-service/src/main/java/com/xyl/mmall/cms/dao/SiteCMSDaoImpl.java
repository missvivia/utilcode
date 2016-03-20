/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.cms.dto.SiteCMSDTO;
import com.xyl.mmall.cms.meta.SiteCMS;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.vo.BasePageParamVO;

/**
 * SiteCMSDaoImpl.java created by yydx811 at 2015年7月16日 下午5:16:00
 * 站点dao接口实现
 *
 * @author yydx811
 */
@Repository
public class SiteCMSDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<SiteCMS> implements SiteCMSDao {
	
	@Override
	public BasePageParamVO<SiteCMSDTO> getSiteCMSList(String searchValue, BasePageParamVO<SiteCMSDTO> pageParamVO) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		if (StringUtils.isNotBlank(searchValue)) {
			searchSQL(sql, searchValue);
		}
		this.appendOrderSql(sql, "UpdateTime", false);
		if (pageParamVO.getIsPage() == 1) {
			int count = getCount(searchValue);
			if (count < 1) {
				return pageParamVO;
			}
			pageParamVO.setTotal(count);
			int offset = pageParamVO.getStartRownum() - 1;
			int limit = pageParamVO.getPageSize();
			sql.append(" LIMIT ").append(offset).append(", ").append(limit);
		}
		List<SiteCMS> siteList = this.queryObjects(sql.toString());
		if (CollectionUtils.isEmpty(siteList)) {
			return pageParamVO;
		}
		pageParamVO.setList(convertToDTO(siteList));
		return pageParamVO;
	}

	public int getCount(String searchValue) {
		StringBuilder sql = new StringBuilder(genCountSql());
		if (StringUtils.isNotBlank(searchValue)) {
			searchSQL(sql, searchValue);
		}
		return this.getSqlSupport().queryCount(sql.toString());
	}
	
	private void searchSQL(StringBuilder sql, String searchValue) {
		searchValue = searchValue.trim();
		sql.append(" AND Name LIKE '%").append(searchValue.replace("'", "\\\\'")).append("%'");
		if (RegexUtils.isAllNumber(searchValue)) {
			sql.append(" OR Id = ").append(searchValue);
		}
	}
	
	private List<SiteCMSDTO> convertToDTO(List<SiteCMS> siteList) {
		List<SiteCMSDTO> retList = new ArrayList<SiteCMSDTO>(siteList.size());
		for (SiteCMS siteCMS : siteList) {
			retList.add(new SiteCMSDTO(siteCMS));
		}
		return retList;
	}

	@Override
	public long addSiteCMS(SiteCMS siteCMS) {
		long id = this.allocateRecordId();
		siteCMS.setId(id);
		return this.addObject(siteCMS) == null ? 0l : id;
	}

	@Override
	public int updateSiteCMS(SiteCMS siteCMS) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET UpdateOperator = ").append(siteCMS.getUpdateOperator());
		String name = siteCMS.getName();
		if (StringUtils.isNotBlank(name)) {
			name = name.replace("'", "\\\\'");
			sql.append(", Name = '").append(name).append("'");
		}
		sql.append(" WHERE Id = ").append(siteCMS.getId());
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public int deleteBulkSiteCMS(List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return 0;
		} else {
			StringBuilder sql = new StringBuilder(genDeleteSql());
			sql.append(" Id IN (");
			for (Long areaId : ids) {
				sql.append(areaId).append(",");
			}
			sql.deleteCharAt(sql.length() - 1).append(")");
			return this.getSqlSupport().excuteUpdate(sql.toString());
		}
	}

	@Override
	public List<SiteCMS> getSiteCMSList(List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return null;
		} else {
			StringBuilder sql = new StringBuilder(genSelectSql());
			sql.append("AND Id IN (");
			for (Long areaId : ids) {
				sql.append(areaId).append(",");
			}
			sql.deleteCharAt(sql.length() - 1).append(")");
			return this.queryObjects(sql.toString());
		}
	}

	@Override
	public List<SiteCMS> getAllSiteCMSList() {
		return super.getAll();
	}
}