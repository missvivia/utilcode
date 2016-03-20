/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;

/**
 * ItemSPU.java created by yydx811 at 2015年5月6日 下午6:41:12
 * 单品
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_SPU", desc = "单品表", dbCreateTimeName = "CreateTime")
public class ItemSPU implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -5528038512756893132L;

	@AnnonOfField(desc = "单品id", autoAllocateId = true, primary = true, policy = true)
	private long id;

	@AnnonOfField(desc = "单品条形码")
	private String barCode;

	@AnnonOfField(desc = "单品名称")
	private String name;

	@AnnonOfField(desc = "商品分类id")
	private long categoryNormalId;

	@AnnonOfField(desc = "品牌id")
	private long brandId;

	@AnnonOfField(desc = "操作人Id")
	private long agentId;
	
	public ItemSPU() {
	}

	public ItemSPU(ItemSPUDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCategoryNormalId() {
		return categoryNormalId;
	}

	public void setCategoryNormalId(long categoryNormalId) {
		this.categoryNormalId = categoryNormalId;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}
}
