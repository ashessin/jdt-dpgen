package com.gof.behavioral.interpreter

//remove if not needed
import scala.collection.JavaConversions._

class TerminalExpression(var data: Boolean) extends AbstractExpression {

  def interpret(context: Context) {
    context.addOperand(this.data)
  }
}
