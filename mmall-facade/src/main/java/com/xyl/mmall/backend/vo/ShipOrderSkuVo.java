/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.xyl.mmall.oms.dto.ShipSkuDTO;

/**
 * @author hzzengdan
 *
 */
public class ShipOrderSkuVo implements Serializable{
	/**
	 * 序列
	 */
	private static final long serialVersionUID = -4877305397160387931L;
	
	private ShipSkuDTO shipSkuDto;
	
	public ShipOrderSkuVo(ShipSkuDTO dto){
		this.setShipSkuDto(dto);
	}

	public ShipSkuDTO getShipSkuDto() {
		return shipSkuDto;
	}

	public void setShipSkuDto(ShipSkuDTO shipSkuDto) {
		this.shipSkuDto = shipSkuDto;
	}
	
}
