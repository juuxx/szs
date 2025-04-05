package org.example.szs.domain.finaltax;

import java.math.BigDecimal;

public record FinalTaxResult(
	BigDecimal taxableIncome,
	BigDecimal calculatedTax,
	BigDecimal finalTax
) {
}
