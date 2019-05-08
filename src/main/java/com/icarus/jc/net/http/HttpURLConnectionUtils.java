package com.icarus.jc.net.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.icarus.jc.StringUtils;

public class HttpURLConnectionUtils {
	
	private static final int TIMEOUT = 5000;
	
//	@SuppressWarnings("restriction")
	private static void doTrustToCertificates() throws Exception {

		// Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
 
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
 
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}
	
	/**
	 * 
	 * @param method
	 * @param spec The String to parse as an URL
	 * @param token
	 * @param mediaType
	 * @param parameterMap
	 * @return
	 */
	public static void sendRequest(String method, String spec, String token, String mediaType,
			Map<String, String> parameterMap) {

		try {
			// If it is HTTPS request
			if (spec.startsWith("https://")) {
				doTrustToCertificates();
			}
			
			URL url = new URL(spec);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// Add request header
			conn.setRequestMethod(method);
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			conn.setRequestProperty("Content-Type", "application/json");
			if (token != null && token.trim().length() > 0) {
				conn.setRequestProperty("Authorization", token);
			}
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(TIMEOUT);
			if (!StringUtils.isEmpty(parameterMap)) {
				String requestBody = RequestParameterUtils.convertParameterToBody(parameterMap, mediaType);
				conn.setDoOutput(true);
				DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(requestBody);
				dos.flush();
				dos.close();
			}
			int responseCode = conn.getResponseCode();
			System.out.println("Response Code : " + responseCode);
			if (responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				String inputLine;
				StringBuilder responseBody = new StringBuilder();
				while ((inputLine = br.readLine()) != null) {
					responseBody.append(inputLine);
				}
				br.close();
				System.out.println("Response Body : " + responseBody.toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		sendRequest("GET", "https://google.com", null, null, null);
	}

}
