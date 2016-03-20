package com.xyl.mmall.itemcenter.dao.sku;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.Sku;

public interface SkuDao extends AbstractDao<Sku> {
	/**
	 * 添加sku
	 * 
	 * @param sku
	 * @return
	 */
	public Sku addNewSku(Sku sku);

	/**
	 * 获取sku列表
	 * 
	 * @param productId
	 *            商品样本库中的商品id
	 * @return
	 */
	public List<Sku> getSkuList(long supplierId, long productId);

	/**
	 * 获取sku
	 * 
	 * @param barCode
	 *            条形码
	 * @return
	 */
	public Sku getSkuByBarCode(long supplierId, String barCode);

	/**
	 * 删除商品样本的所有sku
	 * 
	 * @param productId
	 *            商品样品库的商品id
	 * @return
	 */
	public boolean deleteSkuList(long supplierId, long productId);

	public List<Sku> getExistSkuIds(long supplierId, List<String> barCodes);

	public Sku getSku(long supplierId, long pid, String barCode);

	public int getMaxIndexOfSku(long pid);
}
