package com.gof.behavioral.observer;
public class Observer1 implements Observer {

	private int state;
	private ConcreteSubject subject;

	public Observer1() {
	}

	@Override
	public void update() {
		state = subject.getState();
	}
}