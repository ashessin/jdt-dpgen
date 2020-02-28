package com.gof.behavioral.interpreter

//remove if not needed
import scala.collection.JavaConversions._

abstract class AbstractExpression {

  def interpret(context: Context): Unit
}
