package com.github.edwardsmatt.asic.validator;

import org.scalatest._
import Util._

class UtilSpec extends FlatSpec with Matchers {
	val validACN = "004 085 616"

	"A String with whitespace" should "have the space removed" in {
		val instance  = removeWhitespace(validACN)
		val expected = "004085616"
		instance should be (expected)
	}

	"A String" should "return true if all it's charcters are digits" in {
		val instance  = isNumeric(removeWhitespace(validACN))
		instance should be (true)
	}

	it should "return false if it contains charaters that aren't digits" in {
		val instance  = isNumeric(removeWhitespace("123 abc 456"))
		instance should be (false)
	}
}