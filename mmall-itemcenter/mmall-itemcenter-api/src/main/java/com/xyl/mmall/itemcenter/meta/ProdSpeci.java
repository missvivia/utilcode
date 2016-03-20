/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.meta;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.common.meta.BaseVersion;
import com.xyl.mmall.itemcenter.dto.ProdSpeciDTO;

/**
 * ProdSpeci.java created by yydx811 at 2015年5月14日 下午8:09:49
 * 商品规格表
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_ProdSpeci", desc = "商品规格表", dbCreateTimeName = "CreateTime")
public class ProdSpeci extends BaseVersion {

	/** 序列化id. */
	private static final long serialVersionUID = -958869082805038906L;

	@AnnonOfField(desc = "商品规格id", autoAllocateId = true, primary = true)
	private long id;
	
	@AnnonOfField(desc = "商品id", policy = true)
	private long productSKUId;

	@AnnonOfField(desc = "模型规格id")
	private long modelSpeciId;

	@AnnonOfField(desc = "规格选项id")
	private long modelSpeciOptionId;

	public ProdSpeci() {
	}

	public ProdSpeci(ProdSpeciDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductSKUId() {
		return productSKUId;
	}

	public void setProductSKUId(long productSKUId) {
		this.productSKUId = productSKUId;
	}

	public long getModelSpeciId() {
		return modelSpeciId;
	}

	public void setModelSpeciId(long modelSpeciId) {
		this.modelSpeciId = modelSpeciId;
	}

	public long getModelSpeciOptionId() {
		return modelSpeciOptionId;
	}

	public void setModelSpeciOptionId(long modelSpeciOptionId) {
		this.modelSpeciOptionId = modelSpeciOptionId;
	}
}
