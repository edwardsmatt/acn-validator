package com.github.edwardsmatt.asic.validator;



/**
  * Implementation of the ASIC Australian Company Number (ACN) Check Digit Validation. 
  *
  * Source:
  * http://www.asic.gov.au/for-business/starting-a-company/how-to-start-a-company/australian-company-numbers/australian-company-number-digit-check/
  */
  object Main {
  	import ACNValidator.{isValid}

  	def main(args: Array[String]) = for (i <- args.toList.map(isValid(_, Format.withError))) println(i)	
  }

  object ACNValidator {
  	import Fns._
  	import Util.{isNumeric, removeWhitespace}

	/** Check whether an Int Array is a valid ACN. */
	def isValid(acn: Seq[Int]): Boolean = checkDigit(acn).fold(false){_ == complement(acn)}

	/** Check whether a String is a valid ACN, and returns the formatted acn with a Boolean indicating whether or not it is valid. */
	def isValid(s: String): (String, Boolean) = isValid(s, parseInput, Format.default, Format.asInput)

	/** Check whether a String is a valid ACN, and formats the return message with a f: (String, String) => String */
	def isValid(s: String, f: (String, String) => String): (String, Boolean) = isValid(s, parseInput, Format.default, f)

	/** Check whether a String is a valid ACN, and if not return the reason why validation failed. */
	private def isValid(input: String, f: String => Either[String, Seq[Int]], g: String => String, h: (String, String) => String): (String, Boolean) = {
		f(input).fold(error => (h(g(input), error), false), acn=> (g(input), isValid(acn)))
	}

	/** Parse and sanitize input strings */
	def parseInput(s: String): Either[String, Seq[Int]]  = {
		val stripped = removeWhitespace(s)
		if (stripped.isEmpty) Left[String, Seq[Int]]("Invalid input: blank") 
		else if (!isNumeric(stripped)) Left[String, Seq[Int]]("Invalid input: must be numeric")
		else if (stripped.length != 9) Left[String, Seq[Int]](s"Invalid input: Expected 9 digits (was ${stripped.length})")
		else Right[String, Seq[Int]](stripped.toCharArray.toList.map(c => Integer.parseInt(c+"")))
	}
}

object Fns {
	import Util._
	/** Weighting Seq (8, 7, 6, 5, 4, 3, 2, 1). */
	val WEIGHTING = (1 to 8).reverse.toSeq

	/** Returns a Option[Int] containing the check digit (the last digit) in the ACN, or a None if a is empty*/
	def checkDigit[A](as: Seq[A]): Option[A] = as.lastOption

	/** Returns the checksum digits for and ACN (the first 8 digits). */
	def toCheckSumDigits(is: Seq[Int]): Seq[Int] = is.init

	/** Calculate the complement of an ACN. */
	val complement = (acn: Seq[Int]) => (toComplement compose modTen compose toProduct compose toCheckSumDigits)(acn)

    /** Divide by 10 to obtain remainder. */
    val modTen = remainder(_: Int)(10)

	/** The weighted product from the ACN. */
	def toProduct(is: Seq[Int]): Int = (is zip WEIGHTING).map(a => a._1 * a._2).foldLeft(0)(_ + _)

	/** Calculate the complement from the remainder. */
	val toComplement = (r: Int) => sanitize(10 - r)

	/** Curried Remainder function to get the remainder of a % b */
	private	def remainder(a: Int)(b: Int): Int = a % b

	/** If i = 10, return 0, otherwise return i. */
	private def sanitize(i: Int) = i match {
		case 10 => 0
		case _ => i
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
	def removeWhitespace = (s: String) => s.toCharArray.toList.filter(!_.isWhitespace).mkString
}