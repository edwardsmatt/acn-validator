import collection.mutable.Stack
import org.scalatest._
import OptionValues._
import EitherValues._
import Main._

class ParseInputSpec extends FlatSpec with Matchers {

	val validACN = "004 085 616"
	val validACNArray = Array(0, 0, 4, 0, 8, 5, 6, 1, 6)

	"Valid input" should "return a Right an Array[Int]" in {
		val instance = parseInput(validACN)
		val expected = Array(0, 0, 4, 0, 8, 5, 6, 1, 6)
		instance should be ('right)
		instance.right.value should equal (expected)
	}

	"A Left" should "be returned with blank input" in {
		val instance = Main.parseInput("")
		instance should be (Left("Invalid input: blank"))
	}

	it should "be returned with Long input" in {
		val instance = Main.parseInput("0123456789")
		instance should be (Left("Invalid input: Expected 9 digits (was 10)"))
	}

	it should "be returned with Short input" in {
		val instance = Main.parseInput("01234567")
		instance should be (Left("Invalid input: Expected 9 digits (was 8)"))
	}

	it should "be returned with non-numeric charcters" in { 
		val instance = Main.parseInput("_")
		instance should be (Left("Invalid input: must be numeric"))
	}

	"The check digit" should "be the last digit of the ACN" in {
		val instance = checkDigit(validACNArray)
		instance should be (6)
	}

	"The checksum digits" should "all but the last digit of the ACN" in {
		val instance = checksumDigits(validACNArray)
		instance should be (Array(0, 0, 4, 0, 8, 5, 6, 1))
	}

	"The complement digit" should "be calculated from the checksum digits" in {
		val instance = complement(checksumDigits(validACNArray))
		instance should be (6)
	}

	"A Valid ACN" should "return true (String)" in {
		val instance = isValidAcn(validACN)
		instance should be ((validACN, true))
	}

	it should "return true (Array[Int])" in {
		val instance = isValidAcn(validACNArray)
		instance should be (true)
	}

	"An invalid ACN" should "return false (String)" in {
		val instance = isValidAcn("0 0 0 0 0 0 0 1 ")
		instance should be (("000 000 01", false))
	}

	it should "return false (Array[Int])" in {
		val instance = isValidAcn(Array(0, 0, 0, 0, 0, 0, 0, 1))
		instance should be (false)
	}

	"Input" should "be grouped into 3's for display (valid input)" in {
		val input = "000000019"
		val result = formatAcn(input)
		result should be ("000 000 019")
	}

	it should "be grouped into 3's for display (short input)" in {
		val input = "00000001"
		val result = formatAcn(input)
		result should be ("000 000 01")
	}

	it should "be grouped into 3's for display (long input)" in {
		val input = "0000000190"
		val result = formatAcn(input)
		result should be ("000 000 019 0")
	}
}