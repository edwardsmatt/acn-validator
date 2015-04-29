package com.github.edwardsmatt.asic.validator;

import org.scalatest._
import OptionValues._
import EitherValues._
import com.github.edwardsmatt.asic.validator.ACNValidator._
import com.github.edwardsmatt.asic.validator.Util._
import com.github.edwardsmatt.asic.validator.Fns._

class ACNValidatorSpec extends FlatSpec with Matchers {

	val validACN = "004 085 616"
	val validACNArray = Array(0, 0, 4, 0, 8, 5, 6, 1, 6)

	"Valid input" should "return a Right an Array[Int]" in {
		val instance = parseInput(validACN)
		val expected = Array(0, 0, 4, 0, 8, 5, 6, 1, 6)
		instance should be ('right)
		instance.right.value should equal (expected)
	}

	"A Left" should "be returned with blank input" in {
		val instance = parseInput("")
		instance should be (Left[String, Array[Int]]("Invalid input: blank"))
	}

	it should "be returned with Long input" in {
		val instance = parseInput("0123456789")
		instance should be (Left[String, Array[Int]]("Invalid input: Expected 9 digits (was 10)"))
	}

	it should "be returned with Short input" in {
		val instance = parseInput("01234567")
		instance should be (Left[String, Array[Int]]("Invalid input: Expected 9 digits (was 8)"))
	}

	it should "be returned with non-numeric charcters" in { 
		val instance = parseInput("_")
		instance should be (Left[String, Array[Int]]("Invalid input: must be numeric"))
	}

	"The check digit" should "be the last digit of the ACN" in {
		val instance = checkDigit(validACNArray)
		instance.value should be (6)
	}

	it should "will return None if the ACN array is empty" in {
		val instance = checkDigit(Array.empty[Int])
		instance should equal (None)
	}

	"The checksum digits" should "all but the last digit of the ACN" in {
		val instance = toCheckSumDigits(validACNArray)
		instance should be (Array(0, 0, 4, 0, 8, 5, 6, 1))
	}

	"The complement digit" should "be calculated from the checksum digits" in {
		val instance = complement(validACNArray)
		instance should be (6)
	}

	"A Valid ACN" should "return true (String)" in {
		val instance = isValid(validACN)
		instance should be ((validACN, true))
	}

	it should "return true (Array[Int])" in {
		val instance = isValid(validACNArray)
		instance should be (true)
	}

	"An invalid ACN" should "return false (String)" in {
		val instance = isValid("0 0 0 0 0 0 0 1 ")
		instance should be (("000 000 01", false))
	}

	it should "return false (Array[Int])" in {
		val instance = isValid(Array(0, 0, 0, 0, 0, 0, 0, 1))
		instance should be (false)
	}

	"isValid should return a Tuple which" should "contain the formatted ACN and true when given a valid ACN" in {
		val result = isValid("000000019")
		result should be (("000 000 019", true))
	}

	it should "contain the error message and false when given erroneous data" in {
		val result = isValid("_000000019", Format.withError)
		result should be (("_00 000 001 9: Invalid input: must be numeric", false))
	}

	it should "contain the formatted ACN and false when given an ACN with an erroneous checkdigit" in {
		val result = isValid("000000010")
		result should be (("000 000 010", false))
	}
}