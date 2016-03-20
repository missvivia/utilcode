package com.xyl.mmall.saleschedule.enums;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;

public class BrandProbabilityJsonDeserializer extends JsonDeserializer<BrandProbability> {

	@Override
	public BrandProbability deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		int intValue = (Integer) ((IntNode) node.get("intValue")).numberValue();

		return BrandProbability.values()[0].genEnumByIntValue(intValue);
	}

}
