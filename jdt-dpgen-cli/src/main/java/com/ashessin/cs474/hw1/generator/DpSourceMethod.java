package com.ashessin.cs474.hw1.generator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * POJO representing a <i>design pattern</i> Method Declaration.
 * <p>
 * Has a static inner {@link Builder} class for ease of initialization.
 */
public final class DpSourceMethod {

	private Boolean constructor;
	private String name;
	private String javadoc;
	private AccessModifier accessModifier;
	private List<Modifier> modifiers;
	private String returnType;
	private String body;
	private Map<String, String> parameters;
	private Boolean inherited;

	private DpSourceMethod(Builder builder) {
		constructor = builder.constructor;
		name = builder.name;
		javadoc = builder.javadoc;
		accessModifier = builder.accessModifier;
		modifiers = builder.modifiers;
		returnType = builder.returnType;
		body = builder.body;
		parameters = builder.parameters;
		inherited = builder.inherited;
	}

	/**
	 * A copy method that creates a new {@link DpSourceMethod.Builder} instance
	 * from an existing {@link DpSourceMethod} instance.
	 * <p>
	 * This method should be used to setup inheritable methods from a super
	 * <i>design pattern</i> reference type, ie. superclass or superinterface.
	 *
	 * @param copy the source method declaration
	 *
	 * @return a reference to a new Builder instance with copied parameters
	 */
	public static Builder newBuilder(DpSourceMethod copy) {
		Builder builder = new Builder();
		builder.constructor = copy.isConstructor();
		builder.name = copy.getName();
		builder.javadoc = copy.getJavadoc();
		builder.accessModifier = copy.getAccessModifier();
		builder.modifiers = copy.modifiers.stream()
				.filter(modifier -> modifier != Modifier.ABSTRACT)
				.collect(Collectors.toList());
		builder.returnType = copy.getReturnType();
		builder.body = copy.getBody();
		builder.parameters = copy.getParameters();
		builder.inherited = Boolean.TRUE;
		return builder;
	}

	/**
	 * Creates a new empty {@link DpSourceMethod.Builder} instance.
	 *
	 * @return a reference to a new Builder instance
	 */
	public static Builder newBuilder() {
		return new Builder();
	}

	/**
	 * Returns {@code true} if the current method declaration is a constructor.
	 *
	 * @return {@code true} if the current instance represent a constructor
	 * declaration; false otherwise.
	 */
	public Boolean isConstructor() {
		return constructor;
	}

	/**
	 * Returns the method's name.
	 *
	 * @return the identifier for current method declaration
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the method's javadoc.
	 *
	 * @return javadoc comment for the method
	 */
	public String getJavadoc() {
		return javadoc;
	}

	/**
	 * Returns the formal parameter(s) that the method takes, each specified by
	 * a corresponding identifier and type of value.
	 *
	 * @return a map of corresponding parameter identifier and type
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * Returns the method's access modifier.
	 *
	 * @return access modifier for current method declaration, one of
	 * {@code public}, {@code protected}, {@code private}; otherwise none
	 */
	public AccessModifier getAccessModifier() {
		return accessModifier;
	}

	/**
	 * Returns the method's non-access modifier(s).
	 *
	 * @return non-access modifiers for current method declaration, one or more
	 * of {@code abstract}, {@code static}, {@code final}, {@code synchronized};
	 * otherwise none
	 */
	public List<Modifier> getModifiers() {
		return modifiers;
	}

	/**
	 * Returns the type of value that the method returns, or void to indicate
	 * that the method does not return a value.
	 *
	 * @return unannotated type or {@code void} to indicate that the method does
	 * not return a value
	 */
	public String getReturnType() {
		return returnType;
	}

