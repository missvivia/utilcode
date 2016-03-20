package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.itemcenter.enums.PictureType;

/**
 * 商品和图片的映射关系
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_ProdPicMap", desc = "商品和图片的映射关系", dbCreateTimeName = "CreateTime")
public class ProdPicMap implements Serializable {

	private static final long serialVersionUID = 7220166132020485143L;

	public static final String split = ";";

	@AnnonOfField(desc = "主键", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	/** 图片所属的商品的Id值 */
	@AnnonOfField(desc = "图片所属的商品的Id值", policy = true)
	private long productId;

	/** 图片的类型 */
	@AnnonOfField(desc = "图片的类型")
	private PictureType picType;

	/** nos版本*/
	@AnnonOfField(desc = "nos版本")
	private int nosVersion;
	
	/** 图片路径 */
	@AnnonOfField(desc = "图片路径")
	private String picPath;

	/** 是否在档期下 */
	@AnnonOfField(desc = "是否在档期下")
	private int isInPo;

	/** 创建时间 */
	@AnnonOfField(desc = "创建时间")
	private long cTime;

	/** 修改时间 */
	@AnnonOfField(desc = "修改时间")
	private long uTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getcTime() {
		return cTime;
	}

	public void setcTime(long cTime) {
		this.cTime = cTime;
	}

	public long getuTime() {
		return uTime;
	}

	public void setuTime(long uTime) {
		this.uTime = uTime;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public PictureType getPicType() {
		return picType;
	}

	public void setPicType(PictureType picType) {
		this.picType = picType;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public int getIsInPo() {
		return isInPo;
	}

	public void setIsInPo(int isInPo) {
		this.isInPo = isInPo;
	}

	public long getCTime() {
		return cTime;
	}

	public void setCTime(long cTime) {
		this.cTime = cTime;
	}

	public long getUTime() {
		return uTime;
	}

	public void setUTime(long uTime) {
		this.uTime = uTime;
	}

	public int getNosVersion() {
		return nosVersion;
	}

	public void setNosVersion(int nosVersion) {
		this.nosVersion = nosVersion;
	}

}
