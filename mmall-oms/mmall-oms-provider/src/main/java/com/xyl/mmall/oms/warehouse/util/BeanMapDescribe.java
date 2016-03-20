/**
 * 
 */
package com.xyl.mmall.oms.warehouse.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hzzengchengyuan
 * 
 */
public class BeanMapDescribe {
	private Class<?> sourceType;

	private Class<?> targetType;

	private List<BeanMapFieldDescribe> fieldDescribes;

	/**
	 * @return the sourceType
	 */
	public Class<?> getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType
	 *            the sourceType to set
	 */
	public void setSourceType(Class<?> sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * @return the targetType
	 */
	public Class<?> getTargetType() {
		return targetType;
	}

	/**
	 * @param targetType
	 *            the targetType to set
	 */
	public void setTargetType(Class<?> targetType) {
		this.targetType = targetType;
	}

	/**
	 * @return the fieldDescribes
	 */
	public List<BeanMapFieldDescribe> getFieldDescribes() {
		return fieldDescribes;
	}

	/**
	 * @param fieldDescribes
	 *            the fieldDescribes to set
	 */
	public void setFieldDescribes(List<BeanMapFieldDescribe> fieldDescribes) {
		this.fieldDescribes = fieldDescribes;
	}

	public void addFieldDescribes(BeanMapFieldDescribe fieldDescribe) {
		if (fieldDescribes == null) {
			fieldDescribes = new ArrayList<BeanMapFieldDescribe>();
		}
		this.fieldDescribes.add(fieldDescribe);
	}

	public void addAllFieldDescribes(List<BeanMapFieldDescribe> fieldDescribes) {
		if (this.fieldDescribes == null) {
			this.fieldDescribes = new ArrayList<BeanMapFieldDescribe>();
		}
		if (fieldDescribes != null) {
			this.fieldDescribes.addAll(fieldDescribes);
		}
	}

	public String toString() {
		return getSourceType() + " -> " + getTargetType();
	}

}
