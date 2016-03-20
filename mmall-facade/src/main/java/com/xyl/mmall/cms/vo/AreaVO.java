/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;

/**
 * AreaVO.java created by yydx811 at 2015年7月3日 上午11:06:40
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public class AreaVO implements Serializable {

	/** . */
	private static final long serialVersionUID = 7422130110495790022L;

	/** 区域id. */
	private long areaId;

	/** 区域名. */
	private String areaName;

//	/** 是否取全部子区域. */
//	private int isAll;
//
//	/** 子区域. */
//	private List<AreaVO> subAreaList;

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

//	public int getIsAll() {
//		return isAll;
//	}
//
//	public void setIsAll(int isAll) {
//		this.isAll = isAll;
//	}
//
//	public List<AreaVO> getSubAreaList() {
//		return subAreaList;
//	}
//
//	public void setSubAreaList(List<AreaVO> subAreaList) {
//		this.subAreaList = subAreaList;
//	}
}
