package com.gof.creational.abstractfactory;
public class ConcreteFactory2 implements AbstractFactory {

	@Override
	public AbstractProductA createAbstractProductA() {
		return new ConcreteProductA2();
	}

	@Override
	public AbstractProductB createAbstractProductB() {
		return new ConcreteProductB2();
	}
}