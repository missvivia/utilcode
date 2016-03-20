package com.xyl.mmall.mainsite.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import com.alibaba.fastjson.JSONObject;

public class ControllerResultHandlerUtil {
	
	private static final String RESULT_CODE_RETURN="code";
	
	private static final String SUCCESS_CODE="200";
	
	private static final String FAIL_CODE="400";
	
	private static final String RESULT_KEY_RETURN="result";
	
	private static final String RESULT_KEY_TOTAL = "total";
	
	private static final String RESULT_LIST_KEY="list";
	
	public static void handleResult(Model model,boolean success,Object resultObj){
		if(success){
			model.addAttribute(RESULT_CODE_RETURN, SUCCESS_CODE);
			
			//返回对象为list
			if(resultObj instanceof List){
				Map<String, Object> mapResult=new HashMap<String, Object>();
				mapResult.put(RESULT_LIST_KEY, resultObj);
				model.addAttribute(RESULT_KEY_RETURN, mapResult);
			}else{
				model.addAttribute(RESULT_KEY_RETURN, resultObj);
			}
		}else{
			model.addAttribute(RESULT_CODE_RETURN, FAIL_CODE);
		}
	}
	
	public static void handleResult(JSONObject jsonObject, boolean success,Object resultObj, int total){
		if (success) {
			jsonObject.put(RESULT_CODE_RETURN, SUCCESS_CODE);
			
			//返回对象为list
			if (resultObj instanceof List) {
				Map<String, Object> mapResult = new HashMap<String, Object>();
				mapResult.put(RESULT_KEY_TOTAL, total);
				mapResult.put(RESULT_LIST_KEY, resultObj);
				jsonObject.put(RESULT_KEY_RETURN, mapResult);
			} else {
				jsonObject.put(RESULT_KEY_RETURN, resultObj);
			}
		} else {
			jsonObject.put(RESULT_CODE_RETURN, FAIL_CODE);
		}
	}

}
