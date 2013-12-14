package org.openmrs.module.openhmis.billableobjects.api.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.billableobjects.api.model.IBillableObject;
import org.openmrs.module.openhmis.billableobjects.api.model.IBillingHandler;
import org.reflections.Reflections;

public class BillableObjectsHelper {
	private static final Logger logger = Logger.getLogger(BillableObjectsHelper.class);
	private static volatile List<String> handlerTypeNames;
	private static volatile Map<String, Class<? extends IBillableObject>> classNameToBillableObjectTypeMap;
	
	public static Set<Class<? extends IBillingHandler>> locateHandlers() {
		// Search for any modules that define classes which implement the IBillingHandler interface
		Reflections reflections = new Reflections("org.openmrs.module");
		Set<Class<? extends IBillingHandler>> classes = new HashSet<Class<? extends IBillingHandler>>();
		for (Class<? extends IBillingHandler> cls : reflections.getSubTypesOf(IBillingHandler.class)) {
			// We only care about public instantiable classes so ignore others
			if (!cls.isInterface() &&
					!Modifier.isAbstract(cls.getModifiers()) &&
					Modifier.isPublic(cls.getModifiers())) {
				classes.add(cls);
			}
		}		
		return classes;
	}
	
	private static Map<String, Class<? extends IBillableObject>> locateBillableObjectTypes() {
		Map<String, Class<? extends IBillableObject>> classNameToBillableObjectTypeMap = new HashMap<String, Class<? extends IBillableObject>>();
		Reflections reflections = new Reflections("org.openmrs.module.openhmis.billableobjects.api.type");

		Set<Class<? extends IBillableObject>> allClasses = reflections.getSubTypesOf(IBillableObject.class);

		for (Class<? extends IBillableObject> cls : allClasses) {
			if (Modifier.isAbstract(cls.getModifiers())) continue;
			Annotation[] annotations = cls.getAnnotations();
			if (annotations.length == 0) {
				logger.warn("Found class implementing " + IBillableObject.class.getSimpleName() + " but it does not specify a handled class: skipping.");
				continue;
			}
			
			for (Annotation annotation : annotations) {
				if (annotation instanceof Handler) {
					Handler handler = (Handler) annotation;
					Class<?>[] supported = handler.supports();
				for (Class<?> supportedClass : supported)
		    		classNameToBillableObjectTypeMap.put(supportedClass.getName(), cls);
				}
			}
		}
		return classNameToBillableObjectTypeMap;
	}
	
	public static List<String> getHandlerTypeNames() {
		if (handlerTypeNames == null) {
			Set<Class<? extends IBillingHandler>> classes = locateHandlers();
			handlerTypeNames = new ArrayList<String>(classes.size());
			for (Class<? extends IBillingHandler> cls : classes) {
				handlerTypeNames.add(cls.getSimpleName());
			}
		}
		return handlerTypeNames;
	}
	
	public static Class<? extends IBillableObject> getBillableObjectTypeForClassName(String className) {
		if (classNameToBillableObjectTypeMap == null)
			classNameToBillableObjectTypeMap = locateBillableObjectTypes();
		return classNameToBillableObjectTypeMap.get(className);
	}
	
	public static Set<String> getHandledTypeNames() {
		if (classNameToBillableObjectTypeMap == null)
			classNameToBillableObjectTypeMap = locateBillableObjectTypes();
		return classNameToBillableObjectTypeMap.keySet();		
	}
}
