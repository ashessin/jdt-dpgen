package com.ashessin.cs474.hw1.generator.creational;

import com.ashessin.cs474.hw1.generator.*;
import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Implements the Abstract Factory design pattern.
 *
 * <p>
 * <b>GoF Definition</b><br>
 * Provide an interface for creating families of related or dependent objects
 * without specifying their concrete classes.
 *
 * <p>
 * Examples:
 * <a target="_blank"
 * href="http://docs.oracle.com/javase/8/docs/api/javax/xml/parsers/DocumentBuilderFactory.html#newInstance--">
 * {@code javax.xml.parsers.DocumentBuilderFactory#newInstance()}</a>,
 * <a target="_blank"
 * href="http://docs.oracle.com/javase/8/docs/api/javax/xml/transform/TransformerFactory.html#newInstance--">
 * {@code javax.xml.transform.TransformerFactory#newInstance()}</a>,
 * <a target="_blank"
 * href="http://docs.oracle.com/javase/8/docs/api/javax/xml/xpath/XPathFactory.html#newInstance--">
 * {@code javax.xml.xpath.XPathFactory#newInstance()}</a>
 */
public class AbstractFactoryGen {

	private static final Logger log = LoggerFactory.getLogger(AbstractFactoryGen.class);
	private static final String ABSTRACT = "Abstract";
	private static final String CONCRETE = "Concrete";
	private static final String CREATE = "create";
	private String packageName;
	private String abstractFactoryName;
	private List<String> concreteFactoryNames;
	private Map<String, List<String>> products;

	public AbstractFactoryGen(String packageName,
							  String abstractFactoryName, List<String> concreteFactoryNames,
							  Map<String, List<String>> products) {
		this.packageName = packageName;
		this.abstractFactoryName = abstractFactoryName;
		this.concreteFactoryNames = concreteFactoryNames;
		this.products = products;
	}

	public DpArrayList<DpSource> main() {

		DpArrayList<DpSource> dpSources = new DpArrayList<>();

		if (log.isInfoEnabled()) {
			LoggingReflection.infoLogInstance(this);
		}

		Map<DpInterfaceSource, List<DpClassSource>> dpProducts = products.entrySet().stream()
				.map(e -> makeDpProducts(e.getKey(), e.getValue())).collect(Collectors.toMap(
						AbstractMap.SimpleEntry::getKey,
						AbstractMap.SimpleEntry::getValue));
		dpSources.addAll(dpProducts, this.getClass());

		Stream<DpSourceMethod> dpCreateAbstractProductMethodStream = products.keySet().stream()
				.map(this::createAbstractProductMethod);
		DpInterfaceSource abstractFactory = DpInterfaceSource.newBuilder(this.packageName,
				ABSTRACT + this.abstractFactoryName)
				.setJavadoc("Abstract Factory, defines interface for creation of the abstract product " +
							"objects")
				.addMethods(dpCreateAbstractProductMethodStream.collect(toList()))
				.build();
		dpSources.add(abstractFactory, this.getClass());

		List<List<DpSourceMethod>> dpCreateConcreteProductMethodStream = IntStream.rangeClosed(0,
				(products.values().size() / products.keySet().size()))
				.mapToObj(this::applyProductMethodMappings)
				.map(map -> map.entrySet().stream()
						.map(entry -> createConcreteProductMethod(entry.getValue(), entry.getKey()))
						.collect(toList()))
				.collect(toList());
		List<DpClassSource> concreteFactories = IntStream.range(0, concreteFactoryNames.size())
				.mapToObj(i -> DpClassSource.newBuilder(this.packageName, CONCRETE + concreteFactoryNames.get(i))
						.addImplementsInterface(ABSTRACT + abstractFactoryName)
						.addMethods(dpCreateConcreteProductMethodStream.get(i)))
				.map(DpClassSource.Builder::build).collect(toList());
		dpSources.addAll(concreteFactories, this.getClass());

		return dpSources;
	}

	private Map<String, String> applyProductMethodMappings(int position) {
		return products.values().stream()
				.map(strings -> strings.get(position))
				.map(s -> new AbstractMap.SimpleEntry<>(
						s,
						Objects.requireNonNull(products.entrySet().stream().filter(entry ->
								(entry.getValue().contains(s))).findFirst().orElse(null)).getKey()))
				.collect(LinkedHashMap::new, (hashMap, e)
						-> hashMap.put(e.getKey(), e.getValue()), Map::putAll);
	}

	private DpSourceMethod createAbstractProductMethod(String abstractProductName) {
		return DpSourceMethod.newBuilder()
				.setName(CREATE + ABSTRACT + abstractProductName)
				.addModifier(DpSourceMethod.Modifier.ABSTRACT)
				.setReturnType(ABSTRACT + abstractProductName)
				.build();
	}

	private DpSourceMethod createConcreteProductMethod(String abstractProductName, String concreteProductName) {
		return DpSourceMethod.newBuilder()
				.setName(CREATE + ABSTRACT + abstractProductName)
				.setReturnType(ABSTRACT + abstractProductName)
				.setBody("return new " + CONCRETE + concreteProductName + "();")
				.setIsInherited(Boolean.TRUE)
				.build();
	}

	private AbstractMap.SimpleEntry<DpInterfaceSource, List<DpClassSource>> makeDpProducts(
			String abstractProductName, List<String> concreteProductNames) {
		DpInterfaceSource abstractProduct = makeDpAbstractProducts(abstractProductName);
		List<DpClassSource> concreteProducts = makeDpConcreteProducts(abstractProductName, concreteProductNames);
		return new AbstractMap.SimpleEntry<>(abstractProduct, concreteProducts);
	}

	private DpInterfaceSource makeDpAbstractProducts(String abstractProductName) {
		return DpInterfaceSource.newBuilder(this.packageName, ABSTRACT + abstractProductName)
				.build();
	}

	private List<DpClassSource> makeDpConcreteProducts(String abstractProductName, List<String> concreteProductNames) {
		return Arrays.stream(concreteProductNames.toArray()).map(concreteProductName ->
				DpClassSource.newBuilder(this.packageName, CONCRETE + concreteProductName)
						.addImplementsInterface(ABSTRACT + abstractProductName)
						.setAccessModifier(DpClassSource.AccessModifier.PUBLIC)
						.build()).collect(toList());
	}
}