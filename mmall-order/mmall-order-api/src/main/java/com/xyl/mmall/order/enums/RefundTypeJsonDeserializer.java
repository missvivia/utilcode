package com.xyl.mmall.order.enums;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月9日 下午1:58:40
 *
 */
public class RefundTypeJsonDeserializer extends JsonDeserializer<RefundType> {

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext)
	 */
	@Override
	public RefundType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		int intValue = (Integer) ((IntNode) node.get("value")).numberValue();
		return RefundType.values()[0].genEnumByIntValue(intValue);
	}

}
