package com.xyl.mmall.oms.meta;

import com.xyl.mmall.oms.enums.BociTime;
import com.xyl.mmall.oms.enums.BociType;

/**
 * @author zb<br>
 *         拨次信息
 */
public class BociInfo {
	private BociTime BociTime;

	private long bociStartTime;

	private long bociDeadLine;

	private BociType bociType;

	private int bociInt;

	public BociInfo() {

	}

	public BociInfo(com.xyl.mmall.oms.enums.BociTime bociTime, long bociStartTime, long bociDeadLine,
			BociType bociType,int bociInt) {
		super();
		this.bociStartTime = bociStartTime;
		BociTime = bociTime;
		this.bociDeadLine = bociDeadLine;
		this.bociType = bociType;
		this.bociInt = bociInt;
	}

	public BociTime getBociTime() {
		return BociTime;
	}

	public void setBociTime(BociTime bociTime) {
		BociTime = bociTime;
	}

	public long getBociDeadLine() {
		return bociDeadLine;
	}

	public void setBociDeadLine(long bociDeadLine) {
		this.bociDeadLine = bociDeadLine;
	}

	public BociType getBociType() {
		return bociType;
	}

	public void setBociType(BociType bociType) {
		this.bociType = bociType;
	}

	public long getBociStartTime() {
		return bociStartTime;
	}

	public void setBociStartTime(long bociStartTime) {
		this.bociStartTime = bociStartTime;
	}

	public int getBociInt() {
		return bociInt;
	}

	public void setBociInt(int bociInt) {
		this.bociInt = bociInt;
	}

}
