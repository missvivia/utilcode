package com.xyl.mmall.test;
//package com.xyl.mmall.test;
//
//import java.io.IOException;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.xyl.mmall.cms.facade.param.FrontCODAuditOperationParam;
//import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam;
//
///**
// * 
// * @author hzwangjianyi@corp.netease.com
// *
// */
//public class CODAuditJsonDeserializor {
//
//	private static ObjectMapper objectMapper = new ObjectMapper();
//	
//	public static FrontTimeRangeSearchTypeParam decodeFrontTimeRangeSearchTypeParam(String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		return objectMapper.readValue(jsonParam, FrontTimeRangeSearchTypeParam.class);
//	}
//	
//	public static FrontCODAuditOperationParam decodeFrontCODAuditOperationParam(String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		return objectMapper.readValue(jsonParam, FrontCODAuditOperationParam.class);
//	}
//	 
//	public static Object smapleJson(int tag) {
//		Object ret = null;
//		switch(tag) {
//		case 0:
//			ret = new FrontTimeRangeSearchTypeParam();
//			return ret;
//		case 1:
//			ret = new FrontCODAuditOperationParam();
//			return ret;
//		default:
//			return ret;
//		}
//	}
//}
