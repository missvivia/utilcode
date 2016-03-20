package com.xyl.mmall.itemcenter.intf;

/**
 * 尺码字段接口类
 * 
 * @author hzhuangluqian
 *
 */
public interface Size {

	/**
	 * 获取尺码id
	 * 
	 * @return
	 */
	public long getTemplateKey();

	/**
	 * 设置尺码id
	 * 
	 * @param templateKey
	 */
	public void setTemplateKey(long templateKey);

	/**
	 * 获取尺码字段id
	 * 
	 * @return
	 */
	public long getColumnId();

	/**
	 * 设置尺码字段id
	 * 
	 * @param columnId
	 */
	public void setColumnId(long columnId);

	/**
	 * 获取尺码模板中该尺码字段的次序
	 * 
	 * @return
	 */
	public long getColIndex();

	/**
	 * 设置尺码模板中该尺码字段的次序
	 * 
	 * @param colIndex
	 */
	public void setColIndex(long colIndex);

	/**
	 * 获取该尺码字段是否必要
	 * 
	 * @return
	 */
	public boolean getIsRequired();

	/**
	 * 设置该尺码字段是否必要
	 * 
	 * @param isRequired
	 */
	public void setIsRequired(boolean isRequired);

	/**
	 * 设置该尺码字段是否必要
	 * 
	 * @param isRequired
	 */
	public void setRequired(boolean isRequired);

	/**
	 * 获取该尺码字段是否必要
	 * 
	 * @return
	 */
	public boolean isRequired();

}
