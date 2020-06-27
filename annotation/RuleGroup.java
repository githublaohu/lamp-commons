package com.lamp.commons.lang.annotation;

import java.lang.reflect.Field ;
import java.util.HashMap;
import java.util.Map;

import lombok.Data ;
import lombok.experimental.Accessors;

@Data
public class RuleGroup {

	
	private Rule  main = new Rule();
	
	
	private Map<Class<?> , Rule>  fromRuleMap = new HashMap<>( );
	
	
	
	@Data
	@Accessors(chain=true)
	static class Rule{
		
		private Class<?> main;
		
		private Class<?> clazz;
		
		private String fieldName;
		
		private Field field;
		
		private Field filedMain;
		
		private boolean isList = false;
		
		private Map<Class<?> , Rule>  relyRuleMap = new HashMap<>( );
		
		private Object object;		
	}
	
	
	
	
}
