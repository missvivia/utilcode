/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.member.dto.DealerDTO;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class DealerVO {
	
	private DealerDTO dealer;
	
	private List<BackendRoleVO> groupList;

	public DealerVO() {
		dealer = new DealerDTO();
	}

	public DealerVO(DealerDTO dealer) {
		this.setDealer(dealer);
	}

	/**
	 * @return the dealer
	 */
	@JsonIgnore
	public DealerDTO getDealer() {
		return dealer;
	}

	/**
	 * @param dealer
	 *            the dealer to set
	 */
	public void setDealer(DealerDTO dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the groupList
	 */
	public List<BackendRoleVO> getGroupList() {
		return groupList;
	}

	/**
	 * @param groupList
	 *            the groupList to set
	 */
	public void setGroupList(List<BackendRoleVO> groupList) {
		this.groupList = groupList;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return dealer.getId();
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		dealer.setId(id);
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return dealer.getName();
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		dealer.setName(displayName);
	}

	/**
	 * @return the addTime
	 */
	public long getAddTime() {
		return dealer.getRegTime();
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return dealer.getMobile();
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		dealer.setMobile(mobile);
	}

	/**
	 * @return the editTime
	 */
	public long getEditTime() {
		return dealer.getLastModifiedTime();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return dealer.getRealName();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		dealer.setRealName(name);
	}

	/**
	 * @return the accountNum
	 */
	public String getAccountNum() {
		return dealer.getEmpNumber();
	}

	/**
	 * @param accountNum
	 *            the accountNum to set
	 */
	public void setAccountNum(String accountNum) {
		dealer.setEmpNumber(accountNum);
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return dealer.getDepartment();
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(String department) {
		dealer.setDepartment(department);
	}

	/**
	 * @return the accountStatus
	 */
	public String getAccountStatus() {
		return dealer.getAccountStatus().getDesc();
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return dealer.getAccountStatus().getIntValue();
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return dealer.getDealerType().getDesc();
	}

	public String getPassword() {
		return dealer.getPassword();
	}

	public void setPassword(String password) {
		dealer.setPassword(password);
	}

	public String getPasswordIsChange() {
		return dealer.getPasswordIsChange();
	}

	public void setPasswordIsChange(String passwordIsChange) {
		dealer.setPasswordIsChange(passwordIsChange);
	}
	
	

	
}
