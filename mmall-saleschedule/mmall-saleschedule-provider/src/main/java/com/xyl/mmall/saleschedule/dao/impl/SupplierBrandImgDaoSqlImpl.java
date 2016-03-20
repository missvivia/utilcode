package com.xyl.mmall.saleschedule.dao.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.saleschedule.dao.SupplierBrandImgDao;
import com.xyl.mmall.saleschedule.enums.BrandImgType;
import com.xyl.mmall.saleschedule.meta.SupplierBrandImg;

/**
 * SupplierBrandImgDao的SQL实现
 * @author chengximing
 *
 */
@Repository
public class SupplierBrandImgDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<SupplierBrandImg>
	implements SupplierBrandImgDao {

	@Cacheable(value = "brandCache")
	@Override
	public List<SupplierBrandImg> getBrandVisualImgList(long supplierBrandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierBrandId", supplierBrandId);
		SqlGenUtil.appendExtParamObject(sql, "brandImgType", BrandImgType.IMG_BRANDIMAGE.getIntValue());
		DDBParam param = new DDBParam("imgIndex", true, 0, 0);
		return getListByDDBParam(sql.toString(), param);
	}

	@Cacheable(value = "brandCache")
	@Override
	public List<SupplierBrandImg> getBrandShowCaseImgList(long supplierBrandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierBrandId", supplierBrandId);
		SqlGenUtil.appendExtParamObject(sql, "brandImgType", BrandImgType.IMG_BRANDSHOWCASE.getIntValue());
		DDBParam param = new DDBParam("imgIndex", true, 0, 0);
		return getListByDDBParam(sql.toString(), param);
	}

	@Override
	public boolean setBrandVisualImg(long supplierBrandId, long index, String url) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierBrandId", supplierBrandId);
		SqlGenUtil.appendExtParamObject(sql, "brandImgType", BrandImgType.IMG_BRANDIMAGE.getIntValue());
		SqlGenUtil.appendExtParamObject(sql, "imgIndex", index);
		List<SupplierBrandImg> ret = queryObjects(sql);
		if (ret.size() == 1) {
			SupplierBrandImg img = ret.get(0);
			img.setImageUrl(url);
			updateObjectByKey(img);
			return true;
		} else if (ret.size() == 0) {
			SupplierBrandImg img = new SupplierBrandImg();
			img.setSupplierBrandId(supplierBrandId);
			img.setImgId(-1);
			img.setImageUrl(url);
			img.setImgIndex(index);
			img.setBrandImgType(BrandImgType.IMG_BRANDIMAGE);
			addObject(img);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean setBrandShowCaseImg(long supplierBrandId, long index,
			String url, String desc) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierBrandId", supplierBrandId);
		SqlGenUtil.appendExtParamObject(sql, "brandImgType", BrandImgType.IMG_BRANDSHOWCASE.getIntValue());
		SqlGenUtil.appendExtParamObject(sql, "imgIndex", index);
		List<SupplierBrandImg> ret = queryObjects(sql);
		if (ret.size() == 1) {
			SupplierBrandImg img = ret.get(0);
			img.setImageUrl(url);
			img.setShowCaseDesc(desc);
			updateObjectByKey(img);
			return true;
		} else if (ret.size() == 0) {
			SupplierBrandImg img = new SupplierBrandImg();
			img.setSupplierBrandId(supplierBrandId);
			img.setImgId(-1);
			img.setImageUrl(url);
			img.setImgIndex(index);
			img.setShowCaseDesc(desc);
			img.setBrandImgType(BrandImgType.IMG_BRANDSHOWCASE);
			addObject(img);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean setBrandShowCaseImgDesc(long supplierBrandId, long index, String desc) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierBrandId", supplierBrandId);
		SqlGenUtil.appendExtParamObject(sql, "brandImgType", BrandImgType.IMG_BRANDSHOWCASE.getIntValue());
		SqlGenUtil.appendExtParamObject(sql, "imgIndex", index);
		List<SupplierBrandImg> ret = queryObjects(sql);
		if (ret.size() == 1) {
			SupplierBrandImg img = ret.get(0);
			img.setShowCaseDesc(desc);
			updateObjectByKey(img);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean delSupplierBrandImgs(long supplierBrandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierBrandId", supplierBrandId);
		List<SupplierBrandImg> ret = queryObjects(sql);
		if (ret.size() > 0) {
			for (SupplierBrandImg supplierBrandImg : ret) {
				if (!deleteById(supplierBrandImg.getImgId()))
					return false;
			}
		}
		return true;
	}

	@Override
	public boolean delSupplierBrandVisualImg(long supplierBrandId, long index) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierBrandId", supplierBrandId);
		SqlGenUtil.appendExtParamObject(sql, "imgIndex", index);
		List<SupplierBrandImg> ret = queryObjects(sql);
		if (ret.size() == 1) {
			SupplierBrandImg supplierBrandImg = ret.get(0);
			if (!deleteById(supplierBrandImg.getImgId())) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}


}
