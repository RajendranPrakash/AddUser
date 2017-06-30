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

	public Map<String, Object> stringToMap(String stringJson)
			throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(stringJson, new TypeReference<Map<String, Object>>() {
		});
	}

	/*
	 * public ApiContactInformation stringToPojo(String stringJson) throws
	 * JsonGenerationException, JsonMappingException, IOException{
	 * //System.out.println("string to pojo "+stringJson+ "USERINFORMATION"+
	 * mapper.readValue(stringJson, ApiContactInformation.class)); return
	 * mapper.readValue(stringJson, ApiContactInformation.class); }
	 */

	public String objectToJson(Object stringToJsonAsString)
			throws JsonGenerationException, JsonMappingException, IOException {
		return mapper.writeValueAsString(stringToJsonAsString);
	}

}
