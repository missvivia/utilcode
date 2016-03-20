/*
 * @(#) 2014-9-26
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.activity;

import java.util.List;


/**
 * Activation.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-26
 * @since      1.0
 */
public class Activation implements Comparable<Activation> {
	/**
	 * {
      "id":100,
      "schedules":[
      {
          "id": 1000,
          "brandNameZh": "春水鸟人"
      }
      ],
      "conditions": {
        "type": 1,
        "value": "1000-"
      }, 
      "result": [
        { 
          "type": 1,
          "value": "xxx"
        }
      ]
    }
	 */
	
	private List<ActivitySchedule> schedules;
	
	/**
	 * 促销基本条件
	 */
	private Condition condition;
	
	/**
	 * 促销效果
	 */
	private List<Effect> result;

	public List<ActivitySchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ActivitySchedule> schedules) {
		this.schedules = schedules;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Condition getCondition() {
		return condition;
	}

	public List<Effect> getResult() {
		return result;
	}

	public void setResult(List<Effect> result) {
		this.result = result;
	}

	@Override
	public int compareTo(Activation o) {
		return this.getCondition().compareTo(o.getCondition());
	}
}
