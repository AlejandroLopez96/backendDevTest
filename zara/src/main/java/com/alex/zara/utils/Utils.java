package com.alex.zara.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
	
//	At least one non-public constructor should be defined
	private Utils() {}

	public static String jsonMe(Object o) {
		String json = null;
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			json = ow.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			log.info("Error processing object: ", e.getMessage());
		}
		return json;
	}
	
}
