package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * 第一级类
 * @author jiangww
 *
 */
@JsonInclude(Include.NON_NULL)
public class MobileGiftMoneyVO implements Comparable<MobileGiftMoneyVO>,Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7971591280771514219L;
	//红包ID
	private long giftMoneyId;
	//红包金额
	private double giftMoney;
	
	private double gifMoneyRemain;
	//	0:可以使用，1：已经过期,2:已經使用
	private int status;
	//有效期
	private long startTime;
	//有效期
	private long endTime;
	//名字
	private String name;
	//
	private List<MobileGiftMoneyDetailVO> 	giftMoneyDetail;
	public long getGiftMoneyId() {
		return giftMoneyId;
	}
	public void setGiftMoneyId(long giftMoneyId) {
		this.giftMoneyId = giftMoneyId;
	}
	public double getGiftMoney() {
		return giftMoney;
	}
	public void setGiftMoney(double giftMoney) {
		this.giftMoney = giftMoney;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public List<MobileGiftMoneyDetailVO> getGiftMoneyDetail() {
		return giftMoneyDetail;
	}
	public void setGiftMoneyDetail(List<MobileGiftMoneyDetailVO> giftMoneyDetail) {
		this.giftMoneyDetail = giftMoneyDetail;
	}
	public double getGifMoneyRemain() {
		return gifMoneyRemain;
	}
	public void setGifMoneyRemain(double gifMoneyRemain) {
		this.gifMoneyRemain = gifMoneyRemain;
	}
	@Override
	public int compareTo(MobileGiftMoneyVO vo) {
		if(this.getStatus() == vo.getStatus()){
			if(this.getEndTime() == vo.getEndTime()){
				return 0;
			}
			return this.getEndTime() < vo.getEndTime() ? 1: -1;
		}
		return this.getStatus() > vo.getStatus() ? 1: -1;
	}

	
}
