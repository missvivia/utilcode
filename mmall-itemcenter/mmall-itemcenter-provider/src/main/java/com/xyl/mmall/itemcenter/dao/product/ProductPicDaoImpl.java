package com.xyl.mmall.itemcenter.dao.product;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.dao.UKeyPolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.itemcenter.dao.SQLUtils;
import com.xyl.mmall.itemcenter.enums.PictureType;
import com.xyl.mmall.itemcenter.meta.ProdPicMap;

@Repository
public class ProductPicDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ProdPicMap> implements ProductPicDao {

	@Override
	public ProdPicMap addNewProdPicMap(ProdPicMap prodPicMap) {
		return super.addObject(prodPicMap);
	}

	@Override
	@Cacheable(value = "prodPicDaoCache")
	public ProdPicMap getProdPicMap(long pid, PictureType type, int isInPo) {
		StringBuilder sqlBuffer = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sqlBuffer, "ProductId", pid);
		SQLUtils.appendExtParamObject(sqlBuffer, "PicType", type);
		SQLUtils.appendExtParamObject(sqlBuffer, "IsInPo", isInPo);
		return queryObject(sqlBuffer.toString());
	}

	// TODO
	@Override
	public List<ProdPicMap> getProdPicMap(PictureType type, int isInPo) {
		StringBuilder sqlBuffer = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sqlBuffer, "PicType", type);
		SQLUtils.appendExtParamObject(sqlBuffer, "IsInPo", isInPo);
		return queryObjects(sqlBuffer.toString());
	}

	@Override
	public ProdPicMap getProdPicMapNoCache(long pid, PictureType type, int isInPo) {
		StringBuilder sqlBuffer = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sqlBuffer, "ProductId", pid);
		SQLUtils.appendExtParamObject(sqlBuffer, "PicType", type);
		SQLUtils.appendExtParamObject(sqlBuffer, "IsInPo", isInPo);
		return queryObject(sqlBuffer.toString());
	}

	@Override
	public List<ProdPicMap> getProdPicMap(long pid, int isInPo) {
		StringBuilder sqlBuffer = new StringBuilder(genSelectSql());
		SQLUtils.appendExtParamObject(sqlBuffer, "ProductId", pid);
		SQLUtils.appendExtParamObject(sqlBuffer, "IsInPo", isInPo);
		return queryObjects(sqlBuffer.toString());
	}

	@Override
	public boolean updateProdPicMap(ProdPicMap map) {
		saveObject(map);
		return true;
	}

	@Override
	public boolean deleteProdPicMap(long productId, int isInPo) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SQLUtils.appendExtParamObject(sql, "ProductId", productId);
		SQLUtils.appendExtParamObject(sql, "IsInPo", isInPo);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}
}
