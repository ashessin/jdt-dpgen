package com.gof.behavioral.iterator;
public class ConcreteIterator implements Iterator {

	private ConcreteAggregate ConcreteAggregate;
	private int index = -1;

	public ConcreteIterator(ConcreteAggregate ConcreteAggregate) {
		this.ConcreteAggregate = ConcreteAggregate;
	}

	@Override
	public Object first() {
		index = 0;
		return ConcreteAggregate.getRecords()[index];
	}

	@Override
	public Object next() {
		index++;
		return ConcreteAggregate.getRecords()[index];
	}

	@Override
	public Object current() {
		return ConcreteAggregate.getRecords()[index];
	}

	@Override
	public boolean isDone() {
		return ConcreteAggregate.getRecords().length == (index + 1);
	}
}