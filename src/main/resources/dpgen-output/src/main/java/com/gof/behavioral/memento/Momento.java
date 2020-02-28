package com.gof.behavioral.memento;
public class Momento {

	private Object state;

	protected Momento(Object state) {
		this.state = state;
	}

	protected Object getState() {
		return state;
	}
}