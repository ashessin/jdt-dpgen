package com.gof.behavioral.visitor

//remove if not needed
import scala.collection.JavaConversions._

trait Element {

  def accept(visitor: Visitor): Unit
}
