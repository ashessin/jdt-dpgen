package com.gof.structural.bridge;
public class RefinedAbstraction extends Abstraction {

	public RefinedAbstraction(Implementor implementor) {
		super(implementor);
	}

	@Override
	public String operation() {
		return this.implementor.implementation();
	}
}