package com.gof.creational.builder;

import java.util.List;

public class EmployeeBuilder extends Builder {

	public Employee employee;

	public Builder buildAge(int age) {
		employee.setAge(age);
		return this;
	}

	public Builder buildLastName(String lastName) {
		employee.setLastName(lastName);
		return this;
	}

	public Builder buildFirstName(String firstName) {
		employee.setFirstName(firstName);
		return this;
	}

	public Builder buildResponsibilities(List<String> responsibilities) {
		employee.setResponsibilities(responsibilities);
		return this;
	}
}