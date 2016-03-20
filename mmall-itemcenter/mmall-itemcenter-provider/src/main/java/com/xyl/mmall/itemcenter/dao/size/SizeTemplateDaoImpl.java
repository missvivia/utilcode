package com.xyl.mmall.itemcenter.dao.size;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.dao.category.CategoryDao;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.Product;
import com.xyl.mmall.itemcenter.meta.SizeTemplate;
import com.xyl.mmall.itemcenter.param.SizeTemplateSearchParam;
import com.xyl.mmall.itemcenter.util.ConstantsUtil;

@Repository
public class SizeTemplateDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<SizeTemplate> implements SizeTemplateDao {

	@Autowired
	CategoryDao categoryDao;

	private final int PAGE_SIZE = 5;

	@Override
	public SizeTemplate addNewSizeTemplate(SizeTemplate sizeTempl) {
		return super.saveObject(sizeTempl);
	}

	@Override
	public BaseSearchResult<SizeTemplate> searchSizeTemplate(SizeTemplateSearchParam searchDTO) {
		BaseSearchResult<SizeTemplate> result = new BaseSearchResult<SizeTemplate>();
		StringBuffer sqlBuffer = new StringBuffer(genSelectSql());
		List<Object> param = new ArrayList<Object>();
		long userId = searchDTO.getSupplierId();
		sqlBuffer.append(" AND SupplierId = ? ");
		param.add(userId);
		long categoryId = searchDTO.getLowCategoryId();
		if (categoryId > 0) {
			List<Category> list = new ArrayList<Category>();
			categoryDao.getLowestCategoryById(list, categoryId);
			if (list.size() > 0) {
				sqlBuffer.append(" AND LowCategoryId in ( ");
				for (int i = 0; i < list.size(); i++) {
					Category c = list.get(i);
					if (i == list.size() - 1) {
						sqlBuffer.append("?");
					} else {
						sqlBuffer.append("?, ");
					}
					param.add(c.getId());
				}
				sqlBuffer.append(" ) ");
			}
		}
		long tmpId = searchDTO.getSizeTemplateId();
		if (tmpId > 0) {
			sqlBuffer.append(" AND Id =? ");
			param.add(tmpId);
		}
		String name = searchDTO.getSizeTemplateName();
		if (name != null && !"".equals(name)) {
			sqlBuffer.append(" AND TemplateName = ? ");
			param.add(name);
		}
		long stime = searchDTO.getStime();
		if (stime > 0) {
			sqlBuffer.append(" AND LastModifyTime >= ? ");
			param.add(stime);
		}
		long etime = searchDTO.getEtime();
		if (etime > 0) {
			sqlBuffer.append(" AND LastModifyTime <= ? ");
			param.add(etime);
		}
		// TODO isInPo
		sqlBuffer.append("AND IsInPo = 0");
		DDBParam ddbParam = new DDBParam();
		ddbParam.setOrderColumn("Id");
		ddbParam.setAsc(false);
		ddbParam.setLimit(searchDTO.getLimit());
		ddbParam.setOffset(searchDTO.getOffset());
		List<SizeTemplate> list = getListByDDBParam(sqlBuffer.toString(), ddbParam, param);
		result.setList(list);
		result.setHasNext(ddbParam.isHasNext());
		result.setTotal(ddbParam.getTotalCount());
		return result;
	}

	/**
	 * 查询数据,并返回是否有下一页的标记
	 * 
	 * @param sqlPreffix
	 * @param param
	 * @param prepareParams
	 * @return
	 */
	private List<SizeTemplate> getListByDDBParam(String sqlPreffix, DDBParam param, List<Object> paramList) {
		// 1.生成一个新的DDBParam(limit+1,同时用来计算isHashNext标记)
		if (param == null)
			param = new DDBParam();
		DDBParam paramOfTmp = param != null ? param.cloneParam() : null;
		paramOfTmp.setLimit(param.getLimit() <= 0 ? param.getLimit() : param.getLimit() + 1);
		// 2.生成正常的SQL
		StringBuilder sql = new StringBuilder(256);
		sql.append(sqlPreffix);
		SqlGenUtil.appendDDBParam(sql, paramOfTmp, defaultOrderColumn);
		// 3.执行数据库查询操作
		List<SizeTemplate> list = queryObjects(sql.toString(), paramList);
		// 4.计算isHashNext标记
		boolean isHasNext = false;
		if (paramOfTmp.getLimit() > 0 && list != null && list.size() == paramOfTmp.getLimit()) {
			isHasNext = true;
			list.remove(paramOfTmp.getLimit() - 1);
		}
		param.setHasNext(isHasNext);
		param.setTotalCount(list.size());
		return list;
	}


	@Override
	public List<SizeTemplate> getSizeTemplate(long categoryId, long supplierId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "lowCategoryId", categoryId);
		SQLUtils.appendExtParamObject(sql, "SupplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "IsInPo", ConstantsUtil.NOT_IN_PO);
		return queryObjects(sql.toString());
	}

	@Override
	public void saveSizeTemplate(SizeTemplate sizeTemplate) {
		saveObject(sizeTemplate);
	}
}
