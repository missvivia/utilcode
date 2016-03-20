package com.xyl.mmall.itemcenter.dao.size;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.meta.SizeAssist;

public interface SizeAssistDao extends AbstractDao<SizeAssist> {
	public BaseSearchResult<SizeAssist> getSizeAssistList(long supplierId,int limit,int offset);
}
