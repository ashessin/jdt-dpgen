package com.ashessin.cs474.hw1.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * POJO representing a top level named interface reference type which is part of some <i>design pattern</i>
 * implementation.
 * <p>
 * Has a static inner {@link Builder} class for ease of initialization.
 */
public final class DpInterfaceSource implements DpSource {

	private String packageName;
	private String name;
	private String javadoc;
	private List<DpSourceMethod> methods;

	private DpInterfaceSource(Builder builder) {
		packageName = builder.packageName;
		name = builder.name;
		javadoc = builder.javadoc;
		methods = builder.methods;
	}

	/**
	 * @param packageName fully qualified named package/subpackage name that this top level
	 *                    named <i>design pattern</i> interface reference type belongs to
	 * @param name        @return identifier of this top level named <i>design pattern</i> interface
	 *                    reference type
	 *
	 * @return a reference to this {@link Builder}
	 */
	public static Builder newBuilder(String packageName, String name) {
		return new Builder(packageName, name);
	}

	/**
	 * @return fully qualified named package/subpackage name that this top level
	 * named <i>design pattern</i> interface reference type belongs to
	 */
	@Override
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * @return identifier of this top level named <i>design pattern</i> interface
	 * reference type
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return javadoc comments of the top level named <i>design pattern</i>
	 * interface reference type
	 */
	@Override
	public String getJavadoc() {
		return javadoc;
	}

	/**
	 * @return methods within the current interface's body
	 */
	public List<DpSourceMethod> getMethods() {
		return this.methods;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", DpInterfaceSource.class.getSimpleName() + "[", "]")
			.add("packageName='" + packageName + "'")
			.add("name='" + name + "'")
			.add("javadoc='" + javadoc + "'")
			.add("methods=" + methods)
			.toString();
	}

	/**
	 * Inner {@code static} builder class for {@link DpInterfaceSource} outer class.
	 */
	public static final class Builder {

		private String packageName;
		private String name;
		private String javadoc;
		private List<DpSourceMethod> methods = new ArrayList<>(1);

		private Builder(String packageName, String name) {
			this.packageName = packageName;
			this.name = name;
		}

		/**
		 * Sets the {@code javadoc} and returns a reference to this Builder
		 * so that the {@link DpClassSource.Builder} methods can be chained together.
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
		 * Returns a {@link DpInterfaceSource} built from the parameters previously set.
		 *
		 * @return a {@link DpInterfaceSource} built with parameters of this {@link DpInterfaceSource.Builder}
		 */
		public DpInterfaceSource build() {
			validate();
			return new DpInterfaceSource(this);
		}

		private void validate() {
			// It is a compile-time error if a member type declaration in an
			// interface has the modifier protected or private.
			Optional<DpSourceMethod> invalidAcessModifier = this.methods.stream()
				.findAny()
				.filter(dpSourceMethod -> {
					DpSourceMethod.AccessModifier accessModifer = dpSourceMethod.getAccessModifier();
					return accessModifer.equals(DpSourceMethod.AccessModifier.PRIVATE) ||
						   accessModifer.equals(DpSourceMethod.AccessModifier.PROTECTED);
				});
			if (invalidAcessModifier.isPresent()) {
				String msg = String.format("Interface %s has method(s) with incorrect access modifier.",
					this.packageName + "." + this.name);
				throw new IllegalStateException(msg);
			}
		}
	}
}
