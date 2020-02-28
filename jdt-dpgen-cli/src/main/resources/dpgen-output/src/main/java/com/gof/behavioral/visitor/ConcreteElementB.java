package com.gof.behavioral.visitor;
public class ConcreteElementB implements Element {

	public int counter;

	public void operationElementB() {
		counter++;
	}

	protected int getcounter() {
		return counter;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitConcreteElementB(this);
	}
}