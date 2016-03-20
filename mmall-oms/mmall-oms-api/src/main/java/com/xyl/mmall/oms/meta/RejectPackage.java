package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.enums.RejectPackageState;

/**
 * 用户拒收（未签收）后的商品包裹退货表（拒收后，将包裹商品退回仓库）
 * 
 * @author hzzengchengyuan
 *
 */
@AnnonOfClass(desc = "用户拒收件", tableName = "Mmall_Oms_RejectPackage")
public class RejectPackage implements Serializable {
	
	/** 序列. */
	private static final long serialVersionUID = -93227604113144052L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true, policy = true)
	private long rejectPackageId;

	@AnnonOfField(desc = "快递公司")
	private String expressCompany;

	@AnnonOfField(desc = "快递号")
	private String expressNO;
	
	@AnnonOfField(desc = "返仓时的快递公司")
	private String returnExpressCompany;
	
	@AnnonOfField(desc = "返仓时的快递号")
	private String returnExpressNO;
	
	@AnnonOfField(desc = "仓库Id")
	private long warehouseId;
	
	@AnnonOfField(desc = "OMS订单id")
	private long omsOrderFormId;
	
	@AnnonOfField(desc = "用户订单id")
	private long userOrderFormId;

	@AnnonOfField(desc = "包裹重量")
	private long weight;

	@AnnonOfField(desc = "拒收包裹状态")
	private RejectPackageState state;

	@AnnonOfField(desc = "包裹状态更新时间")
	private long stateUpdateTime;

	@AnnonOfField(desc = "创建时间")
	private long createTime;
	
	public RejectPackage() {
		this(null);
	}

	public RejectPackage(OmsOrderPackage pack) {
		if (pack != null) {
			ReflectUtil.convertObj(this, pack, true);
		}
		setCreateTime(System.currentTimeMillis());
		setStateUpdateTime(System.currentTimeMillis());
		setState(RejectPackageState.SENDWMS);
	}

	public long getRejectPackageId() {
		return rejectPackageId;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public String getExpressNO() {
		return expressNO;
	}

	public String getReturnExpressCompany() {
		return returnExpressCompany;
	}

	public void setReturnExpressCompany(String returnExpressCompany) {
		this.returnExpressCompany = returnExpressCompany;
	}

	public String getReturnExpressNO() {
		return returnExpressNO;
	}

	public void setReturnExpressNO(String returnExpressNO) {
		this.returnExpressNO = returnExpressNO;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public long getOmsOrderFormId() {
		return omsOrderFormId;
	}

	public long getUserOrderFormId() {
		return userOrderFormId;
	}

	public void setUserOrderFormId(long userOrderFormId) {
		this.userOrderFormId = userOrderFormId;
	}

	public long getWeight() {
		return weight;
	}

	public RejectPackageState getState() {
		return state;
	}

	public long getStateUpdateTime() {
		return stateUpdateTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setRejectPackageId(long rejectPackageId) {
		this.rejectPackageId = rejectPackageId;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public void setExpressNO(String expressNO) {
		this.expressNO = expressNO;
	}

	public void setOmsOrderFormId(long omsOrderFormId) {
		this.omsOrderFormId = omsOrderFormId;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}

	/**
	 * 设置包裹商品状态，同时设置状态更新时间为：System.currentTimeMillis()
	 * @param state
	 */
	public void setState(RejectPackageState state) {
		this.state = state;
		setStateUpdateTime(System.currentTimeMillis());
	}

	public void setStateUpdateTime(long packageStateUpdateTime) {
		this.stateUpdateTime = packageStateUpdateTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
