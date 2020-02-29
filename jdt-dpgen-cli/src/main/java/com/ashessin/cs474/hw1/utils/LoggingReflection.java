package com.ashessin.cs474.hw1.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Acts as proxy for logging fields in the source class using reflection.
 */
public final class LoggingReflection {

	private static final Logger log = LoggerFactory.getLogger(LoggingReflection.class);

	private LoggingReflection() {
	}

	public static void debugLogInstance(Object instance) {

		try {
			Class<?> caller = Class.forName(instance.getClass().getName());
			final Logger log = LoggerFactory.getLogger(caller);
			final Field[] declaredFields = caller.getDeclaredFields();

			for (Field field : declaredFields) {
				if (!Modifier.isStatic(field.getModifiers())) {
					field.setAccessible(Boolean.TRUE);
					log.debug("{}={}", field.getName(), field.get(instance));
				}
			}

		} catch (ClassNotFoundException | IllegalAccessException e) {
			log.error("Exception while logging instance fields: {}", e.getMessage());
		}
	}

	public static void debugLogProxy(Object instance, Class<?> originalSrc) {
		final Logger log = LoggerFactory.getLogger(originalSrc);
		log.debug("{}", instance);
	}
}
