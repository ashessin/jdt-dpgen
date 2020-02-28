package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.*;
import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ObserverGen {

	private static final Logger log = LoggerFactory.getLogger(ObserverGen.class);
	private static final String UPDATE = "update";
	private static final String STATE = "State";
	private static final String TYPE = "int";
	private static final String ATTACH = "attach";
	private static final String DETACH = "detach";
	private static final String NOTIFY = "notify";
	private String packageName;
	private String observerName;
	private List<String> concreteObserverNames;
	private String subjectName;
	private String concreteSubjectName;

	public ObserverGen(String packageName, String observerName, List<String> concreteObserverNames,
					   String subjectName, String concreteSubjectName) {
		this.packageName = packageName;
		this.observerName = observerName;
		this.concreteObserverNames = concreteObserverNames;
		this.subjectName = subjectName;
		this.concreteSubjectName = concreteSubjectName;
	}

	public DpArrayList<DpSource> main() {

		DpArrayList<DpSource> dpSources = new DpArrayList<>();

		if (log.isInfoEnabled()) {
			LoggingReflection.infoLogInstance(this);
		}

		DpInterfaceSource observer = DpInterfaceSource.newBuilder(packageName, observerName)
				.addMethod(DpSourceMethod.newBuilder()
						.setName(UPDATE)
						.build())
				.build();

		dpSources.add(observer, this.getClass());

		List<DpClassSource> concreteObservers = concreteObserverNames.stream()
				.map(concreteObserverName -> DpClassSource.newBuilder(packageName, concreteObserverName)
						.addImplementsInterface(observerName)
						.addField(DpSourceField.newBuilder(STATE.toLowerCase(), TYPE)
								.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
								.build())
						.addField(DpSourceField.newBuilder(subjectName.toLowerCase(), concreteSubjectName)
								.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
								.build())
						.addMethod(DpSourceMethod.newBuilder()
								.setIsConstructor(Boolean.TRUE)
								.build())
						.addMethod(DpSourceMethod.newBuilder(observer.getMethods().get(0))
								.setBody(String.format("%s = %s.get%s();",
										STATE.toLowerCase(), subjectName.toLowerCase(), STATE))
								.build())
						.build())
				.collect(toList());

		dpSources.addAll(concreteObservers, this.getClass());

		DpClassSource subject = DpClassSource.newBuilder(packageName, subjectName)
				.setModifier(DpClassSource.Modifier.ABSTRACT)
				.addField(DpSourceField.newBuilder(observerName.toLowerCase() + "s",
						String.format("java.util.List<%s>", observerName),
						"new java.util.ArrayList<>(2)")
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(ATTACH)
						.addParameter(observerName.toLowerCase(), observerName)
						.setBody(String.format("%s.add(%s);",
								observerName.toLowerCase() + "s", observerName.toLowerCase()))
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(DETACH)
						.addParameter(observerName.toLowerCase(), observerName)
						.setBody(String.format("%s.remove(%s);",
								observerName.toLowerCase() + "s", observerName.toLowerCase()))
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(NOTIFY + observerName + "s")
						.setBody(String.format("%s.forEach(%s::%s);",
								observerName.toLowerCase() + "s", observerName, UPDATE))
						.build())
				.build();

		dpSources.add(subject, this.getClass());

		DpClassSource concreteSubject = DpClassSource.newBuilder(packageName, concreteSubjectName)
				.setExtendsClass(subjectName)
				.addField(DpSourceField.newBuilder(STATE.toLowerCase(), TYPE)
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.setGetter(Boolean.TRUE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName("set" + STATE)
						.addParameter(STATE.toLowerCase(), TYPE)
						.setBody(String.format("this.%s = %s; this.%s();",
								STATE.toLowerCase(), STATE.toLowerCase(), NOTIFY + observerName + "s"))
						.build())
				.build();

		dpSources.add(concreteSubject, this.getClass());

		return dpSources;
	}
}