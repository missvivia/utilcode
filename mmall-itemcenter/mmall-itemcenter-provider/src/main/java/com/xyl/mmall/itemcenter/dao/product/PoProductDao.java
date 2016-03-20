package com.xyl.mmall.itemcenter.dao.product;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.dto.CategoryGroupDTO;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.PoProductVo;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.param.PoProductSo;

/**
 * 添加到PO的商品dao
 * 
 * @author hzhuangluqian
 *
 */
public interface PoProductDao extends AbstractDao<PoProduct> {

	/**
	 * 更新商品状态
	 * 
	 * @param status
	 * @param reason
	 * @param descp
	 * @param productIds
	 * @return
	 */

	public boolean updateProductStaus(StatusType status, String reason, String descp, List<Long> productIds);

	public List<PoProduct> getProduct(long poId);

	public boolean submitProduct(long poId);

	/**
	 * 查询尚未通过审核的商品列表
	 * 
	 * @param poId
	 * @return
	 */
	public List<PoProduct> getProductExceptPass(long poId);

	public List<CategoryGroupDTO> getProductGroupByCategory(long poId);

	public boolean updateProductCategorySort(long poId, long categoryId, int index);

	public boolean updateProductSingleSort(long poId, long pid, int index);

	public boolean resetProductSingleSort(long poId);

	public List<PoProduct> getPassProduct(long poId);

	public List<PoProduct> getListBySizeAssistId(long supplierId, long helpId);

	public PoProduct getPoProduct(long supplierId, long poId, String goodsNo, String colorNum);

	public List<POProductDTO> getProductDTOListByPo(long poId);

	public PoProduct getPoProductByPoIdAndProduct(long productId, long poId);

	public long getCategoryIdBySkuId(long skuId);

	public List<PoProduct> getPoProductByName(String productName);

	public List<PoProduct> getPoProductByGoodsNo(String goodsNo);

	public List<PoProductVo> getPoProductosByParam(PoProductSo so);

	public Long getPoProductVosCountByParam(PoProductSo so);

	public POSkuDTO getPoSkuDTOByIdCache(Long skuId);

	public int getProductCount(long poId, int sameAsShop);

	public int getProductNumOfStatus(long poId, StatusType status);

	public int getProductNum(long poId);

	public boolean setProductDeleteFlag(long poId, long pid, int flag);

	public boolean productOnline(long poId);

	public List<POProductDTO> getProductDTOListByCategory(long categoryId);
}
