package com.xyl.mmall.oms.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个行政区的定义
 * 
 * @author hzzengchengyuan
 *
 */
public class Region implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 行政区编码
	 */
	private String code;

	/**
	 * 行政区名称
	 */
	private String name;

	/**
	 * 行政区级别
	 */
	private RegioLevel level;

	/**
	 * 父级行政区编码
	 */
	private String parentCode;

	/**
	 * 父级行政区
	 */
	private Region parent;

	/**
	 * 子行政区
	 */
	private List<Region> children;

	public Region() {

	}

	public Region(String code, String name, RegioLevel level) {
		this(code, name, level, null);
	}

	public Region(String code, String name, RegioLevel level, String parentCode) {
		setCode(code);
		setName(name);
		setLevel(level);
		setParentCode(parentCode);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		if (code == null || code.trim().length() == 0) {
			throw new NullPointerException();
		}
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new NullPointerException();
		}
		this.name = name;
	}

	public RegioLevel getLevel() {
		return level;
	}

	public void setLevel(RegioLevel level) {
		this.level = level;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public Region getParent() {
		return parent;
	}

	public void setParent(Region parent) {
		this.parent = parent;
		if (parent != null) {
			setParentCode(parent.getCode());
		} else {
			setParentCode(null);
		}
	}

	public List<Region> getChildren() {
		return children;
	}

	public void setChildren(List<Region> children) {
		this.children = children;
	}

	public Region addChild(Region child) {
		if (child == null) {
			return this;
		}
		if (this.children == null) {
			this.children = new ArrayList<Region>();
		}
		Region exist = this.searchByCode(child.getCode());
		if (exist == null) {
			this.children.add(child);
			child.setParent(this);
		} else {
			throw new IllegalArgumentException("The region [" + child + "] already exists.");
		}
		return this;
	}

	/**
	 * 获取子占位行政区，即，code为负数的行政区
	 * 
	 * @return
	 */
	public Region getPlaceholderRegion() {
		if (this.children != null) {
			for (Region child : this.children) {
				if (child.getCode().startsWith("-")) {
					return child;
				}
			}
		}
		return null;
	}

	/**
	 * 根据行政区code搜索目标行政区，搜索范围包括本机行政区和下级行政区
	 * 
	 * @param code
	 * @return
	 */
	public Region searchByCode(String code) {
		if (this.getCode().equals(code)) {
			return this;
		}
		if (this.children != null) {
			for (Region child : this.children) {
				Region dist = child.searchByCode(code);
				if (dist != null) {
					return dist;
				}
			}
		}
		return null;
	}

	public Region searchByName(String name) {
		if (getName().equals(name)) {
			return this;
		}
		if (this.children != null) {
			for (Region child : this.children) {
				Region dist = child.searchByName(name);
				if (dist != null) {
					return dist;
				}
			}
		}
		return null;
	}

	public List<Region> searchByNames(String name) {
		List<Region> regions = new ArrayList<Region>();
		if (getName().equals(name)) {
			regions.add(this);
		}
		if (this.children != null) {
			for (Region child : this.children) {
				List<Region> temp = child.searchByNames(name);
				if (temp != null) {
					regions.addAll(temp);
				}
			}
		}
		return regions;
	}

	/**
	 * 根据行政区名字和行政区的级别搜索目标行政区，搜索范围包括本机行政区和下级行政区
	 * 
	 * @param name
	 * @param level
	 * @return
	 */
	public Region searchByName(String name, RegioLevel level) {
		if (this.getLevel() == level && getName().equals(name)) {
			return this;
		}
		if (this.children != null && this.getLevel().getIntValue() < level.getIntValue()) {
			for (Region child : this.children) {
				Region dist = child.searchByName(name, level);
				if (dist != null) {
					return dist;
				}
			}
		}
		return null;
	}

	public List<Region> searchByNames(String name, RegioLevel level) {
		List<Region> regions = new ArrayList<Region>();
		if (this.getLevel().getIntValue() >= level.getIntValue()) {
			return regions;
		}
		if (this.getLevel() == level && getName().equals(name)) {
			regions.add(this);
		}
		if (this.children != null) {
			for (Region child : this.children) {
				List<Region> temp = child.searchByNames(name, level);
				if (temp != null) {
					regions.addAll(temp);
				}
			}
		}
		return regions;
	}

	@Override
	public int hashCode() {
		return this.getCode() == null ? 0 : this.getCode().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Region)) {
			return false;
		}
		Region other = (Region) obj;
		if (getCode() != null && getCode().equals(other.getCode())) {
			return true;
		}
		if (getCode() == null && other.getCode() == null) {
			return true;
		}
		return false;
	}

	public String toString() {
		String defaultNull = "/";
		String name = isBlank(getName()) ? defaultNull : getName();
		String code = isBlank(getCode()) ? defaultNull : getCode();
		String level = this.level == null ? defaultNull : this.level.getName();
		return code.concat("-").concat(name).concat("(").concat(level).concat(")");
	}

	private boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	public long size() {
		long size = 0;
		if (getChildren() != null) {
			for (Region r : getChildren()) {
				size += r.size();
			}
		}
		return size + 1;
	}
}
