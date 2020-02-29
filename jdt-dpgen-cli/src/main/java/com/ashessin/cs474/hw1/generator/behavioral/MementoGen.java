package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.*;

public class MementoGen extends DesignPatternGen {

	private static final String STATE = "State";
	private static final String TYPE = "Object";
	private static final String CREATE = "create";
	private static final String RESTORE = "restore";
	private String packageName;
	private String momentoName;
	private String caretakerName;
	private String originatorName;

	public MementoGen(String packageName, String momentoName, String caretakerName, String originatorName) {
		this.packageName = packageName;
		this.momentoName = momentoName;
		this.caretakerName = caretakerName;
		this.originatorName = originatorName;
	}

	public DpArrayList<DpSource> main() {

		DpSourceField stateField = DpSourceField.newBuilder(STATE.toLowerCase(), TYPE)
				.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
				.build();

		DpClassSource momento = DpClassSource.newBuilder(packageName, momentoName)
				.addField(stateField)
				.addMethod(DpSourceMethod.newBuilder()
						.setIsConstructor(Boolean.TRUE)
						.addParameter(STATE.toLowerCase(), TYPE)
						.setAccessModifier(DpSourceMethod.AccessModifier.PROTECTED)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(String.format("get%s", STATE))
						.setBody(String.format("return %s;", STATE.toLowerCase()))
						.setReturnType(TYPE)
						.setAccessModifier(DpSourceMethod.AccessModifier.PROTECTED)
						.build())
				.build();

		dpSources.add(momento, this.getClass());

		DpClassSource orignator = DpClassSource.newBuilder(packageName, originatorName)
				.addField(stateField)
				.addMethod(DpSourceMethod.newBuilder()
						.setIsConstructor(Boolean.TRUE)
						.addParameter(STATE.toLowerCase(), TYPE)
						.setAccessModifier(DpSourceMethod.AccessModifier.PROTECTED)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(CREATE + momentoName)
						.setReturnType("final " + momentoName)
						.setBody(String.format("return new %s(%s);", momentoName, STATE.toLowerCase()))
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(RESTORE + momentoName)
						.addParameter(momentoName.toLowerCase(), momentoName)
						.setBody(String.format("%s = %s.get%s();",
								STATE.toLowerCase(), momentoName.toLowerCase(), STATE))
						.build())
				.build();

		dpSources.add(orignator, this.getClass());

		DpClassSource caretaker = DpClassSource.newBuilder(packageName, caretakerName)
				.addField(DpSourceField.newBuilder(momentoName.toLowerCase() + "s",
						String.format("java.util.Deque<%s>", momentoName),
						"new java.util.ArrayDeque<>()")
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName("add" + momentoName)
						.addParameter(momentoName.toLowerCase(), momentoName)
						.setBody(String.format("%s.push(%s);",
								momentoName.toLowerCase() + "s", momentoName.toLowerCase()))
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName("get" + momentoName)
						.setReturnType(momentoName)
						.setBody(String.format("return %s.pop();",
								momentoName.toLowerCase() + "s"))
						.build())
				.build();

		dpSources.add(caretaker, this.getClass());

		return dpSources;
	}
}

