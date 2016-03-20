package com.xyl.mmall.itemcenter.dao.sku;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.dao.product.PoProductDao;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.PoSkuVo;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.param.PoSkuSo;

@Repository
public class PoSkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<PoSku> implements PoSkuDao {

	@Autowired
	private PoProductDao poProductDao;

	private static final String SEARCH_POSKU = "select a.* from Mmall_ItemCenter_PoSku a,Mmall_SaleSchedule_Schedule b where a.PoId=b.Id and a.status>1 ";

	private static final String SEARCH_POSKU_COUNT = "select count(1) cnt from Mmall_ItemCenter_PoSku a,Mmall_SaleSchedule_Schedule b where a.PoId=b.Id and a.status>1 ";

	private static final String SEARCH_POSKUVO = "select a.*,b.brandId brandId,b.startTime startTime "
			+ ",b.endTime endTime,b.supplierId supplierId,b.supplierName supplierName"
			+ ",b.brandName brandName,b.brandNameEn brandNameEn,b.saleSiteFlag saleSiteFlag from Mmall_ItemCenter_PoSku a,Mmall_SaleSchedule_Schedule b where a.PoId=b.Id and a.status>1 ";

	private static final String SEARCH_POSKUVO_COUNT = "select count(1) cnt from Mmall_ItemCenter_PoSku a,Mmall_SaleSchedule_Schedule b where a.PoId=b.Id and a.status>1 ";

	private static final String SEARCH_POSKU_BY_POIDS = "select * from Mmall_ItemCenter_PoSku where poId in(";

	private static final String SEARCH_POSKU_BY_PARAM = "select * from Mmall_ItemCenter_PoSku where 1=1 ";

	private static final String GET_POSKUS_BY_IDS = "select * from Mmall_ItemCenter_PoSku where id in(";

	private static final String GET_POSKU_BY_BARCODE = "select * from Mmall_ItemCenter_PoSku where barCode=? AND IsDelete = 0";

