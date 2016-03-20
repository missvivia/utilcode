package com.xyl.mmall.photomgr.meta;

import java.io.InputStream;
import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 相册图片
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "相册图片", tableName = "Mmall_PhotoMgr_AlbumImg", policy = "userId")
public class AlbumImg implements Serializable {

	private static final long serialVersionUID = -275238207457727516L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户Id")
	private long userId;

	@AnnonOfField(desc = "目录Id")
	private long dirId;

	@AnnonOfField(desc = "图片名")
	private String imgName;

	@AnnonOfField(desc = "图片在NOS上的URL")
	private String imgUrl;

	/**
	 * 路径规则： userId/dirName/imgName
	 */
	@AnnonOfField(desc = "图片在NOS上的路径")
	private String nosPath;

	@AnnonOfField(desc = "创建时间")
	private long createDate;
	
	@AnnonOfField(desc = "高度", notNull=false)
	private int height;
	
	@AnnonOfField(desc = "宽度", notNull=false)
	private int width;
	
	@AnnonOfField(desc = "图片类型", notNull=false)
	private String imgType;

	@AnnonOfField(inDB = false)
	private InputStream inputStream;

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

	public long getDirId() {
		return dirId;
	}

	public void setDirId(long dirId) {
		this.dirId = dirId;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getNosPath() {
		return nosPath;
	}

	public void setNosPath(String nosPath) {
		this.nosPath = nosPath;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getImgType() {
		return imgType;
	}

	public void setImgType(String imgType) {
		this.imgType = imgType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public String toString() {
		return "AlbumImg [id=" + id + ", userId=" + userId + ", dirId=" + dirId + ", imgName=" + imgName + ", imgUrl="
				+ imgUrl + ", nosPath=" + nosPath + ", createDate=" + createDate + ", height=" + height + ", width="
				+ width + ", imgType=" + imgType + "]";
	}
}
