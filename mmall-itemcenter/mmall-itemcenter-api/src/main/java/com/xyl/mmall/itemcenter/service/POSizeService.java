package com.xyl.mmall.itemcenter.service;

import java.util.List;

import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.param.SizeDetailPaire;
import com.xyl.mmall.itemcenter.param.SizeTable;

public interface POSizeService {

	public String getSizePrimaryValue(long poId, long skuId);

	/**
	 * 获取尺码字段列表
	 * 
	 * @param templatekey
	 *            模板id
	 * @param sizeType
	 *            尺码类型
	 * @return
	 */
	public abstract List<Size> getSizeList(long templatekey, SizeType sizeType);

	/**
	 * 获取尺码字段的值
	 * 
	 * @param templatekey
	 *            模板id
	 * @param columnId
	 *            尺码字段id
	 * @param sizeId
	 *            尺码id
	 * @param sizeType
	 *            尺码类型
	 * @return
	 */
	public abstract SizeValue getSizeValue(long templatekey, long columnId, long sizeId, SizeType sizeType);

	/**
	 * 删除档期商品的尺码内容
	 * 
	 * @param productId
	 *            档期商品id
	 * @return
	 */
	public boolean deleteCustomizeSizeValue(long productId);

	/**
	 * 获取商品的尺码表对象
	 * 
	 * @param templatekey
	 *            尺码id
	 * @param sizeType
	 *            尺码类型
	 * @return
	 */
	public SizeTable getSizeTable(long templatekey, SizeType sizeType);

	public boolean deleteCustomizeSizeValue(long productId, int sizeIndex);

	public List<SizeValue> getSizeValueList(long templatekey, SizeType sizeType);

}