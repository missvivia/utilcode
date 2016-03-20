package com.xyl.mmall.itemcenter.dao.productsku;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.netease.dbsupport.DBResource;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.util.PrintDaoUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.framework.util.SqlDealUtil;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.meta.ItemSPU;
import com.xyl.mmall.itemcenter.meta.ProductSKU;
import com.xyl.mmall.itemcenter.param.ProductSKUSearchParam;
import com.xyl.mmall.itemcenter.param.ProductSearchMainSiteParam;

@Repository
public class ProductSKUDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ProductSKU> implements ProductSKUDao {

	private static Logger logger = LoggerFactory.getLogger(ProductSKUDaoImpl.class);

	@Override
	public List<ProductSKUDTO> searchProductSKU(ProductSKUSearchParam param) {
		StringBuilder sb = new StringBuilder(512);
		if (param.getSearchType() == 2) {// 在mainstite店铺中搜索商品
			sb.append("select sku.*,price from Mmall_ItemCenter_ProductSKU sku,Mmall_ItemCenter_ProductPrice where sku.Id = productId  ");
		} else {
			sb.append("select sku.*,StockCount from Mmall_ItemCenter_ProductSKU sku,Mmall_Order_SkuOrderStock stock where sku.Id = stock.skuId  ");
		}
		sb.append(genWhereSql(param));
		if (StringUtils.isBlank(param.getOrderColumn())) {
			this.appendOrderSql(sb, "sku.updateTime", false);
		} else if (param.getOrderColumn().equals("price")) {
			this.appendOrderSql(sb, param.getOrderColumn(), param.isAsc());
		} else {
			this.appendOrderSql(sb, "sku." + param.getOrderColumn(), param.isAsc());

		}
		this.appendLimitSql(sb, param.getLimit(), param.getOffset());
		DBResource dbr = this.getSqlSupport().excuteQuery(sb.toString());
		ResultSet rs = dbr.getResultSet();
		List<ProductSKUDTO> productSKUDTOs = new ArrayList<ProductSKUDTO>();
		try {
			while (rs.next()) {
				ProductSKU product = PrintDaoUtil.getObjectFromRs(rs, this);
				ProductSKUDTO productSKUDTO = new ProductSKUDTO(product);
				if (param.getSearchType() != 2) {
					productSKUDTO.setSkuNum(rs.getInt("StockCount"));
				}
				productSKUDTOs.add(productSKUDTO);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			dbr.close();
		}
		return productSKUDTOs;
	}

	@Override
	public int countProductSKUDTOBySearchParam(ProductSKUSearchParam param) {
		StringBuilder sql = new StringBuilder(256);
		if (param.getSearchType() == 2) {// 在mainstite店铺中搜索商品
			sql.append("select count( distinct sku.id) from Mmall_ItemCenter_ProductSKU sku,Mmall_ItemCenter_ProductPrice where sku.Id = productId  ");
		} else {
			sql.append("select count(sku.id) from Mmall_ItemCenter_ProductSKU sku,Mmall_Order_SkuOrderStock stock where sku.Id = stock.skuId  ");
		}
		sql.append(genWhereSql(param));
		return this.getSqlSupport().queryCount(sql.toString());
	}

	private String genWhereSql(ProductSKUSearchParam param) {
		StringBuilder sql = new StringBuilder(256);
		if (param.getBusinessId() > 0) {
			SQLUtils.appendExtParamObject(sql, "BusinessId", param.getBusinessId());
		}
		if (!StringUtils.isBlank(param.getGoodsNo())) {
			SQLUtils.appendExtParamObject(sql, "sku.Id", param.getGoodsNo());
		}
		// 1库存不足 2库存足
		if (param.getStockStatus() == 1) {
			sql.append(" AND SKUAttention>=StockCount");
		} else if (param.getStockStatus() == 2) {
			sql.append(" AND SKUAttention<StockCount");
		}
		if (StringUtils.isNotEmpty(param.getProductName())) {
			sql.append(" AND Name LIKE '%").append(SqlDealUtil.escapeSpecialChars(param.getProductName())).append("%'");
		}
		// 商品状态
		if (param.getStatus() > 0) {
			sql.append(" AND Status = ").append(param.getStatus());
		}
		if (param.getStime() > 0) {
			sql.append(" AND sku.UpdateTime >= '").append(
					DateUtil.dateToString(new Date(param.getStime()), DateUtil.LONG_PATTERN) + "'");
		}
		if (param.getEtime() > 0) {
			sql.append(" AND sku.UpdateTime <= '").append(
					DateUtil.dateToString(new Date(param.getEtime()), DateUtil.LONG_PATTERN) + "'");
		}
		if (param.getSearchType() == 2 && param.getSprice() != null) {
			sql.append(" AND price >= ").append(param.getSprice().doubleValue());
		}
		if (param.getSearchType() == 2 && param.getEprice() != null) {
			sql.append(" AND price <= ").append(param.getEprice().doubleValue());
		}
		if (CollectionUtil.isNotEmptyOfList(param.getBrandIdList())) {
			SqlGenUtil.appendExtParamColl(sql, "brandId", param.getBrandIdList());
		}
		if (CollectionUtil.isNotEmptyOfList(param.getSpuIdList())) {
			SqlGenUtil.appendExtParamColl(sql, "SPUId", param.getSpuIdList());
		}
		if (CollectionUtil.isNotEmptyOfList(param.getSkuIdList())) {
			SqlGenUtil.appendExtParamColl(sql, "sku.Id", param.getSkuIdList());
		}
		if (StringUtils.isNotEmpty(param.getCategoryNormalIds())) {
			sql.append(" AND CategoryNormalId in (").append(param.getCategoryNormalIds()).append(")");
		}
		if (param.getIsLimited() >= 0) {
			sql.append(" AND IsLimited = ").append(param.getIsLimited());
		}
		return sql.toString();
	}

	@Override
	public long addProductSKU(ProductSKU productSKU) {
		long id = allocateRecordId();
		productSKU.setId(id);
		return addObject(productSKU) == null ? -1l : id;
	}

	@Override
	public ProductSKU getProductSKU(ProductSKU sku) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "Id", sku.getId());
		if (sku.getBusinessId() > 0l) {
			SQLUtils.appendExtParamObject(sql, "BusinessId", sku.getBusinessId());
		}
		if (sku.getStatus() > 0) {
			SQLUtils.appendExtParamObject(sql, "Status", sku.getStatus());
		}
		return queryObject(sql.toString());
	}

	@Override
	public boolean batchDeleteProductSKU(List<Long> proIds, long businessId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "BusinessId", businessId);
		SqlGenUtil.appendExtParamColl(sql, "Id", proIds);
		return this.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public boolean deleteProductBybusiIdAndProId(long businessId, long proId) {
		String sql = "DELETE FROM Mmall_ItemCenter_ProductSKU WHERE Id = ? AND BusinessId=?";
		return this.getSqlSupport().updateRecord(sql, proId, businessId);
	}

	@Override
	public boolean updateProductSKUStatus(long prodId, ProductStatusType statusType, long modifyUserId) {
		String sqlUpdate = "UPDATE Mmall_ItemCenter_ProductSKU set Status = ?,UpdateBy = ? WHERE Id = ?  ";
		return this.getSqlSupport().updateRecord(sqlUpdate, statusType.getIntValue(), modifyUserId, prodId);
	}

	@Override
	public boolean updateProductSKUStatusByBusinessId(long businessId, ProductStatusType statusType, long modifyUserId) {
		String sqlUpdate = "UPDATE Mmall_ItemCenter_ProductSKU set Status = ?,UpdateBy = ? WHERE BusinessId = ?  ";
		return this.getSqlSupport().updateRecord(sqlUpdate, statusType.getIntValue(), modifyUserId, businessId);
	}

	@Override
	public boolean batchUpdateProductSKUStatus(List<Long> prodIds, ProductStatusType statusType, long modifyUserId) {
		if (CollectionUtil.isEmptyOfList(prodIds)) {
			return false;
		}
		StringBuilder sqlUpdate = new StringBuilder(256);
		sqlUpdate.append("UPDATE Mmall_ItemCenter_ProductSKU set Status = ").append(statusType.getIntValue())
				.append(", UpdateBy = ").append(modifyUserId).append(" Where id in ");
		sqlUpdate.append("(");
		for (Long id : prodIds) {
			sqlUpdate.append(id).append(",");
		}
		sqlUpdate.deleteCharAt(sqlUpdate.lastIndexOf(","));
		sqlUpdate.append(") ");
		return this.getSqlSupport().excuteUpdate(sqlUpdate.toString()) > 0;
	}

	@Override
	public List<ProductSKU> getProductSKUListByIds(List<Long> ids) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sb, "Id", ids);
		return queryObjects(sb);
	}

	@Override
	public List<ProductSKU> getProductBusinessIdsByIds(List<String> ids) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("SELECT BusinessId, id FROM Mmall_ItemCenter_ProductSKU WHERE id IN (");
		for (String id : ids) {
			sb.append(id).append(",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(");");
		DBResource dbResource = this.getSqlSupport().excuteQuery(sb.toString());
		ResultSet rs = (null == dbResource) ? null : dbResource.getResultSet();
		if (null == rs) {
			return null;
		}
		try {
			List<ProductSKU> result = new ArrayList<ProductSKU>();
			while (rs.next()) {
				ProductSKU sku = new ProductSKU();
				sku.setId(rs.getLong("id"));
				sku.setBusinessId(rs.getLong("businessId"));
				result.add(sku);
			}
			rs.close();
			return result;
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			dbResource.close();
		}
		return null;
	}

	@Override
	public Map<ProductStatusType, Integer> countProductSKUByBusinessId(long businessId) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("select Status,count(id) as Count from Mmall_ItemCenter_ProductSKU WHERE 1=1 ");
		SqlGenUtil.appendExtParamObject(sb, "BusinessId", businessId);
		sb.append(" group by Status");
		DBResource dbr = this.getSqlSupport().excuteQuery(sb.toString());
		ResultSet rs = dbr.getResultSet();
		Map<ProductStatusType, Integer> map = new HashMap<ProductStatusType, Integer>();
		if (null == rs) {
			return map;
		}
		try {
			while (rs.next()) {
				map.put(ProductStatusType.getEnumByIntValue(rs.getInt("Status")), rs.getInt("Count"));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			dbr.close();
		}
		return map;
	}

	@Override
	public List<ProductSKU> getProductSKUList(List<Long> prodIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "id", prodIds);
		sql.append(" order by BusinessId");
		return queryObjects(sql.toString());
	}

	@Override
	public int updateProductSKU(ProductSKU sku) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET UpdateBy = ").append(sku.getUpdateBy());
		sql.append(", IsLimited = ").append(sku.getIsLimited());
		if (StringUtils.isNotBlank(sku.getInnerCode())) {
			sql.append(", InnerCode = '").append(sku.getInnerCode()).append("'");
		}
		if (sku.getTitle() != null) {
			sql.append(", Title = '").append(sku.getTitle()).append("'");
		}
		if (StringUtils.isNotBlank(sku.getUnit())) {
			sql.append(", Unit = '").append(sku.getUnit()).append("'");
		}
		if (StringUtils.isNotBlank(sku.getExpire())) {
			sql.append(", Expire = '").append(sku.getExpire()).append("'");
		}
		if (sku.getProduceDate() != null) {
			sql.append(", ProduceDate = '").append(DateUtil.dateToString(sku.getProduceDate(), DateUtil.LONG_PATTERN))
					.append("'");
		}
		if (sku.getCanReturn() > 0) {
			sql.append(", CanReturn = ").append(sku.getCanReturn());
		}
		if (sku.getSalePrice() != null) {
			sql.append(", SalePrice = '").append(sku.getSalePrice().doubleValue()).append("'");
		}
		if (sku.getSkuNum() >= 0) {
			sql.append(", SKUNum = ").append(sku.getSkuNum());
		}
		if (sku.getBatchNum() >= 0) {
			sql.append(", BatchNum = ").append(sku.getBatchNum());
		}
		if (sku.getSkuAttention() >= 0) {
			sql.append(", SKUAttention = ").append(sku.getSkuAttention());
		}
		if (StringUtils.isNotBlank(sku.getName())) {
			sql.append(", Name = '").append(sku.getName()).append("'");
		}
		sql.append(" WHERE ");
		SQLUtils.appendExtParamObject(sql, "Id", sku.getId());
		SQLUtils.appendExtParamObject(sql, "BusinessId", sku.getBusinessId());
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public int countProductSKUBySPUId(long spuId) {
		StringBuilder sql = new StringBuilder(genCountSql());
		SQLUtils.appendExtParamObject(sql, "SPUId", spuId);
		return this.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public BasePageParamVO<ProductSKUDTO> getProductSKUList(BasePageParamVO<ProductSKUDTO> basePageParamVO,
			ProductSearchMainSiteParam searchParam, List<Long> skuIds) {
		if (CollectionUtils.isEmpty(searchParam.getBusinessIds())) {
			return basePageParamVO;
		}
		StringBuilder sql = new StringBuilder(genSelectSql());
		searchSQL(searchParam, skuIds, sql);
		String[] columns = new String[2];
		boolean[] isASC = new boolean[2];
		if (StringUtils.isBlank(searchParam.getSortColumn())
				|| StringUtils.equals("SaleNum", searchParam.getSortColumn())) {
			columns[0] = "SaleNum";
			isASC[0] = false;
			columns[1] = "UpdateTime";
			isASC[1] = false;
		} else {
			columns[0] = searchParam.getSortColumn();
			isASC[0] = searchParam.getIsAsc();
			columns[1] = "SaleNum";
			isASC[1] = false;
		}
		try {
			SQLUtils.appendExtOrderByColumns(sql, columns, isASC);
		} catch (SQLException e) {
			logger.error("Get productSKUList error! Append sql order by erro!", e);
		}
		if (basePageParamVO.getIsPage() == 1) {
			int count = countProductSKUList(searchParam, skuIds);
			if (count < 1) {
				return basePageParamVO;
			}
			basePageParamVO.setTotal(count);
			SQLUtils.getPageSql(sql, basePageParamVO);
		}
		List<ProductSKU> skuList = queryObjects(sql.toString());
		if (CollectionUtils.isEmpty(skuList)) {
			return basePageParamVO;
		}
		List<ProductSKUDTO> skuDTOList = new ArrayList<ProductSKUDTO>(skuList.size());
		for (ProductSKU sku : skuList) {
			skuDTOList.add(new ProductSKUDTO(sku));
		}
		basePageParamVO.setList(skuDTOList);
		return basePageParamVO;
	}

	public int countProductSKUList(ProductSearchMainSiteParam searchParam, List<Long> skuIds) {
		StringBuilder sql = new StringBuilder(genCountSql());
		searchSQL(searchParam, skuIds, sql);
		return this.getSqlSupport().queryCount(sql.toString());
	}

	private void searchSQL(ProductSearchMainSiteParam searchParam, List<Long> skuIds, StringBuilder sql) {
		if (!CollectionUtils.isEmpty(searchParam.getCategoryIds())) {
			String categoryIdStr = searchParam.getCategoryIds().toString().replace("[", "(").replace("]", ")");
			sql.append(" AND CategoryNormalId IN ").append(categoryIdStr);
		}
		String businessIdStr = searchParam.getBusinessIds().toString().replace("[", "(").replace("]", ")");
		sql.append(" AND BusinessId IN ").append(businessIdStr);
		if (!CollectionUtils.isEmpty(searchParam.getBrandIds())) {
			String brandIdStr = searchParam.getBrandIds().toString().replace("[", "(").replace("]", ")");
			sql.append(" AND BrandId IN ").append(brandIdStr);
		}
		if (!CollectionUtils.isEmpty(skuIds)) {
			String idStr = skuIds.toString().replace("[", "(").replace("]", ")");
			sql.append(" AND Id IN ").append(idStr);
		}
		if (StringUtils.isNotBlank(searchParam.getSearchValue())) {
			String searchValue = searchParam.getSearchValue().replaceAll("'", "\\\\'");
			sql.append(" AND Name LIKE '%").append(searchValue).append("%'");
		}
		SQLUtils.appendExtParamObject(sql, "Status", ProductStatusType.ONLINE);
	}

	@Override
	public List<Long> getCategoryNormalIdsByBusinessId(long businessId) {
		String sql = "select CategoryNormalId from Mmall_ItemCenter_ProductSKU where businessId = ? ";
		DBResource dbr = this.getSqlSupport().excuteQuery(sql, businessId);
		ResultSet rs = dbr.getResultSet();
		List<Long> categoryNormalIds = new ArrayList<Long>();
		if (null == rs) {
			return categoryNormalIds;
		}
		try {
			while (rs.next()) {
				categoryNormalIds.add(rs.getLong("CategoryNormalId"));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			dbr.close();
		}
		return categoryNormalIds;
	}

	@Override
	public boolean isProductStatusOnline(long productId) {
		StringBuilder sql = new StringBuilder(genCountSql());
		SQLUtils.appendExtParamObject(sql, "Id", productId);
		SQLUtils.appendExtParamObject(sql, "Status", ProductStatusType.ONLINE);
		return this.getSqlSupport().queryCount(sql.toString()) > 0;
	}

	@Override
	public Map<Long, Boolean> getProductStatusIsOnline(List<Long> productIds) {
		Map<Long, Boolean> resultMap = new HashMap<Long, Boolean>();
		int length = productIds.size();
		StringBuffer sql = new StringBuffer();
		sql.append("select Id,Status from Mmall_ItemCenter_ProductSKU  ");
		sql.append("WHERE Id IN (");
		for (int i = 0; i < length; i++) {
			sql.append(productIds.get(i));
			if (i < length - 1) {
				sql.append(" ,");
			}
		}
		sql.append(")");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		if (null == rs) {
			return resultMap;
		}
		try {
			while (rs.next()) {
				resultMap.put(rs.getLong("Id"), ProductStatusType.ONLINE.getIntValue() == rs.getInt("Status"));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			dbr.close();
		}
		return resultMap;
	}

	@Override
	public List<Long> getProductIdsByBusinessId(long businessId) {
		String sql = "select Id from Mmall_ItemCenter_ProductSKU sku where businessId = ? ";
		DBResource dbr = this.getSqlSupport().excuteQuery(sql, businessId);
		ResultSet rs = dbr.getResultSet();
		List<Long> productIds = new ArrayList<Long>();
		if (null == rs) {
			return productIds;
		}
		try {
			while (rs.next()) {
				productIds.add(rs.getLong("Id"));
			}
		} catch (SQLException e) {
			logger.error("Get productSKUIds error! BusinessId : " + businessId + ".", e);
		} finally {
			dbr.close();
		}
		return productIds;
	}

	@Override
	public int updateProductSKUSaleNum(long skuId, int increment) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE ").append(this.getTableName()).append(" SET SaleNum = SaleNum + ").append(increment)
				.append(" WHERE Id = ").append(skuId);
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public List<Long> getBrandIdsByCategoryIds(List<Long> categoryNormalIds, String searchValue, Set<Long> businessIds) {
		if (CollectionUtils.isEmpty(businessIds)) {
			return new ArrayList<Long>(0);
		}
		StringBuilder sql = new StringBuilder(
				"SELECT MAX(SaleNum) s, BrandId FROM Mmall_ItemCenter_ProductSKU WHERE 1 = 1 ");
		SqlGenUtil.appendExtParamColl(sql, "BusinessId", businessIds);
		if (!CollectionUtils.isEmpty(categoryNormalIds)) {
			SqlGenUtil.appendExtParamColl(sql, "CategoryNormalId", categoryNormalIds);
		}
		if (StringUtils.isNotBlank(searchValue)) {
			searchValue = searchValue.replaceAll("'", "\\\\'");
			sql.append(" AND Name like '%").append(searchValue).append("%'");
		}
		SQLUtils.appendExtParamObject(sql, "Status", ProductStatusType.ONLINE);
		sql.append(" GROUP BY BrandId").append(" ORDER BY s DESC");
		DBResource dbResource = this.getSqlSupport().excuteQuery(sql.toString());
		List<Long> brandIds = new ArrayList<Long>();
		ResultSet rs = dbResource.getResultSet();
		if (null == rs) {
			return brandIds;
		}
		try {
			while (rs.next()) {
				brandIds.add(rs.getLong(2));
			}
		} catch (Exception e) {
			logger.error("Get brandIds by categoryIds error!", e);
		} finally {
			dbResource.close();
		}
		return brandIds;
	}

	@Override
	public List<ProductSKU> getProductSKUListBySpuIds(List<Long> spuIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "spuid", spuIds);
		sql.append(" order by BusinessId");
		return queryObjects(sql.toString());
	}

	@Override
	public int countSyncSKUBySPU(ItemSPU spu) {
		StringBuilder sql = new StringBuilder(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "SPUId", spu.getId());
		sql.append(" AND (BrandId != ").append(spu.getBrandId());
		sql.append(" OR CategoryNormalId != ").append(spu.getCategoryNormalId()).append(")");
		return this.getSqlSupport().queryCount(sql.toString());
	}
	
	@Override
	public List<Long> getSyncSKUIdBySPU(ItemSPU spu) {
		StringBuilder sql = new StringBuilder("SELECT Id FROM ");
		sql.append(this.getTableName()).append(" WHERE 1 = 1 ");
		SqlGenUtil.appendExtParamObject(sql, "SPUId", spu.getId());
		sql.append(" AND (BrandId != ").append(spu.getBrandId());
		sql.append(" OR CategoryNormalId != ").append(spu.getCategoryNormalId()).append(")");
		DBResource dbResource = this.getSqlSupport().excuteQuery(sql.toString());
		List<Long> skuIds = new ArrayList<Long>();
		ResultSet rs = dbResource.getResultSet();
		if (rs == null) {
			return skuIds;
		}
		try {
			while (rs.next()) {
				skuIds.add(rs.getLong(1));
			}
		} catch (Exception e) {
			logger.error("Get skuIds by spu error!", e);
		} finally {
			dbResource.close();
		}
		return skuIds;
	}

	@Override
	public int syncSKUByIds(String ids, ItemSPU spu) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET ");
		sql.append("BrandId = ").append(spu.getBrandId()).append(", ");
		sql.append("CategoryNormalId = ").append(spu.getCategoryNormalId()).append(" WHERE ");
		sql.append("Id IN (").append(ids).append(")");
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}
}
