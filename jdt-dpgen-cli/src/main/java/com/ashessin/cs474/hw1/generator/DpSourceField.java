package com.ashessin.cs474.hw1.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * POJO representing a <i>design pattern</i> Field Declaration.
 * <p>
 * Has a static inner {@link Builder} class for ease of initialization.
 */
public final class DpSourceField {

	private String name;
	private String type;
	private String value;
	private String javadoc;
	private boolean getter;
	private boolean setter;
	private List<Modifier> modifiers;
	private AccessModifier accessModifier;

	private DpSourceField(Builder builder) {
		name = builder.name;
		type = builder.type;
		value = builder.value;
		javadoc = builder.javadoc;
		getter = builder.getter;
		setter = builder.setter;
		modifiers = builder.modifiers;
		accessModifier = builder.accessModifier;
	}

	/**
	 * Creates a new {@link DpSourceField.Builder} instance.
	 *
	 * @param name identifier of the field declaration
	 * @param type unannotated type of the field declaration
	 */
	public static Builder newBuilder(String name, String type) {
		return new Builder(name, type);
	}

	/**
	 * Creates a new {@link DpSourceField.Builder} instance.
	 *
	 * @param name  identifier of the field declaration
	 * @param type  unannotated type of the field declaration
	 * @param value right hand side value to the simple assignment operator
	 */
	public static Builder newBuilder(String name, String type, String value) {
		return new Builder(name, type, value);
	}

	/**
	 * Returns the field's name.
	 *
	 * @return the identifier for current field declaration
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the field's type
	 *
	 * @return unannotated type of the field declaration
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the field's value
	 *
	 * @return right hand side value to the simple assignment operator
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the field's javadoc
	 *
	 * @return javadoc comment for the field
	 */
	public String getJavadoc() {
		return javadoc;
	}

	/**
	 * Returns {@code true} if the current field declaration should have a
	 * corresponding <b>conventional</b> {@code public} getter method.
	 *
	 * @return {@code true} if field has a {@code public} get method with the identifier
	 * get<i>{@link DpSourceField#name}</i> and return type <i>{@link DpSourceField#value}</i>
	 */
	public boolean hasGetter() {
		return getter;
	}

	/**
	 * Returns {@code true} if the current field declaration should have a
	 * corresponding <b>conventional</b> {@code public} setter method.
	 *
	 * @return {@code true} if field has a {@code public} set method with the identifier
	 * set<i>{@link DpSourceField#name}</i> and return type <i>{@link DpSourceField#value}</i>
	 */
	public boolean hasSetter() {
		return setter;
	}

	/**
	 * Returns the field's non-access modifier(s).
	 *
	 * @return non-access modifiers for current field declaration, one or more
	 * of {@code static}, {@code final}; otherwise none
	 */
	public List<Modifier> getModifiers() {
		return modifiers;
	}

	/**
	 * Returns the field's access modifier.
	 *
	 * @return access modifier for current method declaration, one of
	 * {@code public}, {@code protected}, {@code private}; otherwise none
	 */
	public AccessModifier getAccessModifier() {
		return accessModifier;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", DpSourceField.class.getSimpleName() + "[", "]")
				.add("name='" + name + "'")
				.add("type='" + type + "'")
				.add("value='" + value + "'")
				.add("javadoc='" + javadoc + "'")
				.add("getter=" + getter)
				.add("setter=" + setter)
				.add("modifiers=" + modifiers)
				.add("accessModifier=" + accessModifier)
				.toString();
	}

	/**
	 * The {@code enum} of possible access modifier in a field declaration.
	 */
	public enum AccessModifier {
		PRIVATE, PROTECTED, PUBLIC, NONE
	}

	/**
	 * The {@code enum} of possible non-access modifier(s) in a field declaration.
	 */
	public enum Modifier {
		FINAL, STATIC, NONE
	}

	/**
	 * Inner {@code static} builder class for {@link DpSourceField} outer class.
	 */
	public static final class Builder {

		private String name;
		private String type;
		private String value;
		private String javadoc;
		private boolean getter = Boolean.FALSE;
		private boolean setter = Boolean.FALSE;
		private AccessModifier accessModifier = AccessModifier.PUBLIC;
		private List<Modifier> modifiers = new ArrayList<>(1);

		private Builder(String name, String type) {
			this.name = name;
			this.type = type;
		}

		private Builder(String name, String type, String value) {
			this.name = name;
			this.type = type;
			this.value = value;
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
			javadoc = val;
			return this;
		}

		/**
		 * Sets the {@code getter} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code getter} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder setGetter(boolean val) {
			getter = val;
			return this;
		}

		/**
		 * Sets the {@code setter} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code setter} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder setSetter(boolean val) {
			setter = val;
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
			accessModifier = val;
			return this;
		}

		/**
		 * Adds a new entry in {@code modifiers} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code modifiers} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder addModifier(Modifier val) {
			modifiers.add(val);
			return this;
		}

		/**
		 * Returns a {@link DpSourceField} built from the parameters previously set.
		 *
		 * @return a {@link DpSourceField} built with parameters of this {@link DpSourceField.Builder}
		 */
		public DpSourceField build() {
			validate();
			return new DpSourceField(this);
		}

		private void validate() {
			// It is a compile-time error if the same keyword appears more than
			// once as a modifier for a field declaration.
			List<DpSourceField.Modifier> distinctModifiers = this.modifiers.stream()
					.distinct()
					.collect(Collectors.toList());
			if (distinctModifiers.size() != this.modifiers.size()) {
				String msg = String.format("Field %s has duplicate modifier(s).", this.name);
				throw new IllegalStateException(msg);
			}
		}
	}


}
