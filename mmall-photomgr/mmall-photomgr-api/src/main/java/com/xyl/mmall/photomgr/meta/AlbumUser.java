package com.xyl.mmall.photomgr.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.photomgr.enums.AlbumUserState;

/**
 * 相册用户
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "相册用户", tableName = "Mmall_PhotoMgr_AlbumUser", policy = "userId")
public class AlbumUser implements Serializable {

	private static final long serialVersionUID = 3212103242921705308L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户Id")
	private long userId;

	/**
	 * 沒有用戶名的話。userName和userId相同。
	 */
	@AnnonOfField(desc = "用户名称", notNull = false, defa = "")
	private String userName;

	@AnnonOfField(desc = "创建时间")
	private long userCreateDate;

	@AnnonOfField(desc = "用户状态(0:无效,1:有效)")
	private AlbumUserState status = AlbumUserState.VALID;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getUserCreateDate() {
		return userCreateDate;
	}

	public void setUserCreateDate(long userCreateDate) {
		this.userCreateDate = userCreateDate;
	}

	public AlbumUserState getStatus() {
		return status;
	}

	public void setStatus(AlbumUserState status) {
		this.status = status;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
