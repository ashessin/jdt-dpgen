package com.gof.behavioral.state;
public class ConcreteState2 implements State {

	private boolean handleInvoked;

	@Override
	public void handle() {
		this.handleInvoked = true;
	}

	protected boolean isHandleInvoked() {
		return handleInvoked;
	}
}