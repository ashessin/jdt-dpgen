package com.ashessin.cs474.hw1.parser;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.*;
import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.*;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * The Roaster implementation for parsing custom DpSource classes.
 *
 * <a target="_blank" href="https://github.com/forge/roaster">Roaster</a>
 */
public class DpSourceParserRoaster extends DpSourceParser {

	private static final Logger log = (Logger) LoggerFactory.getLogger(DpSourceParserRoaster.class);

	public DpSourceParserRoaster() {
		log.info("Initialized new Roaster backend.");
	}

	/**
	 * Serializes custom top level, named <i>design pattern</i> reference type
	 * to a actual Java top level, named java reference type (class/interface).
	 *
	 * @param dpSource custom top level, named <i>design pattern</i> reference type
	 *
	 * @return top level, named java reference type (class/interface)
	 */
	@Override
	public byte[] serialize(final DpSource dpSource) {
		try {
			return dpSource instanceof DpClassSource ?
				makeSource((DpClassSource) dpSource).getBytes(StandardCharsets.UTF_8) :
				makeSource((DpInterfaceSource) dpSource).getBytes(StandardCharsets.UTF_8);
		} catch (ParserException e) {
			String dpSourceCN = getDpSourceCN(dpSource);
			e.getProblems().forEach(problem ->
				log.error("Exception while parsing DpSource: {} {}", dpSourceCN, problem));
		}
		return new byte[0];
	}

	/**
	 * Serializes custom top level, named <i>design pattern</i> interface reference type
	 * to a actual Java top level, named java interface reference type.
	 *
	 * @param dpInterfaceSource top level, named <i>design pattern</i> interface
	 *
	 * @return top level, named java interface reference type
	 */
	@Override
	protected String makeSource(final DpInterfaceSource dpInterfaceSource) {
		log.info("Creating new design pattern interface: {}", getDpSourceCN(dpInterfaceSource));
		final JavaInterfaceSource interfaceSource = Roaster.create(JavaInterfaceSource.class)
			.setPackage(dpInterfaceSource.getPackageName())
			.setName(dpInterfaceSource.getName());

		// optionally set javadoc
		if (dpInterfaceSource.getJavadoc() != null && (
			!dpInterfaceSource.getJavadoc().trim().isEmpty())) {
			interfaceSource.getJavaDoc().setFullText(dpInterfaceSource.getJavadoc());
		}

		// process optional interface method(s)
		dpInterfaceSource.getMethods().forEach(dpMethod -> {

			// add new method with name and return value type
			MethodSource<JavaInterfaceSource> method = interfaceSource.addMethod()
				.setName(dpMethod.getName())
				.setReturnType(dpMethod.getReturnType());

			// add optional method parameter(s) specified by name and corresponding type
			dpMethod.getParameters().forEach(
				(name, type) -> method.addParameter(type, name)
			);

			// add optional method non-access modifier(s)
			addMethodModifiers(method, dpMethod.getModifiers());
		});

		return interfaceSource.toString();
	}

	/**
	 * Serializes custom top level, named <i>design pattern</i> class reference type
	 * to a actual Java top level, named java class reference type.
	 *
	 * @param dpClassSource top level, named <i>design pattern</i> class
	 *
	 * @return top level, named java class reference type
	 */
	@Override
	protected String makeSource(final DpClassSource dpClassSource) {
		log.info("Creating new design pattern class: {}", getDpSourceCN(dpClassSource));
		final JavaClassSource classSource = Roaster.create(JavaClassSource.class)
			.setPackage(dpClassSource.getPackageName())
			.setName(dpClassSource.getName());

		// optionally set javadoc
		if (dpClassSource.getJavadoc() != null &&
			!dpClassSource.getJavadoc().trim().isEmpty()) {
			classSource.getJavaDoc().setFullText(dpClassSource.getJavadoc());
		}

		// optionally set the class abstract
		classSource.setAbstract(dpClassSource.getModifier().equals(DpClassSource.Modifier.ABSTRACT));

		// optionally implement some interface(s)
		dpClassSource.getImplementsInterfaces().forEach(classSource::addInterface);

		// optionally extend some super class
		if (dpClassSource.getExtendsClass() != null &&
			!dpClassSource.getExtendsClass().trim().isEmpty() &&
			!dpClassSource.getExtendsClass().equalsIgnoreCase("Object")) {
			classSource.setSuperType(dpClassSource.getExtendsClass());
		}

		// process optional class field(s)
		dpClassSource.getFields().forEach(dpField ->
			processDpClassFields(dpField, classSource)
		);

		// process optional class method(s)
		dpClassSource.getMethods().forEach(dpMethod ->
			processDpClassMethods(dpMethod, classSource)
		);

		return classSource.toString();
	}

	/**
	 * Helper function to retrieve canonical name of a reference type.
	 *
	 * @param dpSource custom top level, named <i>design pattern</i> reference type
	 *
	 * @return the canonical name associated with the reference type
	 */
	protected String getDpSourceCN(DpSource dpSource) {
		return dpSource.getPackageName() + "." + dpSource.getName();
	}

