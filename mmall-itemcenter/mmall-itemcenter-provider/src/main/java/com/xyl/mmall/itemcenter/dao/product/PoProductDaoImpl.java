package com.xyl.mmall.itemcenter.dao.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.PrintDaoUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.dao.category.CategoryDao;
import com.xyl.mmall.itemcenter.dao.sku.PoSkuDao;
import com.xyl.mmall.itemcenter.dao.sku.SkuSpecMapDao;
import com.xyl.mmall.itemcenter.dto.CategoryGroupDTO;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.PoProductVo;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.meta.SkuSpecMap;
import com.xyl.mmall.itemcenter.param.PoProductSo;

@Repository
public class PoProductDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<PoProduct> implements PoProductDao {
	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private PoSkuDao poSkuDao;

	@Autowired
	private SkuSpecMapDao skuSpecMapDao;

	private final String GET_POPRODUCT_BYPOID_PODUCTID = "select * from " + super.getTableName()
			+ " where id=? and poId=?";

	private final String GET_POPRODUCT_BYPOID_PODUCTNAME = "select * from " + super.getTableName()
			+ " where productName=? AND IsDelete = 0";

	private final String GET_POPRODUCT_BY_GOODSNO = "select * from " + super.getTableName()
			+ " where goodsNo=? AND IsDelete = 0";

	private final String GET_POPRODUCT_BY_POID_GOODSNO = "select * from " + super.getTableName()
			+ " where poId=? and goodsNo=?";

	private static final String SEARCH_POPODUCTVO = "select a.*,b.brandId brandId,b.startTime startTime "
			+ ",b.endTime endTime,b.supplierId supplierId,b.supplierName supplierName"
			+ ",b.brandName brandName,b.brandNameEn brandNameEn,b.saleSiteFlag saleSiteFlag from Mmall_ItemCenter_PoProduct a,Mmall_SaleSchedule_Schedule b where a.PoId=b.Id and a.status>1 ";

	private static final String SEARCH_POPRODUCTVO_COUNT = "select count(1) cnt from Mmall_ItemCenter_PoProduct a,Mmall_SaleSchedule_Schedule b where a.PoId=b.Id and a.status>1 ";

	private static final Logger logger = LoggerFactory.getLogger(PoProductDaoImpl.class);

	public PoProduct addObject(PoProduct product) {
		return super.addObject(product);
	}

