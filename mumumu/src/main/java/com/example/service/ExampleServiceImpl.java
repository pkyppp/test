package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class ExampleServiceImpl implements ExampleService {

	public List<Map> getData() {
		Map<String, String> dataMap
		= Maps.newHashMap("aa", "aa1desu");
		
		List list = new ArrayList();
		list.add(dataMap);
		return list;
	}
}
