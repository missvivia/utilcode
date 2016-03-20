/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.content.meta.Article;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

/**
 * @author lihui
 *
 */
@Repository
public class ArticleDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<Article> implements ArticleDao {

	private static final String SQL_FIND_WITH_CATEGORY = "SELECT r.id, r.categoryId, r.parentCategoryId, r.keywords, r.title, r.content, "
			+ "r.lastSaveTime, r.publishTime, r.lastModifiedBy, r.lastModifiedUserName, r.status, r.publishType, c.Name AS categoryName, p.Name AS parentCategoryName "
			+ "FROM Mmall_Content_HelpCategory c, Mmall_Content_Article r, Mmall_Content_HelpCategory p "
			+ "WHERE c.id = r.categoryId AND r.parentCategoryId = p.id ";

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.ArticleDao#findByPublishTypeAndKeywordListAndCategoryId(java.util.List,
	 *      java.util.List, long, long,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<Article> findByPublishTypeAndKeywordListAndCategoryId(List<Integer> publishTypeList,
			List<String> keywordList, long categoryId, long parentCategoryId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_FIND_WITH_CATEGORY);
		if (CollectionUtils.isNotEmpty(publishTypeList)) {
			sql.append(" AND ( ");
			for (int i = 0; i < publishTypeList.size(); i++) {
				sql.append(" r.publishType = ").append(publishTypeList.get(i));
				if (i < publishTypeList.size() - 1) {
					sql.append(" OR ");
				}
			}
			sql.append(" ) ");
		}
		if (categoryId != -1) {
			SqlGenUtil.appendExtParamObject(sql, "r.CategoryId", categoryId);
		}
		if (parentCategoryId != -1) {
			SqlGenUtil.appendExtParamObject(sql, "r.ParentCategoryId", parentCategoryId);
		}
		if (StringUtils.isBlank(param.getOrderColumn())) {
			param.setOrderColumn("r.lastSaveTime");
			param.setAsc(false);
		}
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql.toString(), keywordList.toArray());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.ArticleDao#countByPublishTypeAndKeywordListAndCategoryId(java.util.List,
	 *      java.util.List, long, long)
	 */
	@Override
	public int countByPublishTypeAndKeywordListAndCategoryId(List<Integer> publishTypeList, List<String> keywordList,
			long categoryId, long parentCategoryId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genCountSql());
		if (CollectionUtils.isNotEmpty(publishTypeList)) {
			sql.append(" AND ( ");
			for (int i = 0; i < publishTypeList.size(); i++) {
				sql.append(" publishType = ").append(publishTypeList.get(i));
				if (i < publishTypeList.size() - 1) {
					sql.append(" OR ");
				}
			}
			sql.append(" ) ");
		}
		if (categoryId != -1) {
			SqlGenUtil.appendExtParamObject(sql, "categoryId", categoryId);
		}
		if (parentCategoryId != -1) {
			SqlGenUtil.appendExtParamObject(sql, "parentCategoryId", parentCategoryId);
		}
		return getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public Article getObjectFromRs(ResultSet rs) throws SQLException {
		Article article = super.getObjectFromRs(rs);
		try {
			article.setCategoryName(rs.getString("categoryName"));
			article.setParentCategoryName(rs.getString("parentCategoryName"));
			article.setParentCategoryId(rs.getLong("parentCategoryId"));
		} catch (SQLException e) {
			// SQL don't has categoryName column, do nothing
		}
		return article;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.ArticleDao#findDetailWithParentCategoryById(long)
	 */
	@Override
	public Article findDetailWithParentCategoryById(long id) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_FIND_WITH_CATEGORY);
		SqlGenUtil.appendExtParamObject(sql, "r.id", id);
		return queryObject(sql.toString());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.dao.ArticleDao#findByIdList(java.util.List, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<Article> findByIdList(List<Long> idList, DDBParam param) {
		if(CollectionUtil.isEmptyOfList(idList)) {
			return new ArrayList<Article>();
		}
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "id", idList);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.dao.ArticleDao#findAllIdSet(com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<Long> findAllIdSet(DDBParam param) {
		List<Long> idList = new ArrayList<Long>();
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT Id FROM Mmall_Content_Article");
		DBResource dbr = getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = (null == dbr) ? null : dbr.getResultSet();
		if(null == rs) {
			return idList;
		}
		try {
			while (rs.next()) {
				long id = rs.getLong("Id");
				idList.add(id);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbr.close();
		}
		return idList;
	}
	
}
