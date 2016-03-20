package com.xyl.mmall.test;
//package com.xyl.mmall.test;
//
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.xyl.mmall.cms.facade.param.FrontCWCODBatchReturnParam;
//import com.xyl.mmall.cms.facade.param.FrontCWCODBatchReturnParam.KVPair;
//import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam;
//import com.xyl.mmall.order.param.PassReturnOperationParam;
//import com.xyl.mmall.order.param.ReturnConfirmParam;
//import com.xyl.mmall.order.param.ReturnOperationParam;
//import com.xyl.mmall.test.ReturnJsonDeserializor.OmsConfirmParam.ConfirmParam;
//
///**
// * 
// * @author hzwangjianyi@corp.netease.com
// *
// */
//public class ReturnJsonDeserializor {
//	
//	public static class OmsConfirmParam implements Serializable {
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 7531117700623239022L;
//		
//		public static class ConfirmParam implements Serializable {
//
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 3362224735083936631L;
//			
//			private long orderSkuId;
//			private ReturnConfirmParam confirmInfo = new ReturnConfirmParam();
//			public long getOrderSkuId() {
//				return orderSkuId;
//			}
//			public void setOrderSkuId(long orderSkuId) {
//				this.orderSkuId = orderSkuId;
//			}
//			public ReturnConfirmParam getConfirmInfo() {
//				return confirmInfo;
//			}
//			public void setConfirmInfo(ReturnConfirmParam confirmInfo) {
//				this.confirmInfo = confirmInfo;
//			}
//		}
//		
//		private long retPkgId;
//		private long userId;
//		private List<ConfirmParam> receivedRetOrdSku = new ArrayList<ConfirmParam>();
//		public long getRetPkgId() {
//			return retPkgId;
//		}
//		public void setRetPkgId(long retPkgId) {
//			this.retPkgId = retPkgId;
//		}
//		public long getUserId() {
//			return userId;
//		}
//		public void setUserId(long userId) {
//			this.userId = userId;
//		}
//		public List<ConfirmParam> getReceivedRetOrdSku() {
//			return receivedRetOrdSku;
//		}
//		public void setReceivedRetOrdSku(List<ConfirmParam> receivedRetOrdSku) {
//			this.receivedRetOrdSku = receivedRetOrdSku;
//		}
//	}
//
//	private static ObjectMapper objectMapper = new ObjectMapper();
//	
//	public static FrontTimeRangeSearchTypeParam decodeFrontTimeRangeSearchTypeParam(String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		return objectMapper.readValue(jsonParam, FrontTimeRangeSearchTypeParam.class);
//	}
//	
//	public static PassReturnOperationParam decodePassReturnOperationParam(String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		return objectMapper.readValue(jsonParam, PassReturnOperationParam.class);
//	}
//	
//	public static ReturnOperationParam decodeReturnOperationParam(String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		return objectMapper.readValue(jsonParam, ReturnOperationParam.class);
//	}
//	
//	public static FrontCWCODBatchReturnParam decodeFrontCWCODBatchReturnParam(String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		return objectMapper.readValue(jsonParam, FrontCWCODBatchReturnParam.class);
//	}
//	
//	public static OmsConfirmParam decodeOmsConfirmParam(String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		return objectMapper.readValue(jsonParam, OmsConfirmParam.class);
//	}
//	
//	public static Object smapleJson(int tag) {
//		Object ret = null;
//		switch(tag) {
//		case 0:
//			ret = new FrontTimeRangeSearchTypeParam();
//			return ret;
//		case 1:
//			ret = new PassReturnOperationParam();
//			return ret;
//		case 2:
//			ret = new ReturnOperationParam();
//			return ret;
//		case 3:
//			ret = new FrontCWCODBatchReturnParam();
//			((FrontCWCODBatchReturnParam) ret).getBatchParam().add(new KVPair());
//			return ret;
//		case 4:
//			ret = new OmsConfirmParam();
//			((OmsConfirmParam) ret).getReceivedRetOrdSku().add(new ConfirmParam());
//			return ret;
//		default:
//			return ret;
//		}
//	}
//}