	/*
	 * @Override public BaseSearchResult<PoProduct>
	 * searchProduct(POProductSearchParam param) { BaseSearchResult<PoProduct>
	 * result = new BaseSearchResult<PoProduct>(); StringBuilder sql = new
	 * StringBuilder(64); sql.append(genSelectSql());
	 * 
	 * if (StringUtils.isBlank(param.getBarCode())) { DDBParam ddbParam = new
	 * DDBParam(); ddbParam.setOrderColumn("Id"); ddbParam.setAsc(false);
	 * ddbParam.setLimit(param.getLimit());
	 * ddbParam.setOffset(param.getOffset()); List<PoProduct> list =
	 * getListByDDBParam(appendProductSearchSQL(sql.toString(), param),
	 * ddbParam); result.setList(list); result.setHasNext(ddbParam.isHasNext());
	 * result.setTotal(ddbParam.getTotalCount()); return result; } else { PoSku
	 * sku = poSkuDao.getSku(param.getPoId(), param.getBarCode()); if (sku ==
	 * null) { result.setList(new ArrayList<PoProduct>());
	 * result.setHasNext(false); result.setTotal(0); return result; } else {
	 * long pid = sku.getProductId(); SQLUtils.appendExtParamObject(sql, "Id",
	 * pid); List<PoProduct> list =
	 * queryObjects(appendProductSearchSQL(sql.toString(), param));
	 * result.setList(list); result.setHasNext(false);
	 * result.setTotal(list.size()); return result; } } }
	 */
	/*
	 * @Override public BaseSearchResult<PoProduct>
	 * searchProductReview(ProductReviewParam param, List<Long> poIdList) {
	 * BaseSearchResult<PoProduct> result = new BaseSearchResult<PoProduct>();
	 * StringBuilder sql = new StringBuilder(64); sql.append(genSelectSql()); if
	 * (!StringUtils.isBlank(param.getGoodsNo()))
	 * SQLUtils.appendExtParamObject(sql, "GoodsNo", param.getGoodsNo()); if
	 * (!StringUtils.isBlank(param.getProductName()))
	 * SQLUtils.appendExtParamObject(sql, "ProductName",
	 * param.getProductName());
	 * 
	 * if (param.getStatusType() != null) SQLUtils.appendExtParamObject(sql,
	 * "Status", param.getStatusType()); else sql.append(" AND Status != 1 ");
	 * if (poIdList.size() > 0) { sql.append(" AND PoId IN ("); for (int i = 0;
	 * i < poIdList.size(); i++) { long poId = poIdList.get(i);
	 * sql.append(poId); if (i < poIdList.size() - 1) sql.append(" ,"); }
	 * sql.append(")"); } if (param.getStime() > 0) {
	 * sql.append(" AND submitTime > " + param.getStime()); } if
	 * (param.getEtime() > 0) { sql.append(" AND submitTime < " +
	 * param.getEtime()); }
	 * 
	 * DDBParam ddbParam = new DDBParam(); ddbParam.setOrderColumn("Id");
	 * ddbParam.setAsc(false); ddbParam.setLimit(param.getLimit());
	 * ddbParam.setOffset(param.getOffset()); List<PoProduct> list =
	 * getListByDDBParam(sql.toString(), ddbParam); result.setList(list);
	 * result.setHasNext(ddbParam.isHasNext());
	 * result.setTotal(ddbParam.getTotalCount()); return result; }
	 */
	/*
	 * @Override public BaseSearchResult<PoProduct>
	 * searchProductReview2(List<Long> productIdList, ProductReviewParam param)
	 * { BaseSearchResult<PoProduct> result = new BaseSearchResult<PoProduct>();
	 * StringBuilder sql = new StringBuilder(64); sql.append(genSelectSql()); if
	 * (!StringUtils.isBlank(param.getGoodsNo()))
	 * SQLUtils.appendExtParamObject(sql, "GoodsNo", param.getGoodsNo()); if
	 * (!StringUtils.isBlank(param.getProductName()))
	 * SQLUtils.appendExtParamObject(sql, "ProductName",
	 * param.getProductName());
	 * 
	 * if (param.getStatusType() != null) SQLUtils.appendExtParamObject(sql,
	 * "Status", param.getStatusType()); else sql.append(" AND Status != 1 ");
	 * 
	 * if (productIdList.size() > 0) { sql.append(" AND Id IN ("); for (int i =
	 * 0; i < productIdList.size(); i++) { long productId =
	 * productIdList.get(i); sql.append(productId); if (i < productIdList.size()
	 * - 1) sql.append(" ,"); } sql.append(")"); }
	 * 
	 * DDBParam ddbParam = new DDBParam();
	 * ddbParam.setOffset(param.getOffset()); ddbParam.setOrderColumn("Id");
	 * ddbParam.setAsc(false); ddbParam.setLimit(param.getLimit());
	 * 
	 * List<PoProduct> list = getListByDDBParam(sql.toString(), ddbParam);
	 * result.setList(list); result.setHasNext(ddbParam.isHasNext());
	 * result.setTotal(ddbParam.getTotalCount()); return result; }
	 * 
	 * private String appendProductSearchSQL(String prefix, POProductSearchParam
	 * param) { StringBuilder sql = new StringBuilder(64); sql.append(prefix);
	 * SQLUtils.appendExtParamObject(sql, "SupplierId", param.getSupplierId());
	 * SQLUtils.appendExtParamObject(sql, "PoId", param.getPoId());
	 * 
	 * if (!StringUtils.isBlank(param.getGoodsNo()))
	 * SQLUtils.appendExtParamObject(sql, "GoodsNo", param.getGoodsNo()); if
	 * (!StringUtils.isBlank(param.getProductName()))
	 * SQLUtils.appendExtParamObject(sql, "ProductName",
	 * param.getProductName());
	 * 
	 * long categoryId = param.getLowCategoryId(); if (categoryId > 0) {
	 * List<Category> retList = new ArrayList<Category>();
	 * categoryDao.getLowestCategoryById(retList, categoryId); if
	 * (retList.size() > 0) { sql.append(" AND LowCategoryId IN ("); for (int i
	 * = 0; i < retList.size(); i++) { Category c = retList.get(i);
	 * sql.append(c.getId()); if (i < retList.size() - 1) sql.append(" ,"); }
	 * sql.append(")"); } } return sql.toString(); }
	 */
	@Override
	public List<PoProduct> getProduct(long poId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return queryObjects(sql.toString());
	}

