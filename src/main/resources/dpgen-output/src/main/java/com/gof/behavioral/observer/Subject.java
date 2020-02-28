package com.gof.behavioral.observer;

import java.util.List;

public abstract class Subject {

	private List<Observer> observers = new java.util.ArrayList<>(2);

	public void attach(Observer observer) {
		observers.add(observer);
	}

	public void detach(Observer observer) {
		observers.remove(observer);
	}

	public void notifyObservers() {
		observers.forEach(Observer::update);
	}
}