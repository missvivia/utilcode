package com.xyl.mmall.itemcenter.meta;


import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 商品收藏meta
 *
 */
@AnnonOfClass(desc = "商品收藏表", tableName = "Mmall_ItemCenter_PoProductUserFav")
public class PoProductUserFav implements Serializable {
	
	private static final long serialVersionUID = -1451413033567667918L;

	@AnnonOfField(desc = "商品收藏表主键id", primary = true, autoAllocateId = true)
	private long poFavId;
	
	@AnnonOfField(desc = "用户id", policy = true)
	private long userId;
	
	@AnnonOfField(desc = "被收藏的商品id")
	private long poId;
	
	@AnnonOfField(desc = "收藏的时间")
	private long createDate;

	public long getPoFavId() {
		return poFavId;
	}

	public void setPoFavId(long poFavId) {
		this.poFavId = poFavId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
}
