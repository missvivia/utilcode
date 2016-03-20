package com.xyl.mmall.content.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.content.enums.GiftType;

/**
 * 奖品每日分配情况。 比如四等奖一天50个
 * 索引：type 和 lotteryDate
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(tableName = "Mmall_Content_LotteryGiftStat", desc = "奖品每日分配情况表")
public class LotteryGiftStat implements Serializable {

	private static final long serialVersionUID = -7165510866975546322L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "奖品类型", notNull = false)
	private GiftType type;

	@AnnonOfField(desc = "抽奖日期", notNull = false)
	private long lotteryDate;

	@AnnonOfField(desc = "奖品数量", notNull = false)
	private int lotteryCnt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public GiftType getType() {
		return type;
	}

	public void setType(GiftType type) {
		this.type = type;
	}

	public long getLotteryDate() {
		return lotteryDate;
	}

	public void setLotteryDate(long lotteryDate) {
		this.lotteryDate = lotteryDate;
	}

	public int getLotteryCnt() {
		return lotteryCnt;
	}

	public void setLotteryCnt(int lotteryCnt) {
		this.lotteryCnt = lotteryCnt;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
