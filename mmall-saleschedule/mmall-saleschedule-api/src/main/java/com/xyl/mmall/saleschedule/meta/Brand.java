package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.saleschedule.enums.BrandProbability;

/**
 * 品牌meta
 * 
 * @author chengximing
 * 
 */
@AnnonOfClass(desc = "cms品牌表", tableName = "Mmall_ItemCenter_Brand", dbCreateTimeName = "CreateTime")
public class Brand implements Serializable {

	private static final long serialVersionUID = 20140911L;

	@AnnonOfField(desc = "品牌表主键id", primary = true, autoAllocateId = true)
	private long brandId;

	@AnnonOfField(desc = "品牌中文名称", notNull = false, type = "VARCHAR(64)")
	private String brandNameZh;

	@AnnonOfField(desc = "品牌英文名称", notNull = false, type = "VARCHAR(64)")
	private String brandNameEn;

	@AnnonOfField(desc = "品牌英文名称的首字母", notNull = false, type = "CHAR(4)")
	private String brandNameHead;

	@AnnonOfField(desc = "品牌拼音名称的首字母", notNull = false, type = "CHAR(4)")
	private String brandNameHeadPinYin;

	@AnnonOfField(desc = "创建人", type = "VARCHAR(100)")
	private String createUser;

	@AnnonOfField(desc = "品牌创建时间")
	private long createDate;

	@AnnonOfField(desc = "品牌更新时间")
	private long brandUpdateDate;

	// 这个参数是后来添加的 主要是推荐的时候的概率区分
	@AnnonOfField(desc = "概率的类别")
	private BrandProbability brandProbability;

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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public long getBrandUpdateDate() {
		return brandUpdateDate;
	}

	public void setBrandUpdateDate(long brandUpdateDate) {
		this.brandUpdateDate = brandUpdateDate;
	}

	public BrandProbability getBrandProbability() {
		return brandProbability;
	}

	public void setBrandProbability(BrandProbability brandProbability) {
		this.brandProbability = brandProbability;
	}

	public String getBrandNameHead() {
		return brandNameHead;
	}

	public void setBrandNameHead(String brandNameHead) {
		this.brandNameHead = brandNameHead;
	}

	public String getBrandNameHeadPinYin() {
		return brandNameHeadPinYin;
	}

	public void setBrandNameHeadPinYin(String brandNameHeadPinYin) {
		this.brandNameHeadPinYin = brandNameHeadPinYin;
	}

	/**
	 * 获取品牌中英文名字 中文名字为空就拿英文名字 英文名字为空就拿中文名字
	 * 
	 * @return
	 */
	public String getBrandNameAuto() {
		if (StringUtils.isBlank(this.brandNameZh)) {
			return this.brandNameEn;
		}
		if (StringUtils.isBlank(this.brandNameEn)) {
			return this.brandNameZh;
		}
		return this.brandNameZh;
	}

	/**
	 * 获取品牌首字母，拼音为空就拿英文，英文为空就那拼音，都不为空取拼音
	 * @return
	 */
	public String getBrandNameHeadAuto() {
		if (StringUtils.isBlank(this.brandNameHeadPinYin)) {
			return brandNameHead;
		}
		if (StringUtils.isBlank(this.brandNameHead)) {
			return brandNameHeadPinYin;
		}
		return this.brandNameHeadPinYin;
	}
}