	/**
	 * Returns the method's body.
	 *
	 * @return the method body block for current method declaration
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Returns {@code true} if the current method declaration is inherited from
	 * a super reference type.
	 *
	 * @return {@code true} if the current instance represent a inherited method
	 * declaration; false otherwise
	 *
	 * @see DpSourceMethod#newBuilder(DpSourceMethod)
	 */
	public Boolean isInherited() {
		return inherited;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", DpSourceMethod.class.getSimpleName() + "[", "]")
				.add("constructor=" + constructor)
				.add("name='" + name + "'")
				.add("javadoc='" + javadoc + "'")
				.add("accessModifier=" + accessModifier)
				.add("modifiers=" + modifiers)
				.add("returnType='" + returnType + "'")
				.add("body='" + body + "'")
				.add("parameters=" + parameters)
				.add("inherited=" + inherited)
				.toString();
	}

	/**
	 * The {@code enum} of possible access modifier in a method declaration.
	 */
	public enum AccessModifier {
		PRIVATE, PROTECTED, PUBLIC, NONE
	}

	/**
	 * The {@code enum} of possible non-access modifier(s) in a method declaration.
	 */
	public enum Modifier {
		ABSTRACT, FINAL, STATIC, SYNCHRONIZED, NONE
	}

	/**
	 * Inner {@code static} builder class for {@link DpSourceMethod} outer class.
	 */
	public static final class Builder {

		private Boolean constructor = Boolean.FALSE;
		private String name = "DefaultDpMethodName";
		private String javadoc = "";
		private AccessModifier accessModifier = AccessModifier.PUBLIC;
		private List<Modifier> modifiers = new ArrayList<>(1);
		private String returnType = "void";
		private String body = null;
		private Map<String, String> parameters = new LinkedHashMap<>(1);
		private Boolean inherited = Boolean.FALSE;

		private Builder() {
		}

		/**
		 * Sets the {@code constructor} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code constructor} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder setIsConstructor(Boolean val) {
			constructor = val;
			return this;
		}

		/**
		 * Sets the {@code name} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code name} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder setName(String val) {
			name = val;
			return this;
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
		 * Adds a new entry in {@code modifier} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code modifier} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder addModifier(Modifier val) {
			modifiers.add(val);
			return this;
		}

		/**
		 * Sets the {@code returnType} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code returnType} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder setReturnType(String val) {
			returnType = val;
			return this;
		}

		/**
		 * Sets the {@code body} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code body} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder setBody(String val) {
			body = val;
			return this;
		}

		/**
		 * Adds a new entry in {@code parameters} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param name the identifier for the new entry in {@code parameters} list
		 * @param type the corresponding unannotated type for the new entry in
		 *             {@code parameters} list
		 *
		 * @return a reference to this Builder
		 */
		public Builder addParameter(String name, String type) {
			this.parameters.put(name, type);
			return this;
			//TODO: evaluate usefulness, refactor
		}

		/**
		 * Adds multiple entries in {@code parameters} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param parameters the {@code parameters} to add
		 *
		 * @return a reference to this Builder
		 */
		public Builder addParameters(List<DpSourceField> parameters) {
			parameters.forEach(parameter -> this.parameters.put(parameter.getName(), parameter.getType()));
			return this;
			//TODO: evaluate usefulness, refactor
		}

		/**
		 * Sets the {@code inherited} and returns a reference to this Builder
		 * so that the {@link Builder} methods can be chained together.
		 *
		 * @param val the {@code inherited} to set
		 *
		 * @return a reference to this Builder
		 */
		public Builder setIsInherited(Boolean val) {
			this.inherited = val;
			return this;
			//TODO: evaluate usefulness, refactor
		}

		/**
		 * Returns a {@link DpSourceMethod} built from the parameters previously set.
		 *
		 * @return a {@link DpSourceMethod} built with parameters of this {@link DpSourceMethod.Builder}
		 */
		public DpSourceMethod build() {
			validate();
			return new DpSourceMethod(this);
		}

		private void validate() {
			// It is a compile-time error if the same keyword appears more than
			// once as a modifier for a method declaration.
			List<DpSourceMethod.Modifier> distinctModifiers = this.modifiers.stream()
					.distinct()
					.collect(Collectors.toList());
			if (distinctModifiers.size() != this.modifiers.size()) {
				String msg = String.format("Method %s has duplicate modifier(s).", this.name);
				throw new IllegalStateException(msg);
			}
		}
	}
}
