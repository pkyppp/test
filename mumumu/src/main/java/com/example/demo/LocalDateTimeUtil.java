package com.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeUtil {

	public interface LocalDateTimeStrategy {
		LocalDateTime getCurrentDateTime();
		default LocalDate getCurrentDate() {
			return getCurrentDateTime().toLocalDate();
		}
	}
	private static LocalDateTimeStrategy localDateTimeStrategy;
	public static LocalDate getCurrentDate() {
		if (localDateTimeStrategy == null) {
			return null;
		}
		return localDateTimeStrategy.getCurrentDate();
	}
	public static LocalDateTime getCurrentDateTime() {
		if (localDateTimeStrategy == null) {
			return null;
		}
		return localDateTimeStrategy.getCurrentDateTime();
	}
	
	@Autowired(required = false)
	private void setLocalDateTimeStrategy(LocalDateTimeStrategy strategy) {
		LocalDateTimeUtil.localDateTimeStrategy = strategy;
	}
	
}
