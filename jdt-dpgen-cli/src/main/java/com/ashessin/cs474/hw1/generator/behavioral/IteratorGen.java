package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.*;

public class IteratorGen extends DesignPatternGen {

	private static final String FIRST = "first";
	private static final String NEXT = "next";
	private static final String DONE = "Done";
	private static final String CURRENT = "current";
	private static final String CREATE = "create";
	private static final String RECORDS = "Records";
	private static final String INDEX = "index";
	private static final String OBJECT = "Object";
	private String packageName;
	private String aggregateName;
	private String concreteAggregateName;
	private String iteratorName;
	private String concreteIteratorName;


	public IteratorGen(String packageName,
					   String aggregateName, String concreteAggregateName,
					   String iteratorName, String concreteIteratorName) {
		this.packageName = packageName;
		this.aggregateName = aggregateName;
		this.concreteAggregateName = concreteAggregateName;
		this.iteratorName = iteratorName;
		this.concreteIteratorName = concreteIteratorName;
	}

	public DpArrayList<DpSource> main() {

		DpInterfaceSource iterator = DpInterfaceSource.newBuilder(packageName, iteratorName)
				.addMethod(DpSourceMethod.newBuilder()
						.setName(FIRST)
						.setReturnType(OBJECT)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(NEXT)
						.setReturnType(OBJECT)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(CURRENT)
						.setReturnType(OBJECT)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName("is" + DONE)
						.setReturnType("boolean")
						.build())
				.build();

		dpSources.add(iterator, this.getClass());

		DpInterfaceSource aggregate = DpInterfaceSource.newBuilder(packageName, aggregateName)
				.addMethod(DpSourceMethod.newBuilder()
						.setName(CREATE + iteratorName)
						.setReturnType(iteratorName)
						.build())
				.build();

		dpSources.add(aggregate, this.getClass());

		DpClassSource concreteAggregate = DpClassSource.newBuilder(packageName, concreteAggregateName)
				.addImplementsInterface(aggregateName)
				.addField(DpSourceField.newBuilder(RECORDS.toLowerCase(),
						"String[]", "{\"first\", \"second\", \"third\", \"fourth\"}")
						.addModifier(DpSourceField.Modifier.FINAL)
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.setGetter(Boolean.TRUE)
						.build())
				.addMethod(DpSourceMethod.newBuilder(aggregate.getMethods().get(0))
						.setBody(String.format("return new %s(this);", concreteIteratorName))
						.build())
				.build();

		dpSources.add(concreteAggregate, this.getClass());

		DpClassSource concreteIterator = DpClassSource.newBuilder(packageName, concreteIteratorName)
				.addImplementsInterface(iteratorName)
				.addField(DpSourceField.newBuilder(concreteAggregateName.substring(0, 1).toUpperCase() +
												   concreteAggregateName.substring(1), concreteAggregateName)
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.build())
				.addField(DpSourceField.newBuilder(INDEX, "int", "-1")
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setIsConstructor(Boolean.TRUE)
						.addParameter(concreteAggregateName.substring(0, 1).toUpperCase() +
									  concreteAggregateName.substring(1), concreteAggregateName)
						.build())
				.addMethod(DpSourceMethod.newBuilder(iterator.getMethods().get(0))
						.setBody(String.format("%s = 0; return %s.get%s()[%s];",
								INDEX, concreteAggregate.getName(), RECORDS, INDEX))
						.build())
				.addMethod(DpSourceMethod.newBuilder(iterator.getMethods().get(1))
						.setBody(String.format("%s++; return %s.get%s()[%s];",
								INDEX, concreteAggregate.getName(), RECORDS, INDEX))
						.build())
				.addMethod(DpSourceMethod.newBuilder(iterator.getMethods().get(2))
						.setBody(String.format("return %s.get%s()[%s];",
								concreteAggregate.getName(), RECORDS, INDEX))
						.build())
				.addMethod(DpSourceMethod.newBuilder(iterator.getMethods().get(3))
						.setBody(String.format("return %s.get%s().length == (%s + 1);",
								concreteAggregate.getName(), RECORDS, INDEX))
						.build())
				.build();

		dpSources.add(concreteIterator, this.getClass());

		return dpSources;
	}
}