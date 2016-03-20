package com.xyl.mmall.itemcenter.intf;

/**
 * 尺码字段值接口类
 * 
 * @author hzhuangluqian
 *
 */
public interface SizeValue {

	/**
	 * 获取所属尺码模板/默认模板/自定义模板的模板id
	 * 
	 * @return
	 */
	public long getTemplateKey();

	/**
	 * 设置所属尺码模板/默认模板/自定义模板的尺码id
	 * 
	 * @return
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
	 * 获取尺码id
	 * 
	 * @return
	 */
	public long getRecordIndex();

	/**
	 * 设置尺码id
	 * 
	 * @param recordIndex
	 */
	public void setRecordIndex(long recordIndex);

	/**
	 * 获取尺码字段值
	 * 
	 * @return
	 */
	public String getValue();

	/**
	 * 设置尺码字段值
	 * 
	 * @param value
	 */
	public void setValue(String value);

}
