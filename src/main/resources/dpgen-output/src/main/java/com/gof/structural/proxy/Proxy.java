package com.gof.structural.proxy;
public class Proxy implements Subject {

	public RealSubject realSubject;

	public RealSubject getRealSubject() {
		return realSubject;
	}

	@Override
	public void doOperation() {
		this.realSubject = new RealSubject();
		this.realSubject.doOperation();
	}
}