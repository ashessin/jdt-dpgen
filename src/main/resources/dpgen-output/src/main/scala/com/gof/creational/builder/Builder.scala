package com.gof.creational.builder

import java.util.List
//remove if not needed
import scala.collection.JavaConversions._

abstract class Builder {

  def buildAge(age: Int): Builder

  def buildLastName(lastName: String): Builder

  def buildFirstName(firstName: String): Builder

  def buildResponsibilities(responsibilities: List[String]): Builder
}
