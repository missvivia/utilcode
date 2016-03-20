package com.xyl.mmall.test;
//package com.xyl.mmall.test;
//
//import java.io.IOException;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.xyl.mmall.cms.facade.param.FrontOrderExpInfoUpdateParam;
//
///**
// * 
// * @author hzwangjianyi@corp.netease.com
// *
// */
//public class OrderJsonDeserializor {
//
//	private static ObjectMapper objectMapper = new ObjectMapper();
//	
//	public static FrontOrderExpInfoUpdateParam decodeFrontOrderExpInfoUpdateParam(String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		return objectMapper.readValue(jsonParam, FrontOrderExpInfoUpdateParam.class);
//	}
//	
//	public static Object smapleJson(int tag) {
//		return new FrontOrderExpInfoUpdateParam();
//	}
//}
