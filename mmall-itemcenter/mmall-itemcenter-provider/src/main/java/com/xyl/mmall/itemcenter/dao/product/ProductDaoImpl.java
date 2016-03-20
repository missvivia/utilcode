package com.xyl.mmall.itemcenter.dao.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.dao.category.CategoryDao;
import com.xyl.mmall.itemcenter.dao.sku.SkuDao;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.meta.Product;
import com.xyl.mmall.itemcenter.meta.Sku;
import com.xyl.mmall.itemcenter.param.POProductSearchParam;
import com.xyl.mmall.itemcenter.param.ProductSearchParam;

@Repository
public class ProductDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<Product> implements ProductDao {
	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private ProductDetailDao detailDao;

	private final String GET_PRODUCTS_BY_NAME = "select * from " + super.getTableName() + " where ";

	@Override
	public Product addProduct(Product product) {
		return super.addObject(product);
	}

	@Override
	public Product getProduct(long productId) {
		return getObjectById(productId);
	}

	@Override
	public Product getProduct(long supplierId, long productId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SupplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "Id", productId);
		return queryObject(sql.toString());
	}

	@Override
	public boolean updateProductName(long supplierId, long productId, String productName) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET ProductName = '").append(productName);
		sql.append("' WHERE SupplierId = ").append(supplierId);
		sql.append(" AND Id = ").append(productId);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	// TODO
	@Override
	public boolean updateThumb(long productId, String path) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET ShowPicPath = '").append(path);
		sql.append("' WHERE Id = ").append(productId);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public BaseSearchResult<Product> searchProduct(ProductSearchParam param) {
		BaseSearchResult<Product> result = new BaseSearchResult<Product>();
		StringBuilder sql = new StringBuilder(64);
		if (StringUtils.isBlank(param.getBarCode())) {
			sql.append(genSelectSql());
			DDBParam ddbParam = new DDBParam();
			ddbParam.setOrderColumn("AddTime");
			ddbParam.setAsc(false);
			ddbParam.setLimit(param.getLimit());
			ddbParam.setOffset(param.getOffset());
			List<Product> list = getListByDDBParam(appendProductSearchSQL(genSelectSql(), param), ddbParam);
			result.setList(list);
			result.setHasNext(ddbParam.isHasNext());
			result.setTotal(ddbParam.getTotalCount());
			return result;
		} else {
			Sku sku = skuDao.getSkuByBarCode(param.getSupplierId(), param.getBarCode());
			if (sku == null) {
				result.setList(new ArrayList<Product>());
				result.setHasNext(false);
				result.setTotal(0);
				return result;
			} else {
				long pid = sku.getProductId();
				sql.append(genSelectSql());
				SQLUtils.appendExtParamObject(sql, "Id", pid);
				List<Product> list = queryObjects(appendProductSearchSQL(sql.toString(), param));
				result.setList(list);
				result.setHasNext(false);
				result.setTotal(list.size());
				return result;
			}
		}
	}

	@Override
	public List<Product> searchExportProduct(long supplierId, List<Long> pids) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Mmall_ItemCenter_Product WHERE 1=1 ");
		SQLUtils.appendExtParamObject(sql, "SupplierId", supplierId);
		sql.append(" AND Id IN ( ");
		for (int i = 0; i < pids.size(); i++) {
			long id = pids.get(i);
			sql.append(id);
			if (i < pids.size() - 1)
				sql.append(" ,");
		}
		sql.append(")");
		return super.queryObjects(sql.toString());
	}

	@Override
	public List<Product> searchExportProduct(ProductSearchParam param) {
		if (StringUtils.isBlank(param.getBarCode())) {
			String sql = appendProductSearchSQL(genSelectSql(), param);
			return queryObjects(sql);
		} else {
			Sku sku = skuDao.getSkuByBarCode(param.getSupplierId(), param.getBarCode());
			if (sku == null) {
				return new ArrayList<Product>();
			} else {
				StringBuilder sql = new StringBuilder();
				long pid = sku.getProductId();
				sql.append(genSelectSql());
				SQLUtils.appendExtParamObject(sql, "Id", pid);
				return queryObjects(appendProductSearchSQL(sql.toString(), param));
			}
		}
	}

	@Override
	public BaseSearchResult<Product> searchProduct(POProductSearchParam param) {
		BaseSearchResult<Product> result = new BaseSearchResult<Product>();
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SupplierId", param.getSupplierId());
		if (StringUtils.isBlank(param.getBarCode())) {
			DDBParam ddbParam = new DDBParam();
			ddbParam.setOrderColumn("Id");
			ddbParam.setAsc(false);
			ddbParam.setLimit(param.getLimit());
			ddbParam.setOffset(param.getOffset());
			List<Product> list = getListByDDBParam(appendProductSearchSQL(sql.toString(), param), ddbParam);
			result.setList(list);
			result.setHasNext(ddbParam.isHasNext());
			result.setTotal(ddbParam.getTotalCount());
			return result;
		} else {
			Sku sku = skuDao.getSkuByBarCode(param.getSupplierId(), param.getBarCode());
			if (sku == null) {
				result.setList(new ArrayList<Product>());
				result.setHasNext(false);
				result.setTotal(0);
				return result;
			} else {
				long pid = sku.getProductId();
				SQLUtils.appendExtParamObject(sql, "Id", pid);
				List<Product> list = queryObjects(appendProductSearchSQL(sql.toString(), param));
				result.setList(list);
				result.setHasNext(false);
				result.setTotal(list.size());
				return result;
			}
		}
	}

	private String appendProductSearchSQL(String prefix, ProductSearchParam param) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(prefix);
		SQLUtils.appendExtParamObject(sql, "SupplierId", param.getSupplierId());
		if (!StringUtils.isBlank(param.getGoodsNo()))
			SQLUtils.appendExtParamObject(sql, "GoodsNo", param.getGoodsNo());
		if (!StringUtils.isBlank(param.getProductName()))
			SQLUtils.appendExtParamObject(sql, "ProductName", param.getProductName());

		if (param.getIsBaseInfo() != -1) {
			SQLUtils.appendExtParamObject(sql, "InfoFlag & 8", param.getIsBaseInfo());
		}

		if (param.getIsSizeSet() != -1) {
			SQLUtils.appendExtParamObject(sql, "InfoFlag & 4", param.getIsSizeSet());
		}

		if (param.getIsPicInfo() != -1) {
			SQLUtils.appendExtParamObject(sql, "InfoFlag & 2", param.getIsPicInfo());
		}

		if (param.getIsDetailInfo() != -1) {
			SQLUtils.appendExtParamObject(sql, "InfoFlag & 1", param.getIsDetailInfo());
		}
		if (param.getStime() > 0) {
			sql.append(" AND AddTime >= ").append(param.getStime());
		}
		if (param.getEtime() > 0) {
			sql.append(" AND AddTime <= ").append(param.getEtime());
		}
		long categoryId = param.getLowCategoryId();
		if (categoryId > 0) {
			List<Category> retList = new ArrayList<Category>();
			categoryDao.getLowestCategoryById(retList, categoryId);
			if (retList.size() > 0) {
				sql.append(" AND LowCategoryId IN (");
				for (int i = 0; i < retList.size(); i++) {
					Category c = retList.get(i);
					sql.append(c.getId());
					if (i < retList.size() - 1)
						sql.append(" ,");
				}
				sql.append(")");
			}
		}
		return sql.toString();
	}

	private String appendProductSearchSQL(String prefix, POProductSearchParam param) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(prefix);
		if (!StringUtils.isBlank(param.getGoodsNo()))
			SQLUtils.appendExtParamObject(sql, "GoodsNo", param.getGoodsNo());
		if (!StringUtils.isBlank(param.getProductName()))
			SQLUtils.appendExtParamObject(sql, "ProductName", param.getProductName());

		long categoryId = param.getLowCategoryId();
		if (categoryId > 0) {
			List<Category> retList = new ArrayList<Category>();
			categoryDao.getLowestCategoryById(retList, categoryId);
			if (retList.size() > 0) {
				sql.append(" AND LowCategoryId IN (");
				for (int i = 0; i < retList.size(); i++) {
					Category c = retList.get(i);
					sql.append(c.getId());
					if (i < retList.size() - 1)
						sql.append(" ,");
				}
				sql.append(")");
			}
		}
		return sql.toString();
	}

	@Override
	public List<Product> getListBySizeTemplateId(long supplierId, long sizeTemplateId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SupplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "sizeTemplateId", sizeTemplateId);
		return queryObjects(sql.toString());
	}

	@Override
	public List<Product> getListBySizeAssistId(long supplierId, long helpId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SupplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "SizeAssistId", helpId);
		return queryObjects(sql.toString());
	}

	@Override
	public List<Product> getProductListByGoodsNo(long supplierId, String goodsNo) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SupplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "GoodsNo", goodsNo);
		return queryObjects(sql.toString());
	}

	@Override
	public Long getProductId(long supplierId, String goodNo, String colorNum) {
		Product p = getProduct(supplierId, goodNo, colorNum);
		if (p == null)
			return null;
		else
			return p.getId();
	}

	@Override
	public Product getProduct(long supplierId, String goodNo, String colorNum) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "supplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "GoodsNo", goodNo);
		SQLUtils.appendExtParamObject(sql, "ColorNum", colorNum);
		sql.append(" limit 1");
		return queryObject(sql.toString());
	}

	@Override
	public boolean updateCustomHtmlState(long supplierId, String goodsNo, String colorNum, int state) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET InfoFlag = InfoFlag|").append(1 * state);
		sql.append(" WHERE SupplierId = ").append(supplierId);
		sql.append(" AND GoodsNo = '").append(goodsNo).append("'");
		sql.append(" AND ColorNum = '").append(colorNum).append("'");
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public boolean updatePicState(long supplierId, String goodsNo, String colorNum, int state) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET InfoFlag = InfoFlag|").append(2 * state);
		sql.append(" WHERE SupplierId = ").append(supplierId);
		sql.append(" AND GoodsNo = '").append(goodsNo).append("'");
		sql.append(" AND ColorNum = '").append(colorNum).append("'");
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public boolean updateProductThumb(long supplierId, String goodsNo, String colorNum, String thumb) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET ShowPicPath = '").append(thumb).append("'");
		sql.append(" WHERE SupplierId = ").append(supplierId);
		sql.append(" AND GoodsNo = '").append(goodsNo).append("'");
		sql.append(" AND ColorNum = '").append(colorNum).append("'");
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	// TODO 数据迁移脚本
	@Override
	public List<Long> getProductId() {
		List<Long> retList = new ArrayList<Long>();
		String sql = "select Id from Mmall_ItemCenter_Product where LowCategoryId in (46,81,82,84) and SizeType = 3";
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				int result = 0;
				long pid = rs.getLong("Id");
				retList.add(pid);
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return retList;
	}

	@Override
	public List<PoProduct> getPoProductListByName(String productName) {
		return null;
	}

}
