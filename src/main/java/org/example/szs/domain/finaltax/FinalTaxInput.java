package org.example.szs.domain.finaltax;

import java.math.BigDecimal;

public record FinalTaxInput(
	BigDecimal totalIncome,
	BigDecimal totalDeduction,
	BigDecimal taxCredit
) {
}
