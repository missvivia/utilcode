/**
 * 
 */
package com.xyl.mmall.content.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.content.enums.GiftType;

/**
 * 用户中奖记录
 * 
 * @author hzlihui2014
 * 
 */
@AnnonOfClass(tableName = "Mmall_Content_LotteryGift", desc = "用户中奖记录表")
public class LotteryGift implements Serializable {

	private static final long serialVersionUID = -8494241515780830166L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户id", notNull = false)
	private long userId;

	@AnnonOfField(desc = "用户名称", notNull = false, type = "VARCHAR(128)")
	private String userName;

	@AnnonOfField(desc = "用户手机号码", notNull = false, type = "VARCHAR(64)")
	private String userMobile;

	@AnnonOfField(desc = "奖品类型", notNull = false)
	private GiftType type;
	
	@AnnonOfField(desc = "中奖时间", notNull = false)
	private long hitTime;
	
	@AnnonOfField(desc = "奖品名称", notNull = false, type = "VARCHAR(32)")
	private String name;

	@AnnonOfField(desc = "更新时间", notNull = false)
	private long updateTime;
	
	@AnnonOfField(desc = "奖品图片URL", notNull = false)
	private String image;
	
	@AnnonOfField(desc = "奖品单位", notNull = false, type = "VARCHAR(8)")
	private String unit;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public GiftType getType() {
		return type;
	}

	public void setType(GiftType type) {
		this.type = type;
	}

	public long getHitTime() {
		return hitTime;
	}

	public void setHitTime(long hitTime) {
		this.hitTime = hitTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
