package org.example.szs.domain.taxinfo;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.example.szs.domain.finaltax.TaxBracket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TaxBracketTest {

	@DisplayName("금액에 따라 TaxBracket 이 정확히 매핑되는지 확인")
	@ParameterizedTest(name = "{index} => income={0}, expected={1}")
	@MethodSource("bracketTestCases")
	void testTaxBracketMatching(BigDecimal income, TaxBracket expectedBracket) {
		// when
		TaxBracket actualBracket = TaxBracket.find(income);

		// then
		assertEquals(expectedBracket, actualBracket);
	}

	static Stream<org.junit.jupiter.params.provider.Arguments> bracketTestCases() {
		return Stream.of(
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("0"), TaxBracket.UP_TO_14M),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("14000000"), TaxBracket.UP_TO_14M),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("14000001"), TaxBracket.BETWEEN_14M_AND_50M),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("50000000"), TaxBracket.BETWEEN_14M_AND_50M),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("50000001"), TaxBracket.BETWEEN_50M_AND_88M),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("88000000"), TaxBracket.BETWEEN_50M_AND_88M),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("88000001"), TaxBracket.BETWEEN_88M_AND_150M),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("150000000"), TaxBracket.BETWEEN_88M_AND_150M),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("150000001"), TaxBracket.BETWEEN_150M_AND_300M),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("300000000"), TaxBracket.BETWEEN_150M_AND_300M),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("300000001"), TaxBracket.BETWEEN_300M_AND_500M),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("500000000"), TaxBracket.BETWEEN_300M_AND_500M),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("500000001"), TaxBracket.BETWEEN_500M_AND_1B),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("1000000000"), TaxBracket.BETWEEN_500M_AND_1B),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("1000000001"), TaxBracket.OVER_1B),
			org.junit.jupiter.params.provider.Arguments.of(new BigDecimal("100000000000"), TaxBracket.OVER_1B)
		);
	}
}