	@Override
	public PoSku getSku(long poId, String barCode) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "BarCode", barCode);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return queryObject(sql.toString());
	}

	@Override
	public List<POSkuDTO> getPoSkuList(List<Long> skuIds) {
		StringBuilder sql = new StringBuilder(64);
		String prefix = "select t1.*,t2.BrandId,t2.ProductName,t2.ColorName,t2.ShowPicPath,t3.Value as Size,bs.StoreName from Mmall_ItemCenter_PoProduct t2, Mmall_ItemCenter_PoSku t1,Mmall_ItemCenter_SkuSpecMap t3,Mmall_CMS_Business bs where t1.PoId = t2.PoId AND t2.PoId = t3.PoId AND t1.ProductId = t2.Id AND t1.Id = t3.SkuId and t2.SupplierId = bs.id";
		sql.append(prefix);
		sql.append(" AND t1.Id IN (");
		for (int i = 0; i < skuIds.size(); i++) {
			long id = skuIds.get(i);
			sql.append(id);
			if (i < skuIds.size() - 1)
				sql.append(" ,");
		}
		sql.append(") order by SupplierId");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		List<POSkuDTO> list = new ArrayList<POSkuDTO>();
		try {
			while (rs.next()) {
				PoSku sku = new PoSku();
				sku.setBarCode(rs.getString("BarCode"));
				sku.setBasePrice(rs.getBigDecimal("BasePrice"));
				sku.setCTime(rs.getLong("CTime"));
				sku.setGoodsNo(rs.getString("GoodsNo"));
				sku.setId(rs.getLong("Id"));
				sku.setMarketPrice(rs.getBigDecimal("MarketPrice"));
				sku.setPoId(rs.getLong("PoId"));
				sku.setRejectReason(rs.getString("RejectReason"));
				sku.setSalePrice(rs.getBigDecimal("SalePrice"));
				sku.setSizeIndex(rs.getInt("SizeIndex"));
				sku.setSkuNum(rs.getInt("SkuNum"));
				sku.setStatus(StatusType.APPROVAL.genEnumByIntValue(rs.getInt("Status")));
				sku.setSubmitTime(rs.getLong("SubmitTime"));
				sku.setSupplierId(rs.getLong("SupplierId"));
				sku.setUTime(rs.getLong("UTime"));
				sku.setProductId(rs.getLong("ProductId"));
				POSkuDTO dto = new POSkuDTO(sku);
				dto.setProductName(rs.getString("ProductName"));
				dto.setProductLinkUrl("/detail?id=" + dto.getProductId());
				dto.setColorName(rs.getString("ColorName"));
				dto.setSize(rs.getString("Size"));
				dto.setThumb(rs.getString("ShowPicPath"));
				dto.setStatusName(sku.getStatus().getDesc());
				dto.setBrandId(rs.getLong("BrandId"));
				dto.setStoreName(rs.getString("StoreName"));
				if (list.indexOf(dto) < 0)
					list.add(dto);
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return list;
	}

	@Override
	public PoSku getSku(long poId, String goodsNo, String barCode) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "GoodsNo", goodsNo);
		SQLUtils.appendExtParamObject(sql, "BarCode", barCode);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return queryObject(sql.toString());
	}

	@Override
	public List<PoSku> getPoSkuList(long productId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "ProductId", productId);
		return queryObjects(sql.toString());
	}

	@Override
	public List<POSkuDTO> getPoSkuDTOListByProductId(long pid) {
		String sql = "select t1.*, t2.BrandId, t2.ProductName, t2.ColorName, t2.ShowPicPath, t3.Value as Size from "
				+ "Mmall_ItemCenter_PoProduct t2,Mmall_ItemCenter_PoSku t1,Mmall_ItemCenter_SkuSpecMap t3 where "
				+ "t1.PoId = t2.PoId AND t2.PoId = t3.PoId AND t1.ProductId = t2.Id AND t1.Id = t3.SkuId AND t2.Id = "
				+ pid + " AND t1.IsDelete = 0 AND t2.IsDelete = 0 ORDER BY t1.SizeIndex ASC";
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		List<POSkuDTO> list = new ArrayList<POSkuDTO>();
		try {
			while (rs.next()) {
				PoSku sku = new PoSku();
				sku.setBarCode(rs.getString("BarCode"));
				sku.setBasePrice(rs.getBigDecimal("BasePrice"));
				sku.setCTime(rs.getLong("CTime"));
				sku.setGoodsNo(rs.getString("GoodsNo"));
				sku.setId(rs.getLong("Id"));
				sku.setMarketPrice(rs.getBigDecimal("MarketPrice"));
				sku.setPoId(rs.getLong("PoId"));
				sku.setRejectReason(rs.getString("RejectReason"));
				sku.setSalePrice(rs.getBigDecimal("SalePrice"));
				sku.setSizeIndex(rs.getInt("SizeIndex"));
				sku.setSkuNum(rs.getInt("SkuNum"));
				sku.setSupplierSkuNum(rs.getInt("SupplierSkuNum"));
				sku.setStatus(StatusType.APPROVAL.genEnumByIntValue(rs.getInt("Status")));
				sku.setSubmitTime(rs.getLong("SubmitTime"));
				sku.setSupplierId(rs.getLong("SupplierId"));
				sku.setUTime(rs.getLong("UTime"));
				sku.setProductId(rs.getLong("ProductId"));
				POSkuDTO dto = new POSkuDTO(sku);
				dto.setProductName(rs.getString("ProductName"));
				dto.setProductLinkUrl("/detail?id=" + dto.getProductId());
				dto.setColorName(rs.getString("ColorName"));
				dto.setSize(rs.getString("Size"));
				dto.setThumb(rs.getString("ShowPicPath"));
				dto.setStatusName(sku.getStatus().getDesc());
				dto.setBrandId(rs.getLong("BrandId"));
				if (list.indexOf(dto) < 0)
					list.add(dto);
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return list;
	}

	@Override
	public List<PoSku> getPoSkuListNonCache(long productId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "ProductId", productId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return queryObjects(sql.toString());
	}

	@Override
	public List<POSkuDTO> getPoSkuDTOListByPo(long poId) {
		String sql = "select t1.*, t2.BrandId, t2.ProductName,t2.ColorNum, t2.ColorName, t2.ShowPicPath, t3.Value as Size "
				+ "from Mmall_ItemCenter_PoProduct t2, Mmall_ItemCenter_PoSku t1,Mmall_ItemCenter_SkuSpecMap t3 where "
				+ "t1.PoId = t2.PoId AND t2.PoId = t3.PoId AND t1.ProductId = t2.Id AND t2.Id = t3.ProductId AND t1.PoId = "
				+ poId + " AND t1.IsDelete = 0 AND t2.IsDelete = 0";
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		List<POSkuDTO> list = new ArrayList<POSkuDTO>();
		try {
			while (rs.next()) {
				PoSku sku = new PoSku();
				sku.setBarCode(rs.getString("BarCode"));
				sku.setBasePrice(rs.getBigDecimal("BasePrice"));
				sku.setCTime(rs.getLong("CTime"));
				sku.setGoodsNo(rs.getString("GoodsNo"));
				sku.setId(rs.getLong("Id"));
				sku.setMarketPrice(rs.getBigDecimal("MarketPrice"));
				sku.setPoId(rs.getLong("PoId"));
				sku.setRejectReason(rs.getString("RejectReason"));
				sku.setSalePrice(rs.getBigDecimal("SalePrice"));
				sku.setSizeIndex(rs.getInt("SizeIndex"));
				sku.setSkuNum(rs.getInt("SkuNum"));
				sku.setSupplierSkuNum(rs.getInt("SupplierSkuNum"));
				sku.setStatus(StatusType.APPROVAL.genEnumByIntValue(rs.getInt("Status")));
				sku.setSubmitTime(rs.getLong("SubmitTime"));
				sku.setSupplierId(rs.getLong("SupplierId"));
				sku.setUTime(rs.getLong("UTime"));
				sku.setProductId(rs.getLong("ProductId"));
				POSkuDTO dto = new POSkuDTO(sku);
				dto.setProductName(rs.getString("ProductName"));
				dto.setProductLinkUrl("/detail?id=" + dto.getProductId());
				dto.setColorNum(rs.getString("ColorNum"));
				dto.setColorName(rs.getString("ColorName"));
				dto.setSize(rs.getString("Size"));
				dto.setThumb(rs.getString("ShowPicPath"));
				dto.setStatusName(sku.getStatus().getDesc());
				dto.setBrandId(rs.getLong("BrandId"));
				if (list.indexOf(dto) < 0)
					list.add(dto);
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return list;
	}

	@Override
	public List<PoSku> getPoSkuListByPo(long poId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return queryObjects(sql.toString());
	}

	@Override
	public boolean updateSkuStaus(StatusType status, String reason, List<Long> skuIds) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET Status = ").append(status.getIntValue()).append(",");
		if (StringUtils.isBlank(reason)) {
			sql.append(" RejectReason = ").append("\'\'");
		} else {
			sql.append(" RejectReason = ").append("\'").append(reason).append("\'");
		}
		sql.append(" WHERE Id IN (");
		for (int i = 0; i < skuIds.size(); i++) {
			long pid = skuIds.get(i);
			sql.append(pid);
			if (i < skuIds.size() - 1)
				sql.append(" ,");
		}
		sql.append(") AND IsDelete = 0");
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public boolean submitSku(long poId) {
		long submitTime = new Date().getTime();
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET Status = ").append(StatusType.PENDING.getIntValue());
		sql.append(", SubmitTime = ").append(submitTime);
		sql.append(" WHERE PoId =").append(poId);
		sql.append(" AND Status != ").append(StatusType.APPROVAL.getIntValue());
		sql.append(" AND IsDelete = 0");
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public boolean deleteSkuByProductId(long pid) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "ProductId", pid);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public List<PoSku> getSkuExceptPass(long poId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		sql.append(" AND Status != ").append(StatusType.APPROVAL.getIntValue());
		sql.append(" AND IsDelete = 0");
		return queryObjects(sql.toString());
	}

	@Override
	public int getSkuCountInPo(long poId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public List<PoSku> getPassSku(long poId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "Status", StatusType.APPROVAL);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return queryObjects(sql.toString());
	}

	@Override
	public List<Long> getPOByBarCode(long supplierId, String barCode) {
		List<Long> poList = new ArrayList<Long>();
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SQLUtils.appendExtParamObject(sql, "SupplierId", supplierId);
		SQLUtils.appendExtParamObject(sql, "barCode", barCode);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		List<PoSku> list = queryObjects(sql.toString());
		if (list != null && list.size() > 0) {
			for (PoSku sku : list) {
				poList.add(sku.getPoId());
			}
		}
		return poList;
	}

	@Override
	public List<PoSku> getPoSkuByParam(PoSkuSo so) {
		StringBuilder builder = new StringBuilder(64);
		builder.append(SEARCH_POSKU);

		List<PoSku> poSkuList;
		List<Object> paramList = new ArrayList<Object>();

		// 条形码
		if (!StringUtils.isEmpty(so.getBarCode())) {
			builder.append(" and a.barCode=?");
			paramList.add(so.getBarCode());
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
			builder.append(" and a.ProductId in(");
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
			builder.append(" and a.ProductId in(");
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

		poSkuList = super.queryObjects(builder.toString(), paramList);
		return poSkuList;
	}

	@Override
	public Long getPoSkuCountByParam(PoSkuSo so) {
		StringBuilder builder = new StringBuilder(64);
		builder.append(SEARCH_POSKU_COUNT);

		List<Object> paramList = new ArrayList<Object>();

		// 条形码
		if (!StringUtils.isEmpty(so.getBarCode())) {
			builder.append(" and a.barCode=?");
			paramList.add(so.getBarCode());
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
			builder.append(" and a.ProductId in(");
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
			builder.append(" and a.ProductId in(");
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
	public List<PoSku> getPoSkuListByParam(PoSkuSo so) {

		if (so.getPoIdList() == null || so.getPoIdList().size() <= 0) {
			return null;
		}

		List<Object> objs = new ArrayList<Object>();
		StringBuilder paramBuilder = new StringBuilder(64);
		paramBuilder.append(SEARCH_POSKU_BY_POIDS);
		for (int i = 0; i < so.getPoIdList().size(); i++) {
			if (i > 0) {
				paramBuilder.append(",");
			}
			paramBuilder.append("?");
			objs.add(so.getPoIdList().get(i));
		}
		paramBuilder.append(")");
		return super.queryObjects(paramBuilder.toString(), objs);
	}

	@Override
	public List<POSkuDTO> getPoSkuDTOListByPoAndProduct(long poId, long productId) {
		String sql = "select t1.*, t2.BrandId, t2.ProductName,t2.ColorNum, t2.ColorName, t2.ShowPicPath, t3.Value as Size "
				+ "from Mmall_ItemCenter_PoProduct t2,Mmall_ItemCenter_PoSku t1,Mmall_ItemCenter_SkuSpecMap t3 where "
				+ "t1.PoId = t2.PoId AND t2.PoId = t3.PoId AND t1.ProductId = t2.Id AND t2.Id = t3.ProductId AND t1.PoId = "
				+ poId + " and t1.ProductId=" + productId;
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		List<POSkuDTO> list = new ArrayList<POSkuDTO>();
		try {
			while (rs.next()) {
				PoSku sku = new PoSku();
				sku.setBarCode(rs.getString("BarCode"));
				sku.setBasePrice(rs.getBigDecimal("BasePrice"));
				sku.setCTime(rs.getLong("CTime"));
				sku.setGoodsNo(rs.getString("GoodsNo"));
				sku.setId(rs.getLong("Id"));
				sku.setMarketPrice(rs.getBigDecimal("MarketPrice"));
				sku.setPoId(rs.getLong("PoId"));
				sku.setRejectReason(rs.getString("RejectReason"));
				sku.setSalePrice(rs.getBigDecimal("SalePrice"));
				sku.setSizeIndex(rs.getInt("SizeIndex"));
				sku.setSkuNum(rs.getInt("SkuNum"));
				sku.setSupplierSkuNum(rs.getInt("SupplierSkuNum"));
				sku.setStatus(StatusType.APPROVAL.genEnumByIntValue(rs.getInt("Status")));
				sku.setSubmitTime(rs.getLong("SubmitTime"));
				sku.setSupplierId(rs.getLong("SupplierId"));
				sku.setUTime(rs.getLong("UTime"));
				sku.setProductId(rs.getLong("ProductId"));
				POSkuDTO dto = new POSkuDTO(sku);
				dto.setProductName(rs.getString("ProductName"));
				dto.setProductLinkUrl("/detail?id=" + dto.getProductId());
				dto.setColorNum(rs.getString("ColorNum"));
				dto.setColorName(rs.getString("ColorName"));
				dto.setSize(rs.getString("Size"));
				dto.setThumb(rs.getString("ShowPicPath"));
				dto.setStatusName(sku.getStatus().getDesc());
				dto.setBrandId(rs.getLong("BrandId"));
				if (list.indexOf(dto) < 0)
					list.add(dto);
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return list;
	}

	@Override
	public List<PoSku> searchPoSkusByParam(PoSkuSo so) {
		StringBuilder sqlBuffer = new StringBuilder(32);
		sqlBuffer.append(SEARCH_POSKU_BY_PARAM);
		List<Object> paramsList = new ArrayList<Object>();

		if (so.getPoId() != null) {
			sqlBuffer.append(" and poId=?");
			paramsList.add(so.getPoId());
		}

		if (so.getProductId() != null) {
			sqlBuffer.append(" and productId=?");
			paramsList.add(so.getProductId());
		}
		return super.queryObjects(sqlBuffer.toString(), paramsList);
	}

	@Override
	public List<PoSkuVo> getPoSkuVosByParam(PoSkuSo so) {
		StringBuilder builder = new StringBuilder(64);
		builder.append(SEARCH_POSKUVO);

		List<PoSkuVo> poSkuVoList = new ArrayList<PoSkuVo>();
		List<Object> paramList = new ArrayList<Object>();

		// 条形码
		if (!StringUtils.isEmpty(so.getBarCode())) {
			builder.append(" and a.barCode=?");
			paramList.add(so.getBarCode());
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
			builder.append(" and a.ProductId in(");
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
			builder.append(" and a.ProductId in(");
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
				PoSkuVo sku = new PoSkuVo();
				sku.setBarCode(rs.getString("BarCode"));
				sku.setBasePrice(rs.getBigDecimal("BasePrice"));
				sku.setCTime(rs.getLong("CTime"));
				sku.setGoodsNo(rs.getString("GoodsNo"));
				sku.setId(rs.getLong("Id"));
				sku.setMarketPrice(rs.getBigDecimal("MarketPrice"));
				sku.setPoId(rs.getLong("PoId"));
				sku.setRejectReason(rs.getString("RejectReason"));
				sku.setSalePrice(rs.getBigDecimal("SalePrice"));
				sku.setSizeIndex(rs.getInt("SizeIndex"));
				sku.setSkuNum(rs.getInt("SkuNum"));
				sku.setSupplierSkuNum(rs.getInt("SupplierSkuNum"));
				sku.setStatus(StatusType.APPROVAL.genEnumByIntValue(rs.getInt("Status")));
				sku.setSubmitTime(rs.getLong("SubmitTime"));
				sku.setSupplierId(rs.getLong("SupplierId"));
				sku.setUTime(rs.getLong("UTime"));
				sku.setProductId(rs.getLong("ProductId"));

				sku.setBrandId(rs.getLong("brandId"));
				sku.setStartTime(rs.getLong("startTime"));
				sku.setEndTime(rs.getLong("endTime"));
				sku.setBrandName(rs.getString("brandName"));
				sku.setBrandNameEn(rs.getString("brandNameEn"));
				sku.setSaleSiteFlag(rs.getLong("saleSiteFlag"));
				poSkuVoList.add(sku);
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return poSkuVoList;
	}

	@Override
	public Long getPoSkuVosCountByParam(PoSkuSo so) {
		StringBuilder builder = new StringBuilder(64);
		builder.append(SEARCH_POSKUVO_COUNT);

		List<Object> paramList = new ArrayList<Object>();

		// 条形码
		if (!StringUtils.isEmpty(so.getBarCode())) {
			builder.append(" and a.barCode=?");
			paramList.add(so.getBarCode());
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
			builder.append(" and a.ProductId in(");
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
			builder.append(" and a.ProductId in(");
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
	public List<PoSku> getPoSkusByIds(List<Long> ids) {

		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> paramList = new ArrayList<>();

		sqlBuilder.append(GET_POSKUS_BY_IDS);

		for (int i = 0; i < ids.size(); i++) {
			if (i > 0) {
				sqlBuilder.append(",");
			}
			sqlBuilder.append("?");
			paramList.add(ids.get(i));
		}

		sqlBuilder.append(")");

		return super.queryObjects(sqlBuilder.toString(), paramList);
	}

	@Cacheable(value = "poskuCache")
	@Override
	public PoSku getPoSkuById(Long id) {
		return super.getObjectById(id);
	}

	@Cacheable(value = "poskuCache")
	@Override
	public List<Long> getSkuIds(long poId, long pid) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("SELECT Id FROM Mmall_ItemCenter_PoSku WHERE 1=1 ");
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "ProductId", pid);
		sql.append(" ORDER BY SizeIndex asc");
		return Arrays.asList(super.queryIds(sql.toString()));
	}

	@Override
	public PoSku getPoSkuByBarCode(String barcode) {
		return super.queryObject(GET_POSKU_BY_BARCODE, barcode);
	}

	@Override
	public int getSkuNumOfStatus(long poId, StatusType status) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genCountSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		sql.append(" AND Status = ").append(status.getIntValue());
		return super.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public int getSkuNum(long poId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genCountSql());
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return super.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public boolean setSkuDeleteFlag(long poId, long id, int flag) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET IsDelete = ").append(flag);
		sql.append(" WHERE 1 = 1 ");
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "Id", id);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public boolean skuOnline(long poId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("UPDATE " + getTableName() + " SET IsOnline = 1");
		sql.append(" WHERE 1 = 1 ");
		SQLUtils.appendExtParamObject(sql, "PoId", poId);
		SQLUtils.appendExtParamObject(sql, "IsDelete", 0);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}
}
