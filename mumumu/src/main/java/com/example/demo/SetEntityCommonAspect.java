package com.example.demo;

import org.junit.Before;
import org.springframework.batch.core.Entity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class SetEntityCommonAspect {
	private final AuditorAware<T> auditorAware;
	
	@Before("execution(* jp.co.com.xxx.domain.dao.*Dao.insert*(..))")
	public void insert(JoinPoint jp) throws Exception {
		Entity entity = getEntitySetter(jp);
		if (entity == null) {
			return;
		}
		entity.setTnntid(auditorAware.getCurrentTnntid());
		entity.setCreateDate(auditorAware.getCurrentDateTime());
	}
	
	@Before("execution(* jp.co.com.xxx.domain.dao.*Dao.update*(..))")
	public void update(JoinPoint jp) throws Exception {
		Entity entity = getEntitySetter(jp);
		if (entity == null) {
			return;
		}
		entity.setTnntid(auditorAware.getCurrentTnntid());
	}
	
	private EntitySetter getEntity(JoinPoint jp) {
		Object obj = jp.getArgs()[0];
		if (obj instanceof EntitySetter) {
			return (EntitySetter) obj;			
		}
		return null;
	}
}
