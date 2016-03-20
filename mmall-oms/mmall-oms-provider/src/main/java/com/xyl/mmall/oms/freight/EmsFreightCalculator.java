package com.xyl.mmall.oms.freight;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.oms.dto.Region;
import com.xyl.mmall.oms.enums.WMSExpressCompany;
import com.xyl.mmall.oms.meta.FreightTemplet;
import com.xyl.mmall.oms.service.FreightTempletService;

/**
 * @author hzzengchengyuan
 *
 */
@Service
public class EmsFreightCalculator implements FreightCalculator {

	@Autowired
	private FreightTempletService templetService;

	@Override
	public WMSExpressCompany type() {
		return WMSExpressCompany.EMS;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.freight.FreightCalculator#calcuate(com.xyl.mmall.oms.freight.FreightCalcMeta)
	 */
	@Override
	public FreightCalcResult calcuate(FreightCalcMeta meta) {
		FreightCalcResult result = new FreightCalcResult();
		Region destProvince = meta.getDest().getParent().getParent();
		FreightTemplet templet = templetService.getFreightTemplet(meta.getExpressCompany(), meta.getOrigin(),
				destProvince);
		result.setTemplate(templet);
		// g转换成kg
		double weight = meta.getWeight() / 1000;

		// 1.计算正向物流费
		if (templet.isOnePrice()) {
			result.setStartCharge(0);
			result.setContinueCharge(0);
			result.setOnePrice(templet.getOnePrice());
		} else {
			result.setStartCharge(templet.getStartCost());
			// 如果重量超出了首重，则计算续重费用
			if (weight > templet.getStartWeight()) {
				int count = new BigDecimal((weight - templet.getStartWeight()) / templet.getContinueWeight()).setScale(
						0, BigDecimal.ROUND_UP).intValue();
				result.setContinueCharge(templet.getContinueCost().multiply(new BigDecimal(count)));
			}
		}
		if (meta.isInsurance() && templet.isInsurance()) {
			result.setInsuranceCharge(templet.getRateInsurance().multiply(new BigDecimal(meta.getOrderAmount())));
		}
		result.setWarehouseInsideCharge(templet.getPickPackageCost().add(templet.getConsumablesCost()));
		result.setExpressCharge(result.getStartCharge().add(result.getContinueCharge())
				.add(result.getInsuranceCharge()));

		// 2.计算反向物流费用
		if (meta.isReverse()) {
			//需求方又说，不会有反向保价费，ok，先不计算
			//result.setReverseInsuranceCharge(templet.getRateInsurance().multiply(new BigDecimal(meta.getOrderAmount())));
			result.setReverseServiceCharge(templet.getReverseServiceCharge());
			result.setReverseCharge(result.getStartCharge().add(result.getContinueCharge()).multiply(templet.getRateReverse()));
			result.setReverseTotalCharge(result.getReverseInsuranceCharge().add(result.getReverseCharge()));
		}

		// 3.计算cod应收款
		if (meta.isCod()) {
			result.setCodCharge(templet.getRateCod().multiply(new BigDecimal(meta.getCodAmount())));
			if (result.getCodCharge().compareTo(templet.getCodMinCharge()) < 0 ) {
				result.setCodCharge(templet.getCodMinCharge());
			}
			result.setEducationCharge(getEducationCharge(meta.getDest()));
		}
		if (meta.isReverse() && meta.isCod()) {
			// 如果是反向且是cod，则表示包裹未签收
			result.setReverseServiceCharge(templet.getReverseServiceCharge());
			// #jira MMALL-2002去掉返货处理费（反向服务费）
			result.setCodCollection(result.getEducationCharge().add(result.getReverseCharge()).negate());
		} else if (meta.isCod()) {
			BigDecimal reverseExpressFreight = result.getEducationCharge().add(result.getCodCharge()).add(result.getInsuranceCharge());
			result.setCodCollection(new BigDecimal(meta.getCodAmount()).subtract(reverseExpressFreight));
		}
		result.formatterRoundHalfUp(2);
		return result;
	}

	public int getEducationCharge(Region dest) {
		if (templetService.isEducationDistrict(ExpressCompany.EMS, Long.parseLong(dest.getCode()), Long.parseLong(dest.getParentCode()))) {
			return 5;
		} else {
			return 0;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.freight.FreightCalculator#getUserReturnServieCharge()
	 */
	@Override
	public double getUserReturnServieCharge() {
		return 2;
	}

}
