package com.xyl.mmall.saleschedule.dto;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class ScheduleStatusDTO implements java.io.Serializable {

	private static final long serialVersionUID = -2570359483680073245L;

	private int id;

	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ScheduleStatusDTO [id=" + id + ", name=" + name + "]";
	}

}
