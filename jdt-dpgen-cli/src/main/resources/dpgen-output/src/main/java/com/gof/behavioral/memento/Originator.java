package com.gof.behavioral.memento;
public class Originator {

	private Object state;

	protected Originator(Object state) {
		this.state = state;
	}

	public Momento createMomento() {
		return new Momento(state);
	}

	public void restoreMomento(Momento momento) {
		state = momento.getState();
	}
}