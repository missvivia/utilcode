package com.xyl.mmall.content.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 每日订单预估数量表. 预估活动期间每天订单数量。
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(tableName = "Mmall_Content_LotteryOrderCnt", desc = "每日订单预估数量表")
public class LotteryOrderCnt implements Serializable {

	private static final long serialVersionUID = -3271108526629396732L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "抽奖日期", notNull = false)
	private long lotteryDate;

	@AnnonOfField(desc = "预估每日订单数量", notNull = false)
	private int orderCnt;

	@AnnonOfField(desc = "每日抽奖概率分布", notNull = false)
	private String pd;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLotteryDate() {
		return lotteryDate;
	}

	public void setLotteryDate(long lotteryDate) {
		this.lotteryDate = lotteryDate;
	}

	public int getOrderCnt() {
		return orderCnt;
	}

	public void setOrderCnt(int orderCnt) {
		this.orderCnt = orderCnt;
	}

	public String getPd() {
		return pd;
	}

	public void setPd(String pd) {
		this.pd = pd;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
