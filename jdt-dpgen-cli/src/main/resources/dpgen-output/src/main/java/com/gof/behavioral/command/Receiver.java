package com.gof.behavioral.command;
public class Receiver {

	private boolean operationPerformed = false;

	public void action() {
		operationPerformed = true;
	}

	public boolean isOperationPerformed() {
		return operationPerformed;
	}
}