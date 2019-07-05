package com.example.demo;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.Charset;

import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;

@Component
public class ApiRequestUtil {
	private static RestTemplate restTemplate;
	
	@Autowired(required = false)
	private void setRestTemplate(RestTemplate restTemplate) {
		ApiRequestUtil.restTemplate = restTemplate;
	}
	
	public static ApiResponse request(String requestUrl) {
		RequestEntity<?> requestEntity = createBodyBuilder(requestUrl).build();
		return callApi(requestEntity);
	}
	
	public static ApiResponse request(String requestUrl, Object inputDto) {
		RequestEntity<?> requestEntity = createBodyBuilder(requestUrl).body(inputDto);
		return callApi(requestEntity);
	}
	
	public static HttpComponentsClientHttpRequestFactory createPoolingHttpRequestFactory(
			int connectionMaxSize, boolean isKeepAlive, int timeOutMills) {
		HttpClientBuilder builder = HttpClientBuilder.create();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		
		HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(0, false);
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionTimeOut(timeOutMills)
				.setConnectionRequestTimeOut(timeOutMills)
				.setSocketTimeOut(timeOutMills)
				.build();
		connManager.setMaxTotal(connectionMaxSize);
		connManager.setDefaultMaxPerRoute(connectionMaxSize);
		connManager.setDefaultSocketConfig(
				SocketConfig.custom().setSoKeepAlive(isKeepAlive).build());
		builder.setDefaultRequestConfig(requestConfig);
		builder.setRetryHandler(retryHandler);
		builder.setConnectionManager(connectionManager);
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		HttpClient client = builder.disableCookieManagement().build();
		factory.setHttpClient(client);
		return factory;		
	}
	
	private static BodyBuilder createBodyBuilder(String requestUrl) {
		URI uri = null;
		try {
			UriBuilder uriBuilder = new URIBuilder(requestUrl);
			uri = uriBuilder.build();
			
		} catch (Exception e) {
			throw new SystemException("dsdxxxx URL="+ requestUrl, e);
		}
		return RequestEntity.post(uri) 
				.contentType(new MediaType("application", "json", Charset.forName("utf-8")))
				.accept(MediaType.APPLICATION_JSON_UTF8);
	}
	
	private static ApiResponse callApi(RequestEntity<?> request) {
		ResponseEntity<String> response = null;
		boolean isNoResponse = false;
		boolean isConnectionFailed = false;
		Exception cause = null;
		try {
			response = restTemplate.exchange(request, String.class);
		}catch(ResourceAccessException e) {
			if (e.getCause() instanceof SocketTimeoutException || e.getCause() instanceof NoHttpResponseException) {
				isNoResponse = true;
				cause = e;
			} else {
				isConnectionFailed = true;
				cause = e;
			}
		}
		return new ApiResponse(request, response, isNoResponse, isConnectionFailed, cause);
	}
	
}
