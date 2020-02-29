package com.ashessin.cs474.hw1.generator;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;

public abstract class DesignPatternQ implements Callable<DpArrayList<DpSource>> {

	public String getDesignPatternName() {
		return Objects.requireNonNull(Optional.ofNullable(this.getClass().getName()).filter(s -> s.length() != 0)
				.map(s -> s.substring(0, s.length() - 1))
				.orElse(this.getClass().getName()))
				.replaceAll("([A-Z])", " $1")
				.split("\\. ")[1];
	}
}