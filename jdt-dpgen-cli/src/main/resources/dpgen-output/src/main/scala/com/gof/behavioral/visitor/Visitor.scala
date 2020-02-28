package com.gof.behavioral.visitor

//remove if not needed
import scala.collection.JavaConversions._

trait Visitor {

  def visitConcreteElementA(concreteElementA: ConcreteElementA): Unit

  def visitConcreteElementB(concreteElementB: ConcreteElementB): Unit
}
