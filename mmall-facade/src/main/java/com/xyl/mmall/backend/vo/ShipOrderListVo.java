/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.xyl.mmall.oms.dto.ShipOrderDTO;

/**
 * @author hzzengdan
 *
 */
public class ShipOrderListVo implements Serializable{

	/**
	 * 序列
	 */
	private static final long serialVersionUID = -5319464353387834901L;
	
	private ShipOrderDTO shipDto;
	
	public ShipOrderListVo (ShipOrderDTO dto){
		this.setShipDto(dto);
	}



	public ShipOrderDTO getShipDto() {
		return shipDto;
	}



	public void setShipDto(ShipOrderDTO shipDto) {
		this.shipDto = shipDto;
	}

}
