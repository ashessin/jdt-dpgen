package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.*;

public class CommandGen extends DesignPatternGen {

	private static final String OPERATION = "operation";
	private static final String PERFORMED = "Performed";
	private static final String EXECUTE = "execute";
	private static final String ACTION = "action";
	private String packageName;
	private String commandName;
	private String concreteCommandName;
	private String receiverName;
	private String invokerName;


	public CommandGen(String packageName,
					  String commandName, String concreteCommandName,
					  String receiverName, String invokerName) {
		this.packageName = packageName;
		this.commandName = commandName;
		this.concreteCommandName = concreteCommandName;
		this.receiverName = receiverName;
		this.invokerName = invokerName;
	}

	public DpArrayList<DpSource> main() {

		DpInterfaceSource command = DpInterfaceSource.newBuilder(packageName, commandName)
				.addMethod(DpSourceMethod.newBuilder()
						.setName(EXECUTE)
						.build())
				.build();

		dpSources.add(command, this.getClass());

		String receiverParameterName = receiverName.substring(0, 1).toUpperCase() + receiverName.substring(1);
		DpClassSource concreteCommand = DpClassSource.newBuilder(packageName, concreteCommandName)
				.addImplementsInterface(commandName)
				.addField(DpSourceField.newBuilder(receiverParameterName, receiverName)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setIsConstructor(Boolean.TRUE)
						.addParameter(receiverParameterName, receiverName)
						.build())
				.addMethod(DpSourceMethod.newBuilder(command.getMethods().get(0))
						.setBody(String.format("this.%s.%s();", receiverParameterName, ACTION))
						.build())
				.build();

		dpSources.add(concreteCommand, this.getClass());

		DpClassSource receiver = DpClassSource.newBuilder(packageName, receiverName)
				.addField(DpSourceField.newBuilder(OPERATION + PERFORMED, "boolean", "false")
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(ACTION)
						.setBody(String.format("%s = %s;", OPERATION + PERFORMED, "true"))
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName("is" + OPERATION.substring(0, 1).toUpperCase() + OPERATION.substring(1) + PERFORMED)
						.setReturnType("boolean")
						.setBody(String.format("return %s;", OPERATION + PERFORMED))
						.build())
				.build();

		dpSources.add(receiver, this.getClass());

		DpClassSource invoker = DpClassSource.newBuilder(packageName, invokerName)
				.addField(DpSourceField.newBuilder(commandName.toLowerCase(), commandName).build())
				.addMethod(DpSourceMethod.newBuilder()
						.addParameter(commandName.toLowerCase(), commandName)
						.setIsConstructor(Boolean.TRUE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(EXECUTE)
						.setBody(String.format("%s.%s();", commandName.toLowerCase(), EXECUTE))
						.build())
				.build();

		dpSources.add(invoker, this.getClass());

		return dpSources;
	}
}