package com.gof.creational.abstractfactory;
public class ConcreteFactory1 implements AbstractFactory {

	@Override
	public AbstractProductA createAbstractProductA() {
		return new ConcreteProductA1();
	}

	@Override
	public AbstractProductB createAbstractProductB() {
		return new ConcreteProductB1();
	}
}