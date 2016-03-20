package com.xyl.mmall.cms.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.cms.dto.BusinessConditionDTO;
import com.xyl.mmall.cms.enums.ActiveState;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;

/**
 * 
 * @author hzchaizhf
 * @create 2014年9月15日
 * 
 */
@Repository
public class BusinessDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<Business> implements BusinessDao {

	private static Logger logger = Logger.getLogger(BusinessDaoSqlImpl.class);

	private String tableName = this.getTableName();

	private String sqlDelete = "DELETE FROM " + tableName + " WHERE id=?";

	private String sqlUpdateById = "UPDATE "
			+ tableName
			+ " SET businessAccount=?, companyName=?, legalPerson=?, "
			+ "legalPersonID=?, holderName=?, holderID=?,holderIDPositiveImg=?,siteId=?,BatchCash=?, holderIDNegativeImg=?, "
			+ "accountLicense=?, registrationNumber=?, registrationNumberStart=?, registrationNumberEnd=?, "
			+ "registrationNumberAvaliable=?, registrationImg=?, registrationCopyImg=?, brandImg=?, brandAuthImg=?, "
			+ "contactName=?,  contactTel=?, contactEmail=?, contactProvince=?, contactCity=?, contactCountry=?, "
			+ "contactAddress=?, updateTime=?, registerFund=?, manageType=?, storeName=?, "
			+ "companyDesc=?, actingBrandId=?, type=?, updateBy=?,indexWeight=? WHERE id=? ";

	private String sql_update_business_state = "UPDATE " + tableName
			+ " SET isActive=?,updateTime=?,updateBy=? WHERE id=?";

	private String sql_update_business_batchCash = "UPDATE " + tableName
			+ " SET BatchCash=?,updateTime=?,updateBy=? WHERE id=?";

