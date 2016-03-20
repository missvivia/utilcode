/**
 * 
 */
package com.xyl.mmall.mainsite.vo;

import org.apache.commons.lang.math.RandomUtils;

import com.xyl.mmall.content.dto.BubbleActivityDTO;

/**
 * @author hzlihui2014
 *
 */
public class BubbleActivityVO {

	private BubbleActivityDTO bubbleActivityDTO;

	public BubbleActivityVO() {
		bubbleActivityDTO = new BubbleActivityDTO();
	}

	/**
	 * @param bubbleActivityDTO
	 */
	public BubbleActivityVO(BubbleActivityDTO bubbleActivityDTO) {
		this.bubbleActivityDTO = bubbleActivityDTO;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return bubbleActivityDTO.getId();
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		bubbleActivityDTO.setId(id);
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return bubbleActivityDTO.getType();
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		bubbleActivityDTO.setType(type);
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return RandomUtils.nextInt(4);
	}

}
