import org.apache.commons.lang3.StringUtils
import scalaz._
import Scalaz._


object Main {

	def checkDigit(a: Array[Int]): Int = a.last
	def checksumDigits(a: Array[Int]): Array[Int] = a.take(a.length -1)
	def complement(a: Array[Int]): Int = {
		val weightedProduct = (a zip Weighting).map(a => a._1 * a._2).foldLeft(0)(_ + _)
		 (remainder >>> calculateComplement)(weightedProduct)
	}

	def formatAcn(s: String) = StringUtils.deleteWhitespace(s).grouped(3).toList.mkString(" ")

	def isValidAcn(a: Array[Int]): Boolean = complement(checksumDigits(a)) == checkDigit(a)

	def isValidAcn(s: String): (String, Boolean) = {
		parseInput(s).fold(acn => (formatAcn(s), false), acn => (s, isValidAcn(acn)))
	}

	def getResult(s:String): (String, Boolean) = {
		parseInput(s).fold(error => (s"${formatAcn(s)}: ${error}", false), acn=> (formatAcn(s), isValidAcn(acn)))
	}

	def parseInput(s: String): Either[String, Array[Int]]  = {
		val stripped = StringUtils.deleteWhitespace(s)
		if (stripped.isEmpty) return Left("Invalid input: blank")
		if (!StringUtils.isNumeric(stripped)) return Left("Invalid input: must be numeric")
		if (stripped.length != 9) return Left(s"Invalid input: Expected 9 digits (was ${stripped.length})")
		Right(stripped.toCharArray.map(c => Integer.parseInt(c+"")))
	}

	def main(args: Array[String]) = {
		for (i <- args.map(getResult))
			println(i)
	}

	private val calculateComplement = (r: Int) => {
		val tmp_complement = 10 - r
		(tmp_complement == 10).fold(0, tmp_complement)
	}
	private val remainder = (p: Int) => (p % 10)
	private val Weighting = (1 to 8).reverse.toArray
}