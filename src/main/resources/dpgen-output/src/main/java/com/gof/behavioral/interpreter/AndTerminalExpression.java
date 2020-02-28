package com.gof.behavioral.interpreter;
public class AndTerminalExpression extends AbstractExpression {

	private AbstractExpression firstAbstractExpression;
	private AbstractExpression secondAbstractExpression;

	public AndTerminalExpression(AbstractExpression firstAbstractExpression,
			AbstractExpression secondAbstractExpression) {
		this.firstAbstractExpression = firstAbstractExpression;
		this.secondAbstractExpression = secondAbstractExpression;
	}

	@Override
	public void interpret(Context context) {
		firstAbstractExpression.interpret(context);
		secondAbstractExpression.interpret(context);
		java.util.List<Boolean> operands = context.getOperands();
		Boolean firstOperand = operands.get(0);
		Boolean secondOperand = operands.get(1);
		context.setResult(firstOperand && secondOperand);
	}
}