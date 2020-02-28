package com.gof.behavioral.observer;
public class Observer2 implements Observer {

	private int state;
	private ConcreteSubject subject;

	public Observer2() {
	}

	@Override
	public void update() {
		state = subject.getState();
	}
}