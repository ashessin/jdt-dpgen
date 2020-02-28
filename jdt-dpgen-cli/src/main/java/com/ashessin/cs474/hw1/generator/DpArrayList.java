package com.ashessin.cs474.hw1.generator;

import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DpArrayList<T extends DpSource> extends ArrayList<T> {

	private static final Logger log = LoggerFactory.getLogger(DpArrayList.class);

	@Override
	public boolean add(T t) {
		return super.add(t);
	}

	public boolean add(T t, Class<?> sourceClass) {
		if (log.isDebugEnabled()) {
			LoggingReflection.debugLogProxy(t, sourceClass);
		}
		return add(t);
	}

	public <A extends DpSource> void addAll(List<A> tList, Class<?> sourceClass) {
		tList.forEach(t -> add((T) t, sourceClass));
	}

	// TODO: Eliminate single usage
	public <A extends DpSource, B extends DpSource> void addAll(Map<A, List<B>> tMap, Class<?> sourceClass) {
		tMap.forEach((key, value) -> {
			add((T) key, sourceClass);
			value.forEach(b -> add((T) b, sourceClass));
		});
	}
}
