package com.gof.behavioral.mediator;

import java.util.List;

public class ConcreteMediator implements Mediator {

	public List<Colleague> colleagues;

	public void addColleague(Colleague colleague) {
		colleagues.add(colleague);
	}

	public ConcreteMediator() {
		colleagues = new java.util.ArrayList<>(1);
	}

	@Override
	public void notifyColleague(Colleague colleague, Object message) {
		colleagues.stream().filter(receiverColleague -> colleague != receiverColleague)
				.forEachOrdered(receiverColleague -> receiverColleague.receive(message));
	}
}