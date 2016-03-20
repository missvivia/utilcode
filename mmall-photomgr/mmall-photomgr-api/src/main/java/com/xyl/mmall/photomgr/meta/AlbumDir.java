package com.xyl.mmall.photomgr.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 相册目录
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "相册目录", tableName = "Mmall_PhotoMgr_AlbumDir", policy = "userId")
public class AlbumDir implements Serializable {

	private static final long serialVersionUID = -8394065935352377090L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户Id")
	private long userId;

	@AnnonOfField(desc = "目录名称")
	private String dirName;

	@AnnonOfField(desc = "创建时间")
	private long dirCreateDate;

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

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public long getDirCreateDate() {
		return dirCreateDate;
	}

	public void setDirCreateDate(long dirCreateDate) {
		this.dirCreateDate = dirCreateDate;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
