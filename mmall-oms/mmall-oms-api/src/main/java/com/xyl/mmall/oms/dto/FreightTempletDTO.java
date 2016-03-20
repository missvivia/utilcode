package com.xyl.mmall.oms.dto;

import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.oms.enums.WMSExpressCompany;
import com.xyl.mmall.oms.enums.WarehouseType;

/**
 * 
 * 
 * @author hzzengchengyuan
 *
 */
public class FreightTempletDTO {
	/**
	 * 模板ID
	 */
	private String templetId;

	/**
	 * 仓库公司类别
	 */
	private WarehouseType warehouseType;

	/**
	 * 快递类别，一般情况下仓库和快递公司是同一家
	 */
	private WMSExpressCompany expressCompany;

	/**
	 * 仓库始发地
	 */
	private Region origin;

	/**
	 * 目的地
	 */
	private List<Region> destinations;

	/**
	 * 配送类型，如：市内配送、跨省配送、邻省配送等
	 */
	private String deliverType;

	/**
	 * 首重，单位KG
	 */
	private int startWeight;

	/**
	 * 首重费用
	 */
	private float startCost;

	/**
	 * 每次续重的最小重量，单位KG
	 */
	private int continueWeight;

	/**
	 * 续重费用
	 */
	private float continueCost;

	/**
	 * 货到付款服务费率。如： 0.01 = 1.0%; 0.001 = 1.0‰
	 */
	private float rateCod;

	/**
	 * 反向运费费率，指在未妥投的情况下，包裹返仓的运费费率。
	 */
	private float rateReverse;

	/**
	 * 反向服务费，一般为固定的值，按单收取
	 */
	private double reverseServiceCharge;

	/**
	 * 是否保价
	 */
	private boolean isInsurance;

	/**
	 * 保价率
	 */
	private float rateInsurance;

	/**
	 * 分拣包装费
	 */
	private double pickPackageCost;

	/**
	 * 耗材费
	 */
	private double consumablesCost;

	/**
	 * 拦截费
	 */
	private double interceptOrderCost;

	/**
	 * 是否是一口价
	 */
	private boolean isOnePrice;

	/**
	 * 一口价金额
	 */
	private double onePrice;

	/**
	 * 丢件赔偿额
	 */
	private double lostCompensate;

	public FreightTempletDTO() {

	}

	public String getTempletId() {
		return templetId;
	}

	public void setTempletId(String templetId) {
		this.templetId = templetId;
	}

	public WarehouseType getWarehouseType() {
		return warehouseType;
	}

	public void setWarehouseType(WarehouseType warehouseType) {
		if (warehouseType == null) {
			throw new NullPointerException();
		}
		this.warehouseType = warehouseType;
	}

	public WMSExpressCompany getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(WMSExpressCompany expressCompany) {
		if (expressCompany == null) {
			throw new NullPointerException();
		}
		this.expressCompany = expressCompany;
	}

	public Region getOrigin() {
		return origin;
	}

	public void setOrigin(Region origin) {
		if (origin == null) {
			throw new NullPointerException();
		}
		this.origin = origin;
	}

	public List<Region> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<Region> destinations) {
		this.destinations = destinations;
	}

	public String getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}

	public void addDestination(Region region) {
		if (region == null) {
			throw new NullPointerException();
		}
		if (this.destinations == null) {
			this.destinations = new ArrayList<Region>();
		}
		if (!this.destinations.contains(region)) {
			this.destinations.add(region);
		}
	}

	/**
	 * 是否包含目的地
	 * 
	 * @return
	 */
	public boolean isContainsDest(Region region) {
		if (this.destinations != null) {
			return this.destinations.contains(region);
		}
		return false;
	}

	public boolean isContainsDestByCode(String code) {
		if (this.destinations != null) {
			for (Region r : this.destinations) {
				Region searchResult = r.searchByCode(code);
				if (searchResult != null) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isContainsDestByName(String name) {
		if (this.destinations != null) {
			for (Region r : this.destinations) {
				Region searchResult = r.searchByName(name);
				if (searchResult != null) {
					return true;
				}
			}
		}
		return false;
	}

	public int getStartWeight() {
		return startWeight;
	}

	public void setStartWeight(int startWeight) {
		this.startWeight = startWeight;
	}

	public float getStartCost() {
		return startCost;
	}

	public void setStartCost(float startCost) {
		this.startCost = startCost;
	}

	public int getContinueWeight() {
		return continueWeight;
	}

	public void setContinueWeight(int continueWeight) {
		this.continueWeight = continueWeight;
	}

	public float getContinueCost() {
		return continueCost;
	}

	public void setContinueCost(float continueCost) {
		this.continueCost = continueCost;
	}

	public float getRateCod() {
		return rateCod;
	}

	public void setRateCod(float rateCod) {
		this.rateCod = rateCod;
	}

	public float getRateReverse() {
		return rateReverse;
	}

	public void setRateReverse(float rateReverse) {
		this.rateReverse = rateReverse;
	}

	public boolean isInsurance() {
		return isInsurance;
	}

	public void setInsurance(boolean isInsurance) {
		this.isInsurance = isInsurance;
	}

	public float getRateInsurance() {
		return rateInsurance;
	}

	public void setRateInsurance(float rateInsurance) {
		this.rateInsurance = rateInsurance;
	}

	public double getPickPackageCost() {
		return pickPackageCost;
	}

	public double getConsumablesCost() {
		return consumablesCost;
	}

	public double getInterceptOrderCost() {
		return interceptOrderCost;
	}

	public boolean isOnePrice() {
		return isOnePrice;
	}

	public void setOnePrice(boolean isOnePrice) {
		this.isOnePrice = isOnePrice;
	}

	public double getOnePrice() {
		return onePrice;
	}

	public double getLostCompensate() {
		return lostCompensate;
	}

	public void setPickPackageCost(double pickPackageCost) {
		this.pickPackageCost = pickPackageCost;
	}

	public void setConsumablesCost(double consumablesCost) {
		this.consumablesCost = consumablesCost;
	}

	public void setInterceptOrderCost(double interceptOrderCost) {
		this.interceptOrderCost = interceptOrderCost;
	}

	public void setOnePrice(double onePrice) {
		this.onePrice = onePrice;
	}

	public void setLostCompensate(double lostCompensate) {
		this.lostCompensate = lostCompensate;
	}

	public double getReverseServiceCharge() {
		return reverseServiceCharge;
	}

	public void setReverseServiceCharge(double reverseServiceCharge) {
		this.reverseServiceCharge = reverseServiceCharge;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof FreightTempletDTO)) {
			return false;
		}
		FreightTempletDTO other = (FreightTempletDTO) obj;
		if (getTempletId() != null && getTempletId().equals(other.getTempletId())) {
			return true;
		}
		if (getTempletId() == null && other.getTempletId() == null) {
			return true;
		}
		return false;
	}
}
