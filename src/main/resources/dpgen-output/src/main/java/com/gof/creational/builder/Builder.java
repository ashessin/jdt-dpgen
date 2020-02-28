package com.gof.creational.builder;

import java.util.List;

public abstract class Builder {

	public abstract Builder buildAge(int age);

	public abstract Builder buildLastName(String lastName);

	public abstract Builder buildFirstName(String firstName);

	public abstract Builder buildResponsibilities(List<String> responsibilities);
}