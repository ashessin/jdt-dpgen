package com.gof.behavioral.visitor;
public class ConcreteElementA implements Element {

	public int counter;

	public void operationElementA() {
		counter++;
	}

	protected int getcounter() {
		return counter;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitConcreteElementA(this);
	}
}