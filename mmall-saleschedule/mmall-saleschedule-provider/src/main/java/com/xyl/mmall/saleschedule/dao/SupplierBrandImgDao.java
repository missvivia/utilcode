package com.xyl.mmall.saleschedule.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.meta.SupplierBrandImg;

/**
 * 供应商相关的品牌图片dao
 * @author chengximing
 *
 */
public interface SupplierBrandImgDao extends AbstractDao<SupplierBrandImg> {
	
	/**
	 * 根据供应商和品牌id得到对应品牌的形象图像列表(1 ~ 3个) 按照index排序
	 * @param supplierBrandId
	 * @return
	 */
	public List<SupplierBrandImg> getBrandVisualImgList(long supplierBrandId);
	/**
	 * 根据供应商和品牌id得到对应品牌的橱窗图像列表(6个) 按照index排序
	 * @param supplierBrandId
	 * @return
	 */
	public List<SupplierBrandImg> getBrandShowCaseImgList(long supplierBrandId);
	/**
	 * 设置对应index的形象图
	 * @param supplierBrandId
	 * @param index
	 * @param url
	 * @return
	 */
	public boolean setBrandVisualImg(long supplierBrandId, long index, String url);
	/**
	 * 设置对应index的橱窗图
	 * @param supplierBrandId
	 * @param index
	 * @param url
	 * @param desc
	 * @return
	 */
	public boolean setBrandShowCaseImg(long supplierBrandId, long index, String url, String desc);
	/**
	 * 设置对应index的橱窗图文字描述
	 * @param supplierBrandId
	 * @param index
	 * @param desc
	 * @return
	 */
	public boolean setBrandShowCaseImgDesc(long supplierBrandId, long index, String desc);
	/**
	 * 删除相应的SupplierBrandImg
	 * @param supplierBrandId
	 * @return
	 */
	public boolean delSupplierBrandImgs(long supplierBrandId);
	/**
	 * 删除对应的品牌形象图
	 * @param supplierBrandId
	 * @param index
	 * @return
	 */
	public boolean delSupplierBrandVisualImg(long supplierBrandId, long index);

}
