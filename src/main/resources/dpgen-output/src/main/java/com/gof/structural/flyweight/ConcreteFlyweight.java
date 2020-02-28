package com.gof.structural.flyweight;
public class ConcreteFlyweight implements Flyweight {

	public Object intrinsicState;

	public Object getIntrinsicState() {
		return intrinsicState;
	}

	public ConcreteFlyweight(Object intrinsicState) {
		this.intrinsicState = intrinsicState;
	}

	@Override
	public void operation(Object extrinsicState) {
		;
	}
}