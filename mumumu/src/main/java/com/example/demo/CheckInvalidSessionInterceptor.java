package com.example.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CheckInvalidSessionInterceptor extends HandlerInterceptorAdapter{
	
	private final SystemManager sm;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse reponse, Object handler) throws Exception{
		//check...
		return true;
	}

}
