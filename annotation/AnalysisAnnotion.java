package com.lamp.commons.lang.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator ;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry ;

import com.lamp.commons.lang.annotation.RuleGroup.Rule;
import com.lamp.commons.lang.util.Tuple.Triplet;

import lombok.Data ;
import lombok.experimental.Accessors ;


public class AnalysisAnnotion {

	private Map<Class<?> , List<RuleSerializeEntity>> results  = new HashMap<>( );
	
	private AnalysisRule analysisRule;
	
	private RuleGroup ruleGroup;
	
	private Annotation mainAnnotion;
	
	private Annotation[ ] relyAnnotation;
	
	private List<Triplet< Annotation , Object, Annotation[] >> pair;
	
	public AnalysisAnnotion(AnalysisRule analysisRule){
		this.analysisRule = analysisRule;
	}
	
	private List<RuleSerializeEntity> getResultsList(Class<?> clazz){
		List< RuleSerializeEntity > list = results.get( clazz );
		if(list == null){
			list = new ArrayList<>( );
			results.put( clazz , list );
		}
		return list;
	}
	
	
	private RuleSerializeEntity getResultMap(Class<?> clazz){
		List< RuleSerializeEntity > list = getResultsList( clazz );
		RuleSerializeEntity map = new RuleSerializeEntity( );
		list.add( map );
		return map;
	}
	
	public <T>T getBean(Class<?> t){
		return (T)null ;
		
	}
	
	public void angalysis(List<Class<?>> listClass){
		
	}
	
	@SuppressWarnings( "unchecked" )
	public <T> T angalysis( Class<?> clazz){
		getClassAllAnnotation( clazz );
		RuleSerializeEntity res = angalysis();
		return (T)createBean(res);
	}
	
	private RuleSerializeEntity angalysis(){
		RuleSerializeEntity rseMain =null;
		if( ruleGroup != null){
			Rule ruleMain = ruleGroup.getMain( );
			rseMain = getResultMap( ruleMain.getClazz( ) ).setClazz( ruleMain.getClazz( ) ).setValue( mainAnnotion ).setRule( ruleMain );
			Map< Class< ? > , Rule > relyRuleMap = ruleMain.getRelyRuleMap( );
			if( !relyRuleMap.isEmpty( ) && relyAnnotation != null){
				angalysisRuleSerializeEntity( relyAnnotation , rseMain , ruleMain ,ruleMain.getRelyRuleMap( ));
			}
			
			Map< Class< ? > , Rule > fromRuleMap = ruleGroup.getFromRuleMap( );
			if( !pair.isEmpty( )  && !fromRuleMap.isEmpty( )){
				RuleSerializeEntity res = null;
				for(Triplet< Annotation , Object, Annotation[]  >  triplet : pair){
					res = angalysisRuleSerializeEntity0( triplet.getUnit( ) , rseMain , rseMain.getChileRuleSerializeEntityMap( ) , ruleGroup.getFromRuleMap( ) );
					if(res == null)
						continue;
					res.setObject( triplet.getPair( ) );
					angalysisRuleSerializeEntity(triplet.getTriplet( ) , res , res.getRule( ) ,res.getRule( ).getRelyRuleMap( ) );
				}
			}
		}
		return rseMain;	
	}
	
