package org.example.szs.domain.finaltax;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IncomeRange {

	private final BigDecimal min;
	private final BigDecimal max; // null이면 무한대

	// public boolean contains(BigDecimal amount) {
	// 	return amount.compareTo(min) >= 0 &&
	// 		(max == null || amount.compareTo(max) <= 0);
	// }

	public boolean contains(BigDecimal income) {
		boolean lowerOk = income.compareTo(min) >= 0; // 포함
		boolean upperOk = (max == null) || income.compareTo(max) <= 0; // 포함
		return lowerOk && upperOk;
	}
}