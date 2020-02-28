package com.gof.behavioral.visitor

//remove if not needed
import scala.collection.JavaConversions._

class ConcreteElementA extends Element {

  var counter: Int = _

  def operationElementA() {
    counter += 1
  }

  protected def getcounter(): Int = counter

  override def accept(visitor: Visitor) {
    visitor.visitConcreteElementA(this)
  }
}
