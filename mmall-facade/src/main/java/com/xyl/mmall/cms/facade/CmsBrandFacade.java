package com.xyl.mmall.cms.facade;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;

public interface CmsBrandFacade {

	public RetArg getAllBrandList(DDBParam param);
	
	public RetArg getAllBrandItemList(DDBParam param);
	
	public RetArg getSearchedBrandList(String name, DDBParam param);
	
	public RetArg getSearchedBrandItemList(String name, DDBParam param);
	/**
	 * cms品牌字母索引
	 * @param indexList
	 * @param param
	 * @return
	 */
	public RetArg getBrandItemListByIndex(String index, DDBParam param);
	
	public BaseJsonVO editBrand(BrandItemDTO dto, String userName);
	/**
	 * 搜素对应的审核或者没有审核的品牌列表
	 * @param ids
	 * @param key
	 * @param status
	 * @param param
	 * @return
	 */
	public RetArg searchAuditBrand(List<Long> ids, String key, int status, DDBParam param);
	/**
	 * 批量处理审核通过对应的品牌
	 * @param ids
	 * @return
	 */
	public List<Long> passAuditBrand(List<Long> ids);
	/**
	 * 批量处理拒绝通过的相应品牌
	 * @param ids
	 * @param reason
	 * @return
	 */
	public List<Long> rejectAuditBrand(List<Long> ids, String reason);
	/**
	 * 删除对应的品牌
	 * @param brandId
	 * @return
	 */
	public boolean delBrand(long brandId);
	/**
	 * 通过权限获得相应的区域列表
	 * @param userId
	 * @param permission
	 * @return
	 */
	public List<AreaDTO> getAreaList(long userId, String permission);
	/**
	 * 获得预览的结果
	 * @param supplierBrandId
	 * @return
	 */
	public SupplierBrandFullDTO getSupplierBrandFullOnlyShowOnlineShops(long supplierBrandId);
}
