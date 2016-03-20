/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.meta;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.common.meta.BaseVersion;
import com.xyl.mmall.itemcenter.dto.ProdPicDTO;

/**
 * ProdPic.java created by yydx811 at 2015年5月14日 下午8:31:05
 * 商品图片表
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_ProdPic", desc = "商品图片表", dbCreateTimeName = "CreateTime")
public class ProdPic extends BaseVersion {

	/** 序列化id. */
	private static final long serialVersionUID = 6308458237835380907L;

	@AnnonOfField(desc = "商品图片id", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "商品id", policy = true)
	private long productSKUId;

	@AnnonOfField(desc = "图片的类型")
	private int type;
	
	@AnnonOfField(desc = "图片路径")
	private String path;

	public ProdPic() {
	}

	public ProdPic(ProdPicDTO obj) {
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
