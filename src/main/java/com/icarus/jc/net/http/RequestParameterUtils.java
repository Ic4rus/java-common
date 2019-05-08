package com.icarus.jc.net.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.icarus.jc.StringUtils;

public class RequestParameterUtils {

	/**
	 * Convert an map parameter to string (body of request) by media type of request
	 * 
	 * @param parameterMap
	 * @param mediaType
	 * @return
	 */
	public static String convertParameterToBody(Map<String, String> parameterMap, String mediaType) {
		
		if (StringUtils.isEmpty(parameterMap) || StringUtils.isEmpty(mediaType)) {
			return null;
		}
		// application/json
		if (mediaType == MediaType.APPLICATION_JSON) {
			Gson gson = new Gson();
			return gson.toJson(parameterMap);
		} else if (mediaType == MediaType.APPLICATION_FORM_URLENCODED) {
			StringBuilder requestBody = new StringBuilder();
			parameterMap.forEach((key, value) -> {
				try {
					requestBody.append(String.format("&%s=%s", URLEncoder.encode(key, "UTF-8"),
							URLEncoder.encode(value, "UTF-8")));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			});
			requestBody.deleteCharAt(0);
			return requestBody.toString();
		}
		return null;
	}
}
