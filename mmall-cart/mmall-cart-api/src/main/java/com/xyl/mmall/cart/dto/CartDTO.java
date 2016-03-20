package com.xyl.mmall.cart.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netease.print.common.util.CollectionUtil;

/**
 * @author Yang,Nan
 *
 */
public class CartDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7233307399795866477L;

	public List<CartItemDTO> getCartItemList() {
		return cartItemList;
	}

	public void setCartItemList(List<CartItemDTO> cartItemList) {
		this.cartItemList = cartItemList;
	}

	public List<CartItemDTO> getOvertimeCartItemList() {
		return overtimeCartItemList;
	}

	public void setOvertimeCartItemList(List<CartItemDTO> overtimeCartItemList) {
		this.overtimeCartItemList = overtimeCartItemList;
	}

	public List<CartItemDTO> getInvalidCartItemList() {
		return invalidCartItemList;
	}

	public void setInvalidCartItemList(List<CartItemDTO> invalidCartItemList) {
		this.invalidCartItemList = invalidCartItemList;
	}

	private List<CartItemDTO> cartItemList;

	private List<CartItemDTO> overtimeCartItemList;

	private List<CartItemDTO> invalidCartItemList;

	private List<CartItemDTO> deletedCartItemList;

	private List<CartItemDTO> deletedOvertimeCartItemList;

	private long updateTime;

	public List<CartItemDTO> getSkuOfValid() {
		if (cartItemList == null)
			return new ArrayList<CartItemDTO>();
		List<CartItemDTO> retlist = new ArrayList<CartItemDTO>(cartItemList);
		return retlist;
	}

	public List<CartItemDTO> getDeleteSkuOfValid() {
		if (deletedCartItemList == null)
			return new ArrayList<CartItemDTO>();
		List<CartItemDTO> retlist = new ArrayList<CartItemDTO>(deletedCartItemList);
		return retlist;
	}
	
	public List<CartItemDTO> getInvalidSkuOfValid() {
		if (invalidCartItemList == null)
			return new ArrayList<CartItemDTO>();
		List<CartItemDTO> retlist = new ArrayList<CartItemDTO>(invalidCartItemList);
		return retlist;
	}

	public List<CartItemDTO> getDeleteOvertimeSkuOfValid() {
		if (deletedOvertimeCartItemList == null)
			return new ArrayList<CartItemDTO>();
		List<CartItemDTO> retlist = new ArrayList<CartItemDTO>(deletedOvertimeCartItemList);
		return retlist;
	}

	public List<CartItemDTO> getOvertimeSkuOfValid() {
		if (overtimeCartItemList == null)
			return new ArrayList<CartItemDTO>();
		List<CartItemDTO> retlist = new ArrayList<CartItemDTO>(overtimeCartItemList);
		return retlist;
	}

	@SuppressWarnings("unchecked")
	public List<CartItemDTO> getDeleteAndOvertimeSkuOfValid() {
		return ListUtils.union(ListUtils.union(getDeleteSkuOfValid(), getOvertimeSkuOfValid()),
				getDeleteOvertimeSkuOfValid());
	}

	public List<Long> getSkuidOfValid() {
		if (cartItemList == null)
			return new ArrayList<Long>();
		Function<CartItemDTO, Long> getidFunc = new Function<CartItemDTO, Long>() {
			public Long apply(CartItemDTO from) {
				return from.getSkuid();
			}
		};
		List<Long> cartIdlist = Lists.transform(cartItemList, getidFunc);
		return cartIdlist;
	}

	public List<Long> getDeletedIdOfValid() {
		if (deletedCartItemList == null)
			return new ArrayList<Long>();
		Function<CartItemDTO, Long> getidFunc = new Function<CartItemDTO, Long>() {
			public Long apply(CartItemDTO from) {
				return from.getSkuid();
			}
		};
		List<Long> cartIdlist = Lists.transform(deletedCartItemList, getidFunc);
		return cartIdlist;
	}

	public List<Long> getDeletedOvertimeIdOfValid() {
		if (deletedOvertimeCartItemList == null)
			return new ArrayList<Long>();
		Function<CartItemDTO, Long> getidFunc = new Function<CartItemDTO, Long>() {
			public Long apply(CartItemDTO from) {
				return from.getSkuid();
			}
		};
		List<Long> cartIdlist = Lists.transform(deletedOvertimeCartItemList, getidFunc);
		return cartIdlist;
	}
	
	public List<Long> getInvalidIdOfValid() {
		if (invalidCartItemList == null)
			return new ArrayList<Long>();
		Function<CartItemDTO, Long> getidFunc = new Function<CartItemDTO, Long>() {
			public Long apply(CartItemDTO from) {
				return from.getSkuid();
			}
		};
		List<Long> cartIdlist = Lists.transform(invalidCartItemList, getidFunc);
		return cartIdlist;
	}

	public List<Long> getOverTimeIdOfValid() {
		if (overtimeCartItemList == null)
			return new ArrayList<Long>();
		Function<CartItemDTO, Long> getidFunc = new Function<CartItemDTO, Long>() {
			public Long apply(CartItemDTO from) {
				return from.getSkuid();
			}
		};
		List<Long> cartIdlist = Lists.transform(overtimeCartItemList, getidFunc);
		return cartIdlist;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getDeleteAandOverTimeIdOfValid() {
		return ListUtils.union(ListUtils.union(getDeletedIdOfValid(), getOverTimeIdOfValid()),
				getDeletedOvertimeIdOfValid());
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public List<CartItemDTO> getDeletedCartItemList() {
		return deletedCartItemList;
	}

	public void setDeletedCartItemList(List<CartItemDTO> deletedCartItemList) {
		this.deletedCartItemList = deletedCartItemList;
	}

	public List<CartItemDTO> getDeletedOvertimeCartItemList() {
		return deletedOvertimeCartItemList;
	}

	public void setDeletedOvertimeCartItemList(List<CartItemDTO> deletedOvertimeCartItemList) {
		this.deletedOvertimeCartItemList = deletedOvertimeCartItemList;
	}
	
	public 	Map<Long, CartItemDTO> getValidCartItemMap(){
		Map<Long, CartItemDTO>  cartItemMap = Maps.uniqueIndex(cartItemList, new Function<CartItemDTO, Long>() {
			public Long apply(CartItemDTO cartItemDTO) {
				return cartItemDTO.getSkuid();
			}});
		return cartItemMap;
	}

}
