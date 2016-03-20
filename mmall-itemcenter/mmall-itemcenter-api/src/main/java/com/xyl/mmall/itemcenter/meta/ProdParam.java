/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.meta;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.common.meta.BaseVersion;
import com.xyl.mmall.itemcenter.dto.ProdParamDTO;

/**
 * ProdParam.java created by yydx811 at 2015年5月14日 下午8:14:33
 * 商品属性表
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_ProdParam", desc = "商品属性表", dbCreateTimeName = "CreateTime")
public class ProdParam extends BaseVersion {

	/** 序列化id. */
	private static final long serialVersionUID = 149700969830850015L;

	@AnnonOfField(desc = "商品属性id", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "商品id", policy = true)
	private long productSKUId;

	@AnnonOfField(desc = "模型属性id")
	private long modelParamId;
	
	@AnnonOfField(desc = "属性选项id")
	private long modelParamOptionId;

	public ProdParam() {
	}

	public ProdParam(ProdParamDTO obj) {
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

	public long getModelParamId() {
		return modelParamId;
	}

	public void setModelParamId(long modelParamId) {
		this.modelParamId = modelParamId;
	}

	public long getModelParamOptionId() {
		return modelParamOptionId;
	}

	public void setModelParamOptionId(long modelParamOptionId) {
		this.modelParamOptionId = modelParamOptionId;
	}
}
