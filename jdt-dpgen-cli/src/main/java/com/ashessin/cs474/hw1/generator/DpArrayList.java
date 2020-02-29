package com.ashessin.cs474.hw1.generator;

import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DpArrayList<T extends DpSource> extends ArrayList<T> {

	private static final Logger log = LoggerFactory.getLogger(DpArrayList.class);

	public boolean add(T t, Class<? extends DesignPatternGen> sourceClass) {
		if (log.isDebugEnabled()) {
			LoggingReflection.debugLogProxy(t, sourceClass);
		}
		return add(t);
	}

	public <A extends T> boolean add(List<A> tList, Class<? extends DesignPatternGen> sourceClass) {
		tList.forEach(t -> add(t, sourceClass));
		return true;
	}

	// TODO: Eliminate single usage
	public <A extends T, B extends T> boolean add(Map<A, List<B>> tMap,
												  Class<? extends DesignPatternGen> sourceClass) {
		tMap.forEach((key, value) -> {
			add(key, sourceClass);
			value.forEach(b -> add(b, sourceClass));
		});
		return true;
	}
}
