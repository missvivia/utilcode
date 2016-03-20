/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xyl.mmall.oms.enums.PoReturnOrderState;
import com.xyl.mmall.oms.meta.ReturnPoOrderForm;

/**
 * 退供单
 * 
 * @author hzzengchengyuan
 *
 */
public class PoReturnOrderVO extends ReturnPoOrderForm {
	public static final String DEFAULT_NULL_LABLE = "--";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 所属站点
	 */
	private long province;

	/**
	 * 所属站点名称
	 */
	private String provinceName;

	/**
	 * 仓库名称
	 */
	private String warehouseName;

	/**
	 * po开始时间
	 */
	private long timestar;

	/**
	 * po结束时间
	 */
	private long timeend;

	/**
	 * 商家账号
	 */
	private String supplierAccount;

	/**
	 * 供应商公司名称
	 */
	private String companyName;

	/**
	 * 品牌id
	 */
	private long brandId;

	/**
	 * 品牌
	 */
	private String brandName;

	/**
	 * 快递公司
	 */
	private String expressCompanyName;

	/**
	 * 商品详情
	 */
	private List<PoReturnSkuVO> skuDetails;

	private String stateDesc = PoReturnOrderState.NULL.getDesc();

	/**
	 * 为和前端的字段名returnPoOrderId对应起来
	 * 
	 * @return
	 */
	public long getReturnPoOrderId() {
		return getPoReturnOrderId();
	}

	public String getExpressCompany() {
		return getExpressCompanyName();
	}

	public String getExpressNO() {
		return DEFAULT_NULL_LABLE;
	}

	public String getExpressPhone() {
		return super.getExpressPhone() == null ? DEFAULT_NULL_LABLE : super.getExpressPhone();
	}

	public String getReceiverAddress() {
		return super.getReceiverAddress() == null ? DEFAULT_NULL_LABLE : super.getReceiverAddress();
	}

	/**
	 * @return the timestar
	 */
	public long getTimestar() {
		return timestar;
	}

	/**
	 * @param timestar
	 *            the timestar to set
	 */
	public void setTimestar(long timestar) {
		this.timestar = timestar;
	}

	/**
	 * @return the timeend
	 */
	public long getTimeend() {
		return timeend;
	}

	/**
	 * @param timeend
	 *            the timeend to set
	 */
	public void setTimeend(long timeend) {
		this.timeend = timeend;
	}

	/**
	 * @param stateDesc
	 *            the stateDesc to set
	 */
	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	/**
	 * @return the state
	 */
	public String getStateDesc() {
		return this.stateDesc;
	}

	@Override
	public void setState(PoReturnOrderState state) {
		super.setState(state);
		stateDesc = state == null ? PoReturnOrderState.NULL.getDesc() : state.getDesc();
	}

	/**
	 * @return the province
	 */
	public long getProvince() {
		return province;
	}

	/**
	 * @param site
	 *            the province to set
	 */
	public void setProvince(long site) {
		this.province = site;
	}

	/**
	 * @return the provinceName
	 */
	public String getProvinceName() {
		return provinceName == null ? DEFAULT_NULL_LABLE : provinceName;
	}

	/**
	 * @param provinceName
	 *            the provinceName to set
	 */
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	/**
	 * @return the warehouseName
	 */
	public String getWarehouseName() {
		return warehouseName == null ? DEFAULT_NULL_LABLE : warehouseName;
	}

	/**
	 * @param warehouseName
	 *            the warehouseName to set
	 */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	/**
	 * @return the supplierAccount
	 */
	public String getSupplierAccount() {
		return supplierAccount == null ? DEFAULT_NULL_LABLE : supplierAccount;
	}

	/**
	 * @param supplierName
	 *            the supplierAccount to set
	 */
	public void setSupplierAccount(String supplierName) {
		this.supplierAccount = supplierName;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName == null ? DEFAULT_NULL_LABLE : companyName;
	}

	/**
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the brandId
	 */
	public long getBrandId() {
		return brandId;
	}

	/**
	 * @param brandId
	 *            the brandId to set
	 */
	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	/**
	 * @return the brand
	 */
	public String getBrandName() {
		return brandName == null ? DEFAULT_NULL_LABLE : brandName;
	}

	/**
	 * @param brand
	 *            the brand to set
	 */
	public void setBrandName(String brand) {
		this.brandName = brand;
	}

	/**
	 * @return the expressCompanyName
	 */
	public String getExpressCompanyName() {
		return expressCompanyName == null ? DEFAULT_NULL_LABLE : expressCompanyName;
	}

	/**
	 * @param expressCompanyName
	 *            the expressCompanyName to set
	 */
	public void setExpressCompanyName(String expressCompanyName) {
		this.expressCompanyName = expressCompanyName;
	}