	public Object createBean(RuleSerializeEntity rseMain){
		Object object = null;
		if(rseMain == null)
			return object;
		try {
			int i = 0 , f = 1;
			List<Object> list = null;
			Object value;
			if(rseMain.getValue( )==null){
				f = rseMain.getValueList( ).size( );
				object = list = new ArrayList<>( );
				value = rseMain.getValueList( ).get( i );
			}else{
				value = rseMain.getValue( );
			}
			for( ; ; ){
				object = rseMain.getClazz( ).newInstance( );
				Rule rule = rseMain.getRule( );
				rule.getField( ).set( object ,value  );
				createBean0(object , false , rseMain.getRelyRuleSerializeEntityMap( ));
				createBean0(object , true , rseMain.getChileRuleSerializeEntityMap( ));	
				if(list != null ){
					list.add( object );
					value = rseMain.getValueList( ).get( i );
				}
				if( ++i == f){
					return list == null ? object : list;
				}
			}	
		} catch ( InstantiationException | IllegalAccessException e ) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return object;
		}
	}
	
	
	private void createBean0(Object object ,boolean chile, Map< String , RuleSerializeEntity > chileRuleSerializeEntityMap) throws IllegalArgumentException, IllegalAccessException{
		if( chileRuleSerializeEntityMap.isEmpty( ))
			return ;
		Iterator< Entry< String , RuleSerializeEntity > > iterator = chileRuleSerializeEntityMap.entrySet( ).iterator( );
		RuleSerializeEntity res;
		Object value;
		Field field = null;
		for( ; ; ){
			res = iterator.next( ).getValue( );
			value = chile ? createBean( res ):res.getValue( );
			field = res.getRule( ).getFiledMain( ) != null? res.getRule( ).getFiledMain( ) : res.getRule( ).getField( );
			field.set( object , value );
			if( !iterator.hasNext( ))
				break;
		}
	}
	
	private void angalysisRuleSerializeEntity(Annotation[ ] annotation  , RuleSerializeEntity rseMain, Rule ruleMain ,Map< Class< ? > , Rule > ruleGroup){
		for(Annotation aonnotation : annotation){
			angalysisRuleSerializeEntity0( aonnotation , rseMain , rseMain.getRelyRuleSerializeEntityMap( ) , ruleGroup );
		}
	}
	
	
	private RuleSerializeEntity angalysisRuleSerializeEntity0(Annotation aonnotation  , RuleSerializeEntity rseMain ,Map<String ,RuleSerializeEntity>  resMap ,Map< Class< ? > , Rule > ruleGroup){
		Rule rule = ruleGroup.get( aonnotation.annotationType( ) );
		if( rule == null)
			return null;
		RuleSerializeEntity rse = resMap.get( rule.getFieldName( ) );
		if(rse == null){
			rse = new RuleSerializeEntity().setClazz( rule.getClazz( ) ).setRule( rule );
			resMap.put( rule.getFieldName( ) , rse );					
		}
		if( rule.isList( )){
			List<Annotation> list = rse.getValueList( );
			if(list == null){
				list = rse.setValueList( new ArrayList<>( ) ).getValueList( );
			}
			list.add( aonnotation );
		}else{
			rse.setValue( aonnotation );
		}
		return rse;
	}
	
	private List<Triplet< Annotation , Object,Annotation[] >> getClassAllAnnotation(Class<?> clazz){
		Annotation[ ] annotations = clazz.getAnnotations( );
		for(Annotation aonnotation : annotations){
			ruleGroup = analysisRule.getRuleGroup( aonnotation.annotationType( ) );
			if( ruleGroup != null){
				mainAnnotion   = aonnotation;
				relyAnnotation = annotations;
				continue;
			}
		}
		if( ruleGroup == null){
			return null;
		}
		pair = new ArrayList<>( );
		addPair( annotations , clazz  ,pair);		
		
		Field[ ] fields = clazz.getFields( );
		for(Field field : fields){
			addPair( field.getAnnotations( ) , field  ,pair);			
		}
		Constructor< ? >[ ] constructors = clazz.getConstructors( );
		for(Constructor<?> constructor : constructors){
			addPair( constructor.getAnnotations( ) , constructor  ,pair);			
		}		
		Method[ ] methods = clazz.getMethods( );
		for(Method method : methods){
			addPair( method.getAnnotations( ) , method  ,pair);			
		}
		
		return pair;
	}
	
	private void addPair(Annotation[ ] annotations , Object object , List<Triplet< Annotation , Object, Annotation[]  >> pair){
		if(annotations == null){
			return;
		}
		for(Annotation aonnotation : annotations){
			pair.add(  new Triplet<>( aonnotation , object ,annotations) );
		}
	}
	
	
	@Data
	@Accessors(chain=true)
	static class RuleSerializeEntity{
		
		private Class<?> clazz;
		
		private String fieldName;
		
		private Field field;
		
		private Annotation value;
		
		private List<Annotation> valueList;
		
		private Rule rule;
		
		private RuleSerializeEntity ruleSerializeEntity;
		
		private Map<String ,RuleSerializeEntity>  relyRuleSerializeEntityMap = new HashMap<>( );
		
		private Map<String ,RuleSerializeEntity>  chileRuleSerializeEntityMap = new HashMap<>( );
		
		private Object object;
	}
	
	
}
