package com.xyl.mmall.mobile.web.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;

/**
 * 购物车信息
 * 
 * @author Yang,Nan
 * 
 */
public class CartVO implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8932629665037087205L;

	private long userId;

	/**
	 * 购物车活动
	 */
	List<CartActivationVO> activations;

	/**
	 * 无活动PO
	 */
	private List<CartPOVO> poList;
	
	/**
	 * 购物车店铺商品
	 */
	private List<CartStoreVO>cartStoreList;

	private CartInfoVO cartInfoVO;

	/**
	 * 失效sku list
	 */
	private List<CartSkuItemVO> invalidCartItemList;
	
	private int invalidatedCount = 0;
	
	private int overtimeCount = 0;

	private int retCode;
	
	private Map<String, List<ProductPriceDTO>> productMap;
	
	//商品收藏关系map
	private Map<String, String>poUsrFavMap;
	
	public List<CartStoreVO> getCartStoreList() {
		return cartStoreList;
	}

	public void setCartStoreList(List<CartStoreVO> cartStoreList) {
		this.cartStoreList = cartStoreList;
	}
	
	public Map<String, String> getPoUsrFavMap() {
		return poUsrFavMap;
	}

	public void setPoUsrFavMap(Map<String, String> poUsrFavMap) {
		this.poUsrFavMap = poUsrFavMap;
	}

	public Map<String, List<ProductPriceDTO>> getProductMap() {
		return productMap;
	}

	public void setProductMap(Map<String, List<ProductPriceDTO>> productMap) {
		this.productMap = productMap;
	}
	
	public List<CartActivationVO> getActivations() {
		return activations;
	}

	public void setActivations(List<CartActivationVO> activations) {
		this.activations = activations;
	}

	public List<CartPOVO> getPoList() {
		return poList;
	}

	public void setPoList(List<CartPOVO> poList) {
		this.poList = poList;
	}

	public CartInfoVO getCartInfoVO() {
		return cartInfoVO;
	}

	public void setCartInfoVO(CartInfoVO cartInfoVO) {
		this.cartInfoVO = cartInfoVO;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<CartSkuItemVO> getInvalidCartItemList() {
		return invalidCartItemList;
	}

	public void setInvalidCartItemList(List<CartSkuItemVO> invalidCartItemList) {
		this.invalidCartItemList = invalidCartItemList;
	}

	public int getInvalidatedCount() {
		return invalidatedCount;
	}

	public void setInvalidatedCount(int invalidatedCount) {
		this.invalidatedCount = invalidatedCount;
	}

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public int getOvertimeCount() {
		return overtimeCount;
	}

	public void setOvertimeCount(int overtimeCount) {
		this.overtimeCount = overtimeCount;
	}
}
