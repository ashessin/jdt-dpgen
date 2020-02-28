package com.gof.behavioral.command;
public class Invoker {

	public Command command;

	public Invoker(Command command) {
		this.command = command;
	}

	public void execute() {
		command.execute();
	}
}