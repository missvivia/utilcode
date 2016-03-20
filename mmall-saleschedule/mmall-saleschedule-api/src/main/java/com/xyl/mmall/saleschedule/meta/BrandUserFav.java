package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 品牌收藏meta
 * @author chengximing
 *
 */
@AnnonOfClass(desc = "品牌收藏表", tableName = "Mmall_SaleSchedule_BrandUserFav")
public class BrandUserFav implements Serializable {
	
	private static final long serialVersionUID = 9222412496990123623L;
	
	@AnnonOfField(desc = "品牌收藏表主键id", primary = true, autoAllocateId = true)
	private long favId;
	
	@AnnonOfField(desc = "用户id", policy = true)
	private long userId;
	
	@AnnonOfField(desc = "被收藏的品牌id")
	private long brandId;
	
	@AnnonOfField(desc = "收藏的时间")
	private long createDate;

	public long getFavId() {
		return favId;
	}

	public void setFavId(long favId) {
		this.favId = favId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

}
