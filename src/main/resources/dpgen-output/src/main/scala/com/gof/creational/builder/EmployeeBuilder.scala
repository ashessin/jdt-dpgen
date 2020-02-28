package com.gof.creational.builder

import java.util.List
//remove if not needed
import scala.collection.JavaConversions._

class EmployeeBuilder extends Builder {

  var employee: Employee = _

  def buildAge(age: Int): Builder = {
    employee.setAge(age)
    this
  }

  def buildLastName(lastName: String): Builder = {
    employee.setLastName(lastName)
    this
  }

  def buildFirstName(firstName: String): Builder = {
    employee.setFirstName(firstName)
    this
  }

  def buildResponsibilities(responsibilities: List[String]): Builder = {
    employee.setResponsibilities(responsibilities)
    this
  }
}
