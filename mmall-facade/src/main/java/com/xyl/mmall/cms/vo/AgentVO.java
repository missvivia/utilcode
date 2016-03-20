/**
 * 
 */
package com.xyl.mmall.cms.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.member.dto.AgentDTO;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class AgentVO {

	private AgentDTO agent;

	private List<CmsRoleVO> roleList;

	private long adminId;

	public AgentVO() {
		agent = new AgentDTO();
	}

	public AgentVO(AgentDTO agent) {
		this.agent = agent;
	}

	/**
	 * @return the agent
	 */
	@JsonIgnore
	public AgentDTO getAgent() {
		return agent;
	}

	/**
	 * @param agent
	 *            the agent to set
	 */
	public void setAgent(AgentDTO agent) {
		this.agent = agent;
	}

	/**
	 * @return the roleList
	 */
	public List<CmsRoleVO> getRoleList() {
		return roleList;
	}

	/**
	 * @param roleList
	 *            the roleList to set
	 */
	public void setRoleList(List<CmsRoleVO> roleList) {
		this.roleList = roleList;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return agent.getId();
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		agent.setId(id);
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return agent.getName();
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		agent.setName(displayName);
	}

	/**
	 * @return the addTime
	 */
	public long getAddTime() {
		return agent.getRegTime();
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return agent.getMobile();
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		agent.setMobile(mobile);
	}

	/**
	 * @return the editTime
	 */
	public long getEditTime() {
		return agent.getLastModifiedTime();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return agent.getRealName();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		agent.setRealName(name);
	}

	/**
	 * @return the accountNum
	 */
	public String getAccountNum() {
		return agent.getEmpNumber();
	}

	/**
	 * @param accountNum
	 *            the accountNum to set
	 */
	public void setAccountNum(String accountNum) {
		agent.setEmpNumber(accountNum);
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return agent.getDepartment();
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(String department) {
		agent.setDepartment(department);
	}

	/**
	 * @return the accountStatus
	 */
	public String getAccountStatus() {
		return agent.getAccountStatus().getDesc();
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return agent.getAccountStatus().getIntValue();
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return agent.getAgentType().getDesc();
	}

	/**
	 * @return the adminId
	 */
	public long getAdminId() {
		return adminId;
	}

	/**
	 * @param adminId
	 *            the adminId to set
	 */
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return agent.getEmail();
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		agent.setEmail(email);
	}

	public int getIsModifyPassword() {
		return agent.getIsModifyPassword();
	}
	
	public void setIsModifyPassword(int isModifyPassword) {
		agent.setIsModifyPassword(isModifyPassword);
	}
	
	public String getPassword() {
		return agent.getPassword();
	}
	
	public void setPassword(String password) {
		agent.setPassword(password);
	}
}
