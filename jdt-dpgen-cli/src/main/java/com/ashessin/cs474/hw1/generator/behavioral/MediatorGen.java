package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MediatorGen extends DesignPatternGen {

	private static final String NOTIFY = "notify";
	private static final String RECEIVED = "Received";
	private static final String RECEIVE = "Receive";
	private static final String MESSAGE = "Message";
	private static final String TYPE = "Object";
	private String packageName;
	private String mediatorName;
	private String concreteMediatorName;
	private String colleagueName;
	private List<String> concreteColleagueNames;

	public MediatorGen(String packageName, String mediatorName, String concreteMediatorName,
					   String colleagueName, List<String> concreteColleagueNames) {
		this.packageName = packageName;
		this.mediatorName = mediatorName;
		this.concreteMediatorName = concreteMediatorName;
		this.colleagueName = colleagueName;
		this.concreteColleagueNames = concreteColleagueNames;
	}

	public DpArrayList<DpSource> main() {

		DpInterfaceSource mediator = DpInterfaceSource.newBuilder(packageName, mediatorName)
				.addMethod(DpSourceMethod.newBuilder()
						.setName(NOTIFY + colleagueName)
						.addParameter(colleagueName.toLowerCase(), colleagueName)
						.addParameter(MESSAGE.toLowerCase(), TYPE)
						.build())
				.build();

		dpSources.add(mediator, this.getClass());

		DpClassSource concreteMediator = DpClassSource.newBuilder(packageName, concreteMediatorName)
				.addImplementsInterface(mediatorName)
				.addField(DpSourceField.newBuilder(colleagueName.toLowerCase() + "s",
						String.format("java.util.List<%s>", colleagueName))
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.addParameter(colleagueName.toLowerCase(), colleagueName)
						.setName("add" + colleagueName)
						.setBody(String.format("%s.add(%s);",
								colleagueName.toLowerCase() + "s", colleagueName.toLowerCase()))
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setIsConstructor(Boolean.TRUE)
						.setBody(String.format("%s = new java.util.ArrayList<>(1);",
								colleagueName.toLowerCase() + "s"))
						.build())
				.addMethod(DpSourceMethod.newBuilder(mediator.getMethods().get(0))
						.setBody(String.format(
								"%s.stream()\n" +
								"	.filter(receiver%s -> %s != receiver%s)\n" +
								"	.forEachOrdered(receiver%s -> receiver%s.receive(%s));",
								colleagueName.toLowerCase() + "s",
								colleagueName, colleagueName.toLowerCase(), colleagueName,
								colleagueName, colleagueName, MESSAGE.toLowerCase()))
						.build())
				.build();

		dpSources.add(concreteMediator, this.getClass());

		DpClassSource colleague = DpClassSource.newBuilder(packageName, colleagueName)
				.setModifier(DpClassSource.Modifier.ABSTRACT)
				.addField(DpSourceField.newBuilder(mediatorName.toLowerCase(), mediatorName)
						.setAccessModifier(DpSourceField.AccessModifier.PROTECTED)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setIsConstructor(Boolean.TRUE)
						.addParameter(mediatorName.toLowerCase(), mediatorName)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(NOTIFY + colleagueName)
						.addModifier(DpSourceMethod.Modifier.ABSTRACT)
						.addParameter(MESSAGE.toLowerCase(), TYPE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(RECEIVE.toLowerCase())
						.addModifier(DpSourceMethod.Modifier.ABSTRACT)
						.addParameter(MESSAGE.toLowerCase(), TYPE)
						.build())
				.addField(DpSourceField.newBuilder(RECEIVED.toLowerCase() + MESSAGE, TYPE)
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setReturnType(TYPE)
						.setName("get" + RECEIVED + MESSAGE)
						.setAccessModifier(DpSourceMethod.AccessModifier.PROTECTED)
						.setBody(String.format("return this.%s;", RECEIVED.toLowerCase() + MESSAGE))
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName("set" + RECEIVED + MESSAGE)
						.setAccessModifier(DpSourceMethod.AccessModifier.PROTECTED)
						.addParameter(RECEIVED.toLowerCase() + MESSAGE, TYPE)
						.setBody(String.format("this.%s = %s;", RECEIVED.toLowerCase() + MESSAGE,
								RECEIVED.toLowerCase() + MESSAGE))
						.build())
				.build();

		dpSources.add(colleague, this.getClass());

		List<DpClassSource> concreteColleagues = concreteColleagueNames.stream()
				.map(concreteColleagueName -> DpClassSource.newBuilder(packageName, concreteColleagueName)
						.setExtendsClass(colleagueName)
						.addMethod(DpSourceMethod.newBuilder()
								.setIsConstructor(Boolean.TRUE)
								.addParameter(mediatorName.toLowerCase(), mediatorName)
								.setBody(String.format("super(%s);", mediatorName.toLowerCase()))
								.build())
						.addMethod(DpSourceMethod.newBuilder(colleague.getMethods().get(1))
								.setBody(String.format("this.%s.%s(this, %s);",
										mediatorName.toLowerCase(), NOTIFY + colleagueName,
										mediatorName.toLowerCase()))
								.build())
						.addMethod(DpSourceMethod.newBuilder(colleague.getMethods().get(2))
								.setBody(String.format("this.set%s(%s);",
										RECEIVED + MESSAGE, MESSAGE.toLowerCase()))
								.build())
						.build())
				.collect(toList());

		dpSources.add(concreteColleagues, this.getClass());

		return dpSources;
	}
}