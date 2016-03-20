package com.xyl.mmall.saleschedule.enums;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;

/**
 * BrandShopStatus反序列化
 * @author chengximing
 *
 */
public class BrandShopStatusJsonDeserializer extends JsonDeserializer<BrandShopStatus> {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser,
	 *      com.fasterxml.jackson.databind.DeserializationContext)
	 */
	@Override
	public BrandShopStatus deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		int intValue = (Integer) ((IntNode) node.get("intValue")).numberValue();

		return BrandShopStatus.values()[0].genEnumByIntValue(intValue);
	}

}
