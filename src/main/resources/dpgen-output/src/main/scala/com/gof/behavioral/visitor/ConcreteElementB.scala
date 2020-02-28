package com.gof.behavioral.visitor

//remove if not needed
import scala.collection.JavaConversions._

class ConcreteElementB extends Element {

  var counter: Int = _

  def operationElementB() {
    counter += 1
  }

  protected def getcounter(): Int = counter

  override def accept(visitor: Visitor) {
    visitor.visitConcreteElementB(this)
  }
}
