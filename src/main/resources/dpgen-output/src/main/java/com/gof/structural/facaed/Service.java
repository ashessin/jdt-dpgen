package com.gof.structural.facaed;
public abstract class Service {

	protected boolean actionPerformed;

	public boolean isActionPerformed() {
		return actionPerformed;
	}

	public abstract void performService();
}