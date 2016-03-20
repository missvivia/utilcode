package com.xyl.mmall.cms.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author hzchaizhf
 * @version 2014-09-17
 */
public class ConditionAO {

    public static final int ORDER_DESC = 0;

    protected String order;

    protected int asc = ORDER_DESC;

    public int getAsc() {
        return asc;
    }

    public void setAsc(int asc) {
        this.asc = asc;
    }

    public Map<String, Object> getConditionMap() {
        Map<String, Object> conditionMap = new HashMap<String, Object>();
    
        if (StringUtils.isNotBlank(order) ) {
    
            conditionMap.put("order", order);
            conditionMap.put("asc", asc == ORDER_DESC ? "desc" : "asc");
        }
        return conditionMap;
    }

}
