package com.xyl.mmall.framework.vo;

public class SimpleIdNamePaire {
	private long id;

	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		int elm1 = Long.valueOf(id).hashCode();
		return elm1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleIdNamePaire other = (SimpleIdNamePaire) obj;
		if (id != other.getId())
			return false;
		return true;
	}
}
