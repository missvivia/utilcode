package com.xyl.mmall.oms.meta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.OmsOrderFormState;

/**
 * @author zb<br>
 * 
 */
@AnnonOfClass(desc = "oms订单表", tableName = "Mmall_Oms_OmsOrderForm")
public class OmsOrderForm implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "oms订单Id", primary = true, autoAllocateId = true)
	private long omsOrderFormId;

	@AnnonOfField(desc = "用户订单Id")
	private long userOrderFormId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "下单时间")
	private long orderTime;

	@AnnonOfField(desc = "购物车商品零售总价-结算价")
	private BigDecimal cartRPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "快递费-用户支付价")
	private BigDecimal expUserPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "货到付款")
	private boolean cashOnDelivery;

	@AnnonOfField(desc = "订单状态")
	private OmsOrderFormState omsOrderFormState;

	@AnnonOfField(desc = "入库站点")
	private long storeAreaId;

	/**
	 * 以下为快递信息，跟OrderExpInfo一一对应
	 */
	@AnnonOfField(desc = "快递地址-省份", type = "VARCHAR(16)")
	private String province;

	@AnnonOfField(desc = "快递地址-市", type = "VARCHAR(16)")
	private String city;

	@AnnonOfField(desc = "快递地址-区", type = "VARCHAR(16)")
	private String section;

	@AnnonOfField(desc = "快递地址-街道", type = "VARCHAR(64)", defa = "")
	private String street;

	@AnnonOfField(desc = "快递地址-详细地址", type = "VARCHAR(64)")
	private String address;

	@AnnonOfField(desc = "快递地址-邮政编号", type = "VARCHAR(8)")
	private String zipcode = "";

	@AnnonOfField(desc = "收件人-姓名", type = "VARCHAR(16)")
	private String consigneeName;

	@AnnonOfField(desc = "收件人-固定电话", type = "VARCHAR(32)")
	private String consigneeTel = "";

	@AnnonOfField(desc = "收件人-手机", type = "VARCHAR(16)")
	private String consigneeMobile = "";

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "发货时间")
	private long shipTime;

	@AnnonOfField(desc = "快递地址-省份ID", unsigned = false)
	private long provinceId;

	@AnnonOfField(desc = "快递地址-市ID", unsigned = false)
	private long cityId;

	@AnnonOfField(desc = "快递地址-区ID", unsigned = false)
	private long sectionId;

	@AnnonOfField(desc = "快递地址-街道ID")
	private long streetId;

	@AnnonOfField(desc = "快递费-用户支付价(红包抵扣)")
	private BigDecimal expUserPriceOfRed = BigDecimal.ZERO;

	@AnnonOfField(desc = "订单总金额里红包抵扣的金额")
	private BigDecimal redCash = BigDecimal.ZERO;

	private List<OmsOrderFormSku> omsOrdeFormSkuList;	

	public BigDecimal getExpUserPriceOfRed() {
		return expUserPriceOfRed;
	}

	public void setExpUserPriceOfRed(BigDecimal expUserPriceOfRed) {
		this.expUserPriceOfRed = expUserPriceOfRed;
	}

	public BigDecimal getRedCash() {
		return redCash;
	}

	public void setRedCash(BigDecimal redCash) {
		this.redCash = redCash;
	}

	public long getOmsOrderFormId() {
		return omsOrderFormId;
	}

	public void setOmsOrderFormId(long omsOrderFormId) {
		this.omsOrderFormId = omsOrderFormId;
	}

	public long getUserOrderFormId() {
		return userOrderFormId;
	}

	public void setUserOrderFormId(long userOrderFormId) {
		this.userOrderFormId = userOrderFormId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public BigDecimal getCartRPrice() {
		return cartRPrice;
	}

	public void setCartRPrice(BigDecimal cartRPrice) {
		this.cartRPrice = cartRPrice;
	}

	public BigDecimal getExpUserPrice() {
		return expUserPrice;
	}

	public void setExpUserPrice(BigDecimal expUserPrice) {
		this.expUserPrice = expUserPrice;
	}

	public boolean isCashOnDelivery() {
		return cashOnDelivery;
	}

	public void setCashOnDelivery(boolean cashOnDelivery) {
		this.cashOnDelivery = cashOnDelivery;
	}

	public OmsOrderFormState getOmsOrderFormState() {
		return omsOrderFormState;
	}

	public void setOmsOrderFormState(OmsOrderFormState omsOrderFormState) {
		this.omsOrderFormState = omsOrderFormState;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeTel() {
		return consigneeTel;
	}

	public void setConsigneeTel(String consigneeTel) {
		this.consigneeTel = consigneeTel;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getShipTime() {
		return shipTime;
	}

	public void setShipTime(long shipTime) {
		this.shipTime = shipTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<OmsOrderFormSku> getOmsOrdeFormSkuList() {
		return omsOrdeFormSkuList;
	}

	public void setOmsOrdeFormSkuList(List<OmsOrderFormSku> omsOrdeFormSkuList) {
		this.omsOrdeFormSkuList = omsOrdeFormSkuList;
	}

	public long getStoreAreaId() {
		return storeAreaId;
	}

	public void setStoreAreaId(long storeAreaId) {
		this.storeAreaId = storeAreaId;
	}

	/**
	 * 获取完整地址
	 * 
	 * @return
	 */
	public String getFullAddress() {
		String[] addressItem = new String[] { getProvince(), getCity(), getSection(), getStreet(), getAddress() };
		StringBuilder sb = new StringBuilder();
		boolean preEmpty = true;
		for (String item : addressItem) {
			if (!preEmpty) {
				sb.append("-");
			}
			boolean currentEmpty = isBlank(item);
			if (!currentEmpty) {
				sb.append(item);
			}
			preEmpty = currentEmpty;
		}
		return sb.toString();
	}

	private boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	public long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public long getSectionId() {
		return sectionId;
	}

	public void setSectionId(long sectionId) {
		this.sectionId = sectionId;
	}

	public long getStreetId() {
		return streetId;
	}

	public void setStreetId(long streetId) {
		this.streetId = streetId;
	}

}
