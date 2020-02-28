package com.gof.behavioral.memento;

import java.util.Deque;

public class Caretaker {

	private Deque<Momento> momentos = new java.util.ArrayDeque<>();

	public void addMomento(Momento momento) {
		momentos.push(momento);
	}

	public Momento getMomento() {
		return momentos.pop();
	}
}