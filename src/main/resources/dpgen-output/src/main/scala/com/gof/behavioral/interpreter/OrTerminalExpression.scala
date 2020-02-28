package com.gof.behavioral.interpreter

//remove if not needed
import scala.collection.JavaConversions._

class OrTerminalExpression(var firstAbstractExpression: AbstractExpression, var secondAbstractExpression: AbstractExpression)
    extends AbstractExpression {

  override def interpret(context: Context) {
    firstAbstractExpression.interpret(context)
    secondAbstractExpression.interpret(context)
    val operands = context.getOperands
    val firstOperand = operands.get(0)
    val secondOperand = operands.get(1)
    context.setResult(firstOperand || secondOperand)
  }
}
