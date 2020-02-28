package com.gof.behavioral.chainofresponsibility;
public class HandlerA extends Handler {

	private boolean handleRequestInvoked = false;

	protected boolean isHandleRequest() {
		return handleRequestInvoked;
	}

	@Override
	public void handleRequest() {
		handleRequestInvoked = true;
		if (handleRequestInvoked) {
			succesor.handleRequest();
		}
	}
}