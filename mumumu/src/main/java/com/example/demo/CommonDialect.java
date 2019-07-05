package com.example.demo;

import java.util.Set;

import org.assertj.core.util.Sets;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

@Component
public class CommonDialect extends AbstractProcessorDialect {
	private final CodeManager codeManager;
	private final SystemManager systemManager;
	
	private static final String PRIFIX = "ln";
	private static final String PRECEDENCE = 10000;
	
	public CommonDialect(CodeManager codeManager, SystemManager systemManager) {
		super(PRIFIX, PRIFIX, StandardDialect.PROCESSOR_PRECEDENCE);
		this.codeManager = codeManager;
		this.systemManager = systemManager;
	}
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		Set<IProcessor> processors = Sets.newHashSet();
		processors.add(new CodeSelectTagProcessor(dialectPrefix, "codeSelect", PRECEDENCE, codeManager));
		processors.add(new KznoTextTagProcessor(dialectPrefix, "kznoText", PRECEDENCE, systemManager));
		processors.add(new BrTextTagTagProcessor(dialectPrefix, "brText", PRECEDENCE));
		return processors;
	}
}
