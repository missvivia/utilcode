package com.xyl.mmall.itemcenter.dto;

public class ScheduleAuditData implements java.io.Serializable{
	private static final long serialVersionUID = 5720443750132683857L;

	/** 所有商品清单是否通过审核 */
	private boolean isSkuPass;

	/** 所有商品资料是否通过审核 */
	private boolean isProductPass;

	/** 通过审核的件数 */
	private int passItemNum;

	/** 通过审核的sku数 */
	private int passSkuNum;

	/** 通过审核的款数 */
	private int passProductNum;

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
}
