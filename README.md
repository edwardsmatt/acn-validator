# acn-validator
[![Build Status](https://travis-ci.org/edwardsmatt/acn-validator.svg?branch=master)](https://travis-ci.org/edwardsmatt/acn-validator)

Implementation of the Australian Company Number Check Digit Validation Algorithm in Scala.

The ASIC definition of the algorithm can ve found here:

http://www.asic.gov.au/for-business/starting-a-company/how-to-start-a-company/australian-company-numbers/australian-company-number-digit-check/

## Usage from Java with Maven

        <dependency>
            <groupId>org.scala-lang</groupId>
            <artifactId>scala-library</artifactId>
            <version>2.11.6</version>
        </dependency>
        <dependency>
            <groupId>com.github.edwardsmatt</groupId>
            <artifactId>acn-validator</artifactId>
            <version>2.11-0.0.1</version>
            <type>jar</type>
        </dependency>


        import com.github.edwardsmatt.asic.validator.ACNValidator;
        ...
        ACNValidator.isValid("000 000 019");
