package com.xyl.mmall.saleschedule.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List
;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.saleschedule.dao.BrandLogoImgDao;
import com.xyl.mmall.saleschedule.enums.BrandImgSize;
import com.xyl.mmall.saleschedule.meta.BrandLogoImg;

/**
 * BrandImgDao的SQL实现，由于添加了其他相关的图片，所以图片不一定是logo
 * @author chengximing
 *
 */
@Repository
public class BrandLogoImgDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<BrandLogoImg>
	implements BrandLogoImgDao {

	@Cacheable(value = "brandCache")
	@Override
	public BrandLogoImg getBrandLogoByBrandId(long brandId, BrandImgSize brandImgSize) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "brandId", brandId);
		SqlGenUtil.appendExtParamObject(sql, "brandImgSize", brandImgSize.getIntValue());
		return queryObject(sql);
	}

	@Cacheable(value = "brandCache")
	@Override
	public List<BrandLogoImg> getBrandLogoListByBrandId(long brandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "brandId", brandId);
		return queryObjects(sql);
	}

	@Override
	public boolean delBrandLogoListByBrandId(long brandId) {
		List<BrandLogoImg> brandImgList = getBrandLogoListByBrandId(brandId);
		for (BrandLogoImg brandImg : brandImgList) {
			if (!deleteById(brandImg.getBrandImgId())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean setBrandLogoImg(long brandId, BrandImgSize brandImgSize, String url) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "brandId", brandId);
		SqlGenUtil.appendExtParamObject(sql, "brandImgSize", brandImgSize.getIntValue());
		List<BrandLogoImg> ret = queryObjects(sql);
		if (ret.size() == 1) {
			BrandLogoImg img = ret.get(0);
			img.setBrandImgUrl(url);
			return updateObjectByKey(img);
		} else {
			return false;
		}
	}
	
	@Override
	public void addNewBrandLogo(long brandId, String logo, String visualWeb, String visualApp) {
		List<BrandLogoImg> list = new ArrayList<>(3);
		String[] urls = {logo, visualWeb, visualApp};
		for (int i = 0; i < 3; i++) {
			BrandLogoImg img = new BrandLogoImg();
			img.setBrandId(brandId);
//			img.setBrandImgId(-1);
			img.setBrandImgSize(BrandImgSize.SIZE_BRAND_LOGO.genEnumByIntValue(i));
			img.setBrandImgUrl(urls[i]);
			list.add(img);
		}
		addObjects(list);
	}

	@Override
	public void setNewBrandLogo(long brandId, String logo, String visualWeb, String visualApp) {
		BrandLogoImg logoImg = getBrandLogoByBrandId(brandId, BrandImgSize.SIZE_BRAND_LOGO);
		BrandLogoImg visualImgWeb = getBrandLogoByBrandId(brandId, BrandImgSize.SIZE_BRAND_VISUAL_WEB);
		BrandLogoImg visualImgApp = getBrandLogoByBrandId(brandId, BrandImgSize.SIZE_BRAND_VISUAL_APP);
		StringBuilder sql = new StringBuilder(256);
		sql.append("UPDATE Mmall_SaleSchedule_Schedule SET brandLogo = '").append(logo).
		append("' WHERE brandId = ").append(brandId);
		// getSqlSupport().excuteQuery(sql.toString());
		getSqlSupport().excuteUpdate(sql.toString());
		if (logoImg != null) {
			logoImg.setBrandImgUrl(logo);
			updateObjectByKey(logoImg);
		} else {
			BrandLogoImg img = new BrandLogoImg();
			img.setBrandId(brandId);
			img.setBrandImgId(-1);
			img.setBrandImgSize(BrandImgSize.SIZE_BRAND_LOGO);
			img.setBrandImgUrl(logo);
			addObject(img);
		}
		if (visualImgWeb != null) {
			visualImgWeb.setBrandImgUrl(visualWeb);
			updateObjectByKey(visualImgWeb);
		} else {
			BrandLogoImg img = new BrandLogoImg();
			img.setBrandId(brandId);
			img.setBrandImgId(-1);
			img.setBrandImgSize(BrandImgSize.SIZE_BRAND_VISUAL_WEB);
			img.setBrandImgUrl(visualWeb);
			addObject(img);
		}
		if (visualImgApp != null) {
			visualImgApp.setBrandImgUrl(visualApp);
			updateObjectByKey(visualImgApp);
		} else {
			BrandLogoImg img = new BrandLogoImg();
			img.setBrandId(brandId);
			img.setBrandImgId(-1);
			img.setBrandImgSize(BrandImgSize.SIZE_BRAND_VISUAL_APP);
			img.setBrandImgUrl(visualApp);
			addObject(img);
		}
	}

	@Cacheable(value = "brandCache")
	@Override
	public Map<Long, BrandLogoImg> getBrandLogoListByBrandIdList(
			List<Long> brandIdList, BrandImgSize brandImgSize) {
		Map<Long, BrandLogoImg> retMap = new HashMap<Long, BrandLogoImg>();
		if (brandIdList.size() > 0) {
			StringBuilder idsString = new StringBuilder(100);
			StringBuilder idsStringOrder = new StringBuilder(100);
			idsString.append(" (");
			for (Long brandId : brandIdList) {
				idsString.append(brandId).append(",");
				idsStringOrder.append(brandId).append(",");
			}
			idsString.deleteCharAt(idsString.lastIndexOf(","));
			idsStringOrder.deleteCharAt(idsStringOrder.lastIndexOf(","));
			idsString.append(") ");
			StringBuilder sql = new StringBuilder(256);
			sql.append(genSelectSql());
			SqlGenUtil.appendExtParamObject(sql, "brandImgSize", brandImgSize.getIntValue());
			sql.append(" AND brandId IN").append(idsString).append(" ORDER BY brandId");
			List<BrandLogoImg> list = queryObjects(sql);
			
			for (BrandLogoImg logoImg : list) {
				retMap.put(logoImg.getBrandId(), logoImg);
			}
		}
		return retMap;
	}

}
