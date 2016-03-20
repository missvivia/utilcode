/**
 * 
 */
package com.xyl.mmall.content.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.content.enums.ContentType;
import com.xyl.mmall.content.enums.NCSIndexDispatchState;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
@AnnonOfClass(tableName = "Mmall_Content_NCSContentDispatchLog", desc = "NCS索引记录")
public class NCSContentDispatchLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, policy = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "内容类型")
	private ContentType contentType = ContentType.NULL;
	
	@AnnonOfField(desc = "外键Id")
	private long foreignPrimaryId;
	
	@AnnonOfField(desc = "索引状态")
	private NCSIndexDispatchState dispatchState = NCSIndexDispatchState.NULL;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "更新时间")
	private long updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public long getForeignPrimaryId() {
		return foreignPrimaryId;
	}

	public void setForeignPrimaryId(long foreignPrimaryId) {
		this.foreignPrimaryId = foreignPrimaryId;
	}

	public NCSIndexDispatchState getDispatchState() {
		return dispatchState;
	}

	public void setDispatchState(NCSIndexDispatchState dispatchState) {
		this.dispatchState = dispatchState;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

}
