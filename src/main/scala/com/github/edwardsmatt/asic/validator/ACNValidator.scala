package com.github.edwardsmatt.asic.validator;
import org.apache.commons.lang3.StringUtils

/**
  * Implementation of the ASIC Australian Company Number (ACN) Check Digit Validation. 
  *
  * Source:
  * http://www.asic.gov.au/for-business/starting-a-company/how-to-start-a-company/australian-company-numbers/australian-company-number-digit-check/
  */
object ACNValidator {
	/** Returns the check digit (the last digit) in the ACN. */
	val checkDigit = (a: Array[Int]) => a.last

	/** Returns the checksum digits for and ACN (the first 8 digits). */
	val toCheckSumDigits = (acn: Array[Int]) => acn.take(acn.length -1)

	/** Calculate the complement of an ACN. */
	def complement(acn: Array[Int]): Int = (toCheckSumDigits andThen toProduct andThen toRemainder andThen toComplement)(acn)

	/** Format the ACN as per the ASIC convention,  in three blocks of three characters separated by a space. */
	def formatAcn(s: String) = StringUtils.deleteWhitespace(s).grouped(3).toList.mkString(" ")

	/** Check whether an Int Array is a valid ACN. */
	def isValid(acn: Array[Int]): Boolean = complement(acn) == checkDigit(acn)

	/** Check whether a String is a valid ACN. */
	def isValid(s: String): (String, Boolean) = {
		parseInput(s).fold(acn => (formatAcn(s), false), acn => (s, isValid(acn)))
	}

	/** Check whether a String is a valid ACN, and if not return the reason why validation failed. */
	def isValidAcnWithMessage(s:String): (String, Boolean) = {
		parseInput(s).fold(error => (s"${formatAcn(s)}: ${error}", false), acn=> (formatAcn(s), isValid(acn)))
	}

	/** Parse and sanitize input strings */
	def parseInput(s: String): Either[String, Array[Int]]  = {
		val stripped = StringUtils.deleteWhitespace(s)
		if (stripped.isEmpty) return Left("Invalid input: blank")
		if (!StringUtils.isNumeric(stripped)) return Left("Invalid input: must be numeric")
		if (stripped.length != 9) return Left(s"Invalid input: Expected 9 digits (was ${stripped.length})")
		Right(stripped.toCharArray.map(c => Integer.parseInt(c+"")))
	}

	def main(args: Array[String]) = for (i <- args.map(isValidAcnWithMessage)) println(i)

	/** Calculate the complement from the remainder. Note: if 10-remainder = 10, return 0.*/
	private val toComplement = (remainder: Int) => {
		val tmp_complement = 10 - remainder
		if (tmp_complement == 10) 0 else tmp_complement
	}
	/** Divide by 10 to obtain remainder. */
	private val toRemainder = (p: Int) => (p % 10)

	/** The weighted product from the ACN. */
	val toProduct = (a: Array[Int]) => (a zip Weighting).map(a => a._1 * a._2).foldLeft(0)(_ + _)
	
	/** Weighting Array (8, 7, 6, 5, 4, 3, 2, 1). */
	private val Weighting = (1 to 8).reverse.toArray
}