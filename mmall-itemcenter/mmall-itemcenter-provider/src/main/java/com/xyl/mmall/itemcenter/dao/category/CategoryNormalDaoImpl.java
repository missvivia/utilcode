/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.category;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.dto.CategoryNormalDTO;
import com.xyl.mmall.itemcenter.enums.CategoryNormalLevel;
import com.xyl.mmall.itemcenter.meta.CategoryNormal;

/**
 * CategoryNormalDaoImpl.java created by yydx811 at 2015年4月27日 下午4:42:40
 * 商品分类dao实现
 *
 * @author yydx811
 */

@Repository
public class CategoryNormalDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CategoryNormal> implements CategoryNormalDao {

	private static Logger logger = Logger.getLogger(CategoryNormalDaoImpl.class);
	
	public List<CategoryNormal> _getCategoryNormalList() {
		StringBuffer sql = new StringBuffer(genSelectSql());
		sql.append(" ORDER BY Level, ShowIndex");
		return queryObjects(sql.toString());
	}
	
	@Override
	public List<CategoryNormalDTO> getCategoryNormalList(BasePageParamVO<?> basePageParamVO) {
		// 一级
		List<CategoryNormal> firstList = getFirstCategoryNormalList(basePageParamVO);
		if (!CollectionUtils.isEmpty(firstList)) {
			List<CategoryNormalDTO> retList = new ArrayList<CategoryNormalDTO>(firstList.size());
			for (CategoryNormal firstCN : firstList) {
				CategoryNormalDTO firstDTO = new CategoryNormalDTO(firstCN);
				// 二级
				List<CategoryNormal> secondList = getSubCategoryNormalList(firstCN.getId());
				if (!CollectionUtils.isEmpty(secondList)) {
					List<CategoryNormalDTO> secondDTOList = new ArrayList<CategoryNormalDTO>(secondList.size());
					for (CategoryNormal secondCN : secondList) {
						CategoryNormalDTO secondDTO = new CategoryNormalDTO(secondCN);
						// 三级
						List<CategoryNormal> thirdList = getSubCategoryNormalList(secondCN.getId());
						if (!CollectionUtils.isEmpty(thirdList)) {
							List<CategoryNormalDTO> thirdDTOList = new ArrayList<CategoryNormalDTO>(thirdList.size());
							for (CategoryNormal thirdCN : thirdList) {
								CategoryNormalDTO thirdDTO = new CategoryNormalDTO(thirdCN);
								thirdDTOList.add(thirdDTO);
							}
							secondDTO.setSameParentList(thirdDTOList);
						}
						secondDTOList.add(secondDTO);
					}
					firstDTO.setSameParentList(secondDTOList);
				}
				retList.add(firstDTO);
			}
			return retList;
		} else
			return null;
	}

	@Override
	public List<CategoryNormalDTO> getCategoryNormalList() {
		// 一级
		List<CategoryNormal> firstList = getFirstCategoryNormalList();
		if (!CollectionUtils.isEmpty(firstList)) {
			List<CategoryNormalDTO> retList = new ArrayList<CategoryNormalDTO>(firstList.size());
			for (CategoryNormal firstCN : firstList) {
				CategoryNormalDTO firstDTO = new CategoryNormalDTO(firstCN);
				// 二级
				List<CategoryNormal> secondList = getSubCategoryNormalList(firstCN.getId());
				if (!CollectionUtils.isEmpty(secondList)) {
					List<CategoryNormalDTO> secondDTOList = new ArrayList<CategoryNormalDTO>(secondList.size());
					for (CategoryNormal secondCN : secondList) {
						CategoryNormalDTO secondDTO = new CategoryNormalDTO(secondCN);
						// 三级
						List<CategoryNormal> thirdList = getSubCategoryNormalList(secondCN.getId());
						if (!CollectionUtils.isEmpty(thirdList)) {
							List<CategoryNormalDTO> thirdDTOList = new ArrayList<CategoryNormalDTO>(thirdList.size());
							for (CategoryNormal thirdCN : thirdList) {
								CategoryNormalDTO thirdDTO = new CategoryNormalDTO(thirdCN);
								thirdDTOList.add(thirdDTO);
							}
							secondDTO.setSameParentList(thirdDTOList);
						}
						secondDTOList.add(secondDTO);
					}
					firstDTO.setSameParentList(secondDTOList);
				}
				retList.add(firstDTO);
			}
			return retList;
		} else
			return null;
	}

