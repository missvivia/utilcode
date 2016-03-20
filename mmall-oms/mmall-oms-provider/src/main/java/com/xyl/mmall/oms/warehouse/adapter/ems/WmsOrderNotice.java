package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * @author hzzengchengyuan
 * 
 */
@XmlRootElement(name = "RequestOrder")
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(value = WMSSalesOrderDTO.class)
public class WmsOrderNotice implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 233999305589874648L;

	@MapField(WMSSalesOrderDTO.field_warehouseCode)
	private String warehouse_code = "";

	@MapField(WMSSalesOrderDTO.field_ownerCode)
	private String owner_code = "";

	/**
	 * 目前设置成package id
	 */
	@MapField(WMSSalesOrderDTO.field_orderId)
	private String order_id = "";

	/**
	 * 订单类型
	 */
	private String order_type_code = "";

	/**
	 * 订单类型名称
	 */
	private String order_type_name = "";

	/**
	 * 物流公司编码
	 */
	@MapField(WMSSalesOrderDTO.field_logisticCode)
	@XmlElement(name = "LogisticProviderId")
	private String logisticProviderId;

	/**
	 * 物流公司名称
	 */
	@XmlElement(name = "LogisticProviderName")
	private String logisticProviderName;

	/**
	 * 收货方编码，适用于公司客户、调入仓库编码、供应商编码
	 */
	private String receiver_code;

	/**
	 * 收货方名称
	 */
	private String receiver_name;

	/**
	 * 收货人姓名
	 */
	@MapField(WMSSalesOrderDTO.field_receiverName)
	private String consignee = "";

	@MapField(WMSSalesOrderDTO.field_receiverPostCode)
	private String post_Code = null;

	@MapField(WMSSalesOrderDTO.field_receiverPhone)
	private String phone = "";

	@MapField(WMSSalesOrderDTO.field_receiverMobile)
	private String mobile = "";

	@MapField(WMSSalesOrderDTO.field_receiverProvince)
	private String prov = "";

	@MapField(WMSSalesOrderDTO.field_receiverCity)
	private String city = "";

	@MapField(WMSSalesOrderDTO.field_receiverDistrict)
	private String district = "";

	@MapField(WMSSalesOrderDTO.field_receiverAddress)
	private String address = "";

	/**
	 * 总件数（数量）
	 */
	@MapField(WMSSalesOrderDTO.field_count)
	private int count;

	/**
	 * 该出库单对应商品种类数
	 */
	@MapField(WMSSalesOrderDTO.field_skuCount)
	private int sku_count;

	@MapField(WMSSalesOrderDTO.field_totalPrice)
	private double total_price;
	
	
	public static final String ISCOD_YES = "1";
	
	public static final String ISCOD_NO = "0";
	
	/**
	 * 是否货到付款，1:yes,0:no
	 */
	@MapField(WMSSalesOrderDTO.field_isCod)
	private String iscod;
	
	/**
	 * 货到付款金额
	 */
	@MapField(WMSSalesOrderDTO.field_codAmount)
	private String cod_amount;
	
	
	
	@MapField(WMSSalesOrderDTO.field_note)
	private String remark;
	
	private String ext_attr1;
	
	private String ext_attr2;
	
	private String ext_attr3;
	
	private String ext_attr4;
	
	private String ext_attr5;
	
	@MapField(WMSSalesOrderDTO.field_boci)
	private String ext_attr6;
	
	private String ext_attr7;
	
	private String ext_attr8;
	
	private String ext_attr9;

	private String ext_attr10;
	
	@XmlElementWrapper(name = "details")
	@XmlElement(name = "detail")
	@MapField(WMSSalesOrderDTO.field_skuDetails)
	private List<EmsSku> skulist;
	
	public String getWarehouse_code() {
		return warehouse_code;
	}

	public void setWarehouse_code(String warehouse_code) {
		this.warehouse_code = warehouse_code;
	}

	public String getOwner_code() {
		return owner_code;
	}

	public void setOwner_code(String owner_code) {
		this.owner_code = owner_code;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_type_code() {
		return order_type_code;
	}

	public void setOrder_type_code(String order_type_code) {
		this.order_type_code = order_type_code;
	}

	public String getOrder_type_name() {
		return order_type_name;
	}

	public void setOrder_type_name(String order_type_name) {
		this.order_type_name = order_type_name;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getPost_Code() {
		return post_Code;
	}

	public void setPost_Code(String post_Code) {
		this.post_Code = post_Code;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSku_count() {
		return sku_count;
	}

	public void setSku_count(int sku_count) {
		this.sku_count = sku_count;
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	/**
	 * @return the iscod
	 */
	public String getIscod() {
		return iscod;
	}

	/**
	 * @param iscod the iscod to set
	 */
	public void setIscod(String iscod) {
		this.iscod = iscod;
	}

	/**
	 * @return the cod_amount
	 */
	public String getCod_amount() {
		return cod_amount;
	}

	/**
	 * @param cod_amount the cod_amount to set
	 */
	public void setCod_amount(String cod_amount) {
		this.cod_amount = cod_amount;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExt_attr1() {
		return ext_attr1;
	}

	public String getExt_attr2() {
		return ext_attr2;
	}

	public String getExt_attr3() {
		return ext_attr3;
	}

	public String getExt_attr4() {
		return ext_attr4;
	}

	public String getExt_attr5() {
		return ext_attr5;
	}

	public String getExt_attr6() {
		return ext_attr6;
	}

	public String getExt_attr7() {
		return ext_attr7;
	}

	public String getExt_attr8() {
		return ext_attr8;
	}

	public String getExt_attr9() {
		return ext_attr9;
	}

	public String getExt_attr10() {
		return ext_attr10;
	}

	public void setExt_attr1(String ext_attr1) {
		this.ext_attr1 = ext_attr1;
	}

	public void setExt_attr2(String ext_attr2) {
		this.ext_attr2 = ext_attr2;
	}

	public void setExt_attr3(String ext_attr3) {
		this.ext_attr3 = ext_attr3;
	}

	public void setExt_attr4(String ext_attr4) {
		this.ext_attr4 = ext_attr4;
	}

	public void setExt_attr5(String ext_attr5) {
		this.ext_attr5 = ext_attr5;
	}

	public void setExt_attr6(String ext_attr6) {
		this.ext_attr6 = ext_attr6;
	}

	public void setExt_attr7(String ext_attr7) {
		this.ext_attr7 = ext_attr7;
	}

	public void setExt_attr8(String ext_attr8) {
		this.ext_attr8 = ext_attr8;
	}

	public void setExt_attr9(String ext_attr9) {
		this.ext_attr9 = ext_attr9;
	}

	public void setExt_attr10(String ext_attr10) {
		this.ext_attr10 = ext_attr10;
	}

	public List<EmsSku> getSkulist() {
		return skulist;
	}

	public void setSkulist(List<EmsSku> skulist) {
		this.skulist = skulist;
	}

	public String getLogisticProviderId() {
		return logisticProviderId;
	}

	public void setLogisticProviderId(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}

	public String getLogisticProviderName() {
		return logisticProviderName;
	}

	public void setLogisticProviderName(String logisticProviderName) {
		this.logisticProviderName = logisticProviderName;
	}

	public String getReceiver_code() {
		return receiver_code;
	}

	public void setReceiver_code(String receiver_code) {
		this.receiver_code = receiver_code;
	}

	public String getReceiver_name() {
		return receiver_name;
	}

	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
}
