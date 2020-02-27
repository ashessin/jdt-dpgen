package com.ashessin.cs474.hw1.generator.structural;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.*;
import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.LoggerFactory;

public class AdapterGen {

	private static final Logger log = (Logger) LoggerFactory.getLogger(AdapterGen.class);
	private static final String REQUEST = "Request";
	private static final String SPECIAL = "special";
	private static final String TYPE = "Object";
	private String packageName;
	private String adapterName;
	private String adapteeName;
	private String targetName;

	public AdapterGen(String packageName, String adapterName, String adapteeName, String targetName) {
		this.packageName = packageName;
		this.adapterName = adapterName;
		this.adapteeName = adapteeName;
		this.targetName = targetName;
	}

	public DpArrayList<DpSource> main() {

		DpArrayList<DpSource> dpSources = new DpArrayList<>();

		if (log.isInfoEnabled()) {
			LoggingReflection.infoLogInstance(this);
		}

		DpInterfaceSource target = DpInterfaceSource.newBuilder(packageName, targetName)
			.addMethod(DpSourceMethod.newBuilder()
				.setName(REQUEST.toLowerCase())
				.setReturnType(TYPE)
				.build())
			.build();

		dpSources.add(target, this.getClass());

		DpClassSource adaptee = DpClassSource.newBuilder(packageName, adapteeName)
			.addMethod(DpSourceMethod.newBuilder()
				.setName(SPECIAL + REQUEST)
				.setReturnType(TYPE)
				.setBody(String.format("return \"%s\";", SPECIAL + REQUEST))
				.build())
			.build();

		dpSources.add(adaptee, this.getClass());

		DpClassSource adapter = DpClassSource.newBuilder(packageName, adapterName)
			.addImplementsInterface(targetName)
			.setExtendsClass(adapteeName)
			.addMethod(DpSourceMethod.newBuilder(target.getMethods().get(0))
				.setBody(String.format("return this.%s();", SPECIAL + REQUEST))
				.build())
			.build();

		dpSources.add(adapter, this.getClass());

		return dpSources;
	}
}