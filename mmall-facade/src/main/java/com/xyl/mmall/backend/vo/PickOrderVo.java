/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.util.List;

import com.xyl.mmall.oms.dto.PickOrderDTO;
import com.xyl.mmall.oms.dto.PickSkuDTO;

/**
 * @author hzzengdan
 * @date 2014-09-18
 */
public class PickOrderVo {
  
	private PickOrderDTO pickDto;
	
	private List<PickSkuDTO> pickSkuDtoList;
	
	public PickOrderVo(PickOrderDTO dto){
		this.setPickDto(dto);
	}

	public PickOrderDTO getPickDto() {
		return pickDto;
	}

	public void setPickDto(PickOrderDTO pickDto) {
		this.pickDto = pickDto;
	}

	public List<PickSkuDTO> getPickSkuDtoList() {
		return pickSkuDtoList;
	}

	public void setPickSkuDtoList(List<PickSkuDTO> pickSkuDtoList) {
		this.pickSkuDtoList = pickSkuDtoList;
	}

}
