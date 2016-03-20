/**
 * 
 */
package com.xyl.mmall.backend.vo;

import com.xyl.mmall.oms.dto.PoOrderDTO;

/**
 * @author hzzengdan
 * @date 2014-09-18
 */
public class PoOrderVo {

	private PoOrderDTO poOrder;
	
	public PoOrderVo(PoOrderDTO dto){
		this.setPoOrder(dto);
	}

	public PoOrderDTO getPoOrder() {
		return poOrder;
	}

	public void setPoOrder(PoOrderDTO poOrder) {
		this.poOrder = poOrder;
	}
}
