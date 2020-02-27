package com.ashessin.cs474.hw1.generator.structural;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.*;
import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.LoggerFactory;

public class FlyweightGen {

	private static final Logger log = (Logger) LoggerFactory.getLogger(FlyweightGen.class);
	private static final String STATE = "state";
	private static final String OPERATION = "operation";
	private static final String EXTRINSIC_STATE = "extrinsicState";
	private static final String INTRINSIC_STATE = "intrinsicState";
	private String packageName;
	private String flyweightName;
	private String concreteFlyweightName;
	private String unsharedFlyweightName;
	private String flyweightFactoryName;


	public FlyweightGen(String packageName, String flyweightName,
						String concreteFlyweightName, String unsharedFlyweightName,
						String flyweightFactoryName) {
		this.packageName = packageName;
		this.flyweightName = flyweightName;
		this.concreteFlyweightName = concreteFlyweightName;
		this.unsharedFlyweightName = unsharedFlyweightName;
		this.flyweightFactoryName = flyweightFactoryName;
	}

	public DpArrayList<DpSource> main() {

		DpArrayList<DpSource> dpSources = new DpArrayList<>();

		if (log.isInfoEnabled()) {
			LoggingReflection.infoLogInstance(this);
		}

		DpInterfaceSource flyweight = DpInterfaceSource.newBuilder(packageName, flyweightName)
			.addMethod(DpSourceMethod.newBuilder()
				.setName(OPERATION)
				.addParameter(EXTRINSIC_STATE, "Object")
				.setBody(";")
				.build())
			.build();

		dpSources.add(flyweight, this.getClass());

		DpClassSource concreteFlyweight = DpClassSource.newBuilder(packageName, concreteFlyweightName)
			.addImplementsInterface(flyweightName)
			.addField(DpSourceField.newBuilder(INTRINSIC_STATE, "Object")
				.setGetter(Boolean.TRUE)
				.build())
			.addMethod(DpSourceMethod.newBuilder()
				.setIsConstructor(Boolean.TRUE)
				.addParameter(INTRINSIC_STATE, "Object")
				.setBody(String.format("this.%s = %s;",
					INTRINSIC_STATE, INTRINSIC_STATE))
				.build())
			.addMethod(DpSourceMethod.newBuilder(flyweight.getMethods().get(0))
				.build())
			.build();

		dpSources.add(concreteFlyweight, this.getClass());

		DpClassSource unsharedConcreteFlyweight = DpClassSource.newBuilder(packageName, unsharedFlyweightName)
			.addImplementsInterface(flyweightName)
			.addField(DpSourceField.newBuilder(STATE, "Object")
				.setGetter(Boolean.TRUE)
				.build())
			.addMethod(DpSourceMethod.newBuilder()
				.setIsConstructor(Boolean.TRUE)
				.addParameter(STATE, "Object")
				.setBody(String.format("this.%s = %s;",
					STATE, STATE))
				.build())
			.addMethod(DpSourceMethod.newBuilder(flyweight.getMethods().get(0))
				.build())
			.build();

		dpSources.add(unsharedConcreteFlyweight, this.getClass());

		return dpSources;
	}
}