package com.xyl.mmall.mainsite.vo;

import java.util.List;

import com.xyl.mmall.cart.dto.CartDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;

public class CartInnervVO {
	private FavorCaculateResultDTO favorCaculateResultDTO;
	private POListDTO batchGetScheduleListByIdList;
	private List<POSkuDTO> skuDTOList;
	private FavorCaculateParamDTO paramDTO;
	private CartDTO cartDTO;
	private int retcode;
	
	/**
	 * 是否购物车的选中的id和后台cartitem一致
	 */
	private boolean isCartIdsValid;
	
	public FavorCaculateResultDTO getFavorCaculateResultDTO() {
		return favorCaculateResultDTO;
	}
	public void setFavorCaculateResultDTO(FavorCaculateResultDTO favorCaculateResultDTO) {
		this.favorCaculateResultDTO = favorCaculateResultDTO;
	}
	public POListDTO getBatchGetScheduleListByIdList() {
		return batchGetScheduleListByIdList;
	}
	public void setBatchGetScheduleListByIdList(POListDTO batchGetScheduleListByIdList) {
		this.batchGetScheduleListByIdList = batchGetScheduleListByIdList;
	}
	public FavorCaculateParamDTO getParamDTO() {
		return paramDTO;
	}
	public void setParamDTO(FavorCaculateParamDTO paramDTO) {
		this.paramDTO = paramDTO;
	}
	public List<POSkuDTO> getSkuDTOList() {
		return skuDTOList;
	}
	public void setSkuDTOList(List<POSkuDTO> skuDTOList) {
		this.skuDTOList = skuDTOList;
	}
	public CartDTO getCartDTO() {
		return cartDTO;
	}
	public void setCartDTO(CartDTO cartDTO) {
		this.cartDTO = cartDTO;
	}
	public boolean isCartIdsValid() {
		return isCartIdsValid;
	}
	public void setCartIdsValid(boolean isCartIdsValid) {
		this.isCartIdsValid = isCartIdsValid;
	}
	public int getRetcode() {
		return retcode;
	}
	public void setRetcode(int retcode) {
		this.retcode = retcode;
	}
	
	
	
}
