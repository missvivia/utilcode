package com.xyl.mmall.mobile.web.vo.order;

/**
 * 订单确认页-可选的优惠券列表
 * 
 * @author dingmingliang
 * 
 */
public class OrderCoupon1VO {

	/**
	 * 优惠券Id
	 */
	private long userCouponId;

	/**
	 * 优惠券标题
	 */
	private String title;

	/**
	 * 是否被用户选中
	 */
	private boolean isSelected = false;

	/**
	 * 开始时间
	 */
	private String startDate;

	/**
	 * 结束时间
	 */
	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public long getUserCouponId() {
		return userCouponId;
	}

	public void setUserCouponId(long userCouponId) {
		this.userCouponId = userCouponId;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
