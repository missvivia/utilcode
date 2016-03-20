/**
 * 
 */
package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.dto.RejectPackageDTO;

/**
 * @author hzzengchengyuan
 *
 */
public interface RejectPackageService {

	/**
	 * 同意将包裹设为拒收件
	 * 
	 * @param expressCompany
	 * @param expressNO
	 * @return
	 */
	boolean agreeRejectPackage(String expressCompany, String expressNO);

	/**
	 * 根据设为拒件时间范围查询
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<RejectPackageDTO> queryByCreateTime(long startTime, long endTime);

}
