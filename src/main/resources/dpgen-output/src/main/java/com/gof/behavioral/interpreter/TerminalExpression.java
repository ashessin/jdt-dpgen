package com.gof.behavioral.interpreter;
public class TerminalExpression extends AbstractExpression {

	private boolean data;

	public TerminalExpression(boolean data) {
		this.data = data;
	}

	public void interpret(Context context) {
		context.addOperand(this.data);
	}
}