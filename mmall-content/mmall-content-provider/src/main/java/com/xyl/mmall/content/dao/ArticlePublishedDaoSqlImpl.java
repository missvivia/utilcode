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
import com.xyl.mmall.content.meta.ArticlePublished;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

/**
 * @author lihui
 *
 */
@Repository
public class ArticlePublishedDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<ArticlePublished> implements
		ArticlePublishedDao {

	private static final String SQL_FIND_WITH_CATEGORY = "SELECT r.id, r.categoryId, r.parentCategoryId, r.keywords, r.title, r.content, "
			+ "r.lastSaveTime, r.publishTime, r.lastModifiedBy, r.lastModifiedUserName, r.status, r.publishType, r.orgArticleId, c.Name AS categoryName, "
			+ "p.Name AS parentCategoryName FROM Mmall_Content_HelpCategory c, Mmall_Content_ArticlePublished r, "
			+ "Mmall_Content_HelpCategory p WHERE c.id = r.categoryId AND r.parentCategoryId = p.id ";

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.ArticlePublishedDao#findByPublishTypeAndKeywordListAndCategoryId(java.util.List,
	 *      java.util.List, long, long,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ArticlePublished> findByPublishTypeAndKeywordListAndCategoryId(List<Integer> publishTypeList,
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
			param.setOrderColumn("r.publishTime");
			param.setAsc(false);
		}
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql.toString(), keywordList.toArray());
	}

	@Override
	public ArticlePublished getObjectFromRs(ResultSet rs) throws SQLException {
		ArticlePublished article = super.getObjectFromRs(rs);
		try {
			article.setCategoryName(rs.getString("categoryName"));
			article.setParentCategoryName(rs.getString("parentCategoryName"));
		} catch (SQLException e) {
			// SQL don't has categoryName column, do nothing
		}
		return article;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.ArticlePublishedDao#countByPublishTypeAndKeywordListAndCategoryId(java.util.List,
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
		return getSqlSupport().queryCount(sql.toString(), keywordList.toArray());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.ArticlePublishedDao#findByOrgArticleId(long)
	 */
	@Override
	public ArticlePublished findByOrgArticleId(long id) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orgArticleId", id);
		return queryObject(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.dao.ArticlePublishedDao#findByIdList(java.util.List, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ArticlePublished> findByIdList(List<Long> idList, DDBParam param) {
		if(CollectionUtil.isEmptyOfList(idList)) {
			return new ArrayList<ArticlePublished>();
		}
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "id", idList);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.dao.ArticlePublishedDao#findAllIdSet(com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<Long> findAllIdSet(DDBParam param) {
		List<Long> idList = new ArrayList<Long>();
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT Id FROM Mmall_Content_ArticlePublished");
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
