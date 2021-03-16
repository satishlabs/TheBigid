package com.bigid.web.common;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JSONUtil {
	private static ObjectMapper objMapper = new ObjectMapper();

	private JSONUtil() {

	}

	/**
	 * This method will jsonize any object to string.
	 * 
	 * @param <T>
	 * @param obj
	 * @return
	 * @throws RutimeException
	 */
	public static <T> String jsonizeObject(T obj) throws RuntimeException { 

		String jsonValue;
		try {
			jsonValue = objMapper.writeValueAsString(obj);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return jsonValue;
	}

	/**
	 * This method will create Object based on the targetClass parameter of this
	 * method from the jsonized string.
	 * 
	 * @param <T>
	 * @param jsonDescription
	 * @param targetClass
	 * @return
	 * @throws RuntimeException
	 */
	public static <T> T deJsonizeObject(String jsonDescription,
			Class<T> targetClass) throws RuntimeException { // Should not
															// enforce to catch
		T jsonValue;
		try {
			jsonValue = objMapper.readValue(jsonDescription, targetClass);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return jsonValue;
	}
}
