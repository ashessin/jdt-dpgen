package com.gof.behavioral.iterator;
public interface Iterator {

	Object first();

	Object next();

	Object current();

	boolean isDone();
}