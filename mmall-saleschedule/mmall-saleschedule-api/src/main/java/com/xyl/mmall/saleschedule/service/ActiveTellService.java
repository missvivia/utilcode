package com.xyl.mmall.saleschedule.service;

import java.util.List;

import com.xyl.mmall.saleschedule.dto.ActiveTellCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ActiveTellDTO;
import com.xyl.mmall.saleschedule.meta.ActiveTell;

/**
 * 活动通知服务
 * 
 * @author hzzhaozhenzuo
 *
 */
public interface ActiveTellService {

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
