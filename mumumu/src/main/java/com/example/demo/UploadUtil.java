package com.example.demo;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

public class UploadUtil {
	public interface UploadStrategy {
		Serializable upload(Serializable data);
	}
	
	private static UploadStrategy uploadStrategy;
	
	public static Serializable upload(Serializable data) {
		return uploadStrategy.upload(data);
	}
	
	@Autowired(required = false)
	private void setUploadStrategy(UploadStrategy strategy) {
		UploadUtil.uploadStrategy = strategy;
	}
}
