package com.icarus.jc.net.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp3Utils {
	
	private static enum METHOD {
		GET, POST, PUT
	}
	
	public static void sendRequest(METHOD method, String url, String contentType) {
		
		OkHttpClient client = new OkHttpClient();
		Builder requestBuilder = new Request.Builder();
		if (method == METHOD.GET) {
			requestBuilder.get();
		} else {
			requestBuilder.addHeader("content-type", contentType);
			MediaType mediaType = MediaType.parse(contentType);
			RequestBody body = RequestBody.create(mediaType, "");
			if (method == METHOD.POST) {
				requestBuilder.post(body);
			} else if (method == METHOD.POST) {
				requestBuilder.put(body);
			}
		}
		requestBuilder.url(url);	
		Request request = requestBuilder.build();
		try {
			Response response = client.newCall(request).execute();
			int responseCode = response.code();
			String responseBody = response.body().string();
			System.out.println("Response Code : " + responseCode);
			System.out.println("Response Body : " + responseBody);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