	private String sql_business_list = "SELECT bs.* FROM Mmall_CMS_Business bs WHERE 1=1 ";

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.BusinessDao.dao.ConsigneeAddressDao#deleteConsigneeAddress(long,
	 *      long)
	 */
	@Override
	public boolean deleteBusiness(long id) {
		return this.getSqlSupport().excuteUpdate(sqlDelete, id) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.dao.BusinessDao#getBusinessListByTypeId(int, long,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<Business> getBusinessListByTypeId(int type, long typeId, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (type == 0) {
			SqlGenUtil.appendExtParamObject(sql, "areaId", typeId);
		}
		if (type == 1) {
			SqlGenUtil.appendExtParamObject(sql, "actingBrandId", typeId);
		}
		if (type == 2) {
			SqlGenUtil.appendExtParamObject(sql, "warehouseId", typeId);
		}
		SqlGenUtil.appendDDBParam(sql, ddbParam, "");
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.BusinessDao.dao.BusinessDao#updateBusiness(com.xyl.mmall.BusinessDTO.meta.Business)
	 */
	@Override
	public boolean updateBusiness(Business business) {
		business.setUpdateTime(new Date().getTime());
		return this.getSqlSupport().excuteUpdate(sqlUpdateById, business.getBusinessAccount(),
				business.getCompanyName(), business.getLegalPerson(), business.getLegalPersonID(),
				business.getHolderName(), business.getHolderID(), business.getHolderIDPositiveImg(),
				business.getSiteId(), business.getBatchCash(), business.getHolderIDNegativeImg(),
				business.getAccountLicense(), business.getRegistrationNumber(), business.getRegistrationNumberStart(),
				business.getRegistrationNumberEnd(), business.getRegistrationNumberAvaliable(),
				business.getRegistrationImg(), business.getRegistrationCopyImg(), business.getBrandImg(),
				business.getBrandAuthImg(), business.getContactName(), business.getContactTel(),
				business.getContactEmail(), business.getContactProvince(), business.getContactCity(),
				business.getContactCountry(), business.getContactAddress(), business.getUpdateTime(),
				business.getRegisterFund(), business.getManageType(), business.getStoreName(),
				business.getCompanyDesc(), business.getActingBrandId(), business.getType(), business.getUpdateBy(),
				business.getIndexWeight(), business.getId()) > 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.meta.BusinessDao#getBusinessById(long)
	 */
	@Override
	public Business getBusinessById(long id, int isActive) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "id", id);
		if (isActive >= 0) {
			SqlGenUtil.appendExtParamObject(sql, "IsActive", isActive);
		}
		return queryObject(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.dao.BusinessDao#getTotal(int, long)
	 */
	@Override
	public int getTotal(long areaId, String businessAccount) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		List<Long> areaIds;
		try {
			areaIds = ProvinceCodeMapUtil.getCodeListByProvinceFmt(areaId);

			if (areaIds.size() == 1) {
				sql.append(" AND SiteId&" + areaId + "=" + areaId);
			} else if (areaIds.size() > 1) {
				sql.append(" AND SiteId&" + areaId + "=SiteId");
			}
			// if(areaId!=0){
			// sql.append(" AND SiteId&"+areaId + "=" + areaId);
			// }
			if (businessAccount != null && !"".equals(businessAccount.trim())) {
				if (isNumeric(businessAccount))
					SqlGenUtil.appendExtParamObject(sql, "id", businessAccount);
				else
					SqlGenUtil.appendExtParamObject(sql, "businessAccount", businessAccount);
			}
			return this.getSqlSupport().queryCount(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.dao.BusinessDao#getBusinessListByRegistrationNumber(java.lang.String)
	 */
	@Override
	public List<Business> getBusinessListByRegistrationNumber(String registrationNumber) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "registrationNumber", registrationNumber);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.dao.BusinessDao#getBusinessListByBusinessAccount(java.lang.String)
	 */
	@Override
	public List<Business> getBusinessListByBusinessAccount(String businessAccount) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "businessAccount", businessAccount);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.dao.BusinessDao#getBusinessListByAreaId(long)
	 */
	@Override
	public List<Business> getBusinessListByAreaId(long areaId) throws Exception {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (areaId > 0) {
			Long areaBitMap = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
			sql.append(" AND SiteId&" + areaBitMap + "=" + areaBitMap);
		}
		return queryObjects(sql);

	}

	@Override
	public List<Business> getBusinessListByAreaIdList(List<Long> areaIdList, long areaCode) {
		if (areaIdList == null || areaIdList.size() == 0) {
			return new ArrayList<Business>();
		}
		// StringBuilder idsString = new StringBuilder(256);
		// idsString.append("(");
		// for (Long areaId : areaIdList) {
		// idsString.append(areaId).append(",");
		// }
		// idsString.deleteCharAt(idsString.lastIndexOf(","));
		// idsString.append(") ");
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		// sql.append(" AND areaId IN ").append(idsString);
		Long areaBitMap; // cms管理员的权限
		try {
			areaBitMap = ProvinceCodeMapUtil.getProvinceFmtByCodeList(areaIdList);
			sql.append(" AND SiteId&" + areaBitMap + "=SiteId");
			if (areaCode > 0) {
				areaCode = ProvinceCodeMapUtil.getProvinceFmtByCode(areaCode);
				sql.append(" AND SiteId&" + areaCode + "=" + areaCode);
			}
			return queryObjects(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Business getBusinessAccount(String businessAccount) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "businessAccount", businessAccount);
		return queryObject(sql);

	}

	@Override
	public Business getBusinessByCompanyName(String companyName) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "companyName", companyName);
		return queryObject(sql);
	}

	@Override
	public Business getBusinessByAreaIdAndBrandId(Long areaId, Long brandId) throws Exception {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "actingBrandId", brandId);
		SqlGenUtil.appendExtParamObject(sql, "isActive", 0);
		Long areaBitMap = ProvinceCodeMapUtil.getProvinceFmtByCode(areaId);
		String subSql = " AND SiteId&" + areaBitMap + "=" + areaBitMap;
		return queryObject(sql + subSql);
	}

	@Override
	public List<Business> getBusinessListByProvinceOrAccount(long areaId, String businessAccount, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		try {
			List<Long> areaIds = ProvinceCodeMapUtil.getCodeListByProvinceFmt(areaId);
			if (areaIds.size() == 1) {
				sql.append(" AND SiteId&" + areaId + "=" + areaId);
			} else if (areaIds.size() > 1) {
				sql.append(" AND SiteId&" + areaId + "=SiteId");
			}
			// if(areaId!=0)
			// sql.append(" AND SiteId&"+areaId + "=" + areaId);
			if (businessAccount != null && !"".equals(businessAccount.trim()))
				if (businessAccount != null && !"".equals(businessAccount.trim())) {
					if (isNumeric(businessAccount.trim()))
						SqlGenUtil.appendExtParamObject(sql, "id", businessAccount.trim());
					else
						SqlGenUtil.appendExtParamObject(sql, "businessAccount", businessAccount.trim());
				}
			SqlGenUtil.appendDDBParam(sql, ddbParam, "");
			return queryObjects(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public boolean updateActiveByUserId(long businessid, long userid, ActiveState state) {
		return this.getSqlSupport().excuteUpdate(sql_update_business_state, state.getIntValue(), new Date().getTime(),
				userid, businessid) > 0;
	}

	@Override
	// TODO
	public boolean updateBatchCash(long businessid, long userid, BigDecimal batchCash, boolean isOpen) {
		return this.getSqlSupport().excuteUpdate(sql_update_business_batchCash, batchCash, new Date().getTime(),
				userid, businessid) > 0;
	}

	@Override
	public List<Business> getBusinessByAreaIdOrBrandIdOrAccount(Long areaId, Long brandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (areaId > 0) {
			sql.append(" AND SiteId&" + areaId + "=" + areaId);
		}
		if (brandId > 0) {
			SqlGenUtil.appendExtParamObject(sql, "actingBrandId", brandId);
		}
		SqlGenUtil.appendExtParamObject(sql, "isActive", 0);
		return queryObjects(sql);
	}

	@Override
	public List<Business> getBusinessByAccount(String businessAccount) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "businessAccount", businessAccount);
		return queryObjects(sql);
	}

	@Override
	public Business getBusinessByRegNum(String registrationNumber) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "registrationNumber", registrationNumber);
		SqlGenUtil.appendExtParamObject(sql, "isActive", 0);
		return queryObject(sql);
	}

	@Override
	public List<Business> getBusinessListByIdList(List<Long> idList) {
		if (idList == null || idList.size() == 0) {
			return new ArrayList<Business>();
		}
		StringBuilder idsString = new StringBuilder(256);
		idsString.append("(");
		for (Long id : idList) {
			idsString.append(id).append(",");
		}
		idsString.deleteCharAt(idsString.lastIndexOf(","));
		idsString.append(") ");
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND id IN ").append(idsString);
		return queryObjects(sql);
	}

	@Override
	public List<Business> getActiveBusinessByBrand(Long brandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "actingBrandId", brandId);
		SqlGenUtil.appendExtParamObject(sql, "isActive", 0);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.dao.BusinessDao#getBusinessByBrandIdAndType(java.lang.Long,
	 *      int)
	 */
	@Override
	public Business getBusinessByBrandIdAndType(Long brandId, int type) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "actingBrandId", brandId);
		SqlGenUtil.appendExtParamObject(sql, "type", type);
		SqlGenUtil.appendExtParamObject(sql, "isActive", 0);
		return queryObject(sql);
	}

	@Override
	public Business getBusinessByAreaIdAndBrandId(Long areaId, Long brandId, int type) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "actingBrandId", brandId);
		SqlGenUtil.appendExtParamObject(sql, "type", type);
		SqlGenUtil.appendExtParamObject(sql, "isActive", 0);
		String subSql = " AND SiteId&" + areaId + "=" + areaId;
		return queryObject(sql + subSql);
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	@Override
	public List<Business> getBusinessByBrandId(Long brandId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "actingBrandId", brandId);
		return queryObjects(sql);
	}

	@Override
	public List<Business> getBusinessListByBusinessCondition(BusinessConditionDTO businessConditionDTO) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(sql_business_list);
		// 按站点搜索
		if (businessConditionDTO.getSiteId() > 0) {
			sql.append(" AND SiteId = ").append(businessConditionDTO.getSiteId());
		}
		if (StringUtils.isNotEmpty(businessConditionDTO.getBusinessId())) {
			sql.append(" AND bs.id LIKE '%").append(businessConditionDTO.getBusinessId()).append("%'");
		}
		if (StringUtils.isNotEmpty(businessConditionDTO.getAccount())) {
			sql.append(" AND BusinessAccount LIKE '%").append(businessConditionDTO.getAccount()).append("%'");
		}
		if (StringUtils.isNotEmpty(businessConditionDTO.getCompanyName())) {
			sql.append(" AND CompanyName LIKE '%").append(businessConditionDTO.getCompanyName()).append("%'");
		}
		if (StringUtils.isNotEmpty(businessConditionDTO.getStoreName())) {
			sql.append(" AND StoreName LIKE '%").append(businessConditionDTO.getStoreName()).append("%'");
		}
		if (businessConditionDTO.getIsActive() != -1) {
			sql.append(" AND IsActive = ").append(businessConditionDTO.getIsActive());
		}
		if (StringUtils.isEmpty(businessConditionDTO.getOrderColumn())) {
			businessConditionDTO.setOrderColumn("UpdateTime");
			businessConditionDTO.setAsc(false);
		}
		if (CollectionUtil.isNotEmptyOfList(businessConditionDTO.getAgentIds())) {
			sql.append(" AND CreatorId in ");
			sql.append("(");
			for (Long id : businessConditionDTO.getAgentIds()) {
				sql.append(id).append(",");
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
			sql.append(") ");
		}
		return getListByDDBParam(sql.toString(), (DDBParam) businessConditionDTO);

	}

	@Override
	public boolean batchDeleteBusiness(List<Long> ids) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("DELETE FROM Mmall_CMS_Business WHERE 1=1 and id in ");
		sql.append("(");
		for (Long id : ids) {
			sql.append(id).append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(") ");
		return false;
	}
}
