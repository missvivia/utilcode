package com.xyl.mmall.content.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.content.constants.CategoryContentLevel;
import com.xyl.mmall.content.dto.SearchCategoryContentDTO;
import com.xyl.mmall.content.meta.CategoryContent;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

@Repository
public class CategoryContentDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CategoryContent> implements
		CategoryContentDao {

	@Override
	public List<CategoryContent> getCategoryContentListBySuperId(long superId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "superCategoryId", superId);
		sql.append(" order by ShowIndex asc");
		return queryObjects(sql);
	}

	@Override
	public List<CategoryContent> getCategoryContentListByLevelAndRootId(int level, long rootId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		if (level >= 0) {
			SqlGenUtil.appendExtParamObject(sql, "level", level);
		}
		if (rootId >= 0) {
			SqlGenUtil.appendExtParamObject(sql, "rootId", rootId);
		}
		sql.append(" order by ShowIndex asc");
		return queryObjects(sql);
	}

	@Override
	public List<CategoryContent> searchCategoryContentList(SearchCategoryContentDTO searchDto) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (searchDto.getLevel() > 0) {
			SqlGenUtil.appendExtParamObject(sql, "level", searchDto.getLevel());
		}
		if (StringUtils.isNotEmpty(searchDto.getName())) {
			sql.append(" AND name LIKE '%").append(searchDto.getName()).append("%'");
		}
		return getListByDDBParam(sql.toString(), (DDBParam) searchDto);
	}

	@Override
	public List<CategoryContent> getCategoryContentList() {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		sql.append(" AND Level IN (1,2,3)");
		sql.append(" order by level desc,ShowIndex asc");
		return queryObjects(sql);
	}

	@Override
	public CategoryContent getLastestCategoryContent() {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		sql.append(" order by CreateTime desc limit 1");
		return queryObject(sql);
	}

	@Override
	public List<CategoryContent> getCategoryContentListByIds(List<Long> ids) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND Id IN (");
		for (Long id : ids) {
			sql.append(id).append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(")");
		sql.append(" order by ShowIndex asc");
		return queryObjects(sql);
	}

	@Override
	public List<CategoryContent> getCategoryContentListBySuperIds(List<Long> superIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND SuperCategoryId IN (");
		for (Long id : superIds) {
			sql.append(id).append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(")");
		sql.append(" order by ShowIndex asc");
		return queryObjects(sql);
	}

	@Override
	public List<CategoryContent> queryThirdCategoryContentListBindCategoryNormal() {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "Level", CategoryContentLevel.LEVEL_THIRD.getIntValue());
		sql.append(" AND categoryNormalIds IS NOT NULL AND categoryNormalIds != ''");
		return queryObjects(sql);
	}

	@Override
	public List<CategoryContent> getCategoryContentListByRootId(long rootId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "RootId", rootId);
		sql.append(" order by level desc,ShowIndex asc ");
		return queryObjects(sql);
	}

	@Override
	public boolean deleteCategoryContentByRootId(long rootId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "RootId", rootId);
		return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	@Override
	public boolean updateCategoryContent(CategoryContent categoryContent) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("Name");
		fieldNameSetOfUpdate.add("showIndex");
		fieldNameSetOfUpdate.add("superCategoryId");
		fieldNameSetOfUpdate.add("categoryNormalIds");
		fieldNameSetOfUpdate.add("level");
		fieldNameSetOfUpdate.add("districtIds");
		fieldNameSetOfUpdate.add("districtNames");
		fieldNameSetOfUpdate.add("updateBy");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("id");

		return SqlGenUtil.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, categoryContent,
				getSqlSupport());
	}

}
