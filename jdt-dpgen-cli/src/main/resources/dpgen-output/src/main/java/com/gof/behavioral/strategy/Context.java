package com.gof.behavioral.strategy;
public class Context {

	private Strategy strategy;

	public Context(Strategy strategy) {
		this.strategy = strategy;
	}

	public Object operation() {
		return this.strategy.algorithm();
	}
}