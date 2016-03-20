package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * 第一级类
 * @author jiangww
 *
 */
@JsonInclude(Include.NON_NULL)
public class MobileCouponVO implements Comparable<MobileCouponVO>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7057466647588699910L;
	// 优惠券ID
	private long couponId;
	// 优惠券ID
	private long userCouponId;
	// 优惠券Code
	private String couponCode;
	// 名字
	private String name;
	// 发行人
	private String provider;
	// 使用条件
	private String require;
	// 0:可以使用，1：已经过期
	private int status;
	//有效期
	private long startTime;
	// 有效期
	private long endTime;

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getRequire() {
		return require;
	}

	public void setRequire(String require) {
		this.require = require;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	@Override
	public int compareTo(MobileCouponVO vo) {
		if(this.getStatus() == vo.getStatus()){
			if(this.getEndTime() == vo.getEndTime()){
				return 0;
			}
			return this.getEndTime() < vo.getEndTime() ? 1: -1;
		}
		return this.getStatus() > vo.getStatus() ? 1: -1;
	}

	public long getUserCouponId() {
		return userCouponId;
	}

	public void setUserCouponId(long userCouponId) {
		this.userCouponId = userCouponId;
	}
	
	
	
/*	public static void main(String args[]) throws InterruptedException{
		List<MobileCouponVO> a = new ArrayList<MobileCouponVO>();
		for(int i=0;i<10;i++){
			MobileCouponVO c = new MobileCouponVO();
			c.setStatus(i%5);
			c.setCouponId(i);
			c.setEndTime(System.currentTimeMillis());
			a.add(c);
			Thread.sleep(100);
		}
		
		Collections.sort(a);
		for(MobileCouponVO ccsa: a){
			System.out.println("id-"+ccsa.getCouponId() + " status-"+ ccsa.getStatus() + " time-" + ccsa.getEndTime());
		}
	}*/
}
