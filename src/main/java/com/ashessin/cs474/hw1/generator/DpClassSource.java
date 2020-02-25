package com.ashessin.cs474.hw1.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * POJO representing a top level named class reference type which is part of some <i>design pattern</i>
 * implementation.
 * <p>
 * Has a static inner {@link Builder} class for ease of initialization.
 */
public final class DpClassSource implements DpSource {

	private String packageName;
	private String name;
	private String javadoc;
	private AccessModifier accessModifier;
	private Modifier modifier;
	private List<String> implementsInterfaces;
	private String extendsClass;
	private List<DpSourceMethod> methods;
	private List<DpSourceField> fields;

	private DpClassSource(Builder builder) {
		packageName = builder.packageName;
		name = builder.name;
		javadoc = builder.javadoc;
		accessModifier = builder.accessModifier;
		modifier = builder.modifier;
		implementsInterfaces = builder.implementsInterfaces;
		extendsClass = builder.extendsClass;
		methods = builder.methods;
		fields = builder.fields;
	}

	/**
	 * @param packageName fully qualified named package/subpackage name that this top level
	 *                    named <i>design pattern</i> class reference type belongs to
	 * @param name        identifier of this top level named <i>design pattern</i> class
	 *                    reference type
	 *
	 * @return a reference to this {@link Builder}
	 */
	public static Builder newBuilder(String packageName, String name) {
		return new Builder(packageName, name);
	}

	/**
	 * Returns the FQN of the package/subpackage that this class belongs to.
	 *
	 * @return fully qualified named package/subpackage name that this top level
	 * named <i>design pattern</i> class reference type belongs to
	 */
	@Override
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * Returns the class's name.
	 *
	 * @return identifier of this top level named <i>design pattern</i> class
	 * reference type
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return javadoc comments of the top level named <i>design pattern</i>
	 * class reference type
	 */
	@Override
	public String getJavadoc() {
		return javadoc;
	}

	/**
	 * Returns the class's access modifier.
	 *
	 * @return access modifier for current class declaration, one of
	 * {@code public}, {@code protected}, {@code private}; otherwise none
	 */
	public AccessModifier getAccessModifier() {
		return accessModifier;
	}

	/**
	 * Returns the class's non-access modifier.
	 *
	 * @return non-access modifier for current class declaration, one of
	 * {@code abstract}, {@code final}; otherwise none
	 */
	public Modifier getModifier() {
		return modifier;
	}

	/**
	 * @return the interface(s) that the current class implements
	 */
	public List<String> getImplementsInterfaces() {
		return implementsInterfaces;
	}

	/**
	 * @return the super class the current class extends
	 */
	public String getExtendsClass() {
		return extendsClass;
	}

	/**
	 * @return methods within the current class's body
	 */
	public List<DpSourceMethod> getMethods() {
		return methods;
	}

	/**
	 * @return fields within the current class's body
	 */
	public List<DpSourceField> getFields() {
		return fields;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", DpClassSource.class.getSimpleName() + "[", "]")
			.add("packageName='" + packageName + "'")
			.add("name='" + name + "'")
			.add("javadoc='" + javadoc + "'")
			.add("accessModifier=" + accessModifier)
			.add("modifier=" + modifier)
			.add("implementsInterfaces=" + implementsInterfaces)
			.add("extendsClass='" + extendsClass + "'")
			.add("methods=" + methods)
			.add("fields=" + fields)
			.toString();
	}

	/**
	 * The {@code enum} of possible access modifier for a top level named class reference type.
	 */
	public enum AccessModifier {
		DEFAULT, PUBLIC, NONE
	}

	/**
	 * The {@code enum} of possible non-access modifier for a top level named class reference type.
	 */
	public enum Modifier {
		ABSTRACT, FINAL, NONE
	}

	/**
	 * Inner {@code static} builder class for {@link DpClassSource} outer class.
	 */
	public static class Builder {

		private String packageName;
		private String name;
		private String javadoc;
		private AccessModifier accessModifier = AccessModifier.PUBLIC;
		private Modifier modifier = Modifier.NONE;
		private List<String> implementsInterfaces = new ArrayList<>(1);
		private String extendsClass = "Object";
		private List<DpSourceMethod> methods = new ArrayList<>(1);
		private List<DpSourceField> fields = new ArrayList<>(1);

		private Builder(String packageName, String name) {
			this.packageName = packageName;
			this.name = name;
		}

		/**
		 * Sets the {@code javadoc} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code javadoc} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder setJavadoc(String val) {
			this.javadoc = val;
			return this;
		}

		/**
		 * Sets the {@code accessModifier} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code accessModifier} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder setAccessModifier(AccessModifier val) {
			this.accessModifier = val;
			return this;
		}

		/**
		 * Sets the {@code modifier} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code modifier} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder setModifier(Modifier val) {
			this.modifier = val;
			return this;
		}

		/**
		 * Adds a new entry in {@code implementsInterfaces} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code implementsInterface} to add
		 *
		 * @return a reference to this Builder
		 */
		public Builder addImplementsInterface(String val) {
			this.implementsInterfaces.add(val);
			return this;
		}

		/**
		 * Sets the {@code extendsClass} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code extendsClass} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder setExtendsClass(String val) {
			this.extendsClass = val;
			return this;
		}

		/**
		 * Adds a new entry in {@code methods} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code method} to add
		 *
		 * @return a reference to this Builder
		 */
		public Builder addMethod(DpSourceMethod val) {
			this.methods.add(val);
			return this;
		}

		/**
		 * Adds multiple entries in {@code methods} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the list of {@code method}s to add
		 *
		 * @return a reference to this Builder
		 */
		public Builder addMethods(List<DpSourceMethod> val) {
			val.forEach(this::addMethod);
			return this;
		}

		/**
		 * Adds a new entry in {@code fields} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code field} to add
		 *
		 * @return a reference to this Builder
		 */
		public Builder addField(DpSourceField val) {
			this.fields.add(val);
			return this;
		}

		/**
		 * Adds multiple entries in {@code fields} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the list of {@code field}s to add
		 *
		 * @return a reference to this Builder
		 */
		public Builder addFields(List<DpSourceField> val) {
			val.forEach(this::addField);
			return this;
		}

		/**
		 * Returns a {@link DpClassSource} built from the parameters previously set.
		 *
		 * @return a {@link DpClassSource} built with parameters of this {@link DpClassSource.Builder}
		 */
		public DpClassSource build() {
			validate();
			return new DpClassSource(this);
		}

		private void validate() {
			// It is a compile-time error if a normal class that is not abstract
			// has an abstract method.
			Optional<DpSourceMethod> abstractMethod = this.methods.stream()
				.findAny()
				.filter(dpSourceMethod -> dpSourceMethod
					.getModifiers()
					.contains(DpSourceMethod.Modifier.ABSTRACT));
			if (this.modifier != Modifier.ABSTRACT && abstractMethod.isPresent()) {
				String msg = String.format("Non abstract Class %s has abstract method(s).",
					this.packageName + "." + this.name);
				throw new IllegalStateException(msg);
			}

			// It is a compile-time error for the body of a class declaration to
			// declare two fields with the same name.
			List<String> fieldNames = this.fields.stream()
				.map(DpSourceField::getName)
				.distinct()
				.collect(Collectors.toList());
			if (fieldNames.size() != this.fields.size()) {
				String msg = String.format("Class %s has duplicate field(s).",
					this.packageName + "." + this.name);
				throw new IllegalStateException(msg);
			}
		}
	}
}
