/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;
import java.util.List;

import com.xyl.mmall.oms.dto.ShipOrderDTO;
import com.xyl.mmall.oms.dto.ShipSkuDTO;

/**
 * @author hzzengdan
 * 
 */
public class ShipOrderVo implements Serializable {
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 6825498394394460815L;

	private ShipOrderDTO shipOrderDTO;
	
	private List<ShipSkuDTO> shipSkuDTOs;

	public ShipOrderDTO getShipOrderDTO() {
		return shipOrderDTO;
	}

	public void setShipOrderDTO(ShipOrderDTO shipOrderDTO) {
		this.shipOrderDTO = shipOrderDTO;
	}

	public List<ShipSkuDTO> getShipSkuDTOs() {
		return shipSkuDTOs;
	}

	public void setShipSkuDTOs(List<ShipSkuDTO> shipSkuDTOs) {
		this.shipSkuDTOs = shipSkuDTOs;
	}

}
