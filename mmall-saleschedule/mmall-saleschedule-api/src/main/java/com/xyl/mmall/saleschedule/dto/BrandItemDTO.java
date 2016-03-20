package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.saleschedule.meta.Brand;

/**
 * CMS全部的品牌列表
 * 
 * @author chengximing
 * 
 */
public class BrandItemDTO implements Comparable<BrandItemDTO>, Serializable {
	private static final long serialVersionUID = 6008619310192058958L;

	private long brandId;

	private String brandNameZh;

	private String brandNameEn;

	private long createDate;

	private String createUser;

	private long brandUpdateDate;
	
	/**
	 * 品牌首字母（用于移动端）
	 */
	private String brandHead;

	/**
	 * 品牌logo url 现在定死就只有一个
	 */
	private String logo;

	/**
	 * 用于web展示的品牌形象图
	 */
	private String brandVisualImgWeb;

	/**
	 * 用于手机端的品牌形象图
	 */
	private String brandVisualImgApp;

	/**
	 * 在线的po数量，用在用户收藏列表返回的接口里面
	 */
	private int poCount;

	/**
	 * 该品牌被收藏的人数
	 */
	private int favCount;

	/**
	 * 用在返回全部的列表中标示用户是否收藏的当前品牌
	 */
	private boolean favorited;

	/**
	 * poCount == 0 时候 nextPoTime标示未来4天之内出现的 档期的时间，如果为0说明未来4天没有档期
	 */
	private long nextPoTime;

	/**
	 * poCount == 0 时候未来4天没有档期为0，否则标志未来4天档期结束时间
	 */
	private long nextPoEndTime;

	/**
	 * poCount == 1 时候对应的档期的Id
	 */
	private long scheduleId;

	// private String psdTemplateUrl;
	/**
	 * 品牌被收藏的时间，用在移动端的品牌收藏列表
	 */
	private long favTime;

	private int brandProbability;

	public BrandItemDTO() {
	}

	public BrandItemDTO(Brand obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBrandVisualImgWeb() {
		return brandVisualImgWeb;
	}

	public void setBrandVisualImgWeb(String brandVisualImgWeb) {
		this.brandVisualImgWeb = brandVisualImgWeb;
	}

	public String getBrandVisualImgApp() {
		return brandVisualImgApp;
	}

	public void setBrandVisualImgApp(String brandVisualImgApp) {
		this.brandVisualImgApp = brandVisualImgApp;
	}

	public long getNextPoEndTime() {
		return nextPoEndTime;
	}

	public void setNextPoEndTime(long nextPoEndTime) {
		this.nextPoEndTime = nextPoEndTime;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public long getNextPoTime() {
		return nextPoTime;
	}

	public void setNextPoTime(long nextPoTime) {
		this.nextPoTime = nextPoTime;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public int getPoCount() {
		return poCount;
	}

	public void setPoCount(int poCount) {
		this.poCount = poCount;
	}

	public int getFavCount() {
		return favCount;
	}

	public void setFavCount(int favCount) {
		this.favCount = favCount;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getBrandNameZh() {
		return brandNameZh;
	}

	public void setBrandNameZh(String brandNameZh) {
		this.brandNameZh = brandNameZh;
	}

	public String getBrandNameEn() {
		return brandNameEn;
	}

	public void setBrandNameEn(String brandNameEn) {
		this.brandNameEn = brandNameEn;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public long getBrandUpdateDate() {
		return brandUpdateDate;
	}

	public void setBrandUpdateDate(long brandUpdateDate) {
		this.brandUpdateDate = brandUpdateDate;
	}

	public int getBrandProbability() {
		return brandProbability;
	}

	public void setBrandProbability(int brandProbability) {
		this.brandProbability = brandProbability;
	}

	public long getFavTime() {
		return favTime;
	}

	public void setFavTime(long favTime) {
		this.favTime = favTime;
	}

	public String getBrandHead() {
		return brandHead;
	}

	public void setBrandHead(String brandHead) {
		this.brandHead = brandHead;
	}

	@Override
	public int compareTo(BrandItemDTO o) {
		if ((this.getPoCount() != 0 && o.getPoCount() == 0) || (this.getPoCount() == 0 && o.getPoCount() != 0)) {
			return o.getPoCount() - this.getPoCount();
		} else if ((this.getNextPoTime() != 0L && o.getNextPoTime() == 0L)
				|| (this.getNextPoTime() == 0L && o.getNextPoTime() != 0L)) {
			return (this.getNextPoTime() != 0L ? -1 : 1);
		} else {
			return 1;
		}
	}

}
