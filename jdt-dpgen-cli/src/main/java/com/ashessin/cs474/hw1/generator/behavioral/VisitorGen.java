package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.*;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class VisitorGen extends DesignPatternGen {

	private static final String COUNTER = "counter";
	private static final String ACCEPT = "accept";
	private static final String VISIT = "visit";

	private String packageName;
	private String visitorName;
	private List<String> concreteVisitorNames;
	private String elementName;
	private List<String> concreteElementNames;

	public VisitorGen(String packageName,
					  String visitorName, List<String> concreteVisitorNames,
					  String elementName, List<String> concreteElementNames) {
		this.packageName = packageName;
		this.visitorName = visitorName;
		this.concreteVisitorNames = concreteVisitorNames;
		this.elementName = elementName;
		this.concreteElementNames = concreteElementNames;
	}

	public DpArrayList<DpSource> main() {

		List<DpSourceMethod> visitorMethods = concreteElementNames.stream()
				.map(concreteElementName -> DpSourceMethod.newBuilder()
						.setName(VISIT + concreteElementName)
						.addParameter(concreteElementName.substring(0, 1).toLowerCase() + concreteElementName.substring(1),
								concreteElementName)
						.build())
				.collect(toList());
		DpInterfaceSource visitor = DpInterfaceSource.newBuilder(packageName, visitorName)
				.addMethods(visitorMethods)
				.build();

		dpSources.add(visitor, this.getClass());

		DpSourceMethod acceptMethod = DpSourceMethod.newBuilder()
				.setName(ACCEPT)
				.addParameter(visitorName.toLowerCase(), visitorName)
				.build();
		DpInterfaceSource element = DpInterfaceSource.newBuilder(packageName, elementName)
				.addMethod(acceptMethod)
				.build();

		dpSources.add(element, this.getClass());

		List<DpClassSource> concreteElements = concreteElementNames.stream()
				.map(concreteElementName -> DpClassSource.newBuilder(packageName, concreteElementName)
						.addImplementsInterface(elementName)
						.addField(DpSourceField.newBuilder(COUNTER, "int").build())
						.addMethod(DpSourceMethod.newBuilder()
								.setName("operation" + concreteElementName)
								.setBody(String.format("%s++;", COUNTER))
								.build())
						.addMethod(DpSourceMethod.newBuilder()
								.setName("get" + COUNTER)
								.setAccessModifier(DpSourceMethod.AccessModifier.PROTECTED)
								.setReturnType("int")
								.setBody(String.format("return %s;", COUNTER))
								.build())
						.addMethod(DpSourceMethod.newBuilder(acceptMethod)
								.setBody(String.format("%s.visit%s(this);",
										visitorName.toLowerCase(), concreteElementName))
								.build())
						.build())
				.collect(toList());

		dpSources.add(concreteElements, this.getClass());

		List<DpSourceMethod> concreteVisitorMethods = IntStream.range(0, concreteElementNames.size())
				.mapToObj(i -> DpSourceMethod.newBuilder(visitorMethods.get(i))
						.setBody(String.format("%s.%s();",
								visitorMethods.get(i).getParameters().keySet().toArray()[0],
								concreteElements.get(i).getMethods().get(0).getName()))
						.build())
				.collect(toList());
		List<DpClassSource> concreteVisitors = concreteVisitorNames.stream()
				.map(concreteVisitorName -> DpClassSource.newBuilder(packageName, concreteVisitorName)
						.addImplementsInterface(visitorName)
						.addMethods(concreteVisitorMethods)
						.build())
				.collect(toList());

		dpSources.add(concreteVisitors, this.getClass());

		return dpSources;
	}
}