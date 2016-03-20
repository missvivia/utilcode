package com.xyl.mmall.itemcenter.dao.size;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.CustomizedSize;

/**
 * 自定义尺码字段dao
 * 
 * @author hzhuangluqian
 *
 */
public interface CustomizedSizeDao extends AbstractDao<CustomizedSize> {

	/**
	 * 根据商品id和是否档期商品查询商品的自定义尺码字段
	 * 
	 * @param productId
	 *            商品id
	 * @param IsInPo
	 *            是否是档期商品
	 * @return
	 */
	List<CustomizedSize> getCustomizedSizeList(long productId, int IsInPo);

	/**
	 * 删除指定商品id和是否在档期所有的自定义尺码字段
	 * 
	 * @param productId
	 * @param IsInPo
	 * @return
	 */
	public boolean deleteCustomizedSize(long productId, int IsInPo);

	/**
	 * 获取尺码字段列表
	 * 
	 * @param templatekey
	 *            模板id
	 * @param IsInPo
	 *            是否在档期中
	 * @return
	 */
	public List<Size> getSizeList(long templatekey, int IsInPo);
}
