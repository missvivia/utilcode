package com.xyl.mmall.cms.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.cms.meta.BusiUserRelation;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年8月20日下午3:29:47
 */
public class BusiUserRelationDTO extends BusiUserRelation{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1560054008196785633L;
	
	public BusiUserRelationDTO(){
		
	}
	
	public BusiUserRelationDTO(BusiUserRelation obj){
		ReflectUtil.convertObj(this, obj, false);
	}

}
