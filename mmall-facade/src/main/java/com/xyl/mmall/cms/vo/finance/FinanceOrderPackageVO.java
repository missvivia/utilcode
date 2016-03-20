package com.xyl.mmall.cms.vo.finance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.DateFormatUtil;
import com.xyl.mmall.order.dto.OrderCartItemDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;

/**
 * 财务-包裹.
 * 
 * @author wangfeng
 *
 */
public class FinanceOrderPackageVO implements Serializable {

	private static final long serialVersionUID = 2799507522791230535L;

	/** 包裹号. */
	private String packageId;

	/** 包裹状态. */
	private String packageState;

	/** 快递公司. */
	private String expressCompanyName;

	/** 发货日期. */
	private String expSDate;

	private List<FinanceOrderSkuVO> financeOrderSkuList;

	public FinanceOrderPackageVO() {
		super();
	}

	@SuppressWarnings("unchecked")
	public FinanceOrderPackageVO(OrderPackageDTO orderPackageDTO) {
		super();
		this.packageId = orderPackageDTO.getPackageId() > 0L ? String.valueOf(orderPackageDTO.getPackageId()) : "";
		this.packageState = orderPackageDTO.getOrderPackageState().getDesc();
		this.expressCompanyName = StringUtils.isNotBlank(orderPackageDTO.getExpressCompanyReturn()) ? orderPackageDTO
				.getExpressCompanyReturn() : "";
		this.expSDate = orderPackageDTO.getExpSTime() == 0 ? "" : DateFormatUtil.getFormatDateType5(orderPackageDTO
				.getExpSTime());
		// 包裹下商品信息
		financeOrderSkuList = new ArrayList<FinanceOrderSkuVO>();
		List<OrderCartItemDTO> orderCartItemDTOList = (List<OrderCartItemDTO>) orderPackageDTO
				.getOrderCartItemDTOList();
		if (CollectionUtil.isNotEmptyOfList(orderCartItemDTOList)) {
			for (OrderCartItemDTO orderCartItemDTO : orderCartItemDTOList) {
				List<OrderSkuDTO> orderSkuDTOList = (List<OrderSkuDTO>) orderCartItemDTO.getOrderSkuDTOList();
				if (CollectionUtil.isNotEmptyOfList(orderSkuDTOList)) {
					for (OrderSkuDTO orderSkuDTO : orderSkuDTOList) {
						FinanceOrderSkuVO financeOrderSkuVO = new FinanceOrderSkuVO(orderSkuDTO);
						financeOrderSkuList.add(financeOrderSkuVO);
					}
				}
			}
		}
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getPackageState() {
		return packageState;
	}

	public void setPackageState(String packageState) {
		this.packageState = packageState;
	}

	public String getExpressCompanyName() {
		return expressCompanyName;
	}

	public void setExpressCompanyName(String expressCompanyName) {
		this.expressCompanyName = expressCompanyName;
	}

	public String getExpSDate() {
		return expSDate;
	}

	public void setExpSDate(String expSDate) {
		this.expSDate = expSDate;
	}

	public List<FinanceOrderSkuVO> getFinanceOrderSkuList() {
		return financeOrderSkuList;
	}

	public void setFinanceOrderSkuList(List<FinanceOrderSkuVO> financeOrderSkuList) {
		this.financeOrderSkuList = financeOrderSkuList;
	}

}
