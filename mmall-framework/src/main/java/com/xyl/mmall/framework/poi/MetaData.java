/**
 * 
 */
package com.xyl.mmall.framework.poi;

import java.util.List;

import org.apache.poi.ss.util.CellReference;

/**
 * @author jmy
 *
 */
public class MetaData {
	private String id;
	private Layout layout;
	private DataType dataType;
	private Constraint constraint;
	private Object value;
	private CellReference start;
	private CellReference end;
	private String desc;
	private List<MetaData> subMetaData;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Layout getLayout() {
		return layout;
	}
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	public DataType getDataType() {
		return dataType;
	}
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	public Constraint getConstraint() {
		return constraint;
	}
	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public CellReference getStart() {
		return start;
	}
	public void setStart(CellReference start) {
		this.start = start;
	}
	public CellReference getEnd() {
		return end;
	}
	public void setEnd(CellReference end) {
		this.end = end;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<MetaData> getSubMetaData() {
		return subMetaData;
	}
	public void setSubMetaData(List<MetaData> subMetaData) {
		this.subMetaData = subMetaData;
	}
	
}

enum Layout {
	SINGLE, ARRAY, TABLE
}

enum DataType {
	STRING, NUMERIC, DATE
}

enum Constraint {
	NULLABLE, NONNULL, NONEMPTY
}