	@Override
	public PoProduct getPoProduct(long supplierId, long poId, String goodsNo, String colorNum) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SupplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "GoodsNo", goodsNo);
		SQLUtils.appendExtParamObject(sql, "ColorNum", colorNum);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return queryObject(sql.toString());
	}

	@Override
	public List<POProductDTO> getProductDTOListByPo(long poId) {
		String sql = "select t2.*, t1.Id as skuId, t1.BarCode, t1.sizeIndex, t1.SubmitTime as SkuSubmitTime, t1.Status as SkuStatus, "
				+ "t1.RejectReason as SkuRejectReason, t1.SkuNum,t1.SupplierSkuNum, t1.CTime as SkuCTime, t1.UTime as SkuUTime, t3.Value as Size "
				+ "from Mmall_ItemCenter_PoProduct t2, Mmall_ItemCenter_PoSku t1,Mmall_ItemCenter_SkuSpecMap t3 where "
				+ "t1.PoId = t2.PoId AND t2.PoId = t3.PoId AND t1.ProductId = t2.Id AND t1.Id = t3.SkuId AND t2.PoId = "
				+ poId + " AND t2.IsDelete = 0 AND t1.IsDelete = 0";
		List<POProductDTO> retList = new ArrayList<POProductDTO>();
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				PoProduct product = PrintDaoUtil.getObjectFromRs(rs, this);
				POProductDTO dto = new POProductDTO(product);
				int index = retList.indexOf(dto);
				PoSku sku = new PoSku();
				sku.setBarCode(rs.getString("BarCode"));
				sku.setBasePrice(rs.getBigDecimal("BasePrice"));
				sku.setCTime(rs.getLong("SkuCTime"));
				sku.setGoodsNo(rs.getString("GoodsNo"));
				sku.setId(rs.getLong("SkuId"));
				sku.setMarketPrice(rs.getBigDecimal("MarketPrice"));
				sku.setPoId(rs.getLong("PoId"));
				sku.setRejectReason(rs.getString("SkuRejectReason"));
				sku.setSalePrice(rs.getBigDecimal("SalePrice"));
				sku.setSizeIndex(rs.getInt("SizeIndex"));
				sku.setSkuNum(rs.getInt("SkuNum"));
				sku.setSupplierSkuNum(rs.getInt("SupplierSkuNum"));
				sku.setStatus(StatusType.APPROVAL.genEnumByIntValue(rs.getInt("SkuStatus")));
				sku.setSubmitTime(rs.getLong("SkuSubmitTime"));
				sku.setSupplierId(rs.getLong("SupplierId"));
				sku.setUTime(rs.getLong("SkuUTime"));
				sku.setProductId(rs.getLong("Id"));
				POSkuDTO skudto = new POSkuDTO(sku);
				skudto.setProductName(rs.getString("ProductName"));
				skudto.setProductLinkUrl("/detail?id=" + dto.getProductId());
				skudto.setColorName(rs.getString("ColorName"));
				skudto.setSize(rs.getString("Size"));
				skudto.setThumb(rs.getString("ShowPicPath"));
				skudto.setStatusName(sku.getStatus().getDesc());
				skudto.setBrandId(rs.getLong("BrandId"));
				if (index < 0) {
					List<POSkuDTO> skuList = new ArrayList<POSkuDTO>();
					skuList.add(skudto);
					dto.setSKUList(skuList);
					retList.add(dto);
				} else {
					POProductDTO exist = retList.get(index);
					List<POSkuDTO> skuList = (List<POSkuDTO>) exist.getSKUList();
					if (skuList.indexOf(skudto) < 0)
						skuList.add(skudto);
				}
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return retList;
	}

	@Override
	public List<PoProduct> getProductExceptPass(long poId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		sql.append(" AND Status != ").append(StatusType.APPROVAL.getIntValue());
		return queryObjects(sql.toString());
	}

	@Override
	public int getProductNumOfStatus(long poId, StatusType status) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genCountSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		sql.append(" AND Status = ").append(status.getIntValue());
		return super.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public int getProductNum(long poId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genCountSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return super.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public boolean updateProductStaus(StatusType status, String reason, String descp, List<Long> productIds) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET Status = ").append(status.getIntValue()).append(",");
		if (StringUtils.isBlank(reason)) {
			sql.append(" RejectReason = ").append("\'\'").append(",");
		} else {
			sql.append(" RejectReason = ").append("\'").append(reason).append("\'").append(",");
		}
		if (StringUtils.isBlank(descp)) {
			sql.append(" RejectDescp = ").append("\'\' ");
		} else {
			sql.append(" RejectDescp = ").append("\'").append(descp).append("\' ");
		}
		sql.append("WHERE Id IN (");
		for (int i = 0; i < productIds.size(); i++) {
			long pid = productIds.get(i);
			sql.append(pid);
			if (i < productIds.size() - 1)
				sql.append(" ,");
		}
		sql.append(")");
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public boolean submitProduct(long poId) {
		long submitTime = new Date().getTime();
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET Status = ").append(StatusType.PENDING.getIntValue());
		sql.append(", SubmitTime = ").append(submitTime);
		sql.append(" WHERE PoId = ").append(poId);
		sql.append(" AND Status != ").append(StatusType.APPROVAL.getIntValue());
		sql.append(" AND IsDelete = 0");
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	/*
	 * @Override public List<PoProduct> getProductByNameAndPO(String
	 * productName, String goodsNo, List<Long> poIds) { if (poIds == null ||
	 * poIds.size() == 0) return null; StringBuilder sql = new
	 * StringBuilder(64); sql.append(genSelectSql()); if
	 * (!StringUtils.isBlank(goodsNo)) SQLUtils.appendExtParamObject(sql,
	 * "GoodsNo", goodsNo); if (!StringUtils.isBlank(productName))
	 * SQLUtils.appendExtParamObject(sql, "ProductName", productName);
	 * 
	 * if (poIds != null && poIds.size() > 0) { sql.append(" AND PoId IN (");
	 * for (int i = 0; i < poIds.size(); i++) { long poId = poIds.get(i);
	 * sql.append(poId); if (i < poIds.size() - 1) sql.append(" ,"); }
	 * sql.append(")"); } return queryObjects(sql.toString()); }
	 */
	@Override
	public List<CategoryGroupDTO> getProductGroupByCategory(long poId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("SELECT COUNT(0) AS Total, LowCategoryId, CategoryIndex FROM " + getTableName() + " WHERE 1=1 ");
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		sql.append(" GROUP BY LowCategoryId");
		DBResource dbResource = getSqlSupport().excuteQuery(sql.toString());
		java.sql.ResultSet rs = dbResource.getResultSet();
		List<CategoryGroupDTO> retList = new ArrayList<CategoryGroupDTO>();
		try {
			while (rs.next()) {
				CategoryGroupDTO category = new CategoryGroupDTO();
				category.setCategoryIndex(rs.getInt("CategoryIndex"));
				category.setLowCategoryId(rs.getInt("LowCategoryId"));
				category.setTotal(rs.getInt("Total"));
				retList.add(category);
			}
			rs.close();
			dbResource.close();
		} catch (SQLException e) {
			try {
				rs.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dbResource.close();
			e.printStackTrace();
		} finally {
			dbResource.close();
		}
		return retList;
	}

	@Override
	public boolean updateProductCategorySort(long poId, long categoryId, int index) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET CategoryIndex = ").append(index);
		sql.append(" WHERE 1 = 1 ");
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		SQLUtils.appendExtParamObject(sql, "LowCategoryId", categoryId);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public boolean updateProductSingleSort(long poId, long pid, int index) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET SingleIndex = ").append(index);
		sql.append(" WHERE 1 = 1 ");
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "Id", pid);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public boolean resetProductSingleSort(long poId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET SingleIndex = 0");
		sql.append(" WHERE 1 = 1 ");
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public List<PoProduct> getPassProduct(long poId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		SQLUtils.appendExtParamObject(sql, "Status", StatusType.APPROVAL);
		return queryObjects(sql.toString());
	}

	@Override
	public List<PoProduct> getListBySizeAssistId(long supplierId, long helpId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SupplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "SizeAssistId", helpId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return queryObjects(sql.toString());
	}

	@Cacheable(value = "searchProdByPO")
	@Override
	public PoProduct getPoProductByPoIdAndProduct(long productId, long poId) {
		return super.queryObject(GET_POPRODUCT_BYPOID_PODUCTID, productId, poId);
	}

	@Override
	public long getCategoryIdBySkuId(long skuId) {
		String sql = "select t1.LowCategoryId from Mmall_ItemCenter_PoProduct t1,Mmall_ItemCenter_PoSku t2 where "
				+ "t1.PoId = t2.PoId AND t1.Id = t2.ProductId AND t2.Id = " + skuId;
		List<POProductDTO> retList = new ArrayList<POProductDTO>();
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		long categoryId = 0;
		try {
			while (rs.next()) {
				categoryId = rs.getLong("LowCategoryId");
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return categoryId;
	}

	@Override
	public List<PoProduct> getPoProductByName(String productName) {
		return super.queryObjects(GET_POPRODUCT_BYPOID_PODUCTNAME, productName);
	}

	@Override
	public List<PoProduct> getPoProductByGoodsNo(String goodsNo) {
		return super.queryObjects(GET_POPRODUCT_BY_GOODSNO, goodsNo);
	}

	@Cacheable(value = "searchProdByPO")
	@Override
	public POSkuDTO getPoSkuDTOByIdCache(Long skuId) {
		if (skuId == null) {
			return null;
		}

		PoSku poSkuRes = poSkuDao.getPoSkuById(skuId);
		if (poSkuRes == null) {
			return null;
		}

		POSkuDTO dto = new POSkuDTO(poSkuRes);
		PoProduct poProduct = this.getPoProductByPoIdAndProduct(poSkuRes.getProductId(), poSkuRes.getPoId());
		SkuSpecMap skuSpecMap = skuSpecMapDao.getSkuSpecMapByPoIdProductIdSkuId(poSkuRes.getPoId(),
				poSkuRes.getProductId(), poSkuRes.getId());

		dto.setProductName(poProduct.getProductName());
		dto.setProductLinkUrl("/detail?id=" + poProduct.getId());
		dto.setColorName(poProduct.getColorName());
		if (skuSpecMap != null) {
			dto.setSize(skuSpecMap.getValue());
		}
		dto.setThumb(poProduct.getShowPicPath());
		dto.setStatusName(poSkuRes.getStatus().getDesc());
		dto.setBrandId(poProduct.getBrandId());
		return dto;
	}

	@Override
	public List<PoProductVo> getPoProductosByParam(PoProductSo so) {
		StringBuilder builder = new StringBuilder(64);
		builder.append(SEARCH_POPODUCTVO);

		List<PoProductVo> poProductVoList = new ArrayList<PoProductVo>();
		List<Object> paramList = new ArrayList<Object>();

		// 用户权限条件过滤
		if (!StringUtils.isEmpty(so.getSiteFlagsUserAuth())) {
			builder.append(" and b.SaleSiteFlag&?=b.SaleSiteFlag");
			paramList.add(so.getSiteFlagsUserAuth());
		}

		// 用户搜索的站点过滤
		if (!StringUtils.isEmpty(so.getSiteFlagsUserSelect())) {
			builder.append(" and b.SaleSiteFlag&?>0");
			paramList.add(so.getSiteFlagsUserSelect());
		}

		if (so.getPoId() != null) {
			builder.append(" and a.PoId=?");
			paramList.add(so.getPoId());
		}

		if (so.getSupplierId() != null) {
			builder.append(" and b.SupplierId=?");
			paramList.add(so.getSupplierId());
		}

		if (so.getBrandId() != null) {
			builder.append(" and b.BrandId=?");
			paramList.add(so.getBrandId());
		}

		// 条形码转成商品id条件
		if (so.getProductId() != null) {
			builder.append(" and a.id=?");
			paramList.add(so.getProductId());
		}

		if (so.getProductIdList() != null && so.getProductIdList().size() > 0) {
			builder.append(" and a.id in(");
			for (int i = 0; i < so.getProductIdList().size(); i++) {
				if (i > 0) {
					builder.append(",");
				}
				builder.append("?");
				paramList.add(so.getProductIdList().get(i));
			}
			builder.append(")");
		}

		// goodsNo得到的productId列表
		if (so.getProductIdListFromGoodsNo() != null && so.getProductIdListFromGoodsNo().size() > 0) {
			builder.append(" and a.id in(");
			for (int i = 0; i < so.getProductIdListFromGoodsNo().size(); i++) {
				if (i > 0) {
					builder.append(",");
				}
				builder.append("?");
				paramList.add(so.getProductIdListFromGoodsNo().get(i));
			}
			builder.append(")");
		}

		if (so.getStime() != null && so.getEtime() != null) {
			builder.append(" and a.SubmitTime>=? and a.SubmitTime<=?");
			paramList.add(so.getStime());
			paramList.add(so.getEtime());
		}

		if (so.getStatus() != null) {
			builder.append(" and a.Status=?");
			paramList.add(so.getStatus());
			if (StatusType.PENDING.getIntValue() == so.getStatus() || StatusType.REJECT.getIntValue() == so.getStatus()) {
				// 待审核或审核被拒绝需要加上档期未开启的条件
				builder.append(" and b.startTime>" + new Date().getTime());
			}
		}

		if (so.getLimit() != null && so.getOffset() != null) {
			builder.append(" limit ? offset ?");
			paramList.add(so.getLimit());
			paramList.add(so.getOffset());
		}

		DBResource dbr = this.getSqlSupport().excuteQuery(builder.toString(), paramList);
		ResultSet rs = dbr.getResultSet();

		try {
			while (rs.next()) {
				PoProductVo poProductVo = new PoProductVo();
				poProductVo.setId(rs.getLong("id"));
				// 原商品id
				poProductVo.setProductId(rs.getLong("productId"));
				poProductVo.setPoId(rs.getLong("poId"));
				poProductVo.setLowCategoryId(rs.getLong("lowCategoryId"));
				poProductVo.setSupplierId(rs.getLong("supplierId"));
				poProductVo.setGoodsNo(rs.getString("goodsNo"));
				poProductVo.setStatus(StatusType.APPROVAL.genEnumByIntValue(rs.getInt("Status")));
				poProductVo.setRejectReason(rs.getString("rejectReason"));
				poProductVo.setProductName(rs.getString("productName"));
				poProductVo.setColorNum(rs.getString("colorNum"));
				poProductVo.setColorName(rs.getString("colorName"));
				poProductVo.setSizeType(SizeType.CUST_SIZE.genEnumByIntValue(rs.getInt("sizeType")));
				poProductVo.setSizeTemplateId(rs.getLong("sizeTemplateId"));
				poProductVo.setSizeAssistId(rs.getLong("sizeAssistId"));
				poProductVo.setIsShowSizePic(rs.getBoolean("isShowSizePic"));
				poProductVo.setMarketPrice(rs.getBigDecimal("marketPrice"));
				poProductVo.setSalePrice(rs.getBigDecimal("salePrice"));
				poProductVo.setBasePrice(rs.getBigDecimal("basePrice"));
				poProductVo.setAddTime(rs.getLong("addTime"));
				poProductVo.setuTime(rs.getLong("uTime"));
				poProductVo.setSubmitTime(rs.getLong("submitTime"));
				poProductVo.setShowPicPath(rs.getString("showPicPath"));

				poProductVo.setBrandId(rs.getLong("brandId"));
				poProductVo.setStartTime(rs.getLong("startTime"));
				poProductVo.setEndTime(rs.getLong("endTime"));
				poProductVo.setBrandName(rs.getString("brandName"));
				poProductVo.setBrandNameEn(rs.getString("brandNameEn"));
				poProductVo.setSaleSiteFlag(rs.getLong("saleSiteFlag"));
				poProductVoList.add(poProductVo);
			}
		} catch (SQLException e) {
			logger.error("getPoProductosByParam err", e);
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return poProductVoList;
	}

	@Override
	public Long getPoProductVosCountByParam(PoProductSo so) {
		StringBuilder builder = new StringBuilder(64);
		builder.append(SEARCH_POPRODUCTVO_COUNT);

		List<Object> paramList = new ArrayList<Object>();

		// 条形码转成商品id条件
		if (so.getProductId() != null) {
			builder.append(" and a.id=?");
			paramList.add(so.getProductId());
		}

		// 用户权限条件过滤
		if (!StringUtils.isEmpty(so.getSiteFlagsUserAuth())) {
			builder.append(" and b.SaleSiteFlag&?=b.SaleSiteFlag");
			paramList.add(so.getSiteFlagsUserAuth());
		}

		// 用户搜索的站点过滤
		if (!StringUtils.isEmpty(so.getSiteFlagsUserSelect())) {
			builder.append(" and b.SaleSiteFlag&?>0");
			paramList.add(so.getSiteFlagsUserSelect());
		}

		if (so.getPoId() != null) {
			builder.append(" and a.PoId=?");
			paramList.add(so.getPoId());
		}

		if (so.getSupplierId() != null) {
			builder.append(" and b.SupplierId=?");
			paramList.add(so.getSupplierId());
		}

		if (so.getBrandId() != null) {
			builder.append(" and b.BrandId=?");
			paramList.add(so.getBrandId());
		}

		if (so.getProductIdList() != null && so.getProductIdList().size() > 0) {
			builder.append(" and a.id in(");
			for (int i = 0; i < so.getProductIdList().size(); i++) {
				if (i > 0) {
					builder.append(",");
				}
				builder.append("?");
				paramList.add(so.getProductIdList().get(i));
			}
			builder.append(")");
		}

		// goodsNo得到的productId列表
		if (so.getProductIdListFromGoodsNo() != null && so.getProductIdListFromGoodsNo().size() > 0) {
			builder.append(" and a.id in(");
			for (int i = 0; i < so.getProductIdListFromGoodsNo().size(); i++) {
				if (i > 0) {
					builder.append(",");
				}
				builder.append("?");
				paramList.add(so.getProductIdListFromGoodsNo().get(i));
			}
			builder.append(")");
		}

		if (so.getStime() != null && so.getEtime() != null) {
			builder.append(" and a.SubmitTime>=? and a.SubmitTime<=?");
			paramList.add(so.getStime());
			paramList.add(so.getEtime());
		}

		if (so.getStatus() != null) {
			builder.append(" and a.Status=?");
			paramList.add(so.getStatus());
			if (StatusType.PENDING.getIntValue() == so.getStatus() || StatusType.REJECT.getIntValue() == so.getStatus()) {
				// 待审核或审核被拒绝需要加上档期未开启的条件，已经开启的档期数据不显示
				builder.append(" and b.startTime>" + new Date().getTime());
			}
		}

		DBResource dbResource = null;
		try {
			dbResource = super.getSqlSupport().excuteQuery(builder.toString(), paramList);
			ResultSet rs = dbResource.getResultSet();
			if (rs.next()) {
				return rs.getLong("cnt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbResource != null) {
				dbResource.close();
			}
		}
		return null;
	}

	@Override
	public int getProductCount(long poId, int sameAsShop) {
		StringBuilder sql = new StringBuilder(82);
		sql.append(genCountSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		if (sameAsShop != -1) {
			SQLUtils.appendExtParamObject(sql, "SameAsShop", sameAsShop);
		}
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return super.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public boolean setProductDeleteFlag(long poId, long pid, int flag) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET IsDelete = ").append(flag);
		sql.append(" WHERE 1 = 1 ");
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "Id", pid);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public boolean productOnline(long poId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET IsOnline = 1");
		sql.append(" WHERE 1 = 1 ");
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public List<POProductDTO> getProductDTOListByCategory(long categoryId) {
		String sql = "select t2.*, t1.Id as skuId, t1.BarCode, t1.sizeIndex, t1.SubmitTime as SkuSubmitTime, t1.Status as SkuStatus, "
				+ "t1.RejectReason as SkuRejectReason, t1.SkuNum,t1.SupplierSkuNum, t1.CTime as SkuCTime, t1.UTime as SkuUTime, t3.Value as Size "
				+ "from Mmall_ItemCenter_PoProduct t2, Mmall_ItemCenter_PoSku t1,Mmall_ItemCenter_SkuSpecMap t3 where "
				+ "t1.PoId = t2.PoId AND t2.PoId = t3.PoId AND t1.ProductId = t2.Id AND t1.Id = t3.SkuId AND t2.LowCategoryId = "
				+ categoryId + " AND t2.IsDelete = 0 AND t1.IsDelete = 0";
		List<POProductDTO> retList = new ArrayList<POProductDTO>();
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				PoProduct product = PrintDaoUtil.getObjectFromRs(rs, this);
				POProductDTO dto = new POProductDTO(product);
				int index = retList.indexOf(dto);
				PoSku sku = new PoSku();
				sku.setBarCode(rs.getString("BarCode"));
				sku.setBasePrice(rs.getBigDecimal("BasePrice"));
				sku.setCTime(rs.getLong("SkuCTime"));
				sku.setGoodsNo(rs.getString("GoodsNo"));
				sku.setId(rs.getLong("SkuId"));
				sku.setMarketPrice(rs.getBigDecimal("MarketPrice"));
				sku.setPoId(rs.getLong("PoId"));
				sku.setRejectReason(rs.getString("SkuRejectReason"));
				sku.setSalePrice(rs.getBigDecimal("SalePrice"));
				sku.setSizeIndex(rs.getInt("SizeIndex"));
				sku.setSkuNum(rs.getInt("SkuNum"));
				sku.setSupplierSkuNum(rs.getInt("SupplierSkuNum"));
				sku.setStatus(StatusType.APPROVAL.genEnumByIntValue(rs.getInt("SkuStatus")));
				sku.setSubmitTime(rs.getLong("SkuSubmitTime"));
				sku.setSupplierId(rs.getLong("SupplierId"));
				sku.setUTime(rs.getLong("SkuUTime"));
				sku.setProductId(rs.getLong("Id"));
				POSkuDTO skudto = new POSkuDTO(sku);
				skudto.setProductName(rs.getString("ProductName"));
				skudto.setProductLinkUrl("/detail?id=" + dto.getProductId());
				skudto.setColorName(rs.getString("ColorName"));
				skudto.setSize(rs.getString("Size"));
				skudto.setThumb(rs.getString("ShowPicPath"));
				skudto.setStatusName(sku.getStatus().getDesc());
				skudto.setBrandId(rs.getLong("BrandId"));
				if (index < 0) {
					List<POSkuDTO> skuList = new ArrayList<POSkuDTO>();
					skuList.add(skudto);
					dto.setSKUList(skuList);
					retList.add(dto);
				} else {
					POProductDTO exist = retList.get(index);
					List<POSkuDTO> skuList = (List<POSkuDTO>) exist.getSKUList();
					if (skuList.indexOf(skudto) < 0)
						skuList.add(skudto);
				}
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return retList;
	}
}
