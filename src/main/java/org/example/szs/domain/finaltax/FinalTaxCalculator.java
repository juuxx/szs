package org.example.szs.domain.finaltax;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FinalTaxCalculator {

	public static FinalTaxResult calculate(FinalTaxInput input) {
		BigDecimal taxableIncome = calculateTaxableIncome(input.totalIncome(), input.totalDeduction());
		BigDecimal calculatedTax = calculateCalculatedTax(taxableIncome);
		BigDecimal finalTax = calculateFinalTax(calculatedTax, input.taxCredit());
		return new FinalTaxResult(taxableIncome, calculatedTax, finalTax);
	}

	// 과세표준 계산
	private static BigDecimal calculateTaxableIncome(BigDecimal totalIncome, BigDecimal totalDeduction) {
		return totalIncome.subtract(totalDeduction)
			.max(BigDecimal.ZERO)
			.setScale(0, RoundingMode.HALF_UP);
	}

	// 산출세액 계산
	private static BigDecimal calculateCalculatedTax(BigDecimal taxableIncome) {
		TaxBracket bracket = TaxBracket.find(taxableIncome);
		return bracket.calculate(taxableIncome);
	}

	// 결정세액 계산
	private static BigDecimal calculateFinalTax(BigDecimal calculatedTax, BigDecimal taxCredit) {
		return calculatedTax.subtract(taxCredit)
			.setScale(0, RoundingMode.HALF_UP);
	}
}
