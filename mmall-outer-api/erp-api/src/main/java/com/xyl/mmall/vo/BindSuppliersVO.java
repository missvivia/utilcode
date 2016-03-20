/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.vo;

import java.io.Serializable;
import java.util.List;

/**
 * BindSuppliersVO.java created by yydx811 at 2015年12月2日 下午5:35:10
 * 绑定供应商
 *
 * @author yydx811
 */
public class BindSuppliersVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -3758083576465391807L;

	/** 绑定供应商账号列表. */
	private List<String> businessAccountList;

	/** 用户名. */
	private String userName;

	public List<String> getBusinessAccountList() {
		return businessAccountList;
	}

	public void setBusinessAccountList(List<String> businessAccountList) {
		this.businessAccountList = businessAccountList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
