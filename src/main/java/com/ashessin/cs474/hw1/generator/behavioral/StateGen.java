package com.ashessin.cs474.hw1.generator.behavioral;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.*;
import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class StateGen {

	private static final Logger log = (Logger) LoggerFactory.getLogger(StateGen.class);
	private static final String HANDLE = "Handle";
	private static final String INVOKED = "Invoked";
	private static final String REQUEST = "request";
	private static final String CONCRETE = "Concrete";
	private String packageName;
	private String stateName;
	private List<String> concreteStateNames;
	private String contextName;

	public StateGen(String packageName, String stateName, List<String> concreteStateNames, String contextName) {
		this.packageName = packageName;
		this.stateName = stateName;
		this.concreteStateNames = concreteStateNames;
		this.contextName = contextName;
	}

	public DpArrayList<DpSource> main() {

		DpArrayList<DpSource> dpSources = new DpArrayList<>();

		if (log.isInfoEnabled()) {
			LoggingReflection.infoLogInstance(this);
		}

		DpInterfaceSource state = DpInterfaceSource.newBuilder(packageName, stateName)
			.addMethod(DpSourceMethod.newBuilder()
				.setName(HANDLE.toLowerCase())
				.build())
			.build();

		dpSources.add(state, this.getClass());

		List<DpClassSource> concreteStates = concreteStateNames.stream()
			.map(concreteStateName -> DpClassSource.newBuilder(packageName, CONCRETE + concreteStateName)
				.addImplementsInterface(stateName)
				.addField(DpSourceField.newBuilder(HANDLE.toLowerCase() + INVOKED, "boolean")
					.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
					.build())
				.addMethod(DpSourceMethod.newBuilder(state.getMethods().get(0))
					.setBody(String.format("this.%s = true;", HANDLE.toLowerCase() + INVOKED))
					.build())
				.addMethod(DpSourceMethod.newBuilder()
					.setReturnType("boolean")
					.setAccessModifier(DpSourceMethod.AccessModifier.PROTECTED)
					.setName(String.format("is%s", HANDLE + INVOKED))
					.setBody(String.format("return %s;", HANDLE.toLowerCase() + INVOKED))
					.build())
				.build())
			.collect(Collectors.toList());

		dpSources.addAll(concreteStates, this.getClass());

		DpClassSource context = DpClassSource.newBuilder(packageName, contextName)
			.addField(DpSourceField.newBuilder(stateName.toLowerCase(), stateName)
				.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
				.setSetter(Boolean.TRUE)
				.build())
			.addMethod(DpSourceMethod.newBuilder()
				.setName(REQUEST)
				.setBody(String.format("%s.%s();", stateName.toLowerCase(), HANDLE.toLowerCase()))
				.build())
			.build();

		dpSources.add(context, this.getClass());

		return dpSources;
	}
}

