package com.gof.creational.builder

import java.util.List
//remove if not needed
import scala.collection.JavaConversions._

class Employee {

  var age: Int = _

  var lastName: String = _

  var firstName: String = _

  var responsibilities: List[String] = _

  def getAge(): Int = age

  def setAge(age: Int) {
    this.age = age
  }

  def getLastName(): String = lastName

  def setLastName(lastName: String) {
    this.lastName = lastName
  }

  def getFirstName(): String = firstName

  def setFirstName(firstName: String) {
    this.firstName = firstName
  }

  def getResponsibilities(): List[String] = responsibilities

  def setResponsibilities(responsibilities: List[String]) {
    this.responsibilities = responsibilities
  }
}
