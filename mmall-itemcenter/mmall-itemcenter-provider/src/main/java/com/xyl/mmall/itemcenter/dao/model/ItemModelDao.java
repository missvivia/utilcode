/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.model;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.meta.ItemModel;

/**
 * ItemModelDao.java created by yydx811 at 2015年5月5日 上午10:10:37
 * 商品模型dao
 *
 * @author yydx811
 */
public interface ItemModelDao extends AbstractDao<ItemModel> {

	/**
	 * 获取全部商品模型
	 * @param searchValue
	 * @param startTime
	 * @param endTime
	 * @return List<ItemModel>
	 */
	public List<ItemModel> getItemModelList(String searchValue, String startTime, String endTime);
	
	/**
	 * 分页获取商品模型
	 * @param pageParamVO
	 * @param searchValue
	 * @param startTime
	 * @param endTime
	 * @return List<ItemModel>
	 */
	public List<ItemModel> getItemModelList(BasePageParamVO<?> pageParamVO, String searchValue, 
			String startTime, String endTime);
	
	/**
	 * 按条件获取数量
	 * @param searchValue
	 * @param startTime
	 * @param endTime
	 * @return int
	 */
	public int getItemModelCount(String searchValue, String startTime, String endTime);
	
	/**
	 * 按条件获取商品模型
	 * @param itemModel
	 * @return ItemModel
	 */
	public ItemModel getItemModel(ItemModel itemModel);
	
	/**
	 * 添加商品模型
	 * @param model
	 * @return long
	 */
	public long addItemModel(ItemModel model);

	/**
	 * 更新商品模型
	 * @param model
	 * @return int
	 */
	public int updateItemModel(ItemModel model);
	
	/**
	 * 删除商品模型
	 * @param id
	 * @return
	 */
	public int deleteItemModel(long id);
}
