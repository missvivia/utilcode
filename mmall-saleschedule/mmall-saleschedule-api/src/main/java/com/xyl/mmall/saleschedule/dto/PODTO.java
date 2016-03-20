package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;

/**
 * PO DTO. 一个PO=1schedule+1个品购页+1个banner
 * 
 * @author hzzhanghui
 * 
 */
public class PODTO implements Serializable {

	private static final long serialVersionUID = 2222744422242819094L;

	private ScheduleDTO scheduleDTO;

	private SchedulePageDTO pageDTO;

	private ScheduleBannerDTO bannerDTO;
	
	// 冗余字段
	private int favoriteNum = 0;

	// 0-unlike 1-like
	private int favorite = 0;

	// PO是否过期。 true表示过期，false表示不过期
	private boolean isExpire;
	
	// PO是否有效。 true表示有效，false表示无效
	private boolean isValid;

	// Primary key of Mmall_SaleSchedule_ScheduleLike
	private long likeId;

	// PO overall status, combined with page,banner,prd list, prd info 's status
	private int poStatus;

	// sku (prd list) audit status 
	private boolean isSkuPass = false;

	// product (prd info) audit status
	private boolean isProductPass = false;

	// item cnt
	private int passItemNum = 0;

	// sku cnt
	private int passSkuNum = 0;

	// prd num
	private int passProductNum = 0;
	
	// -1: invalid  1-4: show status
	private int showFlag = -1;
	
	// desc of promotion the PO belong to
	private String promotionDesc = "";

	public ScheduleDTO getScheduleDTO() {
		return scheduleDTO;
	}

	public void setScheduleDTO(ScheduleDTO scheduleDTO) {
		this.scheduleDTO = scheduleDTO;
	}

	public SchedulePageDTO getPageDTO() {
		return pageDTO;
	}

	public void setPageDTO(SchedulePageDTO pageDTO) {
		this.pageDTO = pageDTO;
	}

	public ScheduleBannerDTO getBannerDTO() {
		return bannerDTO;
	}

	public void setBannerDTO(ScheduleBannerDTO bannerDTO) {
		this.bannerDTO = bannerDTO;
	}

	public int getFavoriteNum() {
		return favoriteNum;
	}

	public void setFavoriteNum(int favoriteNum) {
		this.favoriteNum = favoriteNum;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

	public boolean isExpire() {
		return isExpire;
	}

	public void setExpire(boolean isExpire) {
		this.isExpire = isExpire;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public long getLikeId() {
		return likeId;
	}

	public void setLikeId(long likeId) {
		this.likeId = likeId;
	}

	public boolean isSkuPass() {
		return isSkuPass;
	}

	public void setSkuPass(boolean isSkuPass) {
		this.isSkuPass = isSkuPass;
	}

	public boolean isProductPass() {
		return isProductPass;
	}

	public void setProductPass(boolean isProductPass) {
		this.isProductPass = isProductPass;
	}

	public int getPassItemNum() {
		return passItemNum;
	}

	public void setPassItemNum(int passItemNum) {
		this.passItemNum = passItemNum;
	}

	public int getPassSkuNum() {
		return passSkuNum;
	}

	public void setPassSkuNum(int passSkuNum) {
		this.passSkuNum = passSkuNum;
	}

	public int getPassProductNum() {
		return passProductNum;
	}

	public void setPassProductNum(int passProductNum) {
		this.passProductNum = passProductNum;
	}

	public int getPoStatus() {
		return poStatus;
	}

	public void setPoStatus(int poStatus) {
		this.poStatus = poStatus;
	}

	public int getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(int showFlag) {
		this.showFlag = showFlag;
	}
	
	public String getPromotionDesc() {
		return promotionDesc;
	}

	public void setPromotionDesc(String promotionDesc) {
		this.promotionDesc = promotionDesc;
	}

	@Override
	public String toString() {
		return "PODTO [scheduleDTO=" + scheduleDTO + ", pageDTO=" + pageDTO + ", bannerDTO=" + bannerDTO
				+ ", favoriteNum=" + favoriteNum + ", favorite=" + favorite + ", isExpire=" + isExpire + ", likeId="
				+ likeId + ", poStatus=" + poStatus + ", isSkuPass=" + isSkuPass + ", isProductPass=" + isProductPass
				+ ", passItemNum=" + passItemNum + ", passSkuNum=" + passSkuNum + ", passProductNum=" + passProductNum
				+ ", showFlag=" + showFlag + "]";
	}


}
