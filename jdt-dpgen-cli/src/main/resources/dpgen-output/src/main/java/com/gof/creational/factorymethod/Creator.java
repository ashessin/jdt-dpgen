package com.gof.creational.factorymethod;
public abstract class Creator {

	protected abstract Product factoryMethod();

	public Product factory() {
		return factoryMethod();
	}
}