package com.ashessin.cs474.hw1.generator.structural;

import com.ashessin.cs474.hw1.generator.*;

public class AdapterGen extends DesignPatternGen {

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