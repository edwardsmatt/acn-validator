package com.github.edwardsmatt.asic.validator;

import org.scalatest._

class FormatSpec extends FlatSpec with Matchers {

	"Input" should "be grouped into 3's for display (valid input)" in {
		val input = "000000019"
		val result = Format.default(input)
		result should be ("000 000 019")
	}

	it should "be grouped into 3's for display (short input)" in {
		val input = "00000001"
		val result = Format.default(input)
		result should be ("000 000 01")
	}

	it should "be grouped into 3's for display (long input)" in {
		val input = "0000000190"
		val result = Format.default(input)
		result should be ("000 000 019 0")
	}

	"Errors" should "be formatted correctly" in {
		val error = "Error Message"
		val value = "000000019"
		val result = Format.withError(value, error)
		result should be ("000000019: Error Message")
	}


	"No formatting" should "not change the input" in {
		val value = "000000019"
		val result = Format.asInput(value, "ignored")
		result should be ("000000019")
	}
}