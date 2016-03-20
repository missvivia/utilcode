package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.framework.vo.BaseJsonListVO;

@JsonInclude(Include.NON_NULL)
public class MobilePODetailVO extends BaseJsonListVO implements Serializable{
		
	private static final long serialVersionUID = -487700508495212338L;
	//专场信息
	private MobilePoVO	po;
	//过滤头部信息返回接口
	private List<MobileKeyPairVO> filterList;
	public MobilePoVO getPo() {
		return po;
	}
	public void setPo(MobilePoVO po) {
		this.po = po;
	}
	public List<MobileKeyPairVO> getFilterList() {
		return filterList;
	}
	public void setFilterList(List<MobileKeyPairVO> filterList) {
		this.filterList = filterList;
	}
	
	
}
