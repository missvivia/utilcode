package com.xyl.mmall.mobile.web.vo;

import java.math.BigDecimal;
import java.util.List;

import com.xyl.mmall.backend.vo.BrandVO;
import com.xyl.mmall.backend.vo.SizeAssistVO;
import com.xyl.mmall.backend.vo.SizeTmplTableVO;
import com.xyl.mmall.mainsite.vo.SizeSpecVO;

/**
 * 商品详情页的商品相关的VO
 * 
 * @author hzhuangluqian
 *
 */
public class DetailProductVO {
	private String productId;

	private int preview;

	/** 商品名 */
	private String productName;

	/** 货号 */
	private String goodsNo;

	/** 档期id */
	private String poId;

	/** 选择的skuid */
	private String skuId;

	/** 销量 */
	private int sellNum;

	/** 品牌信息 */
	private BrandVO brand;

	private DetailPOVO schedule;

	private int isFollow;

	private String companyName;
	
	/** 正品价 */
	private BigDecimal marketPrice;

	/** 销售价 */
	private BigDecimal salePrice;

	/** 尺码规格列表 */
	private List<SizeSpecVO> sizeSpecList;

	/** 商品参数列表 */
	private List<BaseNameValueVO> productDetail;

	/** 尺码参照表 */
	private SizeTmplTableVO productSize;

	/** 商品展示图列表 */
	private List<String> prodShowPicList;

	/** 商家自定义编辑 */
	private String customEditHTML;

	/** 状态信息 1:正常 2:已过期 3:还未开始 4:抢光了 */
	private int status;

	/** 是否显示尺码图 */
	private int isShowSizePic;

	/** 尺码助手vo */
	private SizeAssistVO helper;

	/** 是否专柜同款 */
	private int sameAsShop;

	/** 无线短标题 */
	private String wirelessTitle;

	/** 售卖单位 */
	private int unit = 0;

	/** 是否航空禁运品 */
	private int airContraband = 0;

	/** 是否大件 */
	private int big;

	/** 是否易碎品 */
	private int fragile = 0;

	/** 是否贵重品 */
	private int valuables = 0;

	/** 是否消费税 */
	private int consumptionTax;

	/** 洗涤、使用说明 */
	private String careLabel;

	/** 商品描述 */
	private String productDescp;

	/** 配件说明 */
	private String accessory;

	/** 售后说明 */
	private String afterMarket;

	/** 产地 */
	private String producing;

	/** 长 */
	private String length;

	/** 宽 */
	private String width;

	/** 高 */
	private String height;

	/** 重量 */
	private String weight;

	private int isRecommend;

	public int getPreview() {
		return preview;
	}

	public void setPreview(int preview) {
		this.preview = preview;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getPoId() {
		return poId;
	}

	public void setPoId(String poId) {
		this.poId = poId;
	}

	public DetailPOVO getSchedule() {
		return schedule;
	}

	public void setSchedule(DetailPOVO schedule) {
		this.schedule = schedule;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public List<SizeSpecVO> getSizeSpecList() {
		return sizeSpecList;
	}

	public void setSizeSpecList(List<SizeSpecVO> sizeSpecList) {
		this.sizeSpecList = sizeSpecList;
	}

	public List<BaseNameValueVO> getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(List<BaseNameValueVO> productDetail) {
		this.productDetail = productDetail;
	}

	public SizeTmplTableVO getProductSize() {
		return productSize;
	}

	public void setProductSize(SizeTmplTableVO productSize) {
		this.productSize = productSize;
	}

	public List<String> getProdShowPicList() {
		return prodShowPicList;
	}

	public void setProdShowPicList(List<String> prodShowPicList) {
		this.prodShowPicList = prodShowPicList;
	}

	public String getCustomEditHTML() {
		return customEditHTML;
	}

	public void setCustomEditHTML(String customEditHTML) {
		this.customEditHTML = customEditHTML;
	}

	public BrandVO getBrand() {
		return brand;
	}

	public void setBrand(BrandVO brand) {
		this.brand = brand;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public int getIsShowSizePic() {
		return isShowSizePic;
	}

	public void setIsShowSizePic(int isShowSizePic) {
		this.isShowSizePic = isShowSizePic;
	}

	public SizeAssistVO getHelper() {
		return helper;
	}

	public void setHelper(SizeAssistVO helper) {
		this.helper = helper;
	}

	public int getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(int isFollow) {
		this.isFollow = isFollow;
	}

	public int getSameAsShop() {
		return sameAsShop;
	}

	public void setSameAsShop(int sameAsShop) {
		this.sameAsShop = sameAsShop;
	}

	public String getWirelessTitle() {
		return wirelessTitle;
	}

	public void setWirelessTitle(String wirelessTitle) {
		this.wirelessTitle = wirelessTitle;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public int getAirContraband() {
		return airContraband;
	}

	public void setAirContraband(int airContraband) {
		this.airContraband = airContraband;
	}

	public int getBig() {
		return big;
	}

	public void setBig(int big) {
		this.big = big;
	}

	public int getFragile() {
		return fragile;
	}

	public void setFragile(int fragile) {
		this.fragile = fragile;
	}

	public int getValuables() {
		return valuables;
	}

	public void setValuables(int valuables) {
		this.valuables = valuables;
	}

	public int getConsumptionTax() {
		return consumptionTax;
	}

	public void setConsumptionTax(int consumptionTax) {
		this.consumptionTax = consumptionTax;
	}

	public String getCareLabel() {
		return careLabel;
	}

	public void setCareLabel(String careLabel) {
		this.careLabel = careLabel;
	}

	public String getProductDescp() {
		return productDescp;
	}

	public void setProductDescp(String productDescp) {
		this.productDescp = productDescp;
	}

	public String getAccessory() {
		return accessory;
	}

	public void setAccessory(String accessory) {
		this.accessory = accessory;
	}

	public String getAfterMarket() {
		return afterMarket;
	}

	public void setAfterMarket(String afterMarket) {
		this.afterMarket = afterMarket;
	}

	public String getProducing() {
		return producing;
	}

	public void setProducing(String producing) {
		this.producing = producing;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public int getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(int isRecommend) {
		this.isRecommend = isRecommend;
	}

	public int getSellNum() {
		return sellNum;
	}

	public void setSellNum(int sellNum) {
		this.sellNum = sellNum;
	}

}
