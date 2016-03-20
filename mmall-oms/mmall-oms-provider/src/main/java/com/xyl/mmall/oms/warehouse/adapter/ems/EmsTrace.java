/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import com.xyl.mmall.oms.dto.warehouse.WMSOrderTrace;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * @author hzzengchengyuan
 *
 */
@XmlAccessorType (XmlAccessType.FIELD)
@MapClass(WMSOrderTrace.class)
public class EmsTrace implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 1L;

	/**
	 * 事务ID
	 */
	private String transaction_id;

	/**
	 * 运单号
	 */
	@MapField(WMSOrderTrace.field_logisticNo)
	private String mailno;

	/**
	 * 订单号
	 */
	@MapField(WMSOrderTrace.field_orderId)
	private String order_id;

	/**
	 * 操作时间
	 */
	@MapField(value = WMSOrderTrace.field_timestamp, isDate = true)
	private String operate_time;

	/**
	 * 操作类型
	 */
	@MapField(WMSOrderTrace.field_operate)
	private String operate_type;

	/**
	 * 操作描述
	 */
	@MapField(WMSOrderTrace.field_operateDesc)
	private String operate_desc;

	/**
	 * 操作机构
	 */
	@MapField(WMSOrderTrace.field_operateOrg)
	private String operate_org;

	/**
	 * 扩展属性1(妥投信息)
	 */
	@MapField(WMSOrderTrace.field_signedInfo)
	private String ext_attr1;

	/**
	 * 扩展属性2(未妥投的原因)
	 */
	@MapField(WMSOrderTrace.field_unreceiptedInfo)
	private String ext_attr2;

	/**
	 * 扩展属性3
	 */
	private String ext_attr3;

	/**
	 * 备注
	 */
	@MapField(WMSOrderTrace.field_desc)
	private String remark;

	/**
	 * @return the transaction_id
	 */
	public String getTransaction_id() {
		return transaction_id;
	}

	/**
	 * @param transaction_id
	 *            the transaction_id to set
	 */
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	/**
	 * @return the mailno
	 */
	public String getMailno() {
		return mailno;
	}

	/**
	 * @param mailno
	 *            the mailno to set
	 */
	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id
	 *            the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the operate_time
	 */
	public String getOperate_time() {
		return operate_time;
	}

	/**
	 * @param operate_time
	 *            the operate_time to set
	 */
	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}

	/**
	 * @return the operate_type
	 */
	public String getOperate_type() {
		return operate_type;
	}

	/**
	 * @param operate_type
	 *            the operate_type to set
	 */
	public void setOperate_type(String operate_type) {
		this.operate_type = operate_type;
	}

	/**
	 * @return the operate_desc
	 */
	public String getOperate_desc() {
		return operate_desc;
	}

	/**
	 * @param operate_desc
	 *            the operate_desc to set
	 */
	public void setOperate_desc(String operate_desc) {
		this.operate_desc = operate_desc;
	}

	/**
	 * @return the operate_org
	 */
	public String getOperate_org() {
		return operate_org;
	}

	/**
	 * @param operate_org
	 *            the operate_org to set
	 */
	public void setOperate_org(String operate_org) {
		this.operate_org = operate_org;
	}

	/**
	 * @return the ext_attr1
	 */
	public String getExt_attr1() {
		return ext_attr1;
	}

	/**
	 * @param ext_attr1
	 *            the ext_attr1 to set
	 */
	public void setExt_attr1(String ext_attr1) {
		this.ext_attr1 = ext_attr1;
	}

	/**
	 * @return the ext_attr2
	 */
	public String getExt_attr2() {
		return ext_attr2;
	}

	/**
	 * @param ext_attr2
	 *            the ext_attr2 to set
	 */
	public void setExt_attr2(String ext_attr2) {
		this.ext_attr2 = ext_attr2;
	}

	/**
	 * @return the ext_attr3
	 */
	public String getExt_attr3() {
		return ext_attr3;
	}

	/**
	 * @param ext_attr3
	 *            the ext_attr3 to set
	 */
	public void setExt_attr3(String ext_attr3) {
		this.ext_attr3 = ext_attr3;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
