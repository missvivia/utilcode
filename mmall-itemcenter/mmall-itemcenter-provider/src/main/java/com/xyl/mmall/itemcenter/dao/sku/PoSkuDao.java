package com.xyl.mmall.itemcenter.dao.sku;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.PoSkuVo;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.param.PoSkuSo;

public interface PoSkuDao extends AbstractDao<PoSku> {

	/**
	 * 获取档期sku
	 * 
	 * @param poId
	 *            档期id
	 * @param barCode
	 *            条形码
	 * 
	 * @return
	 */
	public PoSku getSku(long poId, String barCode);

	/**
	 * 获取档期sku
	 * 
	 * @param poId
	 *            档期id
	 * @param goodsNo
	 *            货号
	 * @param barCode
	 *            条形码
	 * 
	 * @return
	 */
	public PoSku getSku(long poId, String goodsNo, String barCode);

	/**
	 * 获取档期sku列表
	 * 
	 * @param productId
	 *            档期商品id
	 * @return
	 */
	public List<PoSku> getPoSkuList(long productId);

	/**
	 * 更新档期sku的审核状态
	 * 
	 * @param status
	 *            审核状态
	 * @param reason
	 *            审核理由
	 * @param skuIds
	 *            skuId列表
	 * @return
	 */
	public boolean updateSkuStaus(StatusType status, String reason, List<Long> skuIds);

	/**
	 * 删除某个商品下的所有sku
	 * 
	 * @param pid
	 *            档期商品id
	 * @return
	 */
	public boolean deleteSkuByProductId(long pid);

	public boolean submitSku(long poId);

	/**
	 * 根据skuId列表获PoSku对象列表
	 * 
	 * @param skuIds
	 * @return
	 */
	public List<POSkuDTO> getPoSkuList(List<Long> skuIds);

	public List<PoSku> getSkuExceptPass(long poId);

	public List<POSkuDTO> getPoSkuDTOListByPo(long poId);

	public List<PoSku> getPassSku(long poId);

	public int getSkuCountInPo(long poId);

	public List<Long> getPOByBarCode(long supplierId, String barCode);

	public List<PoSku> getPoSkuListNonCache(long productId);

	public List<PoSku> getPoSkuListByPo(long poId);

	public List<POSkuDTO> getPoSkuDTOListByProductId(long pid);

	public List<PoSku> getPoSkuByParam(PoSkuSo so);

	public Long getPoSkuCountByParam(PoSkuSo so);

	public List<PoSku> getPoSkuListByParam(PoSkuSo so);

	public List<POSkuDTO> getPoSkuDTOListByPoAndProduct(long poId, long productId);

	public List<PoSku> searchPoSkusByParam(PoSkuSo so);

	public List<PoSkuVo> getPoSkuVosByParam(PoSkuSo so);

	public Long getPoSkuVosCountByParam(PoSkuSo so);

	public List<PoSku> getPoSkusByIds(List<Long> ids);

	public PoSku getPoSkuById(Long id);

	public List<Long> getSkuIds(long poId, long pid);

	public PoSku getPoSkuByBarCode(String barcode);

	public int getSkuNumOfStatus(long poId, StatusType status);

	public int getSkuNum(long poId);

	public boolean setSkuDeleteFlag(long poId, long id, int flag);

	public boolean skuOnline(long poId);

}
