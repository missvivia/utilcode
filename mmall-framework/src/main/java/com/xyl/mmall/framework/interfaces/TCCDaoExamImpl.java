package com.xyl.mmall.framework.interfaces;

import java.util.List;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.TCCUtil;

/**
 * @author dingmingliang
 *
 */
final class TCCDaoExamImpl  extends PolicyObjectDaoSqlBaseOfAutowired<String>  implements TCCDaoInterface<String> {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.framework.interfaces.TCCDaoInterface#getListByTranId(long)
	 */
	public List<String> getListByTranId(long tranId) {
		return TCCUtil.getListByTranId(tranId, this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.framework.interfaces.TCCDaoInterface#deleteByTranId(long)
	 */
	public boolean deleteByTranId(long tranId) {		
		return TCCUtil.deleteByTranId(tranId, this);
	}

}
