package com.lamp.commons.lang.annotation ;

import java.lang.annotation.Annotation ;
import java.lang.reflect.Field ;
import java.lang.reflect.Modifier ;
import java.util.HashMap ;
import java.util.Map ;

;

public class Utils {

	private static final Map< Class< ? > , Field[] > CLASS_HASH_MAP = new HashMap< Class< ? > , Field[] >( ) ;

	/*public static Object decodeCommandCustomHeader(Class<?> classHeader) throws RemotingCommandException {
	            Object objectHeader;
	            try {
	                objectHeader = classHeader.newInstance();
	            } catch (InstantiationException e) {
	                return null;
	            } catch (IllegalAccessException e) {
	                return null;
	            }

	            if ( extFields != null) {

	                Field[] fields = getClazzFields(classHeader);
	                for (Field field : fields) {
	                    if (!Modifier.isStatic(field.getModifiers())) {
	                        String fieldName = field.getName();
	                        if (!fieldName.startsWith("this")) {
	                            try {
	                                String value = this.extFields.get(fieldName);
	                                if (null == value) {
	                                    Annotation annotation = getNotNullAnnotation(field);
	                                    if (annotation != null) {
	                                        throw new RemotingCommandException("the custom field <" + fieldName + "> is null");
	                                    }

	                                    continue;
	                                }

	                                field.setAccessible(true);
	                                String type = getCanonicalName(field.getType());
	                                Object valueParsed;

	                                field.set(objectHeader, valueParsed);

	                            } catch (Throwable e) {
	                            }
	                        }
	                    }
	                }

	                objectHeader.checkFields();
	            }

	            return objectHeader;
	}*/

	public static Field[] getClazzFields ( Class< ? > classHeader ) {
		Field[] field = CLASS_HASH_MAP.get( classHeader ) ;

		if ( field == null ) {
			field = classHeader.getDeclaredFields( ) ;
			synchronized ( CLASS_HASH_MAP ) {
				CLASS_HASH_MAP.put( classHeader , field ) ;
			}
		}
		return field ;
	}

}
