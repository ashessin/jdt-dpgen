package com.ashessin.cs474.hw2.model;

/**
 * POJO for maintaining the command to be used with {@code jdt-dpgen-cli}.
 */
public class DpGenerate {

	public static final DpGenerate INSTANCE = new DpGenerate();
	private String[] command;

	private DpGenerate() {
	}

	public String[] getCommand() {
		return command;
	}

	public void setCommand(String[] command) {
		this.command = command;
	}

}