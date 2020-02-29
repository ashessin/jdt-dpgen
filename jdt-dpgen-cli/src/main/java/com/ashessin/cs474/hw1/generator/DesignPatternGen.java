package com.ashessin.cs474.hw1.generator;

import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DesignPatternGen {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	protected DpArrayList<DpSource> dpSources = new DpArrayList<>();

	public final DpArrayList<DpSource> method() {
		if (log.isInfoEnabled()) {
			LoggingReflection.debugLogInstance(this);
		}
		return main();
	}

	protected abstract DpArrayList<DpSource> main();
}
