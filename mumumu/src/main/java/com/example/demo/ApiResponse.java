package com.example.demo;

import org.omg.CORBA.SystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.Getter;

public class ApiResponse {
	@Getter
	private RequestEntity<?> request;
	@Getter
	private ResponseEntity<?> response;
	@Getter
	private boolean isNoResponse = false;
	@Getter
	private boolean isConnectionFailed = false;
	@Getter
	private Exception cause;
	
	public ApiResponse(RequestEntity<?> request, ResponseEntity<?> response, boolean isNoResponse,
			boolean isConnectionFailed, Exception cause) {

		this.request = request;
		this.response = response;
		this.isNoResponse = isNoResponse;
		this.isConnectionFailed = isConnectionFailed;
		this.cause = cause;
	}
	
	public String getRequestToStr() {
		if (request != null) {
			return request.toString();
		}
		return "None";
	}
	
	public String getResponseToStr() {
		if (response != null) {
			return response.toString();
		}
		return "None";
	}
	public HttpStatus getStatusCode() {
		if (response != null) {
			return response.getStatusCode();
		}
		return null;
	}
	
	public <T> T getApiData(Class<T> target) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		T dto;
		try {
			dto = mapper.readValue(response.getBody(), target);
		}catch(Exception e) {
			throw new SystemException("djnfjdnsfk get Class=" + target +", response="+ getResponseToStr(), e)
		}
	}
	
}
