package com.gof.creational.abstractfactory;
/**
 * Abstract Factory, defines interface for creation of the abstract product
 * objects
 */
public interface AbstractFactory {

	abstract AbstractProductA createAbstractProductA();

	abstract AbstractProductB createAbstractProductB();
}