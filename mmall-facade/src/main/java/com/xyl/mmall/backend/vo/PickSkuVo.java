/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.xyl.mmall.oms.dto.PickSkuDTO;

/**
 * @author hzzengdan
 *
 */
public class PickSkuVo implements Serializable{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 3922739510627886718L;
	
	private PickSkuDTO pickSkuDto;
  
  public PickSkuVo(PickSkuDTO dto){
	  this.setPickSkuDto(dto);
  }

  public PickSkuDTO getPickSkuDto() {
	return pickSkuDto;
  }

  public void setPickSkuDto(PickSkuDTO pickSkuDto) {
	this.pickSkuDto = pickSkuDto;
  }
  
  
}
