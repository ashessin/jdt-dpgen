package com.ashessin.cs474.hw1.dp.generator.creational;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.dp.generator.DpSourceClass;
import com.ashessin.cs474.hw1.dp.generator.DpSourceInterface;
import com.ashessin.cs474.hw1.dp.generator.DpMethod;
import com.ashessin.cs474.hw1.utils.RoasterParser;
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
 * </p>
 *
 * <p>
 * Use the Abstract Factory pattern when
 *     <ul>
 *         <li>.</li>
 *         <li>.</li>
 *     </ul>
 *
 * <p>
 *     Examples:
 *     <a target="_blank"
 *     href="http://docs.oracle.com/javase/8/docs/api/javax/xml/parsers/DocumentBuilderFactory.html#newInstance--">
 *         {@code javax.xml.parsers.DocumentBuilderFactory#newInstance()}</a>,
 *     <a target="_blank"
 *     href="http://docs.oracle.com/javase/8/docs/api/javax/xml/transform/TransformerFactory.html#newInstance--">
 *         {@code javax.xml.transform.TransformerFactory#newInstance()}</a>,
 *     <a target="_blank"
 *     href="http://docs.oracle.com/javase/8/docs/api/javax/xml/xpath/XPathFactory.html#newInstance--">
 *         {@code javax.xml.xpath.XPathFactory#newInstance()}</a>
 */
public class AbstractFactoryGen {

	private static final Logger log = (Logger) LoggerFactory.getLogger(AbstractFactoryGen.class);
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


	public void main() {

		Map<DpSourceInterface, List<DpSourceClass>> dpProducts = products.entrySet().stream()
				.map(e -> makeDpProducts(e.getKey(), e.getValue())).collect(Collectors.toMap(
						AbstractMap.SimpleEntry::getKey,
						AbstractMap.SimpleEntry::getValue));

		log.info("{}", dpProducts);
		dpProducts.forEach((dpInterfaceSource, dpClassSources) -> {
			RoasterParser.makeSource(dpInterfaceSource);
			dpClassSources.forEach(RoasterParser::makeSource);
		});

		Stream<DpMethod> dpCreateAbstractProductMethodStream = products.keySet().stream()
				.map(this::createAbstractProductMethod);
		DpSourceInterface abstractFactory = DpSourceInterface.newBuilder(this.packageName, ABSTRACT + this.abstractFactoryName)
				.addMethods(dpCreateAbstractProductMethodStream.collect(toList()))
				.build();

		log.info("{}", abstractFactory);
		RoasterParser.makeSource(abstractFactory);


		List<List<DpMethod>> dpCreateConcreteProductMethodStream = IntStream.rangeClosed(0,
				(products.values().size() / products.keySet().size()))
				.mapToObj(this::applyProductMethodMappings)
				.map(map -> map.entrySet().stream()
						.map(entry -> createConcreteProductMethod(entry.getValue(), entry.getKey()))
						.collect(toList()))
				.collect(toList());
		List<DpSourceClass> concreteFactories = IntStream.range(0, concreteFactoryNames.size())
				.mapToObj(i -> DpSourceClass.newBuilder(this.packageName, CONCRETE + concreteFactoryNames.get(i))
						.setImplementsInterface(ABSTRACT + abstractFactoryName)
						.addMethods(dpCreateConcreteProductMethodStream.get(i)))
				.map(DpSourceClass.Builder::build).collect(toList());

		log.info("{}", concreteFactories);
		concreteFactories.forEach(RoasterParser::makeSource);
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

	private DpMethod createAbstractProductMethod(String abstractProductName) {
		return DpMethod.newBuilder()
				.setName(CREATE + ABSTRACT + abstractProductName)
				.addModifier(DpMethod.Modifier.ABSTRACT)
				.setReturnType(ABSTRACT + abstractProductName)
				.build();
	}

	private DpMethod createConcreteProductMethod(String abstractProductName, String concreteProductName) {
		return DpMethod.newBuilder()
				.setName(CREATE + ABSTRACT + abstractProductName)
				.setReturnType(ABSTRACT + abstractProductName)
				.setBody("return new " + CONCRETE + concreteProductName + "();")
				.setIsInherited(Boolean.TRUE)
				.build();
	}

	private AbstractMap.SimpleEntry<DpSourceInterface, List<DpSourceClass>> makeDpProducts(String abstractProductName, List<String> concreteProductNames) {
		DpSourceInterface abstractProduct = makeDpAbstractProducts(abstractProductName);
		log.info("{}", abstractProduct);
		List<DpSourceClass> concreteProducts = makeDpConcreteProducts(abstractProductName, concreteProductNames);
		concreteProducts.forEach(e -> log.info(String.valueOf(e)));
		return new AbstractMap.SimpleEntry<>(abstractProduct, concreteProducts);
	}

	private DpSourceInterface makeDpAbstractProducts(String abstractProductName) {
		return DpSourceInterface.newBuilder(this.packageName, ABSTRACT + abstractProductName)
				.build();
	}

	private List<DpSourceClass> makeDpConcreteProducts(String abstractProductName, List<String> concreteProductNames) {
		return Arrays.stream(concreteProductNames.toArray()).map(concreteProductName ->
				DpSourceClass.newBuilder(this.packageName, CONCRETE + concreteProductName)
						.setImplementsInterface(ABSTRACT + abstractProductName)
						.setAccessModifier(DpSourceClass.AccessModifier.PUBLIC)
						.build()).collect(toList());
	}
}