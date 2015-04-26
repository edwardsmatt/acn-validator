# acn-validator
Implementation of the Australian Company Number Check Digit Validation Algorithm in Scala.

The original description can be found here:

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
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.4</version>
        </dependency>


        import com.github.edwardsmatt.asic.validator.ACNValidator;
        ...
        ACNValidator.isValid("000 000 019");
