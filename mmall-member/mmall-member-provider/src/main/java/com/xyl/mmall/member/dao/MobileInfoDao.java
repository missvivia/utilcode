/**
 * 
 */
package com.xyl.mmall.member.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.member.meta.MobileInfo;

/**
 * @author lihui
 *
 */
public interface MobileInfoDao extends AbstractDao<MobileInfo> {

	/**
	 * @param initId
	 * @return
	 */
	MobileInfo findByInitId(String initId);

	/**
	 * @param token
	 * @return
	 */
	MobileInfo findByMobileToken(String token);

	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	MobileInfo findByInitIdAndMobileToken(String initId, String token);

}
