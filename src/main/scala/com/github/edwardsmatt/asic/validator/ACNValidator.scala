package com.github.edwardsmatt.asic.validator;



/**
  * Implementation of the ASIC Australian Company Number (ACN) Check Digit Validation. 
  *
  * Source:
  * http://www.asic.gov.au/for-business/starting-a-company/how-to-start-a-company/australian-company-numbers/australian-company-number-digit-check/
  */
object main {
 	import ACNValidator.{isValid}

  	def main(args: Array[String]) = for (i <- args.map(isValid(_, Format.withError))) println(i)	
}

object ACNValidator {
 	import Fns._
 	import Util.{isNumeric, removeWhitespace}

	/** Check whether an Int Array is a valid ACN. */
	def isValid(acn: Array[Int]): Boolean = checkDigit(acn).fold(false){_ == complement(acn)}

	/** Check whether a String is a valid ACN, and returns the formatted acn with a Boolean indicating whether or not it is valid. */
	def isValid(s: String): (String, Boolean) = isValid(s, parseInput, Format.default, Format.asInput)

	/** Check whether a String is a valid ACN, and formats the return message with a f: (String, String) => String */
	def isValid(s: String, f: (String, String) => String): (String, Boolean) = isValid(s, parseInput, Format.default, f)
	
	/** Check whether a String is a valid ACN, and if not return the reason why validation failed. */
	private def isValid(input: String, f: String => Either[String, Array[Int]], g: String => String, h: (String, String) => String): (String, Boolean) = {
		f(input).fold(error => (h(g(input), error), false), acn=> (g(input), isValid(acn)))
	}

	/** Parse and sanitize input strings */
	def parseInput(s: String): Either[String, Array[Int]]  = {
		val stripped = removeWhitespace(s)
		if (stripped.isEmpty) Left[String, Array[Int]]("Invalid input: blank") 
		else if (!isNumeric(stripped)) Left[String, Array[Int]]("Invalid input: must be numeric")
		else if (stripped.length != 9) Left[String, Array[Int]](s"Invalid input: Expected 9 digits (was ${stripped.length})")
		else Right[String, Array[Int]](stripped.toCharArray.map(c => Integer.parseInt(c+"")))
	}
}

object Fns {
	import Util._
	/** Weighting Array (8, 7, 6, 5, 4, 3, 2, 1). */
	val WEIGHTING = (1 to 8).reverse.toArray

	/** Returns a Option[Int] containing the check digit (the last digit) in the ACN, or a None if a is empty*/
	def checkDigit(a: Array[Int]): Option[Int] = a.lastOption

	/** Returns the checksum digits for and ACN (the first 8 digits). */
	val toCheckSumDigits = (acn: Array[Int]) => acn.take(acn.length -1)

	/** Calculate the complement of an ACN. */
	def complement(acn: Array[Int]): Int = (toComplement compose toRemainder compose toProduct compose toCheckSumDigits)(acn)

	/** Divide by 10 to obtain remainder. */
	val toRemainder = (p: Int) => (p % 10)

	/** The weighted product from the ACN. */
	val toProduct = (a: Array[Int]) => (a zip WEIGHTING).map(a => a._1 * a._2).foldLeft(0)(_ + _)

	/** Calculate the complement from the remainder. Note: if 10-remainder = 10, return 0.*/
	val toComplement = (remainder: Int) => {
		val tmp_complement = 10 - remainder
		if (tmp_complement == 10) 0 else tmp_complement
	}
}

object Format {
	import Util.{removeWhitespace}
	/** Format the ACN as per the ASIC convention,  in three blocks of three characters separated by a space.*/
	def default(s: String) = removeWhitespace(s).grouped(3).toList.mkString(" ")
	/** Format's the ACN without any messages. */
	def asInput(a: String, e: String): String = a
	/** Format's the ACN with any error message. */
	def withError(a: String, e: String): String = "%s: %s".format(a, e)
}

object Util {
	/** Checks whether all Characters in a string are digits. */
	def isNumeric(s: String): Boolean = s.forall(_.isDigit)
	/** Removes any whitespace characters from s: String . */
	def removeWhitespace = (s: String) => s.toCharArray.filter(!_.isWhitespace).mkString
}