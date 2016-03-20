package com.xyl.mmall.mobile.ios.facade.pageView.cartList;

import java.math.BigDecimal;
import java.util.List;

public class IosCart {

	private List<IosCartStore> cartStores;

	private int productNum = 0;
	private int storeNum = 0;
	private BigDecimal totalPrice = new BigDecimal(0);

	public List<IosCartStore> getCartStores() {
		return cartStores;
	}

	public void setCartStores(List<IosCartStore> cartStores) {
		this.cartStores = cartStores;
		if (cartStores != null) {
			this.storeNum = cartStores.size();
			for (IosCartStore iosCartStore : cartStores) {
				this.productNum += iosCartStore.getCartItems().size();
				for (IosCartItem iosCartItem : iosCartStore.getCartItems()) {
					if (iosCartItem.getPriceList() != null && !iosCartItem.getPriceList().isEmpty()) {
						BigDecimal price = iosCartItem.getPriceList().get(0).getPrice();
						BigDecimal bigDecimal = new BigDecimal(0).add(price).multiply(new BigDecimal(iosCartItem.getCount()));
						this.totalPrice = this.totalPrice.add(bigDecimal);
					}
				}

			}
		}
	}

	public int getProductNum() {
		return productNum;
	}

	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}

	public int getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(int storeNum) {
		this.storeNum = storeNum;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

}
