package com.xyl.mmall.itemcenter.dao.size;

import java.util.List;
import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.CustomizedSize;
import com.xyl.mmall.itemcenter.meta.CustomizedSizeValue;

/**
 * 自定义尺码字段值dao
 * 
 * @author hzhuangluqian
 *
 */
public interface CustomizedSizeValueDao extends AbstractDao<CustomizedSizeValue> {

	/**
	 * 根据商品id，skuid，是否是档期商品来查询自定义字段值列表
	 * 
	 * @param productId
	 *            商品id
	 * @param sizeId
	 *            skuId
	 * @param isInPo
	 *            是否是档期商品
	 * @return
	 */
	public List<CustomizedSizeValue> getCustomizedSizeValueList(long productId, long sizeId, int isInPo);

	/**
	 * 删除指定商品id的所有自定义尺码值列表
	 * 
	 * @param productId
	 *            商品id
	 * @param isInPo
	 *            是否在档期中
	 * @return
	 */
	public boolean deleteCustomizedSizeValue(long productId, int isInPo);

	/**
	 * 获取尺码字段值列表
	 * 
	 * @param templatekey
	 *            模板id
	 * @param sizeId
	 *            尺码id
	 * @param isInPo
	 *            是否在档期中
	 * @return
	 */
	public List<SizeValue> getSizeValueList(long templatekey, long sizeId, int isInPo);

	/**
	 * 获取指定商品id、尺码字段id、skuId的自定义尺码字段值
	 * 
	 * @param productId
	 *            商品id
	 * @param columnId
	 *            尺码字段id
	 * @param skuId
	 *            skuId
	 * @param isInPo
	 *            是否在档期中
	 * @return
	 */
	public CustomizedSizeValue getCustomizedSizeValue(long productId, long columnId, long skuId, int isInPo);

	/**
	 * 获取尺码字段值
	 * 
	 * @param templatekey
	 *            模板id
	 * @param columnId
	 *            字段id
	 * @param sizeId
	 *            尺码id
	 * @param isInPo
	 *            是否在档期中
	 * @return
	 */
	public SizeValue getSizeValue(long templatekey, long columnId, long sizeId, int isInPo);

	/**
	 * 删除指定商品id和skuId的自定义尺码值
	 * 
	 * @param productId
	 *            商品id
	 * @param sizeId
	 *            skuId
	 * @param isInPo
	 *            是否在档期中
	 * @return
	 */
	public boolean deleteCustomizedSizeValue(long productId, long sizeId, int isInPo);

	public List<SizeValue> getSizeValueList(long templatekey, int isInPo);

	public Map<Long, String> getSize(long productId, int isInPo);

}
