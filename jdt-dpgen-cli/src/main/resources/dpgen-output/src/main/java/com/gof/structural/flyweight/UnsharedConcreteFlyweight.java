package com.gof.structural.flyweight;
public class UnsharedConcreteFlyweight implements Flyweight {

	public Object state;

	public Object getState() {
		return state;
	}

	public UnsharedConcreteFlyweight(Object state) {
		this.state = state;
	}

	@Override
	public void operation(Object extrinsicState) {
		;
	}
}