	private void processDpClassMethods(DpSourceMethod dpClassMethod, JavaClassSource classSource) {

		// add new method or constructor with name and return value type
		MethodSource<JavaClassSource> method = classSource.addMethod()
			.setName(dpClassMethod.getName())
			.setConstructor(dpClassMethod.isConstructor())
			.setReturnType(dpClassMethod.getReturnType());

		// optionally set method javadoc
		if (dpClassMethod.getJavadoc() != null &&
			!dpClassMethod.getJavadoc().trim().isEmpty()) {
			method.getJavaDoc().setFullText(dpClassMethod.getJavadoc());
		}

		// optionally add method or constructor parameter(s) specified by name and corresponding type
		dpClassMethod.getParameters().forEach(
			(name, type) -> method.addParameter(type, name)
		);

		// optionally set body for parameterized constructor with empty body
		if (dpClassMethod.isConstructor().equals(Boolean.TRUE) && (
			dpClassMethod.getBody() == null || dpClassMethod.getBody().trim().isEmpty())) {
			StringBuilder constructorWithParameters = new StringBuilder();
			dpClassMethod.getParameters().forEach(
				(name, type) -> constructorWithParameters
					.append(String.format("this.%s = %s;", name, name)));
			method.setBody(constructorWithParameters.toString());
		}

		// optionally add override annotation for inherited method
		if (Boolean.TRUE.equals(dpClassMethod.isInherited())) {
			method.addAnnotation(Override.class);
		}

		// set optional method access modifier(s)
		setMethodAccessModifier(method, dpClassMethod.getAccessModifier());

		// add optional method non-access modifier(s)
		addMethodModifiers(method, dpClassMethod.getModifiers());

		// optionally set method body
		if (dpClassMethod.getBody() != null && !dpClassMethod.getBody().trim().isEmpty()) {
			method.setBody(dpClassMethod.getBody());
		} else if (dpClassMethod.getBody() != null && dpClassMethod.getBody().equalsIgnoreCase(";")) {
			method.setBody("");
		}
	}

	private void processDpClassFields(DpSourceField dpClassField, JavaClassSource classSource) {
		// add new field (as property) with name, type and optional getter
		PropertySource<JavaClassSource> property = classSource
			.addProperty(dpClassField.getType(), dpClassField.getName())
			.setAccessible(dpClassField.hasGetter());

		// optionally set field javadoc
		if (dpClassField.getJavadoc() != null &&
			!dpClassField.getJavadoc().trim().isEmpty()) {
			property.getField().getJavaDoc().setFullText(dpClassField.getJavadoc());
		}

		// optionally add field setter
		if (!dpClassField.hasSetter()) {
			property.removeMutator();
		}

		// optionally set field initialization
		if (dpClassField.getValue() != null && !dpClassField.getValue().trim().isEmpty()) {
			classSource.getField(dpClassField.getName()).setLiteralInitializer(dpClassField.getValue());
		}

		// optionally set field access modifier
		if (dpClassField.getAccessModifier() != DpSourceField.AccessModifier.NONE) {
			setFieldAccessModifier(classSource.getField(dpClassField.getName()), dpClassField.getAccessModifier());
		}

		// optionally add field non-access modifier(s)
		if (!dpClassField.getModifiers().isEmpty()) {
			addFieldModifiers(classSource.getField(dpClassField.getName()), dpClassField.getModifiers());
		}
	}

	private void setMethodAccessModifier(MethodSource<?> method, DpSourceMethod.AccessModifier accessModifier) {
		switch (accessModifier) {
			case PRIVATE:
				method.setPrivate();
				break;
			case PROTECTED:
				method.setProtected();
				break;
			case PUBLIC:
				method.setPublic();
				break;
			case NONE:
				break;
			default:
				log.warn("Unexpected method access modifier: {}", accessModifier);
		}
	}

	private void addMethodModifiers(MethodSource<?> method, List<DpSourceMethod.Modifier> modifiers) {
		modifiers.forEach(modifier -> {
			switch (modifier) {
				case ABSTRACT:
					method.setAbstract(Boolean.TRUE);
					break;
				case FINAL:
					method.setFinal(Boolean.TRUE);
					break;
				case STATIC:
					method.setStatic(Boolean.TRUE);
					break;
				case SYNCHRONIZED:
					method.setSynchronized(Boolean.TRUE);
					break;
				case NONE:
					method.setAbstract(Boolean.FALSE);
					break;
				default:
					log.warn("Unexpected method modifier: {}", modifier);
			}
		});
	}

	private void setFieldAccessModifier(FieldSource<?> field, DpSourceField.AccessModifier accessModifier) {
		switch (accessModifier) {
			case PRIVATE:
				field.setPrivate();
				break;
			case PROTECTED:
				field.setProtected();
				break;
			case PUBLIC:
				field.setPublic();
				break;
			default:
				log.warn("Unexpected field access modifier: {}", accessModifier);
		}
	}

	private void addFieldModifiers(FieldSource<?> field, List<DpSourceField.Modifier> modifiers) {
		modifiers.forEach(modifier -> {
			switch (modifier) {
				case FINAL:
					field.setFinal(Boolean.TRUE);
					break;
				case STATIC:
					field.setStatic(Boolean.TRUE);
					break;
				default:
					log.warn("Unexpected field modifier: {}", modifier);
			}
		});
	}


}
