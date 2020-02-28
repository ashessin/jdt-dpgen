package com.gof.behavioral.interpreter;

import java.util.List;

public class Context {

	private List<Boolean> operands = new java.util.ArrayList<>();
	private Boolean result = null;

	public List<Boolean> getOperands() {
		return operands;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public boolean isResult() {
		return result;
	}

	public void addOperand(Boolean operand) {
		operands.add(operand);
	}
}