package com.example.demo;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ElDialect implements IExpressionObjectDialect{

	private final ViewHelper viewHelper;
	private static final String EXPRESSION_NAME = "viewHelper";
	private static final Set<String> ALL_EXPRESSION_NAMES = new HashSet<String>() {
		private static final long serialVersionUID = -2345667L;
		{
			add(EXPRESSION_NAME);
		}
	};
	
	public String getName() {
		return EXPRESSION_NAME;
	}
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new IExpressionObjectFactory() {
			@Override
			public Set<String> getAllExpressionObjectNames() {
				return ALL_EXPRESSION_NAMES;
			}
			@Override
			public Object buildObject(IExpressionContext context, String expressionObjectName) {
				if(expressionObjectName.equals(EXPRESSION_NAME)) {
					return viewHelper;
				}
				return null;
			}
			@Override
			public boolean isCacheable(String expressionObjectName) {
				return false;
			}
		};
	}
	
	
}
