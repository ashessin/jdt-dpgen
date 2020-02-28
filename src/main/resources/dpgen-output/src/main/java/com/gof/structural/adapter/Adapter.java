package com.gof.structural.adapter;
public class Adapter extends Adaptee implements Target {

	@Override
	public Object request() {
		return this.specialRequest();
	}
}