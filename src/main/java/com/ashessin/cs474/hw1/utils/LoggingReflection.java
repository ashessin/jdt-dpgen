package com.ashessin.cs474.hw1.utils;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Acts as proxy for logging fields in the source class using reflection.
 */
public class LoggingReflection {

	private LoggingReflection() {
	}

	public static void infoLogInstance(Object instance) {

		try {
			Class<?> caller = Class.forName(instance.getClass().getName());
			final Logger log = (Logger) LoggerFactory.getLogger(caller);
			final Field[] declaredFields = caller.getDeclaredFields();

			for (Field field : declaredFields) {
				if (!Modifier.isStatic(field.getModifiers())) {
					field.setAccessible(Boolean.TRUE);
					log.info("{}={}", field.getName(), field.get(instance));
				}
			}

		} catch (ClassNotFoundException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void debugLogProxy(Object instance, Class<?> originalSrc) {
		final Logger log = (Logger) LoggerFactory.getLogger(originalSrc);
		log.debug("{}", instance);
	}
}
