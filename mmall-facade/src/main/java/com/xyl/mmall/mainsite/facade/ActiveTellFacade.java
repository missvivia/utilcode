package com.xyl.mmall.mainsite.facade;

import java.util.List;

import com.xyl.mmall.saleschedule.dto.ActiveTellCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ActiveTellDTO;
import com.xyl.mmall.saleschedule.meta.ActiveTell;

/**
 * active tell service
 * 
 * @author hzzhaozhenzuo
 *
 */
public interface ActiveTellFacade {
	
	/**
	 * 保存活动通知
	 * 
	 * @param activeTellDTO
	 * @return
	 */
	public ActiveTellDTO saveActiveTell(ActiveTellDTO activeTellDTO);

	/**
	 * 根据条件对象查找活动通知记录
	 * 
	 * @param paramSo
	 * @return
	 */
	public List<ActiveTell> getActiveTellByParam(ActiveTellCommonParamDTO paramSo);
	
	/**
	 * 移除activeTell记录
	 * @param activeTellList
	 * @return
	 */
	public boolean removeActiveTell(List<ActiveTell> activeTellList);
	
}
