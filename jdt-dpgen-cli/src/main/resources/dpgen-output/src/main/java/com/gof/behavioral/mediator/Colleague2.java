package com.gof.behavioral.mediator;
public class Colleague2 extends Colleague {

	public Colleague2(Mediator mediator) {
		super(mediator);
	}

	@Override
	public void notifyColleague(Object message) {
		this.mediator.notifyColleague(this, mediator);
	}

	@Override
	public void receive(Object message) {
		this.setReceivedMessage(message);
	}
}