	/**
	 * @return the skuDetails
	 */
	public List<PoReturnSkuVO> getSkuDetails() {
		return skuDetails;
	}

	/**
	 * @param skuDetails
	 *            the skuDetails to set
	 */
	public void setSkuDetails(List<PoReturnSkuVO> skuDetails) {
		this.skuDetails = skuDetails;
	}

	public void addSkuDetail(PoReturnSkuVO sku) {
		if (this.skuDetails == null) {
			this.skuDetails = new ArrayList<PoReturnSkuVO>();
		}
		this.skuDetails.add(sku);
	}

	// //////////
	// 以下代码只是为了退供单状态显示“更人性化”，将状态中文描述转换为运营人员或供应商视角
	// //////////

	private static Map<PoReturnOrderState, String> CMS_STATE_DESC = new HashMap<PoReturnOrderState, String>();

	private static Map<PoReturnOrderState, String> SUPPLIER_STATE_DESC = new HashMap<PoReturnOrderState, String>();
	static {
		CMS_STATE_DESC.put(PoReturnOrderState.NEW, "供方未确认");
		CMS_STATE_DESC.put(PoReturnOrderState.CONFIRM, "供方已确认");
		CMS_STATE_DESC.put(PoReturnOrderState.SHIPPED, "我方已退货");
		CMS_STATE_DESC.put(PoReturnOrderState.RECEIPTED, "退货完成");
		SUPPLIER_STATE_DESC.put(PoReturnOrderState.NEW, "未确认");
		SUPPLIER_STATE_DESC.put(PoReturnOrderState.CONFIRM, "已确认");
		SUPPLIER_STATE_DESC.put(PoReturnOrderState.SHIPPED, "对方已退货");
		SUPPLIER_STATE_DESC.put(PoReturnOrderState.RECEIPTED, "退货完成");
	}

	private static EnumBean[] CMS_STATE_ENUMBEAN = new EnumBean[CMS_STATE_DESC.size()];

	private static EnumBean[] SUPPLIER_STATE_ENUMBEAN = new EnumBean[SUPPLIER_STATE_DESC.size()];
	static {
		CMS_STATE_ENUMBEAN[0] = new EnumBean(PoReturnOrderState.NEW.getIntValue(), PoReturnOrderState.NEW.name(),
				CMS_STATE_DESC.get(PoReturnOrderState.NEW));
		CMS_STATE_ENUMBEAN[1] = new EnumBean(PoReturnOrderState.CONFIRM.getIntValue(),
				PoReturnOrderState.CONFIRM.name(), CMS_STATE_DESC.get(PoReturnOrderState.CONFIRM));
		CMS_STATE_ENUMBEAN[2] = new EnumBean(PoReturnOrderState.SHIPPED.getIntValue(),
				PoReturnOrderState.SHIPPED.name(), CMS_STATE_DESC.get(PoReturnOrderState.SHIPPED));
		CMS_STATE_ENUMBEAN[3] = new EnumBean(PoReturnOrderState.RECEIPTED.getIntValue(),
				PoReturnOrderState.RECEIPTED.name(), CMS_STATE_DESC.get(PoReturnOrderState.RECEIPTED));
		SUPPLIER_STATE_ENUMBEAN[0] = new EnumBean(PoReturnOrderState.NEW.getIntValue(), PoReturnOrderState.NEW.name(),
				SUPPLIER_STATE_DESC.get(PoReturnOrderState.NEW));
		SUPPLIER_STATE_ENUMBEAN[1] = new EnumBean(PoReturnOrderState.CONFIRM.getIntValue(),
				PoReturnOrderState.CONFIRM.name(), SUPPLIER_STATE_DESC.get(PoReturnOrderState.CONFIRM));
		SUPPLIER_STATE_ENUMBEAN[2] = new EnumBean(PoReturnOrderState.SHIPPED.getIntValue(),
				PoReturnOrderState.SHIPPED.name(), SUPPLIER_STATE_DESC.get(PoReturnOrderState.SHIPPED));
		SUPPLIER_STATE_ENUMBEAN[3] = new EnumBean(PoReturnOrderState.RECEIPTED.getIntValue(),
				PoReturnOrderState.RECEIPTED.name(), SUPPLIER_STATE_DESC.get(PoReturnOrderState.RECEIPTED));
	}

	public void toCmsStateDesc() {
		setStateDesc(CMS_STATE_DESC.get(getState()));
	}

	public void toSupplierStateDesc() {
		setStateDesc(SUPPLIER_STATE_DESC.get(getState()));
	}

	public static EnumBean[] getCmsStateEnumBean() {
		return CMS_STATE_ENUMBEAN;
	}

	public static EnumBean[] getSupplierStateEnumBean() {
		return SUPPLIER_STATE_ENUMBEAN;
	}

}
