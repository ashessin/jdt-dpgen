package com.gof.behavioral.mediator;
public abstract class Colleague {

	protected Mediator mediator;
	private Object receivedMessage;

	public Colleague(Mediator mediator) {
		this.mediator = mediator;
	}

	public abstract void notifyColleague(Object message);

	public abstract void receive(Object message);

	protected Object getReceivedMessage() {
		return this.receivedMessage;
	}

	protected void setReceivedMessage(Object receivedMessage) {
		this.receivedMessage = receivedMessage;
	}
}