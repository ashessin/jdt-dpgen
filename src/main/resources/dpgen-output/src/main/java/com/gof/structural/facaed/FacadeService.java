package com.gof.structural.facaed;
public class FacadeService {

	private final Service1 service1;
	private final Service2 service2;
	private final Service3 service3;

	public FacadeService() {
		service1 = new Service1();
		service2 = new Service2();
		service3 = new Service3();
	}

	public boolean isService1Performed() {
		return service1.actionPerformed;
	}

	public boolean isService2Performed() {
		return service2.actionPerformed;
	}

	public boolean isService3Performed() {
		return service3.actionPerformed;
	}
}