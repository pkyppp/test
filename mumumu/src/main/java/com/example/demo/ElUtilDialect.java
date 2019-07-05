package com.example.demo;

import java.util.Set;

import org.assertj.core.util.Sets;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
@Component
public class ElUtilDialect extends AbstractDialect implements IExpressionObjectDialect {
	private FunctionUtil functionUtil;
	private ViewManager viewManager;
	private SystemManager systemManager;
	
	public ElUtilDialect(FunctionUtil functionUtil, ViewManager viewManager, SystemManager systemManager) {
		super("ElUtilDialect");
		this.functionUtil = functionUtil;
		this.viewManager = viewManager;
		this.systemManager = systemManager;
	}
	
	private static final String NXEL_FUNCTION_UTIL_NAME = "el";
	private static final String NXEL_VIEWMANAGER_NAME = "elView";
	private static final String NXEL_SYSTEMMANAGER_NAME = "elFg";
	
	private static final Set<String> ALL_EXPRESSION_NAMES = Sets.newHashSet(NXEL_FUNCTION_UTIL_NAME,
			NXEL_VIEWMANAGER_NAME, NXEL_SYSTEMMANAGER_NAME);
	
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new IExpressionObjectFactory() {
			@Override
			public Set<String> getAllExpressionObjectNames() {
				return ALL_EXPRESSION_NAMES;
			}
			@Override
			public Object buildObject(IExpressionContext context, String expressionObjectName) {
				if(expressionObjectName.equals(NXEL_FUNCTION_UTIL_NAME)) {
					return functionUtil;
				}
				if(expressionObjectName.equals(NXEL_VIEWMANAGER_NAME)) {
					return viewManager;
				}
				if(expressionObjectName.equals(NXEL_SYSTEMMANAGER_NAME)) {
					return systemManager;
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
