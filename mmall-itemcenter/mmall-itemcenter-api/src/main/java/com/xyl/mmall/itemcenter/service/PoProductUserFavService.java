package com.xyl.mmall.itemcenter.service;

import java.util.List;

import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.PoProductUserFavDTO;
import com.xyl.mmall.itemcenter.param.ProductUserFavParam;


/**
 * 收藏商品service
 * @author author:lhp
 *
 * @version date:2015年7月8日下午1:11:14
 */
public interface PoProductUserFavService {
	
	/**
	 * 添加商品收藏
	 * @param userId
	 * @param brandId
	 * @return
	 */
	public boolean addPoProductIntoFavList(long userId, long poId);
	
	/**
	 * 删除商品收藏
	 * @param userId
	 * @param brandId
	 * @return
	 */
	public boolean removePoProductFromFavList(long userId, long poId);
	
	
	/**
	 * 根据userid或者商品Ids取收藏商品
	 * @param userId
	 * @param poIds
	 * @return
	 */
	public List<PoProductUserFavDTO> getPoProductFavListByUserIdOrPoIds(Long userId, List<Long>poIds);
	
	
	/**
	 * 分页取得收藏商品
	 * @param param
	 * @return
	 */
	public BasePageParamVO<PoProductUserFavDTO> getPageProductUserFavDTOByUserId(ProductUserFavParam param);

}
