package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.*;

public class InterpreterGen extends DesignPatternGen {

	private static final String INTERPRET = "interpret";
	private static final String FIRST = "first";
	private static final String SECOND = "second";
	private static final String OPERAND = "Operand";
	private static final String OPERANDS = OPERAND + "s";
	private static final String DATA = "data";
	private static final String RESULT = "Result";
	private static final String BOOLEAN = "boolean";

	private String packageName;
	private String contextName;
	private String abstractExpressionName;
	private String terminalExpressionName;
	private String orTerminalExpressionName;
	private String andTerminalExpressionName;


	public InterpreterGen(String packageName, String contextName,
						  String abstractExpressionName, String terminalExpressionName,
						  String orTerminalExpressionName, String andTerminalExpressionName) {
		this.packageName = packageName;
		this.contextName = contextName;
		this.abstractExpressionName = abstractExpressionName;
		this.terminalExpressionName = terminalExpressionName;
		this.orTerminalExpressionName = orTerminalExpressionName;
		this.andTerminalExpressionName = andTerminalExpressionName;
	}

	public DpArrayList<DpSource> main() {

		DpClassSource abstractExpression = DpClassSource.newBuilder(packageName, abstractExpressionName)
				.setModifier(DpClassSource.Modifier.ABSTRACT)
				.addMethod(DpSourceMethod.newBuilder()
						.addModifier(DpSourceMethod.Modifier.ABSTRACT)
						.setName(INTERPRET)
						.addParameter(contextName.toLowerCase(), contextName)
						.build())
				.build();

		dpSources.add(abstractExpression, this.getClass());

		String interpretMethodBody = String.format(
				"%s.%s(%s);\n" +
				"%s.%s(%s);\n" +
				"\n" +
				"java.util.List<Boolean> %s = %s.get%s();\n" +
				"\n" +
				"Boolean %s = %s.get(0);\n" +
				"Boolean %s = %s.get(1);",
				FIRST + abstractExpressionName, INTERPRET, contextName.toLowerCase(),
				SECOND + abstractExpressionName, INTERPRET, contextName.toLowerCase(),
				OPERANDS.toLowerCase(), contextName.toLowerCase(), OPERANDS,
				FIRST + OPERAND, OPERANDS.toLowerCase(),
				SECOND + OPERAND, OPERANDS.toLowerCase());
		String andInterpreterMethodBody = interpretMethodBody.concat(String.format("%s.setResult(%s %s %s);",
				contextName.toLowerCase(), FIRST + OPERAND, "&&", SECOND + OPERAND));
		String orInterpreterMethodBody = interpretMethodBody.concat(String.format("%s.setResult(%s %s %s);",
				contextName.toLowerCase(), FIRST + OPERAND, "||", SECOND + OPERAND));

		DpClassSource andInterpreter = getInterpreter(abstractExpression, andInterpreterMethodBody,
				andTerminalExpressionName);

		dpSources.add(andInterpreter, this.getClass());

		DpClassSource orInterpreter = getInterpreter(abstractExpression, orInterpreterMethodBody,
				orTerminalExpressionName);

		dpSources.add(orInterpreter, this.getClass());

		DpClassSource terminalExpression = DpClassSource.newBuilder(packageName, terminalExpressionName)
				.setExtendsClass(abstractExpressionName)
				.addField(DpSourceField.newBuilder(DATA, BOOLEAN)
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setIsConstructor(Boolean.TRUE)
						.addParameter(DATA, BOOLEAN)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(INTERPRET)
						.addParameter(contextName.toLowerCase(), contextName)
						.setBody(String.format("%s.add%s(this.%s);",
								contextName.toLowerCase(), OPERAND, DATA))
						.build())
				.build();

		dpSources.add(terminalExpression, this.getClass());

		DpClassSource context = DpClassSource.newBuilder(packageName, contextName)
				.addField(DpSourceField.newBuilder(OPERANDS.toLowerCase(),
						"java.util.List<Boolean>", "new java.util.ArrayList<>()")
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.setGetter(Boolean.TRUE)
						.build())
				.addField(DpSourceField.newBuilder(RESULT.toLowerCase(), "Boolean", "null")
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.setSetter(Boolean.TRUE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setReturnType(BOOLEAN)
						.setName("is" + RESULT)
						.setBody(String.format("return %s;", RESULT.toLowerCase()))
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName("add" + OPERAND)
						.addParameter(OPERAND.toLowerCase(), "Boolean")
						.setBody(String.format("%s.add(%s);", OPERANDS.toLowerCase(), OPERAND.toLowerCase()))
						.build())
				.build();

		dpSources.add(context, this.getClass());

		return dpSources;
	}

	private DpClassSource getInterpreter(DpClassSource abstractExpression, String orInterpreterMethodBody,
										 String orTerminalExpressionName) {
		return DpClassSource.newBuilder(packageName, orTerminalExpressionName)
				.setExtendsClass(abstractExpressionName)
				.addField(DpSourceField.newBuilder(FIRST + abstractExpressionName, abstractExpressionName)
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.build())
				.addField(DpSourceField.newBuilder(SECOND + abstractExpressionName, abstractExpressionName)
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setIsConstructor(Boolean.TRUE)
						.addParameter(FIRST + abstractExpressionName, abstractExpressionName)
						.addParameter(SECOND + abstractExpressionName, abstractExpressionName)
						.build())
				.addMethod(DpSourceMethod.newBuilder(abstractExpression.getMethods().get(0))
						.addModifier(DpSourceMethod.Modifier.NONE)
						.setBody(orInterpreterMethodBody)
						.build())
				.build();
	}
}