	@Override
	public int getCategoryNormalCount(CategoryNormal categoryNormal) {
		StringBuilder sql = new StringBuilder("SELECT COUNT(Id) FROM ");
		sql.append(this.getTableName()).append(" WHERE 1=1 ");
		createSQL(sql, categoryNormal, false);
		return this.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public List<CategoryNormal> getFirstCategoryNormalList(BasePageParamVO<?> basePageParamVO) {
		CategoryNormal categoryNormal = new CategoryNormal();
		categoryNormal.setLevel(CategoryNormalLevel.LEVEL_FIRST.getIntValue());
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "Level", CategoryNormalLevel.LEVEL_FIRST.getIntValue());
		// 排序
		try {
			orderByShowIndexASC(sql);
			SQLUtils.getPageSql(sql, basePageParamVO);
		} catch (SQLException e) {
			logger.error(e);
			return null;
		}
		return queryObjects(sql.toString());
	}

	@Override
	public List<CategoryNormal> getFirstCategoryNormalList() {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "Level", CategoryNormalLevel.LEVEL_FIRST.getIntValue());
		// 排序
		try {
			orderByShowIndexASC(sql);
		} catch (SQLException e) {
			logger.error(e);
			return null;
		}
		return queryObjects(sql.toString());
	}

	@Override
	public List<CategoryNormal> getSubCategoryNormalList(long superCategoryId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SuperCategoryId", superCategoryId);
		// 排序
		try {
			orderByShowIndexASC(sql);
		} catch (SQLException e) {
			logger.error(e);
			return null;
		}
		return queryObjects(sql.toString());
	}

	@Override
	public CategoryNormal getCategoryNormalById(long id) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "Id", id);
		return queryObject(sql.toString());
	}

	@Override
	public int createCategoryNormal(CategoryNormal categoryNormal) {
		categoryNormal.setShowIndex(getMaxShowIndex(categoryNormal) + 1);
		addObject(categoryNormal);
		return 1;
	}

	@Override
	public int getMaxShowIndex(CategoryNormal categoryNormal) {
		StringBuilder sql = new StringBuilder("SELECT MAX(ShowIndex) FROM ");
		sql.append(this.getTableName()).append(" WHERE 1=1 ");
		SQLUtils.appendExtParamObject(sql, "SuperCategoryId", categoryNormal.getSuperCategoryId());
		SQLUtils.appendExtParamObject(sql, "Level", categoryNormal.getLevel());
		return this.getSqlSupport().queryCount(sql.toString());
	}
	
	@Override
	public CategoryNormal getCategoryNormal(CategoryNormal categoryNormal) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		createSQL(sql, categoryNormal, true);
		return queryObject(sql.toString());
	}

	@Override
	public int updateCategoryNormal(CategoryNormal categoryNormal) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET ");
		sql.append("AgentId = ").append(categoryNormal.getAgentId());
		if (categoryNormal.getLevel() > 0) {
			sql.append(", Level = ").append(categoryNormal.getLevel());
		}
		if (StringUtils.isNotBlank(categoryNormal.getName())) {
			sql.append(", Name = '").append(categoryNormal.getName()).append("'");
		}
		if (categoryNormal.getSuperCategoryId() >= 0l) {
			sql.append(", SuperCategoryId = ").append(categoryNormal.getSuperCategoryId());
		}
		sql.append(" WHERE Id = ").append(categoryNormal.getId());
		return this.getSqlSupport().excuteUpdate(sql.toString());
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
	
	private void createSQL(StringBuilder sql, CategoryNormal categoryNormal, boolean isExact) {
		if (categoryNormal.getId() != 0l) {
			SQLUtils.appendExtParamObject(sql, "Id", categoryNormal.getId());
		}
		if (categoryNormal.getLevel() != 0) {
			SQLUtils.appendExtParamObject(sql, "Level", categoryNormal.getLevel());
		}
		if (categoryNormal.getSuperCategoryId() != 0l) {
			SQLUtils.appendExtParamObject(sql, "SuperCategoryId", categoryNormal.getSuperCategoryId());
		}
		if (categoryNormal.getShowIndex() != 0) {
			SQLUtils.appendExtParamObject(sql, "ShowIndex", categoryNormal.getShowIndex());
		}
		if (StringUtils.isNotBlank(categoryNormal.getName())) {
			if (isExact) {
				String name = categoryNormal.getName().replaceAll("'", "\\\\'");
				sql.append("Name like '%").append(name).append("%'");
			} else {
				sql.append("Name = '").append(categoryNormal.getName()).append("'");
			}
		}
	}

	@Override
	public int updateCategoryNormalSort(CategoryNormal categoryNormal, int isUp) {
		Connection conn = null;
		try {
			conn = this.getSqlSupport().getConnection();
			conn.setAutoCommit(false);
			int newIndex = 0;
			if (isUp == 1) {
				newIndex = categoryNormal.getShowIndex() - 1;
			} else {
				newIndex = categoryNormal.getShowIndex() + 1;
			}
			String sqlHead = "UPDATE " + this.getTableName() + " SET ShowIndex = ";
			StringBuilder sql = new StringBuilder(sqlHead);
			if (isUp == 1) {
				sql.append("ShowIndex + 1");
			} else {
				sql.append("ShowIndex - 1");
			}
			sql.append(", AgentId = ").append(categoryNormal.getAgentId()).append(" WHERE 1=1 ");
			SQLUtils.appendExtParamObject(sql, "SuperCategoryId", categoryNormal.getSuperCategoryId());
			SQLUtils.appendExtParamObject(sql, "Level", categoryNormal.getLevel());
			SQLUtils.appendExtParamObject(sql, "ShowIndex", newIndex);
			int res = conn.prepareStatement(sql.toString()).executeUpdate();
			if (res < 1) {
				conn.rollback();
				return -1;
			}
			sql = new StringBuilder(sqlHead);
			sql.append(newIndex);
			sql.append(", AgentId = ").append(categoryNormal.getAgentId()).append(" WHERE 1=1 ");
			SQLUtils.appendExtParamObject(sql, "Id", categoryNormal.getId());
			res = conn.prepareStatement(sql.toString()).executeUpdate();
			if (res < 1) {
				conn.rollback();
			}
			return res;
		} catch (SQLException e) {
			logger.error(e);
			return -1;
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
	}

	@Override
	public int deleteCategoryNormal(long id) {
		CategoryNormal old = getCategoryNormalById(id);
		if (old == null || old.getId() < 1l) {
			return 1;
		}
		StringBuilder sql = new StringBuilder("DELETE FROM ");
		sql.append(this.getTableName()).append(" WHERE Id = ").append(id);
		int res = this.getSqlSupport().excuteUpdate(sql.toString());
		if (res > 0) {
			sql = new StringBuilder("UPDATE ");
			sql.append(this.getTableName()).append(" SET ShowIndex = ShowIndex - 1 WHERE SuperCategoryId = ")
				.append(old.getSuperCategoryId()).append(" AND Level = ").append(old.getLevel())
				.append(" AND ShowIndex > ").append(old.getShowIndex());
			this.getSqlSupport().excuteUpdate(sql.toString());
		}
		return res;
	}

	@Override
	public List<CategoryNormal> getCategoryListByIds(List<Long> ids) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		sql.append(" AND Id IN (");
		for (int i = 0; i < ids.size(); i++) {
			long id = ids.get(i);
			sql.append(id);
			if (i < ids.size() - 1)
				sql.append(" ,");
		}
		sql.append(")");
		return queryObjects(sql.toString());
	}

	@Override
	public List<CategoryNormal> getALLCategoryNormalList() {
		return _getCategoryNormalList();
	}

	@Override
	public List<CategoryNormal> getCategoryNormalByName(String categoryNormalName, int level, long superId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "Name", categoryNormalName);
		SQLUtils.appendExtParamObject(sql, "Level", level);
		SQLUtils.appendExtParamObject(sql, "superCategoryId", superId);
		return queryObjects(sql.toString());
	}
}
