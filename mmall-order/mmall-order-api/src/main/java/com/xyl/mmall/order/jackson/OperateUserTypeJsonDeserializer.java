package com.xyl.mmall.order.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.xyl.mmall.order.enums.OperateUserType;

/**
 * jackson反序列化JSON字符串到OperateUserType
 * @author author:lhp
 *
 * @version date:2015年6月17日下午3:32:07
 */
public class OperateUserTypeJsonDeserializer extends JsonDeserializer<OperateUserType> {

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser,
		 *      com.fasterxml.jackson.databind.DeserializationContext)
		 */
		public OperateUserType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
				JsonProcessingException {
			JsonNode node = jp.getCodec().readTree(jp);
			int intValue = (Integer) ((IntNode) node.get("intValue")).numberValue();

			return OperateUserType.values()[0].genEnumByIntValue(intValue);
		}
}
