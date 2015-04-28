package com.github.edwardsmatt.asic.validator;

/**
  * Implementation of the ASIC Australian Company Number (ACN) Check Digit Validation. 
  *
  * Source:
  * http://www.asic.gov.au/for-business/starting-a-company/how-to-start-a-company/australian-company-numbers/australian-company-number-digit-check/
  */
object ACNValidator {

	def main(args: Array[String]) = for (i <- args.map(isValidAcnWithMessage(_, validateInput, formatAcn, formatError))) println(i)

	/** Returns the check digit (the last digit) in the ACN. */
	val checkDigit = (a: Array[Int]) => a.last

	/** Returns the checksum digits for and ACN (the first 8 digits). */
	val toCheckSumDigits = (acn: Array[Int]) => acn.take(acn.length -1)

	/** Calculate the complement of an ACN. */
	def complement(acn: Array[Int]): Int = (toComplement compose toRemainder compose toProduct compose toCheckSumDigits)(acn)

	/** Format the ACN as per the ASIC convention,  in three blocks of three characters separated by a space.*/
	def formatAcn(s: String) = removeWhitespace(s).grouped(3).toList.mkString(" ")

	/** Check whether an Int Array is a valid ACN. */
	def isValid(acn: Array[Int]): Boolean = complement(acn) == checkDigit(acn)

	/** Check whether a String is a valid ACN. */
	def isValid(s: String): (String, Boolean) = validateInput(s).fold(acn => (formatAcn(s), false), acn => (s, isValid(acn)))
	
	/** Parse and sanitize input strings */
	def validateInput(s: String): Either[String, Array[Int]]  = {
		val stripped = removeWhitespace(s)
		if (stripped.isEmpty) Left[String, Array[Int]]("Invalid input: blank") 
		else if (!isNumeric(stripped)) Left[String, Array[Int]]("Invalid input: must be numeric")
		else if (stripped.length != 9) Left[String, Array[Int]](s"Invalid input: Expected 9 digits (was ${stripped.length})")
		else Right[String, Array[Int]](stripped.toCharArray.map(c => Integer.parseInt(c+"")))
	}

	/** Format any error messages. */
	private def formatError(a: String, e: String): String = "%s: %s".format(a, e)

	/** Check whether a String is a valid ACN, and if not return the reason why validation failed. */
	private def isValidAcnWithMessage(s: String, f: String => Either[String, Array[Int]], g: String => String, h: (String, String) => String): (String, Boolean) = {
		f(s).fold(error => (h(g(s), error), false), acn=> (g(s), isValid(acn)))
	}

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

	def removeWhitespace = (s: String) => s.toCharArray.filter(!_.isWhitespace).mkString
	def isNumeric(s: String): Boolean = s.forall(_.isDigit)
}