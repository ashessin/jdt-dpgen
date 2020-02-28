package com.gof.behavioral.iterator;
public class ConcreteAggregate implements Aggregate {

	private final String[] records = {"first", "second", "third", "fourth"};

	public String[] getRecords() {
		return records;
	}

	@Override
	public Iterator createIterator() {
		return new ConcreteIterator(this);
	}
}