package com.ashessin.cs474.hw2.model;

import com.intellij.psi.PsiDirectory;

/**
 * POJO for maintaining the command to be used with {@code jdt-dpgen-cli}.
 */
public class DpGenerate {

	public static final DpGenerate INSTANCE = new DpGenerate();
	private String[] command;
	private PsiDirectory packageDirectory;

	private DpGenerate() {
	}

	public String[] getCommand() {
		return command;
	}

	public void setCommand(String[] command) {
		this.command = command;
	}

	public PsiDirectory getPackageDirectory() {
		return packageDirectory;
	}

	public void setPackageDirectory(PsiDirectory packageDirectory) {
		this.packageDirectory = packageDirectory;
	}
}