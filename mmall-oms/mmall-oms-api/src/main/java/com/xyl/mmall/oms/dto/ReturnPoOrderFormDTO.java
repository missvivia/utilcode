/**
 * 
 */
package com.xyl.mmall.oms.dto;

import java.util.ArrayList;
import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.meta.ReturnPoOrderForm;

/**
 * @author hzzengchengyuan
 *
 */
public class ReturnPoOrderFormDTO extends ReturnPoOrderForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 退供单商品明细
	 */
	private List<ReturnPoOrderFormSkuDTO> skuDetails;

	/**
	 * 退货人信息，仅在商家确认退供单时使用和提供
	 */
	private PersonContactInfoDTO receivePerson;

	public ReturnPoOrderFormDTO() {

	}

	public ReturnPoOrderFormDTO(ReturnPoOrderForm form) {
		ReflectUtil.convertObj(this, form, false);
	}

	/**
	 * @return the skuDetails
	 */
	public List<ReturnPoOrderFormSkuDTO> getSkuDetails() {
		return skuDetails;
	}

	/**
	 * @param skuDetails
	 *            the skuDetails to set
	 */
	public void setSkuDetails(List<ReturnPoOrderFormSkuDTO> skuDetails) {
		this.skuDetails = skuDetails;
	}

	public void addSkuDetail(ReturnPoOrderFormSkuDTO sku) {
		if (this.skuDetails == null) {
			this.skuDetails = new ArrayList<ReturnPoOrderFormSkuDTO>();
		}
		this.skuDetails.add(sku);
	}

	/**
	 * @return the returnPerson
	 */
	public PersonContactInfoDTO getReturnPerson() {
		return receivePerson;
	}

	/**
	 * @param returnPerson
	 *            the returnPerson to set
	 */
	public void setReturnPerson(PersonContactInfoDTO returnPerson) {
		this.receivePerson = returnPerson;
	}

}
