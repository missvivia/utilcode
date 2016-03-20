/**
 * 
 */
package com.xyl.mmall.member.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.member.enums.DealerType;

/**
 * @author lihui
 * 
 */
@AnnonOfClass(tableName = "Mmall_Member_Dealer", desc = "商家后台管理用户信息")
public class Dealer implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true, policy = true)
	private long id;

	@AnnonOfField(desc = "用户名", type = "VARCHAR(64)", uniqueKey = true)
	private String name;

	@AnnonOfField(desc = "注册时间")
	private long regTime;

	@AnnonOfField(desc = "手机号码", type = "VARCHAR(16)", notNull = false, safeHtml = true)
	private String mobile;

	@AnnonOfField(desc = "员工姓名", type = "VARCHAR(64)", notNull = false)
	private String realName;

	@AnnonOfField(desc = "员工工号", type = "VARCHAR(64)", notNull = false)
	private String empNumber;

	@AnnonOfField(desc = "员工所属部门", type = "VARCHAR(64)", notNull = false)
	private String department;

	@AnnonOfField(desc = "账号状态(0:正常,1:已锁定,2:已停用)")
	private AccountStatus accountStatus;

	@AnnonOfField(desc = "供应商Id")
	private long supplierId;

	@AnnonOfField(desc = "供应商用户类型(0:商户,1:店员)")
	private DealerType dealerType;

	@AnnonOfField(desc = "最后登录的时间")
	private long lastLoginTime;

	@AnnonOfField(desc = "最后修改人Id")
	private long lastModifiedBy;

	@AnnonOfField(desc = "最后修改时间")
	private long lastModifiedTime;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the regTime
	 */
	public long getRegTime() {
		return regTime;
	}

	/**
	 * @param regTime
	 *            the regTime to set
	 */
	public void setRegTime(long regTime) {
		this.regTime = regTime;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the accountStatus
	 */
	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	/**
	 * @param accountStatus
	 *            the accountStatus to set
	 */
	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	/**
	 * @return the supplierId
	 */
	public long getSupplierId() {
		return supplierId;
	}

	/**
	 * @param supplierId
	 *            the supplierId to set
	 */
	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * @return the dealerType
	 */
	public DealerType getDealerType() {
		return dealerType;
	}

	/**
	 * @param dealerType
	 *            the dealerType to set
	 */
	public void setDealerType(DealerType dealerType) {
		this.dealerType = dealerType;
	}

	/**
	 * @return the lastLoginTime
	 */
	public long getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * @param lastLoginTime
	 *            the lastLoginTime to set
	 */
	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @param realName
	 *            the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return the empNumber
	 */
	public String getEmpNumber() {
		return empNumber;
	}

	/**
	 * @param empNumber
	 *            the empNumber to set
	 */
	public void setEmpNumber(String empNumber) {
		this.empNumber = empNumber;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public long getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy
	 *            the lastModifiedBy to set
	 */
	public void setLastModifiedBy(long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @return the lastModifiedTime
	 */
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * @param lastModifiedTime
	 *            the lastModifiedTime to set
	 */
	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

}
