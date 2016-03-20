package com.xyl.mmall.mobile.web.vo.order;

import java.util.List;

import com.xyl.mmall.mobile.ios.facade.pageView.common.OrderCartItemVO;

/**
 * 订单确认页-订单活动
 * 
 * @author dingmingliang
 * 
 */
public class OrderActionVO {

	/**
	 * 活动Id
	 */
	private long actId;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 活动效果描述
	 */
	private String effectDesc;

	/**
	 * 活动Tag列表
	 */
	private List<ActionTagVO> tagList;

	/**
	 * 订单明细
	 */
	private List<OrderCartItemVO> cartList;

	public String getEffectDesc() {
		return effectDesc;
	}

	public void setEffectDesc(String effectDesc) {
		this.effectDesc = effectDesc;
	}

	public List<ActionTagVO> getTagList() {
		return tagList;
	}

	public void setTagList(List<ActionTagVO> tagList) {
		this.tagList = tagList;
	}

	public long getActId() {
		return actId;
	}

	public void setActId(long actId) {
		this.actId = actId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<OrderCartItemVO> getCartList() {
		return cartList;
	}

	public void setCartList(List<OrderCartItemVO> cartList) {
		this.cartList = cartList;
	}
}