package com.xyl.mmall.itemcenter.meta;

import java.math.BigDecimal;
import java.util.Date;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.common.meta.BaseVersion;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年5月14日下午8:02:59
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_ProductSKU", desc = "商品表", dbCreateTimeName = "CreateTime")
public class ProductSKU extends BaseVersion {

	private static final long serialVersionUID = 2996961742715821995L;
	
	/** 产品/商品id */
	@AnnonOfField(desc = "商品id", primary = true, primaryIndex = 1, autoAllocateId = true, policy = true)
	private long id;

	/** 商家id */
	@AnnonOfField(desc = "商家id")
	private long businessId;

	/** 单品id */
	@AnnonOfField(desc = "单品id", policy = true)
	private long spuId;

	@AnnonOfField(desc = "商品名称", type = "VARCHAR(255)", notNull = true)
	private String name;

	@AnnonOfField(desc = "副标题", type = "VARCHAR(100)", defa = "")
	private String title;
	
	/** 商品内码*/
	@AnnonOfField(desc = "商品内码", type = "VARCHAR(50)")
	private String innerCode;

	/** 销售单位 */
	@AnnonOfField(desc = "销售单位 ", type = "VARCHAR(20)")
	private String unit;

	/** 保质期 */
	@AnnonOfField(desc = "保质期 ", type = "VARCHAR(10)")
	private String expire;
	
	/** 生产日期 */
	@AnnonOfField(desc = "生产日期")
	private Date produceDate;
	
	/** 生退货政策，1允许，2拒绝 */
	@AnnonOfField(desc = "退货政策，1允许，2拒绝")
	private int canReturn;
	
	/** 起批数量 */
	@AnnonOfField(desc = "起批数量")
	private int batchNum;
	
	/** 建议零售价 */
	@AnnonOfField(desc = "建议零售价", defa = "0")
	private BigDecimal salePrice;
	
	/** 库存数量 */
	@AnnonOfField(desc = "库存数量", defa = "0")
	private int skuNum;
	
	/** 不足提醒*/
	@AnnonOfField(desc = "不足提醒", defa = "0")
	private int skuAttention;
	
	/** 商品状态 */
	@AnnonOfField(desc = "商品状态，1未审核，2审核中，3审核未通过，4已上架，5已下架", defa = "1")
    private int status;
	
	@AnnonOfField(desc = "销售数量", defa = "0")
	private int saleNum;
	
	@AnnonOfField(desc = "品牌id")
	private long brandId;

	@AnnonOfField(desc = "商品分类id")
	private long categoryNormalId;

	@AnnonOfField(desc = "是否限购，1限购，其余不限购", defa = "0")
	private int isLimited;
	
	public ProductSKU() { 
	}
	
	public ProductSKU(ProductSKUDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public long getSpuId() {
		return spuId;
	}

	public void setSpuId(long spuId) {
		this.spuId = spuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInnerCode() {
		return innerCode;
	}

	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	public Date getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}

	public int getCanReturn() {
		return canReturn;
	}

	public void setCanReturn(int canReturn) {
		this.canReturn = canReturn;
	}

	public int getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(int batchNum) {
		this.batchNum = batchNum;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public int getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(int skuNum) {
		this.skuNum = skuNum;
	}

	public int getSkuAttention() {
		return skuAttention;
	}

	public void setSkuAttention(int skuAttention) {
		this.skuAttention = skuAttention;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public long getCategoryNormalId() {
		return categoryNormalId;
	}

	public void setCategoryNormalId(long categoryNormalId) {
		this.categoryNormalId = categoryNormalId;
	}

	public int getIsLimited() {
		return isLimited;
	}

	public void setIsLimited(int isLimited) {
		this.isLimited = isLimited;
	}
}
