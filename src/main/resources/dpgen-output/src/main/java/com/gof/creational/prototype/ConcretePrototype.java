package com.gof.creational.prototype;
public class ConcretePrototype implements Prototype {

	public Object property1;
	public Object property2;

	public ConcretePrototype(Object property1, Object property2) {
		this.property1 = property1;
		this.property2 = property2;
	}

	protected ConcretePrototype(ConcretePrototype original) {
		super();
		this.property1 = original.property1;
		this.property2 = original.property2;
	}

	@Override
	public Prototype copy() {
		return new ConcretePrototype(this);
	}
}