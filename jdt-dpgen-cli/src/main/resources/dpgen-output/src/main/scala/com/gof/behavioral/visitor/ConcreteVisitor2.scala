package com.gof.behavioral.visitor

//remove if not needed
import scala.collection.JavaConversions._

class ConcreteVisitor2 extends Visitor {

  override def visitConcreteElementA(concreteElementA: ConcreteElementA) {
    concreteElementA.operationElementA()
  }

  override def visitConcreteElementB(concreteElementB: ConcreteElementB) {
    concreteElementB.operationElementB()
  }
}
