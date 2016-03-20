package com.xyl.mmall.content.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.content.enums.ContentType;
import com.xyl.mmall.content.enums.NCSIndexDispatchState;
import com.xyl.mmall.content.meta.NCSContentDispatchLog;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public interface NCSContentDispatchLogDao extends AbstractDao<NCSContentDispatchLog> {

	/**
	 * 根据内容类型、索引状态以及最小Id查询索引记录
	 * 
	 * @param minId
	 * @param contentType
	 * @param states
	 * @param param
	 * @return
	 */
	public List<NCSContentDispatchLog> getDispatchLogByTypeAndStateWithMinId(long minId, 
			ContentType[] contentTypes, NCSIndexDispatchState[] states, DDBParam param);
	
	/**
	 * 更新索引状态
	 * 
	 * @param log
	 * @param newStates
	 * @param oldStates
	 * @return
	 */
	public boolean updateDispatchLog(NCSContentDispatchLog log, NCSIndexDispatchState newState, NCSIndexDispatchState[] oldStates);
}
