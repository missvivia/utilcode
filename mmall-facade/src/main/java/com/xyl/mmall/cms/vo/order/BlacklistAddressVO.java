package com.xyl.mmall.cms.vo.order;

import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.order.dto.CODBlacklistAddressDTO;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public class BlacklistAddressVO {

	private long total;
	
	private List<BlackAddress> blackAddressList = new ArrayList<BlackAddress>();
	
	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<BlackAddress> getBlackAddressList() {
		return blackAddressList;
	}

	public void setBlackAddressList(List<BlackAddress> blackAddressList) {
		this.blackAddressList = blackAddressList;
	}

	public static class BlackAddress {
		private String id;
		private String userId;
		private String userName;
		private String blackMobile;
		private String blackAddress;
		private String auditUserName;
		private long ctime;
		public BlackAddress fillWithInfo(CODBlacklistAddressDTO addressDTO, String userName, String auditUserName) {
			if(null != addressDTO) {
				this.id = String.valueOf(addressDTO.getUserId());
				this.userId = String.valueOf(addressDTO.getUserId());
				this.blackMobile = addressDTO.getConsigneeMobile();
				this.blackAddress = addressDTO.mergeAddress();
				this.ctime = addressDTO.getCtime();
			}
			this.userName = userName;
			this.auditUserName = auditUserName;
			return this;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
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
		public String getBlackMobile() {
			return blackMobile;
		}
		public void setBlackMobile(String blackMobile) {
			this.blackMobile = blackMobile;
		}
		public String getBlackAddress() {
			return blackAddress;
		}
		public void setBlackAddress(String blackAddress) {
			this.blackAddress = blackAddress;
		}
		public String getAuditUserName() {
			return auditUserName;
		}
		public void setAuditUserName(String auditUserName) {
			this.auditUserName = auditUserName;
		}
		public long getCtime() {
			return ctime;
		}
		public void setCtime(long ctime) {
			this.ctime = ctime;
		}
	}
}
