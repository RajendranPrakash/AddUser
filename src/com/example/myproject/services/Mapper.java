package com.example.myproject.services;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class Mapper {
	ObjectMapper mapper;

	public Mapper() {
		mapper = new ObjectMapper();
	}

	public Map<String, Object> stringToMap(String stringToMap)
			throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(stringToMap, new TypeReference<Map<String, Object>>() {
		});
	}

	public String pojoStringToJson(Object stringToJsonAsString)
			throws JsonGenerationException, JsonMappingException, IOException {
		return mapper.writeValueAsString(stringToJsonAsString);
	}
	
}
