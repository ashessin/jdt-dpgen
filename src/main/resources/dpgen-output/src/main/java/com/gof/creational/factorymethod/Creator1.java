package com.gof.creational.factorymethod;
public class Creator1 extends Creator {

	@Override
	protected Product factoryMethod() {
		return new ConcreteProduct1();
	}
}