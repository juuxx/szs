package org.example.szs.api.finaltax;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.example.szs.domain.finaltax.FinalTaxCalculator;
import org.example.szs.domain.finaltax.FinalTaxInput;
import org.example.szs.domain.finaltax.FinalTaxResult;
import org.example.szs.domain.finaltax.TaxBracket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FinalTaxControllerTest {

	private final BigDecimal totalIncome = new BigDecimal("20000000");     // 종합소득금액
	private final BigDecimal pensionDeduction = new BigDecimal("2100002.40");
	private final BigDecimal cardDeduction = new BigDecimal("800001.10");
	private final BigDecimal taxCredit = new BigDecimal("300000");         // 세액공제

	/**
	 * 전체 세액 계산 로직 시나리오 테스트
	 * - 과세표준 계산
	 * - 산출세액 계산
	 * - 세액공제 반영
	 */
	@Test
	@DisplayName("전체 세액 계산 로직 시나리오 테스트")
	void calculate_final_tax_step_by_step() {
		// given
		BigDecimal totalDeduction = pensionDeduction.add(cardDeduction);
		BigDecimal taxableIncome = totalIncome.subtract(totalDeduction)
			.setScale(0, RoundingMode.HALF_UP);

		assertEquals(new BigDecimal("17099997"), taxableIncome);

		// when
		TaxBracket bracket = TaxBracket.find(taxableIncome);
		BigDecimal calculatedTax = bracket.calculate(taxableIncome);

		assertEquals(new BigDecimal("1305000"), calculatedTax);

		BigDecimal finalTax = calculatedTax.subtract(taxCredit);

		// then
		assertEquals(new BigDecimal("1005000"), finalTax);
	}

	@Test
	@DisplayName("산출세액 calculator 계산 로직 테스트")
	void calculate_final_tax_using_service() {
		// given
		BigDecimal totalDeduction = pensionDeduction.add(cardDeduction);

		// when
		FinalTaxInput input = new FinalTaxInput(
			totalIncome,
			totalDeduction,
			taxCredit
		);
		// 계산
		FinalTaxResult output = FinalTaxCalculator.calculate(input);

		// then
		assertEquals(new BigDecimal("1005000"), output.finalTax());
	}

}