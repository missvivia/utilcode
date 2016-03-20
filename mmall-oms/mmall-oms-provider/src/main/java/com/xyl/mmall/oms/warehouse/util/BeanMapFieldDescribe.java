
/**
 * 
 */
package com.xyl.mmall.oms.warehouse.util;

import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * @author hzzengchengyuan
 * 
 */
public class BeanMapFieldDescribe {
	private Class<?> sourceFieldType;

	private String sourceFieldName;

	private Class<?>[] sourceFieldPzTypes;

	private Class<?> targetFieldType;

	private String targetFieldName;

	private Class<?>[] targetFieldPzTypes;

	private MapField sourceFieldAnnotation;

	public BeanMapFieldDescribe() {

	}

	/**
	 * @param sourceFieldType
	 * @param sourceFieldName
	 * @param targetFieldType
	 * @pClass<?> targetFieldName
	 */
	public BeanMapFieldDescribe(Class<?> sourceFieldType, String sourceFieldName, Class<?> targetFieldType,
			String targetFieldName) {
		this(sourceFieldType, sourceFieldName, targetFieldType, targetFieldName, null);
	}

	/**
	 * @param sourceFieldType
	 * @param sourceFieldName
	 * @param targetFieldType
	 * @param targetFieldName
	 * @param isDate
	 */
	public BeanMapFieldDescribe(Class<?> sourceFieldType, String sourceFieldName, Class<?> targetFieldType,
			String targetFieldName, MapField fieldAnnotation) {
		super();
		this.sourceFieldType = sourceFieldType;
		this.sourceFieldName = sourceFieldName;
		this.targetFieldType = targetFieldType;
		this.targetFieldName = targetFieldName;
		this.sourceFieldAnnotation = fieldAnnotation;
	}

	/**
	 * @return the sourceFieldType
	 */
	public Class<?> getSourceFieldType() {
		return sourceFieldType;
	}

	/**
	 * @param sourceFieldType
	 *            the sourceFieldType to set
	 */
	public void setSourceFieldType(Class<?> sourceFieldType) {
		this.sourceFieldType = sourceFieldType;
	}

	/**
	 * @return the sourceFieldName
	 */
	public String getSourceFieldName() {
		return sourceFieldName;
	}

	/**
	 * @param sourceFieldName
	 *            the sourceFieldName to set
	 */
	public void setSourceFieldName(String sourceFieldName) {
		this.sourceFieldName = sourceFieldName;
	}

	/**
	 * @return the sourceFieldPzTypes
	 */
	public Class<?>[] getSourceFieldPzTypes() {
		return sourceFieldPzTypes;
	}

	/**
	 * @param sourceFieldPzTypes
	 *            the sourceFieldPzTypes to set
	 */
	public void setSourceFieldPzTypes(Class<?>[] sourceFieldPzTypes) {
		this.sourceFieldPzTypes = sourceFieldPzTypes;
	}

	/**
	 * @return the targetFieldType
	 */
	public Class<?> getTargetFieldType() {
		return targetFieldType;
	}

	/**
	 * @param targetFieldType
	 *            the targetFieldType to set
	 */
	public void setTargetFieldType(Class<?> targetFieldType) {
		this.targetFieldType = targetFieldType;
	}

	/**
	 * @return the targetFieldName
	 */
	public String getTargetFieldName() {
		return targetFieldName;
	}

	/**
	 * @param targetFieldName
	 *            the targetFieldName to set
	 */
	public void setTargetFieldName(String targetFieldName) {
		this.targetFieldName = targetFieldName;
	}

	/**
	 * @return the targetFieldPzTypes
	 */
	public Class<?>[] getTargetFieldPzTypes() {
		return targetFieldPzTypes;
	}

	/**
	 * @param targetFieldPzTypes
	 *            the targetFieldPzTypes to set
	 */
	public void setTargetFieldPzTypes(Class<?>[] targetFieldPzTypes) {
		this.targetFieldPzTypes = targetFieldPzTypes;
	}

	public MapField getSourceFieldAnnotation() {
		return sourceFieldAnnotation;
	}

	public void setSourceFieldAnnotation(MapField sourceFieldAnnotation) {
		this.sourceFieldAnnotation = sourceFieldAnnotation;
	}

	public String toString() {
		return getSourceFieldName() + " -> " + getTargetFieldName();
	}
}
