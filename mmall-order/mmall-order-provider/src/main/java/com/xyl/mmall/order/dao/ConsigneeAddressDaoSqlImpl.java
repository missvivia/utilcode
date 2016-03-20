package com.xyl.mmall.order.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.ConsigneeAddress;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月11日 下午4:58:24
 *
 */
@Repository
public class ConsigneeAddressDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<ConsigneeAddress> implements
		ConsigneeAddressDao {

	private String tableName = this.getTableName();

	private String sqlDelete = "DELETE FROM " + tableName + " WHERE id=? AND userId=?";

	private String sqlSelectDefaultByUserId = "SELECT id, userId, provinceId, cityId, sectionId, streetId, address, zipcode, consigneeName, consigneeTel, consigneeMobile, isDefault, ctime,addressComment,areaCode, addFrom FROM "
			+ tableName + " WHERE userId=? AND isDefault=1 LIMIT 1";

	private String sqlUpdateDefault = "UPDATE " + tableName + " SET isDefault=? WHERE id=? AND userId=?";

	private String sqlUpdateAllDefault = "UPDATE " + tableName + " SET isDefault=? WHERE userId=?";

	private String sqlUpdateById = "UPDATE "
			+ tableName
			+ " SET provinceId=?, cityId=?, sectionId=?, streetId=?, address=?, zipcode=?, consigneeName=?, consigneeTel=?, consigneeMobile=?, isDefault=? , addressComment=?, areaCode=? WHERE id=? AND userId=?";

	private static final String SQL_SELECT_USER_ID_BY_MOBILE = "SELECT DISTINCT UserId FROM Mmall_Order_ConsigneeAddress WHERE consigneeMobile=?";

	private static final String SQL_COUNT_USER_ID_BY_MOBILE = "SELECT COUNT(DISTINCT UserId) FROM Mmall_Order_ConsigneeAddress WHERE consigneeMobile=?";

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.ConsigneeAddressDao#deleteConsigneeAddress(long,
	 *      long)
	 */
	@Override
	public boolean deleteConsigneeAddress(long id, long userId) {
		return this.getSqlSupport().excuteUpdate(sqlDelete, id, userId) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.ConsigneeAddressDao#getConsigneeAddressListByUserId(long)
	 */
	@Override
	public List<ConsigneeAddress> getConsigneeAddressListByUserId(long userId) {
		DDBParam ddbParam = DDBParam.genParam500();
		ddbParam.setOrderColumn("ctime");
		ddbParam.setAsc(false);
		return queryInfoListByUserId(userId, ddbParam);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.ConsigneeAddressDao#updateConsigneeAddress(com.xyl.mmall.order.meta.ConsigneeAddress)
	 */
	@Override
	public boolean updateConsigneeAddress(ConsigneeAddress consigneeAddress) {
		/**
		 * SET provinceId=?, cityId=?, sectionId=?, streetId=?, address=?,
		 * zipcode=?, consigneeName=?, consigneeTel=?, consigneeMobile=?,
		 * isDefault=? WHERE id=? AND userId=?
		 */
		return this.getSqlSupport().excuteUpdate(sqlUpdateById, consigneeAddress.getProvinceId(),
				consigneeAddress.getCityId(), consigneeAddress.getSectionId(), consigneeAddress.getStreetId(),
				consigneeAddress.getAddress(), consigneeAddress.getZipcode(), consigneeAddress.getConsigneeName(),
				consigneeAddress.getConsigneeTel(), consigneeAddress.getConsigneeMobile(),
				consigneeAddress.isDefault(),consigneeAddress.getAddressComment(),consigneeAddress.getAreaCode(), consigneeAddress.getId(), consigneeAddress.getUserId()) >= 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.ConsigneeAddressDao#getDefaultByUserId(long)
	 */
	@Override
	public ConsigneeAddress getDefaultByUserId(long userId) {
		return queryObject(sqlSelectDefaultByUserId, userId);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.ConsigneeAddressDao#updateDefault(long,
	 *      long, boolean)
	 */
	@Override
	public boolean updateDefault(long id, long userId, boolean bDef) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateDefault, bDef, id, userId) >= 0;
	}

	@Override
	public List<ConsigneeAddress> queryInfoListByUserId(long userId, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		return getListByDDBParam(sql.toString(), ddbParam);
	}	

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.ConsigneeAddressDao#getConsigneeAddressByIdAndUserId(long,
	 *      long)
	 */
	@Override
	public ConsigneeAddress getConsigneeAddressByIdAndUserId(long id, long userId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "Id", id);
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		return queryObject(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.ConsigneeAddressDao#queryUserIdByConsigneeMobile(java.lang.String,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ConsigneeAddress> queryUserIdByConsigneeMobile(String consigneeMobile, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_SELECT_USER_ID_BY_MOBILE);
		SqlGenUtil.appendDDBParam(sql, ddbParam, null);
		return queryObjects(sql.toString(), consigneeMobile);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.ConsigneeAddressDao#countUserIdByConsigneeMobile(java.lang.String)
	 */
	@Override
	public int countUserIdByConsigneeMobile(String consigneeMobile) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_COUNT_USER_ID_BY_MOBILE);
		return getSqlSupport().queryCount(sql.toString(), consigneeMobile);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.ConsigneeAddressDao#updateAllDefault(long,
	 *      boolean)
	 */
	@Override
	public boolean updateAllDefault(long userId, boolean isDefault) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateAllDefault, isDefault, userId) >= 0;
	}

}
