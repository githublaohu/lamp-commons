package com.lamp.commons.lang.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lamp.commons.lang.annotation.RuleGroup.Rule;
import com.lamp.commons.lang.annotation.annotation.Main;

public class AnalysisRule {

	private Map<Class<?> , Rule>      ruleHash      = new HashMap<>( );
	
	private Map<Class<?> , RuleGroup> ruleGroupHash = new HashMap<>( );
	
	
	public void analysis( List< Class<?> > listClass ) {
		RuleGroup ruleGroup;
		for(Class<?> clazz : listClass){
			ruleGroup = fromClassGetRuleGroup(clazz);
			ruleGroupHash.put( ruleGroup.getMain( ).getMain( ) , ruleGroup );
		}
	}

	public RuleGroup getRuleGroup(Class<?> clazz){
		return ruleGroupHash.get( clazz );
	}
	
	
	private RuleGroup fromClassGetRuleGroup( Class<?> clazz ) {
		RuleGroup relueGroup = new RuleGroup( );
		getRule( relueGroup.getMain( ) , clazz , relueGroup );
		return relueGroup;
	}
	
	private void getRule( Rule ruleMain , Class<?> clazz, RuleGroup relueGroup ) {
		Rule rule = ruleHash.get( clazz );
		if( rule != null){
			if( relueGroup == null){
				ruleMain.getRelyRuleMap( ).put(clazz, rule );
			}else{
				relueGroup.getFromRuleMap( ).put(clazz, rule );
			}
			return;
		}
		Class<?> type=null;
		Type ty;
		Field[ ] fields =Utils.getClazzFields( clazz );
		for ( Field field : fields ) {					
			ty = field.getGenericType( );
			field.setAccessible( true );
			rule = new Rule( ).setClazz( clazz ).setField( field ).setFieldName( field.getName( ) );
			ruleHash.put( clazz , rule );
			if( ty instanceof Class){
				type = (Class<?>)ty;
				if( !type.isAnnotation( ))
					continue;
				if ( field.getAnnotation( Main.class ) != null ) {
					if(ruleMain.getField( ) != null){
						ruleMain.setFiledMain( ruleMain.getField( ) );
					}
					ruleMain.setMain( type ).setClazz( clazz ).setField( field ).setFieldName( field.getName( ) );
					continue;
				}
			}else if(ty instanceof ParameterizedType){
				ParameterizedType  pt = ( ParameterizedType ) ty;
				type = ( Class< ? > ) pt.getActualTypeArguments( )[ 0 ];
				rule.setList( true );
				if ( pt.getRawType( ) == List.class && !type.isAnnotation( )) {					
					getRule(  rule , type ,null );
					relueGroup.getFromRuleMap( ).put(rule.getMain( ), rule );
					continue;
				} 
			}
			ruleMain.getRelyRuleMap( ).put(type, rule );
		}
	}
}
