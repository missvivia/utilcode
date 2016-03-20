package com.xyl.mmall.mobile.ios.facade;

import com.xyl.mmall.mobile.ios.facade.pageView.cartList.IosCart;

/**
 * @author dingjiang
 *
 */
public interface IosCartFacade {


	public IosCart getCartInfo(long userId, int siteId);

}
