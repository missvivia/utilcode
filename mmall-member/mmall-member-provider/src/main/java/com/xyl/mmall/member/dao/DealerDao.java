/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.member.meta.Dealer;

/**
 * @author lihui
 *
 */
public interface DealerDao extends AbstractDao<Dealer> {

	/**
	 * 
	 * @param name
	 * @return
	 */
	Dealer findByName(String name);

	/**
	 * @param supplierId
	 */
	List<Dealer> findBySupplierId(long supplierId);

	/**
	 * @param userId
	 * @param param
	 * @return
	 */
	List<Dealer> findByRoleOwnerId(long userId, DDBParam param);

	/**
	 * @param userId
	 * @return
	 */
	int countByRoleOwnerId(long userId);

	/**
	 * @param supplierId
	 * @param userId
	 */
	boolean lockAllSupplierAccount(long supplierId, long userId);

	/**
	 * @param idList
	 * @return
	 */
	List<Dealer> findDealerByIdList(List<Long> idList);

	/**
	 * @param supplierId
	 * @param param
	 * @return
	 */
	List<Dealer> findBySupplierId(long supplierId, DDBParam param);

	/**
	 * @param supplierId
	 * @return
	 */
	int countBySupplierId(long supplierId);
}
