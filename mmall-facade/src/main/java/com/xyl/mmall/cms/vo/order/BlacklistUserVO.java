package com.xyl.mmall.cms.vo.order;

import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;

/**
 * 用户黑名单
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public class BlacklistUserVO {

	private long total = 0;
	
	private List<BlackUser> blackUserList = new ArrayList<BlackUser>();
	
	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<BlackUser> getBlackUserList() {
		return blackUserList;
	}

	public void setBlackUserList(List<BlackUser> blackUserList) {
		this.blackUserList = blackUserList;
	}

	public static class BlackUser {
		private String userId;
		private String userName;
		private String bindMobile;
		private String auditUser;
		private long ctime;
		public BlackUser fillWithInfo(UserProfileDTO user, AgentDTO auditUser, long ctime) {
			if(null != user) {
				this.userId = String.valueOf(user.getUserId());
				this.userName = user.getUserName();
				this.bindMobile = user.getMobile();
			}
			if(null != auditUser) {
				this.auditUser = auditUser.getName();
			}
			this.ctime = ctime;
			return this;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getBindMobile() {
			return bindMobile;
		}
		public void setBindMobile(String bindMobile) {
			this.bindMobile = bindMobile;
		}
		public String getAuditUser() {
			return auditUser;
		}
		public void setAuditUser(String auditUser) {
			this.auditUser = auditUser;
		}
		public long getCtime() {
			return ctime;
		}
		public void setCtime(long ctime) {
			this.ctime = ctime;
		}
	}
	
}
