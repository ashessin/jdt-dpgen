package com.gof.behavioral.command;
public class ConcreteCommand implements Command {

	public Receiver Receiver;

	public ConcreteCommand(Receiver Receiver) {
		this.Receiver = Receiver;
	}

	@Override
	public void execute() {
		this.Receiver.action();
	}
}