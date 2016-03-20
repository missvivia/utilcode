package com.xyl.mmall.order.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.xyl.mmall.order.enums.OrderFormState;

/**
 * jackson反序列化JSON字符串到OrderFormState
 * 
 * @author dingmingliang
 * 
 */
public class OrderFormStateJsonDeserializer extends JsonDeserializer<OrderFormState> {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser,
	 *      com.fasterxml.jackson.databind.DeserializationContext)
	 */
	public OrderFormState deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		int intValue = (Integer) ((IntNode) node.get("intValue")).numberValue();

		return OrderFormState.values()[0].genEnumByIntValue(intValue);
	}
}