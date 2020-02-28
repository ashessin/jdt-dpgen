package com.gof.behavioral.visitor;
public class ConcreteVisitor2 implements Visitor {

	@Override
	public void visitConcreteElementA(ConcreteElementA concreteElementA) {
		concreteElementA.operationElementA();
	}

	@Override
	public void visitConcreteElementB(ConcreteElementB concreteElementB) {
		concreteElementB.operationElementB();
	}
}