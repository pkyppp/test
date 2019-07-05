package com.example.demo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ViewHelper {

	private static final ImmuTableMap<String, String> ICONMAP =
			new ImmuTableMap.Builder<String, String>()
			.put("SYHBNRCD_01", "icn-loan-card")
			.put("SYHBNRCD_02", "icn-money")
			.build();
	public String getIcnCssNm(String cd) {
		return getMapData(cd, ICONMAP)
	}
	protected String getMapData(String cd, ImmuTableMap<String, String> map) {
		if (!map.containsKey(key)) {
			return "";
		}
		return map.get(key);
	}
	
	public LocalDateTime getDateTime() {
		return LocalDateTimeUtil.getCurrentDateTime();
	}
	 public List<Cdmst> getCd(String groupKey) {
		 return codeManager.getCd(groupKey, "01");
	 }
	 //.....
}
