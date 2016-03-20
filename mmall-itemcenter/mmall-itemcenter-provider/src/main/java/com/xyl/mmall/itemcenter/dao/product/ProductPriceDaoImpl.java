package com.xyl.mmall.itemcenter.dao.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.netease.dbsupport.DBResource;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.meta.ProductPrice;
import com.xyl.mmall.itemcenter.param.ProductPriceParam;

@Repository
public class ProductPriceDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ProductPrice> implements ProductPriceDao {
	
	
	private static Logger logger = LoggerFactory.getLogger(ProductPriceDaoImpl.class);

	private final String GET_PRICE_LIST_BY_PRODUCTID = "SELECT * FROM " + super.getTableName() 
			+ " WHERE ProductId = ? ORDER BY MinNumber";
	
	@Override
	public List<ProductPriceDTO> getProductPriceDTOByProductId(long productId) {
		List<ProductPriceDTO> ret = new ArrayList<ProductPriceDTO>();
		List<ProductPrice> list = super.queryObjects(GET_PRICE_LIST_BY_PRODUCTID, productId);
		if (!CollectionUtils.isEmpty(list)) {
			for (ProductPrice productPrice : list) {
				ret.add(new ProductPriceDTO(productPrice));
			}
		}
		return ret;
	}

	@Override
	public List<ProductPrice> getProductPriceByProductIds(
			List<Long> productIds) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Mmall_ItemCenter_ProductPrice WHERE 1=1 ");
		sql.append(" AND ProductId IN ( ");
		for (int i = 0; i < productIds.size(); i++) {
			long id = productIds.get(i);
			sql.append(id);
			if (i < productIds.size() - 1)
				sql.append(" ,");
		}
		sql.append(")");
		return super.queryObjects(sql.toString());
	}

	@Override
	public boolean addBulkProductPrice(List<ProductPrice> priceList) {
		return addObjects(priceList);
	}

	@Override
	public int deleteProductPriceBySKUId(long skuId) {
		StringBuilder sql = new StringBuilder(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "ProductId", skuId);
		return this.getSqlSupport().excuteUpdate(sql.toString());
	}

	@Override
	public boolean batchDeleteProductPriceBySKUIds(List<Long> skuIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql()).append(" 1=1 ");
		SqlGenUtil.appendExtParamColl(sql, "ProductId", skuIds );
		return super.getSqlSupport().updateRecords(sql.toString())>0;
	}

	@Override
	public List<Long> getProductIdsByProductPriceParam(ProductPriceParam productPriceParam) {
		 StringBuffer sql = new StringBuffer(); 
		 sql.append("select ProductId from Mmall_ItemCenter_ProductPrice,Mmall_ItemCenter_ProductSKU sku where ProductId = sku.id ");
		 if(productPriceParam.getStatus()!=-1){
			 sql.append(" AND status = " +productPriceParam.getStatus());
		 }
		 if (productPriceParam.getSprice()!= null) {
			sql.append(" AND Price >= ").append(productPriceParam.getSprice().doubleValue());
		 }
		 if (productPriceParam.getEprice() != null) {
			sql.append(" AND Price <= ").append(productPriceParam.getEprice().doubleValue());
		 }
		 if(CollectionUtil.isNotEmptyOfList(productPriceParam.getSkuIds())){
			 sql.append(" AND productId IN ( ");
			 for (int i = 0; i < productPriceParam.getSkuIds().size(); i++) {
				 long id = productPriceParam.getSkuIds().get(i);
				 sql.append(id);
				 if (i < productPriceParam.getSkuIds().size() - 1)
					 sql.append(" ,");
			 }
			 sql.append(")");
		 }
		 String order = productPriceParam.isAsc()?"asc":"desc";
		 sql.append(" order by Price "+order);
		 DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
         ResultSet rs = dbr.getResultSet();
         List<Long>skuIds = new ArrayList<Long>();
         try {
             while (rs .next()) {
            	 skuIds.add(rs.getLong("productId"));
             }
         } catch (SQLException e ) {
             e.printStackTrace();
             logger.error(e .getMessage(), e);
         }
		return skuIds;
	}

}
