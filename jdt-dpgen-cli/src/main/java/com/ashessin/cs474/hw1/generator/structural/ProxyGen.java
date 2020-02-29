package com.ashessin.cs474.hw1.generator.structural;

import com.ashessin.cs474.hw1.generator.*;

import static java.util.stream.Collectors.toList;

public class ProxyGen extends DesignPatternGen {

	private static final String OPERATION_NAME = "doOperation";
	private String packageName;
	private String proxyName;
	private String subjectName;
	private String realSubjectName;

	public ProxyGen(String packageName,
					String proxyName, String subjectName, String realSubjectNames) {
		this.packageName = packageName;
		this.proxyName = proxyName;
		this.subjectName = subjectName;
		this.realSubjectName = realSubjectNames;
	}

	public DpArrayList<DpSource> main() {

		DpInterfaceSource subject = DpInterfaceSource.newBuilder(packageName, subjectName)
				.addMethod(DpSourceMethod.newBuilder()
						.setName(OPERATION_NAME)
						.build())
				.build();

		dpSources.add(subject, this.getClass());

		DpClassSource realSubject = DpClassSource.newBuilder(packageName, realSubjectName)
				.addImplementsInterface(subjectName)
				.addMethods(subject.getMethods().stream()
						.map(dpMethod -> DpSourceMethod.newBuilder(dpMethod)
								.setBody(";")
								.build())
						.collect(toList()))
				.build();

		dpSources.add(realSubject, this.getClass());

		String realSubjectField = realSubjectName.substring(0, 1).toLowerCase() + realSubjectName.substring(1);
		DpClassSource proxy = DpClassSource.newBuilder(packageName, proxyName)
				.addImplementsInterface(subjectName)
				.addField(DpSourceField.newBuilder(realSubjectField, realSubjectName)
						.setGetter(Boolean.TRUE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setIsInherited(Boolean.TRUE)
						.setName(OPERATION_NAME)
						.setBody(String.format(
								"this.%s = new %s();\n" +
								"this.%s.%s();",
								realSubjectField, realSubjectName,
								realSubjectField, OPERATION_NAME))
						.build())
				.build();

		dpSources.add(proxy, this.getClass());

		return dpSources;
	}
}